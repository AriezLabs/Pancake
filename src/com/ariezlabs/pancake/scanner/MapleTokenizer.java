package com.ariezlabs.pancake.scanner;

import java.io.IOException;
import java.io.Reader;

public class MapleTokenizer {
    private final Reader r;
    private int currentChar;
    private StringBuilder literal;

    public MapleTokenizer(Reader r) {
        this.r = r;
    }

    public Token next() throws IOException {
        int seenSpaces = 0;
        literal = new StringBuilder();

        while (currentChar == ' ') {
            seenSpaces++;
            nextChar();
        }

        // Scanner FSM
        switch (currentChar) {
            case -1: // EOF
                return new Token(seenSpaces, 1, Token.Type.EOF, null);
            case 'c':
        }


        return new Token(seenSpaces, 1, Token.Type.SPACE, literal.toString());
    }

    /**
     * read next character from reader into currentChar,
     * if not a space append to literal
     */
    private void nextChar() throws IOException {
        currentChar = r.read();
        if(currentChar != ' ')
            literal.append((char) currentChar);
    }
}
