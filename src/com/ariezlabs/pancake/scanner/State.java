package com.ariezlabs.pancake.scanner;

import java.util.HashMap;

/**
 * Type parameter for label had to go in order to construct FSM from file
 */
class State {
    private HashMap<Integer, State> transitions;
    private State defaultTransition;
    private String label;
    private boolean accept;
    private boolean readNext;

    public State(String label, boolean accept, boolean readNext) {
        // if readNext is false, accept must be true, else  we'll run into a problem
        assert !readNext || accept : "non-accepting state cannot not read next";

        this.label = label;
        this.accept = accept;
        this.readNext = readNext;
        this.transitions = new HashMap<>();
    }

    /**
     * add specific transition on input to state if not yet present
     * @return state to if no transition was set, or state transition was set to (no change undertaken)
     */
    void putTransition(Integer input, State to) {
        assert transitions.putIfAbsent(input, to) == to : String.format("transition to state %s on %d already present", to.getLabel(), input);
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

    boolean isReadNext() {
        return readNext;
    }

    boolean accepts() {
        return accept;
    }
}
