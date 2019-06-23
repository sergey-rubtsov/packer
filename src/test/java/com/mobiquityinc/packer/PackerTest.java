package com.mobiquityinc.packer;

import com.mobiquityinc.model.Thing;
import org.junit.Test;

import java.util.List;

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

    @Test
    public void packOptimalWeight() {
        String test = "(1,2,€7) (2,1,€7) (3,2,€6)";
        List<Thing> things = Parser.parseThings(test);
        String result = Packer.processPackage(4, things);
        String expected = "2,3";
        assertEquals(expected, result);
    }

    @Test
    public void calculateFactor() {
        List<Thing> things = Parser.parseThings("(1,85,€29) (2,14,€74) (2,14,€74)");
        assertEquals(1, Packer.calculateFactor(things));
        things = Parser.parseThings("(1,85.1,€29) (2,14.5,€74) (2,14,€74)");
        assertEquals(10, Packer.calculateFactor(things));
        things = Parser.parseThings("(1,85.1,€29) (2,14.56,€74) (2,14,€74)");
        assertEquals(100, Packer.calculateFactor(things));
    }
}
