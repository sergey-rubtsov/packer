package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Package;
import com.mobiquityinc.model.Thing;

import java.util.ArrayList;
import java.util.Collections;
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
        List<Package> packages = Parser.parsePackages(filePath);
        return processPackages(packages);
    }

    private static String processPackages(List<Package> packages) {
        return packages.stream().map(p -> processPackage(p.getCapacity(), p.getThings()))
                .collect(Collectors.joining("\n"));
    }

    /**
     * @param things - list of items
     *               We can't effectively solve 0-1 Knapsack problem for non-integer
     *               values of weights, so we need to scale them to integers and
     *               for that the factor need to be calculated
     * @return factor value
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

    protected static String processPackage(int capacity, List<Thing> things) {
        List<Integer> items = solveUnboundedKnapsackProblem(capacity, things);
        if (items.isEmpty()) {
            return "-";
        }
        return items.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private static List<Integer> solveUnboundedKnapsackProblem(int capacity, List<Thing> things) {
        int factor = calculateFactor(things);
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
                items.add(things.get(i - 1).getId());
                result = result - things.get(i - 1).getCost();
                capacity = capacity - (int) things.get(i - 1).getWeight();
            }
        }
        Collections.sort(items);
        return items;
    }

}
