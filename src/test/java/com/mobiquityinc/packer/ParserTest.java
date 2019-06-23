package com.mobiquityinc.packer;

import com.mobiquityinc.model.Package;
import com.mobiquityinc.model.Thing;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsIterableContainingInOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.nio.charset.Charset;
import java.util.List;

public class ParserTest {

    @Test
    public void parsePackages() {
    }

    @Test
    public void parseThings() {
        String test = getTestUtfString("(1,85,€29) (2,14.55,€74)");
        List<Thing> result = Parser.parseThings(test);
        assertThat(result, IsCollectionWithSize.hasSize(2));
        Thing expected0 = Thing.builder().id(1).weight(85).cost(29).build();
        Thing expected1 = Thing.builder().id(2).weight(14.55).cost(74).build();
        assertThat(result, IsIterableContainingInOrder.contains(expected0, expected1));
    }

    private static String getTestUtfString(String text) {
        return new String(text.getBytes(), Charset.forName("UTF-8"));
    }

    @Test
    public void parsePackage() {
        String test = getTestUtfString("56 : (1,90.99,€13) (2,33.6,€40) (3,43,€10)");
        Package result = Parser.parsePackage(test);
        assertThat(result.getThings(), IsCollectionWithSize.hasSize(3));
        assertEquals(56, result.getCapacity());
    }
}
