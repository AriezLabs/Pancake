package com.ariezlabs.pancake.scanner;

import java.io.IOException;
import java.io.Reader;

public class Scanner {
    private final Reader input;
    private final FSM<Integer> fsm;

    Scanner(Reader input, FSM<Integer> fsm) {
        this.fsm = fsm;
        this.input = input;
    }

    public Scanner(Reader input, Reader syntaxFile) throws IOException {
        this.input = input;
        this.fsm = new FSM<>(syntaxFile);
    }

    /**
     * process input (do corresponding transition)
     * if input is in ignoredChars, do nothing
     * @return type of state reached, null if transition undefined or in undefined state already
     */
    public String nextToken() throws IOException {
        int readerPosition = input.read();

        //TODO should look something like...
        while (!fsm.accepts()) {
            fsm.transition(readerPosition);

            if (fsm.inErrorState()) // unrecognized symbol
                return null;

            if (fsm.inReadNextState())
                readerPosition = input.read();
        }

        // remember to close input on reaching EOF
        return fsm.currentStateLabel();
    }

    public String getLiteral() {
        return String.valueOf(fsm.getPath().stream()
                .filter(n -> n != -1)
                .map(n -> (char) (int) n));
    }
}
