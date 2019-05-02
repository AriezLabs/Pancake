package com.ariezlabs.pancake.scanner;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class MapleTokenizerTest {

    @org.junit.jupiter.api.Test
    void next() throws IOException {
        MapleTokenizer test = new MapleTokenizer(new StringReader("asd  daas   "));
        Token t;
        while ((t = test.next()).type != Token.Type.EOF)
            System.out.println(t);
        System.out.println(t);
    }
}