package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Package;
import com.mobiquityinc.model.Thing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final int MAX_PACKAGE_WEIGHT = 100;
    private static final int MAX_ITEMS_NUMBER = 15;
    private static final int MAX_ITEM_COST_AND_WEIGHT = 100;

    private static final Pattern PACKAGE_PATTERN = Pattern.compile("(\\d+)\\s*[:](.*)");
    private static final Pattern ALL_ITEMS_PATTERN = Pattern.compile(
            "([(](\\d+)[,](\\d+([.]\\d+)?)[,]\\p{Sc}(\\d+)[)]\\s*)+", Pattern.UNICODE_CHARACTER_CLASS);
    private static final Pattern ITEM_PATTERN = Pattern.compile(
            "[(](\\d+)[,](\\d+([.]\\d+)?)[,]\\p{Sc}(\\d+)[)]", Pattern.UNICODE_CHARACTER_CLASS);

    /**
     * @param filePath a path to a filename.
     *               The Java regular expression API works on the char type.
     *               If we have UTF-8 data we will need to transcode it to
     *               UTF-16 on input if this is not already being done.
     * @return list of parsed cases.
     * @throws APIException if file does not exist or for some reason is inaccessible.
     */
    protected static List<Package> parseInputFile(String filePath) throws APIException {
        try {
            Scanner scanner = new Scanner(new File(filePath), "UTF-8");
            scanner.useDelimiter(System.getProperty("line.separator"));
            List<Package> result = new ArrayList<>();
            while (scanner.hasNext()) {
                String line = scanner.next();
                result.add(parsePackage(line));
            }
            return result;
        } catch (FileNotFoundException e) {
            throw new APIException(String.format("The file with the specified pathname '%s' " +
                    "does not exist or for some reason is inaccessible", filePath), e);
        }
    }

    /**
     * @param things a line contains the weight that the package can take (before the colon)
     *               and the list of things need to be chosen.
     * @return parsed Package object
     * @throws APIException if the line doesn't match the pattern or weight of the package
     * exceeds constraint.
     */
    protected static Package parsePackage(String things) throws APIException {
        Matcher matcher = PACKAGE_PATTERN.matcher(things);
        if (matcher.find()) {
            int capacity = Integer.valueOf(matcher.group(1));
            if (capacity > MAX_PACKAGE_WEIGHT) {
                throw new APIException(String.format("Max weight that a package can take must be ≤ 100, " +
                        "but input capacity is %d", capacity));
            }
            return Package.builder()
                    .capacity(capacity)
                    .things(parseThings(matcher.group(2).trim()))
                    .build();
        } else {
            throw new APIException(String.format("Unable to parse line '%s'", things));
        }
    }

    /**
     * @param things a list contains one or more items in brackets.
     * @return the list of things need to be chosen.
     * @throws APIException if the list of things doesn't match the pattern
     * or if number of all items, weight or cost of any item exceeds constraints.
     */
    protected static List<Thing> parseThings(String things) throws APIException {
        if (!ALL_ITEMS_PATTERN.matcher(things).matches()) {
            throw new APIException(String.format("Sequence '%s' doesn't match the pattern.", things));
        }
        List<Thing> result = new ArrayList<>();
        Matcher matcher = ITEM_PATTERN.matcher(things);
        while (matcher.find()) {
            int index = Integer.parseInt(matcher.group(1));
            double weight = Double.parseDouble(matcher.group(2));
            if (weight > MAX_ITEM_COST_AND_WEIGHT) {
                throw new APIException(String.format("Max weight of an item must be ≤ 100, " +
                        "but input weight is %.2f", weight));
            }
            int cost = Integer.parseInt(matcher.group(4));
            if (cost > MAX_ITEM_COST_AND_WEIGHT) {
                throw new APIException(String.format("Max cost of an item must be ≤ 100, " +
                        "but input cost is %d", cost));
            }
            result.add(Thing.builder().index(index).weight(weight).cost(cost).build());
        }
        if (result.size() > MAX_ITEMS_NUMBER) {
            throw new APIException(String.format("Max number of items must be 15, " +
                    "but input contains %d items", result.size()));
        }
        return result;
    }

}
