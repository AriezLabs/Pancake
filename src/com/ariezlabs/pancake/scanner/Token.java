package com.ariezlabs.pancake.scanner;

public class Token {
    public final int spacesBefore; // for nested lists
    public final int count; // for h1-h6, code blocks etc.
    public final Type type;
    public final String literal;

    public Token(int spacesBefore, int count, Type type, String literal) {
        this.spacesBefore = spacesBefore;
        this.count = count;
        this.type = type;
        this.literal = literal;
    }

    @Override
    public String toString() {
        return String.format("Token \"%s\" (type: %s, count: %d, #spaces: %d)", literal, type, count,spacesBefore);
    }

    public enum Type {
        UNKNOWN,
        EOF,
        POUND,
        QUOTE,
        NEWLINE,
        SPACE,
        BACKTICK,
        UNDERSCORE,
        LBRACKET,
        RBRACKET,
        COLON,
        DASH,

    }
}
