package Evaluation;

import Algorithms.Algorithm;
import AttackTree.AT;
import BDD.BDD;
import Examples.AT_Examples;
import Structures.AttackTree.AttackTree;
import Structures.AttackTree.Type;
import Structures.Traversal.Entry;
import Structures.Traversal.EntryOrdering;

import java.util.ArrayList;

import static Evaluation.Helpers.combine;

public class Evaluation {
    public static void main(String[] args) {
        // Uppaal example - SAND
//        evaluationPareto(AT_Examples.openJSON("Uppaal_Example.json"));
//        evaluationTime(AT_Examples.openJSON("Uppaal_Example.json"));
//        evaluationCost(AT_Examples.openJSON("Uppaal_Example.json"));
//        printUppaal(AT_Examples.openJSON("Uppaal_Example.json"), 3);


        // Figure 3 - SAND
//        evaluationPareto(AT_Examples.openJSON("Paper_Figure_3.json"));
//        evaluationCost(AT_Examples.openJSON("Paper_Figure_3.json"));
//        evaluationTime(AT_Examples.openJSON("Paper_Figure_3.json"));
//        printUppaal(AT_Examples.openJSON("Paper_Figure_3.json"), 3);

        // Figure 3 - OR
        printUppaal(AT_Examples.openJSON("Paper_Figure_3.json"), 3, Type.Or);
    }

    /**
     * Evaluate all data
     * @param at
     */
    private static void evaluationAll(AT at) {
        System.out.println("--- Conventional - Time - OR");
        evaluateLinear(at.copy(), at, Type.Or, 20, 1000, 1, EntryOrdering.time, false, false);
        System.out.println("--- Conventional - Time - AND");
        evaluateLinear(at.copy(), at, Type.And, 20, 1000, 1, EntryOrdering.time, false, false);
        System.out.println("--- Conventional - Time - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000, 1, EntryOrdering.time, false, false);
        System.out.println("--- Conventional - Cost - OR");
        evaluateLinear(at.copy(), at, Type.Or, 20, 1000, 1, EntryOrdering.cost, false, false);
        System.out.println("--- Conventional - Cost - AND");
        evaluateLinear(at.copy(), at, Type.And, 20, 1000, 1, EntryOrdering.cost, false, false);
        System.out.println("--- Conventional - Cost - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000, 1, EntryOrdering.cost, false, false);
        System.out.println("--- Conventional - Pareto - OR");
        evaluateLinear(at.copy(), at, Type.Or, 20, 1000, Integer.MAX_VALUE, EntryOrdering.cost, false, false);
        System.out.println("--- Conventional - Pareto - AND");
        evaluateLinear(at.copy(), at, Type.And, 20, 1000, Integer.MAX_VALUE, EntryOrdering.cost, false, false);
        System.out.println("--- Conventional - Pareto - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000, Integer.MAX_VALUE, EntryOrdering.cost, false, false);

        System.out.println("--- Subsuming - Time - OR");
        evaluateLinear(at.copy(), at, Type.Or, 20, 1000, 1, EntryOrdering.time, true, false);
        System.out.println("--- Subsuming - Time - AND");
        evaluateLinear(at.copy(), at, Type.And, 20, 1000, 1, EntryOrdering.time, true, false);
        System.out.println("--- Subsuming - Time - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000, 1, EntryOrdering.time, true, false);
        System.out.println("--- Subsuming - Cost - OR");
        evaluateLinear(at.copy(), at, Type.Or, 20, 1000, 1, EntryOrdering.cost, true, false);
        System.out.println("--- Subsuming - Cost - AND");
        evaluateLinear(at.copy(), at, Type.And, 20, 1000, 1, EntryOrdering.cost, true, false);
        System.out.println("--- Subsuming - Cost - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000, 1, EntryOrdering.cost, true, false);
        System.out.println("--- Subsuming - Pareto - OR");
        evaluateLinear(at.copy(), at, Type.Or, 20, 1000, Integer.MAX_VALUE, EntryOrdering.cost, true, false);
        System.out.println("--- Subsuming - Pareto - AND");
        evaluateLinear(at.copy(), at, Type.And, 20, 1000, Integer.MAX_VALUE, EntryOrdering.cost, true, false);
        System.out.println("--- Subsuming - Pareto - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000, Integer.MAX_VALUE, EntryOrdering.cost, true, false);

    }

    /**
     * Evaluate data for graphs of SAND
     * @param at
     */
    private static void evaluationPareto(AT at) {
        System.out.println("--- Conventional - Pareto - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 5000, Integer.MAX_VALUE, EntryOrdering.cost, false, false);
        System.out.println("--- Subsuming - Pareto - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 5000, Integer.MAX_VALUE, EntryOrdering.cost, true, false);
    }

    private static void evaluationTime(AT at) {
        System.out.println("--- Conventional - Time - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000, 1, EntryOrdering.time, false, false);

        System.out.println("--- Subsuming - Time - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 1000, 1, EntryOrdering.time, true, false);
    }

    private static void evaluationCost(AT at) {
        System.out.println("--- Conventional - Cost - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 5000, 1, EntryOrdering.cost, false, false);

        System.out.println("--- Subsuming - Cost - SAND");
        evaluateLinear(at.copy(), at, Type.Sand, 20, 5000, 1, EntryOrdering.cost, true, false);
    }

    private static void evaluateLinear(AT at, AT combine, Type type, int maxAmount, long maxTime, int k, EntryOrdering eo, boolean subsuming, boolean resultPrints) {
        for (int i = 0; i < 5; i++) {
            ArrayList<Result> results = new ArrayList<>();
            evaluateLinear(at.copy(), combine, type, maxAmount, maxTime, k, eo, subsuming, resultPrints, results);

            System.out.println("- Nodes - ");
            for (Result r: results) System.out.println(r.totalNodes);
            System.out.println("- Time - ");
            for (Result r: results) System.out.println(r.time);
            System.out.println("- Results -");
            for (Result r: results) System.out.println(r.resultSize);
        }
    }


    private static void evaluateLinear(AT at, AT combine, Type type, int maxAmount, long maxTime, int k, EntryOrdering eo, boolean subsuming, boolean resultPrints, ArrayList<Result> results) {
        if (maxAmount == 0) return;
        results.add(evaluate(at, k, eo, subsuming, resultPrints));

        //System.out.println(at.toUPPAAL());

        if (results.get(results.size() - 1).time > maxTime) return;
        evaluateLinear(combine(at, combine.copy(), type), combine, type, maxAmount - 1, maxTime, k ,eo, subsuming, resultPrints, results);
    }

    private static Result evaluate(AT at, int k, EntryOrdering eo, boolean subsuming, boolean resultPrints) {
        float time = System.nanoTime();
        ArrayList<Entry> result = Algorithm.evaluate(at, eo, k, subsuming, false);
        time = (System.nanoTime() - time) / 1000000;

        return new Result(time, at.getTotalNodes(at.getAttackTree()), result.size());
    }

    private static void printUppaal(AT at, int amount, Type gate) {
        AT combine = at.copy();
        for (int i = 0; i < amount; i++) {
            System.out.println(combine.toUPPAAL());
            combine = combine(combine, at.copy(), gate);
        }
    }

    private static class Result {
        float time;
        int totalNodes;
        int resultSize;

        Result(float time, int totalNodes, int resultSize) {
            this.time = time;
            this.totalNodes = totalNodes;
            this.resultSize = resultSize;
        }

        public String toString() {
            return "(" + totalNodes + ", " + resultSize + ", " + time + ")";
        }
    }

}
