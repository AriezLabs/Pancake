package com.ariezlabs.pancake.scanner;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

/**
 * Not a FSM in the traiditional sense since it deals with concatenated symbols potentially without
 * a specific separator. Allows accepting states that won't move the current reader position ahead
 * so that e.g. a text literal can be terminated by a "[" without dropping the "[" by reading ahead.
 */
public class FSM {
    private State initial;
    private State current;
    private Reader in;
    private StringBuilder path;
    private HashMap<String, State> states = new HashMap<>(); // for access and to ensure each state has unique labels

    /**
     * This constructor creates another FSM with a hardcoded language that parses the language file and initializes
     * this FSM.
     * @param fsmDefinition Reader to read from
     */
    public FSM(Reader input, Reader fsmDefinition) throws IOException {
        FSM reader = new FSM(fsmDefinition);

        reader.putState("initial", 0);
        reader.putState("dash", 1);
        reader.putState("newline", 1);
        reader.putState("colon", 1);
        reader.putState("pound", 1);
        reader.putState("eof", 1);
        reader.putState("lbracket", 1);
        reader.putState("rbracket", 1);
        reader.putState("inText", 0);
        reader.putState("text", 2);

        reader.putTransition("initial", "dash", '-');
        reader.putTransition("initial", "newline", '\n');
        reader.putTransition("initial", "colon", ':');
        reader.putTransition("initial", "pound", '#');
        reader.putTransition("initial", "eof", -1);
        reader.putTransition("initial", "lbracket", '[');
        reader.putTransition("initial", "rbracket", ']');

        reader.putDefaultTransition("initial", "inText");
        reader.putDefaultTransition("inText", "inText");
        reader.putTransition("inText", "text", '-', '\n', ':', '#', -1, '[', ']');

        fsmDefinition.close();
        reset();
    }

    /**
     * process input (do corresponding transition)
     * if input is in ignoredChars, do nothing
     * @return type of state reached, null if transition undefined or in undefined state already
     */
    public String nextToken() throws IOException {
        int readerPosition = in.read();

        //TODO should look something like...
        while (!current.accepts()) {
            current = current.getTransition(readerPosition);

            if (current == null) // unrecognized symbol
                return null;

            if (current.isReadNext())
                readerPosition = in.read();
        }

        // remember to close input on reaching EOF
        return current.getLabel();
    }

    /**
     * Resets FSM to initial state and forgets history. Does not reset reader position.
     */
    public void reset() {
        path = new StringBuilder(100);
        current = initial;
    }

    // methods used for constructing FSM from file

    private FSM(Reader fsmDefinition) {
        this.in = fsmDefinition;
        reset();
    }

    /**
     * @param label label of new state
     * @param type 0: nonaccepting state, 1: accepting state, 2: accepting state + do not move reader position
     */
    private void putState(String label, int type) {
        State toPut = new State(label, type >>> 1 == 1, (type & 1) == 1);
        assert states.putIfAbsent(label, toPut) == toPut : String.format("state with label %s already present", label);
    }

    private void putTransition(String fromLabel, String toLabel, int... onInput) {
        if (states.containsKey(fromLabel) && states.containsKey(toLabel))
            for (int input : onInput)
                states.get(fromLabel).putTransition(input, states.get(toLabel));
        else
            throw new IllegalArgumentException(String.format("cannot put transition: unknown states %s and/or %s", fromLabel, toLabel));
    }

    private void putDefaultTransition(String fromLabel, String toLabel) {
        if (states.containsKey(fromLabel) && states.containsKey(toLabel))
            states.get(fromLabel).putDefaultTransition(states.get(toLabel));
        else
            throw new IllegalArgumentException(String.format("cannot put default transition: unknown state labels %s and/or %s", fromLabel, toLabel));
    }

    private void setInitial(State initial) {
        assert this.initial == null : "cannot reassign initial state";
        this.initial = initial;
    }

}
