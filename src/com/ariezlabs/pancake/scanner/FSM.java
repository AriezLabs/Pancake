package com.ariezlabs.pancake.scanner;


import java.util.ArrayList;
import java.util.HashMap;

public class FSM<T> {
    private State<T> initial;
    private State<T> current;
    private ArrayList<T> path;
    private ArrayList<State<T>> states;

    public FSM() {
        initial = new State<>(Token.Type.SPACE);
        states = new ArrayList<>();
        reset();
    }

    public Token.Type feed(T input) {
        path.add(input);
        return current == null ? Token.Type.UNKNOWN : (current = current.getTransition(input)).getType();
    }

    public ArrayList<T> getPath() {
        return path;
    }

    public void reset() {
        path = new ArrayList<>(100);
        current = initial;
    }

    /**
     * learn to recognize a symbol
     * @param symbol symbol literal
     * @param associatedType type of symbol
     */
    public void associate(ArrayList<T> symbol, Token.Type associatedType) {
        reset();
        State<T> entryPoint = current;
        int i;

        // if prefix states of symbol are present, move down that path
        for (i = 0; i < symbol.size() && current != null; i++) {
            entryPoint = current;
            feed(symbol.get(i));
        }

        // if symbol is prefix of previously learned symbol
        if (current != null) {
            assert current.getType() == Token.Type.UNKNOWN : "cannot override previously learned symbol";
            current.setType(associatedType);
        }

        // add chain of states for each remaining item of symbol
        while (i < symbol.size()) {
            entryPoint.addTransition(symbol.get(i), new State<>(Token.Type.UNKNOWN));
            entryPoint = entryPoint.getTransition(symbol.get(i));
        }

        entryPoint.setType(associatedType);
        reset();
    }

    public static class State<T> {
        private HashMap<T, State<T>> transitions;
        private State<T> defaultTransition;
        private Token.Type type;

        public State(Token.Type type) {
            this.type = type;
            this.transitions = new HashMap<>();
        }

        public State<T> addTransition(T input, State<T> to) {
            return transitions.putIfAbsent(input, to);
        }

        public void setDefaultTransition(State<T> defaultTransition) {
            this.defaultTransition = defaultTransition;
        }

        public State<T> getTransition(T in) {
            return transitions.getOrDefault(in, defaultTransition);
        }

        public Token.Type getType() {
            return type;
        }

        public void setType(Token.Type type) {
            this.type = type;
        }
    }
}
