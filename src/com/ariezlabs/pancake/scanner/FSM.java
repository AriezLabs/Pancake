package com.ariezlabs.pancake.scanner;


import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

public class FSM {
    private State initial;
    private State current;
    private Reader in;
    private StringBuilder path;
    private HashMap<String, State> states; // to ensure each state has unique labels
    private ArrayList<Integer> ignoredChars;

    /**
     * This constructor creates another FSM with hardcoded syntax that parses the language file and initializes
     * this FSM.
     * @param language Reader to read from
     */
    public FSM(Reader input, Reader language) throws IOException {
        FSM reader = new FSM();
        // construct FSM reading FSM...
        language.close();
        reset();
    }

    /**
     * TODO rework - should be something like nextToken(), use this under the hood at best
     * process input (do corresponding transition)
     * if input is in ignoredChars, do nothing
     * @return type of state reached, null if transition undefined or in undefined state already
     */
    public String nextToken() {
        int input;
        // remember to close input on reaching EOF
        return "";
    }

    public void reset() {
        path = new StringBuilder(100);
        current = initial;
    }

    // methods used for constructing FSM from file

    private FSM() {}

    private State putState(String label) {
        return states.putIfAbsent(label, new State(label));
    }

    private State putTransition(String fromLabel, String toLabel, Integer onInput) {
        if (states.containsKey(fromLabel) && states.containsKey(toLabel))
            return states.get(fromLabel).putTransition(onInput, states.get(toLabel));
        else
            throw new IllegalArgumentException(String.format("cannot put transition: unknown state labels %s and/or %s", fromLabel, toLabel));
    }

    private void putDefaultTransition(String fromLabel, String toLabel) {
        if (states.containsKey(fromLabel) && states.containsKey(toLabel))
            states.get(fromLabel).putDefaultTransition(states.get(toLabel));
        else
            throw new IllegalArgumentException(String.format("cannot put default transition: unknown state labels %s and/or %s", fromLabel, toLabel));
    }

    /**
     * Type parameter for label had to go in order to construct FSM from file
     */
    private static class State {
        private HashMap<Integer, State> transitions;
        private State defaultTransition;
        private String label;

        public State(String label) {
            this.label = label;
            this.transitions = new HashMap<>();
        }

        /**
         * add specific transition on input to state if not yet present
         * @return state to if no transition was set, or state transition was set to (no change undertaken)
         */
        State putTransition(Integer input, State to) {
            return transitions.putIfAbsent(input, to);
        }

        /**
         * set a transition taken on any input, shadowed by specific transitions
         * @param defaultTransition state to transition to by default
         */
        void putDefaultTransition(State defaultTransition) {
            assert this.defaultTransition == null : "cannot reassign default transition";
            this.defaultTransition = defaultTransition;
        }

        /**
         * @return state we transition to upon reading in, or null if none defined
         */
        State getTransition(Integer in) {
            return transitions.getOrDefault(in, defaultTransition);
        }

        String getLabel() {
            return label;
        }

        void setLabel(String label) {
            this.label = label;
        }
    }
}
