package com.mobiquityinc.packer;

import com.mobiquityinc.model.Thing;
import org.junit.Test;

import java.util.Collections;
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
    public void packEmptyPackage() {
        List<Thing> things = Collections.emptyList();
        String result = Packer.processPackage(4, things);
        assertEquals("-", result);
        things = Parser.parseThings("(1,2,€3) (2,1,€3) (3,2,€6)");
        result = Packer.processPackage(0, things);
        assertEquals("-", result);
    }

    @Test
    public void packOptimalWeight() {
        List<Thing> things = Parser.parseThings("(1,2,€3) (2,1,€3) (3,2,€6)");
        String result = Packer.processPackage(4, things);
        assertEquals("2,3", result);
        things = Parser.parseThings("(1,90.72,€13) (2,33.8088,€40) (3,43.15,€10) (4,37.97,€16) " +
                "(5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)");
        result = Packer.processPackage(56, things);
        assertEquals("8,9", result);
        Collections.shuffle(things);
        result = Packer.processPackage(56, things);
        assertEquals("8,9", result);
        things = Parser.parseThings("(1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) " +
                "(5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95988998,€78)");
        result = Packer.processPackage(75, things);
        assertEquals("2,7", result);
        Collections.shuffle(things);
        result = Packer.processPackage(75, things);
        assertEquals("2,7", result);
    }

    @Test
    public void calculateFactor() {
        List<Thing> things = Parser.parseThings("(1,85,€29) (2,14,€74) (2,14,€74)");
        assertEquals(1, Packer.calculateFactor(things));
        things = Parser.parseThings("(1,85.1,€29) (2,14.5,€74) (2,14,€74)");
        assertEquals(10, Packer.calculateFactor(things));
        things = Parser.parseThings("(1,85.1,€29) (2,14.56999,€74) (2,14,€74)");
        assertEquals(100, Packer.calculateFactor(things));
    }

}
