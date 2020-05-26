package Evaluation;

import Algorithms.Algorithm;
import AttackTree.AT;
import BDD.BDD;
import Examples.AT_Examples;
import Structures.AttackTree.Type;
import Structures.Traversal.Entry;
import Structures.Traversal.EntryOrdering;

import java.util.ArrayList;

import static Evaluation.Helpers.combine;

public class Evaluation {
    public static void main(String[] args) {
        AT at = AT_Examples.getAttackTree5();

        //AT at2 = combine(at, at.copy(), Type.Or);
        //ArrayList<Entry> result = Algorithm.evaluate(at2, EntryOrdering.time, 20, false, false);
        evaluation1(at);
    }

    private static void evaluation1(AT at) {
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

    private static void evaluateLinear(AT at, AT combine, Type type, int maxAmount, long maxTime, int k, EntryOrdering eo, boolean subsuming, boolean resultPrints) {
        if (maxAmount == 0) return;
        long time = evaluate(at, k, eo, subsuming, resultPrints);
        if (time > maxTime) return;
        evaluateLinear(combine(at, combine.copy(), type), combine, type, maxAmount - 1, maxTime, k ,eo, subsuming, resultPrints);
    }

    private static long evaluate(AT at, int k, EntryOrdering eo, boolean subsuming, boolean resultPrints) {
        long time = System.currentTimeMillis();
        ArrayList<Entry> result = Algorithm.evaluate(at, eo, k, subsuming, false);
        time = System.currentTimeMillis() - time;

        System.out.println("(" + at.getTotalNodes(at.getAttackTree()) + ", " + time + ")");
        if (resultPrints) {
            System.out.println("Total: " + result.size());
            for (Entry e: result) {
                if (eo == EntryOrdering.cost) System.out.println(e.cost);
                else System.out.println(e.time);
            }
        }

        return time;
    }
}
