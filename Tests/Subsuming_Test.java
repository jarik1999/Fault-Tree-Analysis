package Tests;

import Algorithms.Algorithm;
import AttackTree.AT;
import AttackTree.Ordering;
import BDD.BDD;
import Examples.AT_Examples;
import Structures.Traversal.Entry;
import Structures.Traversal.EntryOrdering;

import java.util.ArrayList;

public class Subsuming_Test {
    public static void main(String[] args) {
        // Figure 3
        test(AT_Examples.openJSON("Paper_Figure_1.json"));
    }

    private static void test(AT at) {
        System.out.println("--- EVALUATION ---");
        System.out.println("Nodes: " + at.getTotalNodes(at.getAttackTree()));
        test(at, false);
        test(at, true);
    }

    private static void test(AT at, boolean subsuming) {
        long time = System.currentTimeMillis();
        Ordering ordering = at.attackTreeOrdering();
        BDD bdd = new BDD(at, ordering, subsuming);
        ArrayList<Entry> result = Algorithm.evaluate(at, EntryOrdering.cost, subsuming, false);
        time = System.currentTimeMillis() - time;

        System.out.println("--- ORDERING ---");
        System.out.println(ordering.toString());
        System.out.println("--- DOT AT ---");
        System.out.println(at.toDOT());
        System.out.println("--- DOT BDD ---");
        System.out.println(bdd.toDOT());
        System.out.println("--- RESULT ---");
        System.out.println("Time: " + time);
        System.out.println("Total: " + result.size());
        for (Entry e: result) {
            int c = e.cost;
            int t = e.getTime(at.getAttackTree());
            ArrayList<Integer> comleted = e.getCompleted(at.getMapping());
            System.out.println("Cost: " + c + ", Time: " + t + ", Completed: " + comleted);
        }
    }
}
