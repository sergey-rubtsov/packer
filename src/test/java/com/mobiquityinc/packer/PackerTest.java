package com.mobiquityinc.packer;

import org.junit.Test;

import static org.junit.Assert.*;

public class PackerTest {

    @Test
    public void pack() {
        ClassLoader classLoader = PackerTest.class.getClassLoader();
        String file = classLoader.getResource("input.txt").getPath();
        String result = Packer.pack(file);
        String expected =
                "4\n" +
                "-\n" +
                "2,7\n" +
                "8,9";
        assertEquals(expected, result);
    }
}
