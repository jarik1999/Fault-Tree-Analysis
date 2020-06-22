package Evaluation;

import Algorithms.Algorithm;
import AttackTree.AT;
import Examples.ATExamples;
import Structures.AttackTree.Type;
import Structures.Traversal.Entry;
import Structures.Traversal.EntryOrdering;

import java.util.ArrayList;

import static Evaluation.Helpers.combine;

public class Evaluation {

    /**
     * Evaluate certain AT. Contains the evaluations of two ATs that were used in the paper. They correspond
     * to AT 1 and AT 2. We should not that better execution times are obtained in Linux. On windows, there
     * might be some deviations in results as the OS might interrupt the program causing variation in execution time.
     * This can results in times like (0, 0, 67, 0, 0).
     * @param args, no system arguments
     */
    public static void main(String[] args) {
        // Evaluate Uppaal AT example from the paper
        evaluateATSAND("Uppaal_Example.json");

        // Evaluate Figure 3 from the paper
        evaluateATSAND("Paper_Figure_3.json");
    }

    /**
     * Evaluate an AT by combining with the SAND-gate
     * @param name, name of the AT
     */
    private static void evaluateATSAND(String name) {
        evaluationPareto(ATExamples.openJSON(name));
        evaluationTime(ATExamples.openJSON(name));
        evaluationCost(ATExamples.openJSON(name));
        printUppaal(ATExamples.openJSON(name), 3, Type.Sand);
    }


    /**
     * Evaluate all algorithms and gates for an AT
     * @param at, the AT to evaluate
     */
    private static void evaluationAll(AT at) {
        System.out.println("--- Conventional - Time - OR");
        evaluateLinear(at.copy(), at, Type.Or, 20, 1000000000, 1, EntryOrdering.time, false);
        System.out.println("--- Conventional - Time - AND");
        evaluateLinear(at.copy(), at, Type.And, 20, 1000000000, 1, EntryOrdering.time, false);
        System.out.println("--- Conventional - Time - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000000000, 1, EntryOrdering.time, false);
        System.out.println("--- Conventional - Cost - OR");
        evaluateLinear(at.copy(), at, Type.Or, 20, 1000000000, 1, EntryOrdering.cost, false);
        System.out.println("--- Conventional - Cost - AND");
        evaluateLinear(at.copy(), at, Type.And, 20, 1000000000, 1, EntryOrdering.cost, false);
        System.out.println("--- Conventional - Cost - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000000000, 1, EntryOrdering.cost, false);
        System.out.println("--- Conventional - Pareto - OR");
        evaluateLinear(at.copy(), at, Type.Or, 20, 1000000000, Integer.MAX_VALUE, EntryOrdering.cost, false);
        System.out.println("--- Conventional - Pareto - AND");
        evaluateLinear(at.copy(), at, Type.And, 20, 1000000000, Integer.MAX_VALUE, EntryOrdering.cost, false);
        System.out.println("--- Conventional - Pareto - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000000000, Integer.MAX_VALUE, EntryOrdering.cost, false);

        System.out.println("--- Subsuming - Time - OR");
        evaluateLinear(at.copy(), at, Type.Or, 20, 1000000000, 1, EntryOrdering.time, true);
        System.out.println("--- Subsuming - Time - AND");
        evaluateLinear(at.copy(), at, Type.And, 20, 1000000000, 1, EntryOrdering.time, true);
        System.out.println("--- Subsuming - Time - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000000000, 1, EntryOrdering.time, true);
        System.out.println("--- Subsuming - Cost - OR");
        evaluateLinear(at.copy(), at, Type.Or, 20, 1000000000, 1, EntryOrdering.cost, true);
        System.out.println("--- Subsuming - Cost - AND");
        evaluateLinear(at.copy(), at, Type.And, 20, 1000000000, 1, EntryOrdering.cost, true);
        System.out.println("--- Subsuming - Cost - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000000000, 1, EntryOrdering.cost, true);
        System.out.println("--- Subsuming - Pareto - OR");
        evaluateLinear(at.copy(), at, Type.Or, 20, 1000000000, Integer.MAX_VALUE, EntryOrdering.cost, true);
        System.out.println("--- Subsuming - Pareto - AND");
        evaluateLinear(at.copy(), at, Type.And, 20, 1000000000, Integer.MAX_VALUE, EntryOrdering.cost, true);
        System.out.println("--- Subsuming - Pareto - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000000000, Integer.MAX_VALUE, EntryOrdering.cost, true);

    }

    /**
     * Evaluate data for Pareto Curves on ATs combined with the SAND-gate
     * @param at, the AT to evaluate
     */
    private static void evaluationPareto(AT at) {
        System.out.println("--- Conventional - Pareto - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000000000, Integer.MAX_VALUE, EntryOrdering.cost, false);
        System.out.println("--- Subsuming - Pareto - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000000000, Integer.MAX_VALUE, EntryOrdering.cost, true);
    }

    /**
     * Evaluate data for Pareto Curves on ATs combined with the OR-gate
     * @param at, the AT to evaluate
     */
    private static void evaluationParetoOr(AT at) {
        System.out.println("--- Conventional - Pareto - OR");
        evaluateLinear(at.copy(), at, Type.Or, 20, 1000000000, Integer.MAX_VALUE, EntryOrdering.cost, false);
        System.out.println("--- Subsuming - Pareto - OR");
        evaluateLinear(at.copy(), at, Type.Or, 20, 1000000000, Integer.MAX_VALUE, EntryOrdering.cost, true);
    }

    /**
     * Evaluate data for Times on ATs combined with the SAND-gate
     * @param at, the AT to evaluate
     */
    private static void evaluationTime(AT at) {
        System.out.println("--- Conventional - Time - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000000000, 1, EntryOrdering.time, false);

        System.out.println("--- Subsuming - Time - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000000000, 1, EntryOrdering.time, true);
    }

    /**
     * Evaluate data for Costs on ATs combined with the SAND-gate
     * @param at, the AT to evaluate
     */
    private static void evaluationCost(AT at) {
        System.out.println("--- Conventional - Cost - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000000000, 1, EntryOrdering.cost, false);

        System.out.println("--- Subsuming - Cost - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000000000, 1, EntryOrdering.cost, true);
    }

    /**
     * Evaluate an AT by linearly scaling it up. Each AT is evaluated five times.
     * Ends if the maximum amount of replications is exceeded or the execution time of an AT exceeds the maximum time.
     * In the end it prints the sets of nodes, times and results.
     * @param at, the AT to evaluate
     * @param combine, the AT to combine with at each replication
     * @param type, the gate to combine ATs with for linear scaling
     * @param maxAmount, maximum amount of replications for the AT
     * @param maxTime, maximum time for execution (nanoseconds)
     * @param k, amount of solutions to compute
     * @param eo, ordering of the entries (cost/time)
     * @param subsuming, whether to perfrom subsuming
     */
    private static void evaluateLinear(AT at, AT combine, Type type, int maxAmount, long maxTime, int k, EntryOrdering eo, boolean subsuming) {
        for (int i = 0; i < 5; i++) {
            ArrayList<Result> results = new ArrayList<>();
            evaluateLinear(at.copy(), combine, type, maxAmount, maxTime, k, eo, subsuming, results);

            System.out.println("- Nodes - ");
            for (Result r: results) System.out.println(r.totalNodes);
            System.out.println("- Time - ");
            for (Result r: results) System.out.println(r.time);
            System.out.println("- Results -");
            for (Result r: results) System.out.println(r.resultSize);
        }
    }

    /**
     * Evaluate an AT by linearly scaling it up. End if the maximum amount of replications is exceeded or the execution
     * time of an AT exceeds the maximum time. In the end it prints the sets of nodes, times and results.
     * @param at, the AT to evaluate
     * @param combine, the AT to combine with at each replication
     * @param type, the gate to combine ATs with for linear scaling
     * @param maxAmount, maximum amount of replications for the AT
     * @param maxTime, maximum time for execution (nanoseconds)
     * @param k, amount of solutions to compute
     * @param eo, ordering of the entries (cost/time)
     * @param subsuming, whether to perform subsuming
     * @param results, store current results of the algorithm
     */
    private static void evaluateLinear(AT at, AT combine, Type type, int maxAmount, long maxTime, int k, EntryOrdering eo, boolean subsuming, ArrayList<Result> results) {
        if (maxAmount == 0) return;
        results.add(evaluate(at, k, eo, subsuming));

        if (results.get(results.size() - 1).time > maxTime) return;

        evaluateLinear(combine(at, combine.copy(), type), combine, type, maxAmount - 1, maxTime, k ,eo, subsuming, results);
    }

    /**
     * Evaluate result of a single AT
     * @param at, the AT to evaluate
     * @param k, amount of solutions to compute
     * @param eo, ordering of Entries (cost/time)
     * @param subsuming, whether to perform subsuming
     * @return Result of the AT
     */
    private static Result evaluate(AT at, int k, EntryOrdering eo, boolean subsuming) {
        long time = System.nanoTime();
        ArrayList<Entry> result = Algorithm.evaluate(at, eo, k, subsuming, false);
        time = System.nanoTime() - time;

        return new Result(time, at.getTotalNodes(at.getAttackTree()), result.size());
    }

    /**
     * Print the Uppaal files for a certain amount of replications
     * @param at, the AT to print replications from
     * @param amount, amount of replications
     * @param gate, gate to combine the AT with
     */
    private static void printUppaal(AT at, int amount, Type gate) {
        AT combine = at.copy();
        for (int i = 0; i < amount; i++) {
            System.out.println(combine.toUPPAAL());
            combine = combine(combine, at.copy(), gate);
        }
    }

    /**
     * Result of an AT. Stores execution time in nanoseconds, the nodes in the AT, and the amount of solutions obtained.
     */
    private static class Result {
        long time;
        int totalNodes;
        int resultSize;

        Result(long time, int totalNodes, int resultSize) {
            this.time = time;
            this.totalNodes = totalNodes;
            this.resultSize = resultSize;
        }

        public String toString() {
            return "(" + totalNodes + ", " + resultSize + ", " + time + ")";
        }
    }

}
