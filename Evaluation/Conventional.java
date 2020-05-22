package Evaluation;

import Algorithms.Algorithm;
import AttackTree.AT;
import Examples.AT_Examples;
import Structures.AttackTree.AttackTree;
import Structures.AttackTree.Gate;
import Structures.AttackTree.Type;
import Structures.BDD.Structure;
import Structures.Traversal.Entry;

import java.util.ArrayList;

public class Conventional {
    public static void main(String[] args) {

//        evaluateConventional(AT_Examples.getAttackTree1());
//        evaluateConventional(AT_Examples.getAttackTree2());
//        evaluateConventional(AT_Examples.getAttackTree3());
//        evaluateConventional(AT_Examples.getAttackTree4());
//        evaluateConventional(AT_Examples.getAttackTree5());

        evaluateDoubling(10, AT_Examples.getAttackTree5());
    }

    private static void evaluateDoubling(int count, AT at) {
        if (count == 0) return;
        evaluateConventional(at, 1);
        evaluateDoubling(--count, combine(at, AT_Examples.getAttackTree5()));
    }

    private static AT combine(AT x, AT y) {
        int n = x.getTotalNodes(x.getAttackTree());
        int m = x.getTotalNodes(y.getAttackTree());

        y.incrementIDs(n);

        AttackTree at = new Gate(n + m, "Combine", Type.Or, x.getAttackTree(), y.getAttackTree());
        return new AT(at);
    }

    private static void evaluateConventional(AT at, int k) {
        long time = System.currentTimeMillis();
        ArrayList<Entry> result = Algorithm.evaluate(at, k,false);
        time = System.currentTimeMillis() - time;

        System.out.println("--- Evaluation ---");
        System.out.println("Nodes: " + at.getTotalNodes(at.getAttackTree()));
        System.out.println("Time: " + time);
        System.out.println("Min cost: " + result.get(0).cost);
        System.out.println("Total: " + result.size());
    }
}
