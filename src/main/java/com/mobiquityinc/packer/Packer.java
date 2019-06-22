package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Package;
import com.mobiquityinc.model.Thing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
     * @param things we can't effective solve 0-1 Knapsack problem for double weights,
     *               so we need to round them to integers and scale all weights
     * @return scale value
     */
    private static int scaleWeights(Map<Integer, Thing> things) {
        things.values().forEach(v -> {
            double weight = v.getWeight() * 100;
            double basis = Math.floor(weight) * 100;
            if (weight - basis > 10) {
                System.out.println(v.getWeight() + ">");
            } else System.out.println(v.getWeight() + "<");

        });
        return 1;
    }

    private static String processPackage(int capacity, Map<Integer, Thing> things) {
        List<Integer> items = solveUnboundedKnapsackProblem(capacity, things);
        if (items.isEmpty()) {
            return "-";
        }
        return items.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private static List<Integer> solveUnboundedKnapsackProblem(int capacity, Map<Integer, Thing> things) {
        int factor = scaleWeights(things);
        capacity = capacity * factor;
        int matrix[][] = new int[things.size() + 1][capacity + 1];
        for (int i = 0; i <= things.size(); i++) {
            for (int j = 0; j <= capacity; j++) {
                if (i == 0 || j == 0) {
                    matrix[i][j] = 0;
                } else {
                    Thing thing = things.get(i - 1);
                    if (thing.getWeight() <= j) {
                        matrix[i][j] = Math.max(thing.getCost() +
                            matrix[i - 1][j - (int) Math.round(thing.getWeight())], matrix[i - 1][j]);
                    } else {
                        matrix[i][j] = matrix[i - 1][j];
                    }
                }

            }
        }
        return new ArrayList<>();
    }

}
