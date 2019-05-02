package com.ariezlabs.pancake.scanner;

import java.io.IOException;
import java.io.StringReader;

class SyrupTokenizerTest {

    @org.junit.jupiter.api.Test
    void next() throws IOException {
        SyrupTokenizer test = new SyrupTokenizer(new StringReader("asd  daas   "));
        Token t;
        while ((t = test.next()).type != Token.Type.EOF)
            System.out.println(t);
        System.out.println(t);
    }
}