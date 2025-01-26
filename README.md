# Genetic-Algorithms

This project implements Genetic Algorithms for solving the knapsack problem. It includes two main classes: `GA` and `GALocalSearch`, which implement the genetic algorithm and genetic algorithm with local search, respectively.

## Files

- `Main.java`: The main entry point for running the simulations.
- `GA.java`: Implements the basic genetic algorithm.
- `GALocalSearch.java`: Implements the genetic algorithm with local search.
- `GA.txt`: Contains results from running the basic genetic algorithm.
- `GALocal.txt`: Contains results from running the genetic algorithm with local search.
- `Calculate.xlsx`: Excel file for calculations.
- `Known Optimums.xlsx`: Excel file containing known optimum values.
- `jacocoagent.jar`, `jacocoant.jar`, `jacococli.jar`: JAR files for code coverage analysis.
- `knapPI_1_100_1000_1`: Data file for the knapsack problem.
- `f1_l-d_kp_10_269` to `f10_l-d_kp_20_879`: Additional data files for the knapsack problem.
- `Technical Specification`: File that describes algorithm specification.


## Usage

To run the simulations, execute the `Main` class. The `main` method in `Main.java` runs both the basic genetic algorithm and the genetic algorithm with local search on the specified data file.

```sh
javac Main.java
java Main