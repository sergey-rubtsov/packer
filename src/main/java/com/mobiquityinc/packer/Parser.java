package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Package;
import com.mobiquityinc.model.Thing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final Pattern PACKAGE_PATTERN = Pattern.compile("(\\d+)\\s*[:]\\s*(.*)\\n");
    private static final Pattern ITEMS_PATTERN = Pattern
            .compile("[(](\\d+)[,](\\d+([.]\\d+)?)[,]\\p{Sc}(\\d+)[)]", Pattern.UNICODE_CHARACTER_CLASS);

    protected static List<Package> parsePackages(String filePath) {
        try {
            Scanner scanner = new Scanner(new File(filePath), "UTF-8");
            scanner.useDelimiter(System.getProperty("line.separator"));
            List<Package> result = new ArrayList<>();
            while (scanner.hasNext()) {
                String[] s = scanner.next().split(":");
                Package item = Package.builder()
                        .capacity(Integer.valueOf(s[0].trim()))
                        .things(parseThings(s[1].trim()))
                        .build();
                result.add(item);
            }
            return result;
        } catch (FileNotFoundException e) {
            throw new APIException("File not found", e);
        }
    }

    /**
     * @param things contains one or more items in brackets.
     *               The Java regular expression API works on the char type
     *               if we have UTF-8 data we will need to transcode it to
     *               UTF-16 on input if this is not already being done
     * @return the list of things need to be chosen.
     */
    protected static Map<Integer, Thing> parseThings(String things) {
        Matcher matcher = ITEMS_PATTERN.matcher(things);
        Map<Integer, Thing> result = new HashMap<>();
        while (matcher.find()) {
            Integer index = Integer.valueOf(matcher.group(1));
            double weight = Double.parseDouble(matcher.group(2));
            int cost = Integer.parseInt(matcher.group(4));
            result.put(index, Thing.builder().weight(weight).cost(cost).build());
        }
        return result;
    }

}
