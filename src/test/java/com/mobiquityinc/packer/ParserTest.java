package com.mobiquityinc.packer;

import com.mobiquityinc.model.Thing;
import org.hamcrest.collection.IsIterableWithSize;
import org.hamcrest.collection.IsMapContaining;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Map;

public class ParserTest {

    @Test
    public void parsePackages() {
    }

    @Test
    public void parseThings() {
        String test = getTestUtfString("(1,85,€29) (2,14.55,€74)");
        Map<Integer, Thing> result = Parser.parseThings(test);
        assertThat(result.entrySet(), IsIterableWithSize.iterableWithSize(2));
        assertThat(result, IsMapContaining.hasKey(1));
        assertThat(result, IsMapContaining.hasKey(2));
        Thing expected0 = Thing.builder().weight(85).cost(29).build();
        assertThat(result, IsMapContaining.hasValue(expected0));
        Thing expected1 = Thing.builder().weight(14.55).cost(74).build();
        assertThat(result, IsMapContaining.hasValue(expected1));
    }

    private String getTestUtfString(String text) {
        return new String(text.getBytes(), Charset.forName("UTF-8"));
    }
}
