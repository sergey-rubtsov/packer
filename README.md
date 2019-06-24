# Package Challenge.

## Problem
It is needed to send a package with different things.
Each thing inside the package has such parameters as index number, weight and cost. 
The package has a weight limit.

The goal is to determine which things to put into the package so that the total weight is less than or equal to the package limit and the total cost is as large as possible.

It would be preferable to send a package which weights less in case there is more than one package with the same price.
## Solution
The problem is well known decision problem, form of the knapsack problem.

It is NP-complete, thus there is no known algorithm both correct and fast (polynomial-time) in all cases.
This dynamic programming solution uses an algorithm for solving the 0/1 knapsack problem because of its high performance.

It runs in pseudo-polynomial time.

Time Complexity: O(nW) and O(nW) space where n is the number of items and W is the capacity of package.
## Input sample
The method with signature
``` public static String pack(String filePath) throws APIException ```
accepts as its argument a path to a filename.

The input file contains several lines. Each line is one package.
Each line contains the weight that the package can take (before the colon) and the list of things you
need to choose. Each thing is enclosed in parentheses where the 1st number is a thing's index number,
the 2nd is its weight and the 3rd is its cost. E.g.
```
81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)
8 : (1,15.3,€34)
75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)
56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)
```
## Output sample
For each set of things that we put into the package provided a list (items’ index numbers are separated by comma). E.g.
```
4
-
2,7
8,9
```
## Constraints
1. It throws an APIException if incorrect are being passed.
2. Max weight that a package can take is ≤ 100.
3. There might be up to 15 items you need to choose from.
4. Max weight and cost of an item is ≤ 100.
5. There is no effective algorithm for solving this problem for floating point weight values.
Therefore, the weight values and the accuracy of calculations are rounded to hundredths.

## Build dependency
This is gradle project, for compiling dependency file just run 

> gradle build

Gradle will build jar file in directory /build/libs/packer-0.0.1.jar

Project uses Lombok annotations, the IDE should be configured to support them.
Test coverage is 100%.