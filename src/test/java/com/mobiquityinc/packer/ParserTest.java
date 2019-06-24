package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Package;
import com.mobiquityinc.model.Thing;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsIterableContainingInOrder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.charset.Charset;
import java.util.List;

public class ParserTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseInputFileError() {
        thrown.expect(APIException.class);
        thrown.expectMessage(equalTo("The file with the specified pathname " +
                "'wrong_file_path' does not exist or for some reason is inaccessible"));
        Parser.parseInputFile("wrong_file_path");
    }

    @Test
    public void parseThings() {
        String test = getTestUtfString("(1,85,€29) (2,14.55,€74)");
        List<Thing> result = Parser.parseThings(test);
        assertThat(result, IsCollectionWithSize.hasSize(2));
        Thing expected0 = Thing.builder().index(1).weight(85).cost(29).build();
        Thing expected1 = Thing.builder().index(2).weight(14.55).cost(74).build();
        assertThat(result, IsIterableContainingInOrder.contains(expected0, expected1));
    }

    @Test
    public void parseThingsError() {
        String test = getTestUtfString("(1,85,€29) 2,14.55,€74)");
        thrown.expect(APIException.class);
        thrown.expectMessage(equalTo("Sequence '(1,85,€29) 2,14.55,€74)' doesn't match the pattern."));
        Parser.parseThings(test);
    }
    @Test
    public void parseThingsMaxItemWeightError() {
        String test = getTestUtfString("(1,101,€29) (2,14.55,€74)");
        thrown.expect(APIException.class);
        thrown.expectMessage(startsWith("Max weight of an item must be ≤ 100, but input weight is 101"));
        Parser.parseThings(test);
    }

    @Test
    public void parseThingsMaxItemCostError() {
        String test = getTestUtfString("(1,1,€101) (2,14.55,€74)");
        thrown.expect(APIException.class);
        thrown.expectMessage(equalTo("Max cost of an item must be ≤ 100, but input cost is 101"));
        Parser.parseThings(test);
    }

    @Test
    public void parseThingsMaxItemsError() {
        String test = getTestUtfString("(1,90.72,€13) (2,33.8088,€40) (3,43.15,€10) (4,37.97,€16) " +
                "(5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)" +
                "(10,90.72,€13) (11,90.72,€13) (12,33.8088,€40) (13,43.15,€10) (14,37.97,€16) (15,46.81,€36)" +
                "(16,48.77,€79)");
        thrown.expect(APIException.class);
        thrown.expectMessage(equalTo("Max number of items must be 15, but input contains 16 items"));
        Parser.parseThings(test);
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

    @Test
    public void parsePackageError() {
        String test = getTestUtfString("56  (1,90.99,€13) (2,33.6,€40)");
        thrown.expect(APIException.class);
        thrown.expectMessage(equalTo("Unable to parse line '56  (1,90.99,€13) (2,33.6,€40)'"));
        Parser.parsePackage(test);
    }

    @Test
    public void parsePackageMaxWeightError() {
        String test = getTestUtfString("101 : (1,90.99,€13) (2,33.6,€40)");
        thrown.expect(APIException.class);
        thrown.expectMessage(equalTo("Max weight that a package can take must be ≤ 100, but input capacity is 101"));
        Parser.parsePackage(test);
    }
}
