package com.ariezlabs.pancake.scanner;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Not a FSM in the traditional sense since it deals with concatenated symbols potentially without
 * a specific separator. Allows accepting states that won't move the current reader position ahead
 * so that e.g. a text literal can be terminated by a "[" without dropping the "[" by reading ahead.
 * @param <T> defines input type, Scanner will only work with Integer but the parser used in the
 *            constructor works with Strings
 */
class FSM<T> {
    private State<T> initial;
    private State<T> current;
    private ArrayList<T> path;
    private HashMap<String, State<T>> states = new HashMap<>(); // for access and to ensure each state has unique labels

    /**
     * This constructor bootstraps another FSM with a hardcoded language according to my .fsm specification
     * and uses that as a scanner, feeding the output into yet another parser FSM (.fsm is regular)
     * which finally reads the fsmDefinition file and constructs a FSM with the corresponding language
     * @param fsmDefinition Reader to read FSM specification from
     */
    FSM(Reader fsmDefinition) throws IOException {
        // symbols
        final String EOF = "eof";
        final String POUND = "pound";
        final String INITIAL = "initial";
        final String DASH = "dash";
        final String NEWLINE = "newline";
        final String COLON = "colon";
        final String LBRACKET = "lbracket";
        final String RBRACKET = "rbracket";
        final String TEXT = "text";
        final String INTEXT = "inText";

        // .fsm scanner setup
        FSM<Integer> scannerFsm = new FSM<>();

        scannerFsm.putState(INITIAL, 0);
        scannerFsm.putState(DASH, 1);
        scannerFsm.putState(NEWLINE, 1);
        scannerFsm.putState(COLON, 1);
        scannerFsm.putState(POUND, 1);
        scannerFsm.putState(EOF, 1);
        scannerFsm.putState(LBRACKET, 1);
        scannerFsm.putState(RBRACKET, 1);
        scannerFsm.putState(INTEXT, 0);
        scannerFsm.putState(TEXT, 2);

        scannerFsm.putTransition(INITIAL, DASH, (int) '-');
        scannerFsm.putTransition(INITIAL, NEWLINE, (int) '\n');
        scannerFsm.putTransition(INITIAL, COLON, (int) ':');
        scannerFsm.putTransition(INITIAL, POUND, (int) '#');
        scannerFsm.putTransition(INITIAL, EOF, -1);
        scannerFsm.putTransition(INITIAL, LBRACKET, (int) '[');
        scannerFsm.putTransition(INITIAL, RBRACKET, (int) ']');

        scannerFsm.putDefaultTransition(INITIAL, INTEXT);
        scannerFsm.putDefaultTransition(INTEXT, INTEXT);
        scannerFsm.putTransition(INTEXT, TEXT, (int) '-', (int) '\n', (int) ':', (int) '#', -1, (int) '[', (int) ']');

        scannerFsm.setInitial(INITIAL);
        scannerFsm.reset();

        Scanner scanner = new Scanner(fsmDefinition, scannerFsm);

        // TODO setup parser - need to allow side effects on transitions?

        fsmDefinition.close();
        reset();
    }

    boolean accepts() {
        return current != null && current.accepts();
    }

    boolean inErrorState() {
        return current == null;
    }

    boolean inReadNextState() {
        return current.isReadNext();
    }

    String currentStateLabel() {
        return current.getLabel();
    }

    void transition(T input) {
        path.add(input);
        if (current != null) {
            current = current.getTransition(input);
        }
    }

    /**
     * Resets FSM to initial state and forgets history. Does not reset reader position.
     */
    void reset() {
        path = new ArrayList<>(100);
        current = initial;
    }

    ArrayList<T> getPath() {
        return path;
    }

    /**
     * @return string representing this FSM in parseable .fsm format
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (State<T> s : states.values())
            sb.append(s).append("\n");
        return sb.toString();
    }


    /***********************************************
     * methods used for constructing FSM from file *
     ***********************************************/

    private FSM() {
        reset();
    }

    /**
     * @param label label of new state
     * @param type 0: nonaccepting state, 1: accepting state, 2: accepting state + do not move reader position
     */
    private void putState(String label, int type) {
        State<T> toPut = new State<>(label, type);
        assert states.putIfAbsent(label, toPut) == toPut : String.format("state with label %s already present", label);
    }

    /**
     * add a transition
     * @param fromLabel from state
     * @param toLabel to state
     * @param onInput list of inputs on which to take this transition
     */
    @SafeVarargs
    private void putTransition(String fromLabel, String toLabel, T... onInput) {
        assert states.containsKey(fromLabel) : "unknown 'from' state " + fromLabel;
        assert states.containsKey(toLabel) : "unknown 'to' state " + fromLabel;
        for (T input : onInput)
            states.get(fromLabel).putTransition(input, states.get(toLabel));
    }

    /**
     * add a transition to be taken when no other transition applies
     */
    private void putDefaultTransition(String fromLabel, String toLabel) {
        assert states.containsKey(fromLabel) : "unknown 'from' state " + fromLabel;
        assert states.containsKey(toLabel) : "unknown 'to' state " + fromLabel;
        states.get(fromLabel).putDefaultTransition(states.get(toLabel));
    }

    /**
     * @param label state to become new initial state
     */
    private void setInitial(String label) {
        assert this.initial == null : "cannot reassign initial state";
        assert states.get(label) != null : "unknown state " + label;
        this.initial = states.get(label);
    }
}
