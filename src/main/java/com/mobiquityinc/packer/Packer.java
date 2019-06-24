package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Package;
import com.mobiquityinc.model.Thing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Packer {

    /**
     * @param filePath an absolute path to a filename.
     * @return solution, which is the set of things that you put into the package
     * provided a list (column of items’ index numbers are separated by comma
     * or symbol '-' if the package can't take anything). E.g.
     * 4
     * -
     * 2,7
     * 8,9
     * @throws APIException if incorrect parameters are being passed.
     */
    public static String pack(String filePath) throws APIException {
        List<Package> packages = Parser.parseInputFile(filePath);
        return processPackages(packages);
    }

    /**
     * @param packages list of parsed packages.
     * @return a list of things that into the package
     * (list of items’ index numbers separated by comma converted to String)
     */
    private static String processPackages(List<Package> packages) {
        return packages.stream().map(p -> processPackage(p.getCapacity(), p.getThings()))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Determines set of things that you put into the package and sort them.
     * @param capacity weight limit of package.
     * @param things the list of all things for the package.
     * @return the sorted set of things' indexes need to be chosen,
     * or symbol '-' if package is empty.
     */
    protected static String processPackage(int capacity, List<Thing> things) {
        List<Integer> items = solveUnboundedKnapsackProblem(capacity, things);
        if (items.isEmpty()) {
            return "-";
        }
        Collections.sort(items);
        return items.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    /**
     * The algorithm determines which things to put into the package so that the
     * total weight is less than or equal to the package limit and the total cost
     * is as large as possible.
     * In case there is more than one thing with the same price it would choose
     * the thing which weights less.
     * @param capacity weight limit of package.
     * @param things the list of all things for the package.
     * @return the list of things' indexes need to be chosen.
     */
    private static List<Integer> solveUnboundedKnapsackProblem(int capacity, List<Thing> things) {
        if (things.isEmpty() || capacity == 0) {
            return Collections.emptyList();
        }
        int factor = calculateFactor(things);
        //to send a package which weights less in case there is more than one package with the
        //same price, we need to group them by cost and sort by weight.
        things.sort(Comparator.comparingInt(Thing::getCost).thenComparing(Thing::getWeight));
        List<Integer> items = new ArrayList<>();
        capacity = capacity * factor;
        int[][] matrix = new int[things.size() + 1][capacity + 1];
        for (int i = 0; i <= things.size(); i++) {
            for (int j = 0; j <= capacity; j++) {
                if (i == 0 || j == 0) {
                    matrix[i][j] = 0;
                } else {
                    Thing thing = things.get(i - 1);
                    if (thing.getWeight() * factor <= j) {
                        matrix[i][j] = Math.max(thing.getCost() +
                            matrix[i - 1][j - (int) (thing.getWeight() * factor)], matrix[i - 1][j]);
                    } else {
                        matrix[i][j] = matrix[i - 1][j];
                    }
                }
            }
        }
        int result = matrix[things.size()][capacity];
        for (int i = things.size(); i > 0 && result > 0; i--) {
            if (result != matrix[i - 1][capacity]) {
                items.add(things.get(i - 1).getIndex());
                result = result - things.get(i - 1).getCost();
                capacity = capacity - (int) things.get(i - 1).getWeight();
            }
        }
        return items;
    }

    /**
     * We can't effectively solve 0-1 Knapsack problem for non-integer
     * values of weights, so we need to scale them to integers and
     * for that the scale factor need to be calculated.
     * @param things list of parsed items.
     * @return factor value can be 1, 10 or 100.
     */
    protected static int calculateFactor(List<Thing> things) {
        int result = 1;
        for (Thing thing : things) {
            if (hasDecimal(thing.getWeight())) {
                result = 10;
            }
            if (hasCentesimal(thing.getWeight())) {
                return 100;
            }
        }
        return result;
    }

    private static boolean hasCentesimal(double number) {
        return (number * 10) % 1 != 0;
    }

    private static boolean hasDecimal(double number) {
        return number % 1 != 0;
    }

}
