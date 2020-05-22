package Algorithms;

import BDD.BDD;
import Structures.AttackTree.AttackTree;
import AttackTree.AT;
import Structures.BDD.ITE;
import Structures.BDD.Structure;
import Structures.BDD.Value;
import Structures.Traversal.Entry;
import AttackTree.Ordering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Algorithm {
    /**
     * Evaluate the cut sets of the Attack Tree using the algorithm
     * @param at, the Attack Tree
     * @param k, maximum number of results
     * @param prints, prints for AT-DOT, AT-UPPAAL, Variable ordering, and BDD-DOT.
     * @return results for the AT (results.size() <= K)
     */
    public static ArrayList<Entry> evaluate(AT at, int k, boolean subsuming, boolean prints) {
        AttackTree[] mapping = at.getMapping();

        Ordering ordering = at.attackTreeOrdering();
        BDD bdd = new BDD(at, ordering, subsuming);

        if (prints) {
            System.out.println("--- Attack Tree DOT ---");
            System.out.println(at.toDOT());
            System.out.println(at.toUPPAAL());

            System.out.println("--- Variable Ordering ---");
            System.out.println(ordering.toString());

            System.out.println("--- BDD DOT ---");
            System.out.println(bdd.toDOT());
        }

        return evaluate(bdd, mapping, k);
    }

    /**
     * Evaluate the cut sets of the Attack Tree using the algorithm. Unbounded.
     * @param at, the Attack Tree
     * @param prints, prints for AT-DOT, AT-UPPAAL, Variable ordering, and BDD-DOT.
     * @return results for the AT (results.size() <= K)
     */
    public static ArrayList<Entry> evaluate(AT at, boolean subsuming, boolean prints) {
        return evaluate(at, Integer.MAX_VALUE, subsuming, prints);
    }


    /**
     * Traverse the BDD based on the sorting of the entries.
     * @param bdd, BDD of the AT
     * @param mapping, mapping of ID -> AttackTree
     * @param k, maximum number of results (to determine top-K)
     * @return possible cut sets
     */
    private static ArrayList<Entry> evaluate(BDD bdd, AttackTree[] mapping, int k) {
        ArrayList<Entry> result = new ArrayList<>();
        PriorityQueue<Entry> pq = new PriorityQueue<>();

        // Add top bdd entry
        int n = mapping.length;
        int[] X = new int[n];
        Arrays.fill(X, -1);
        Entry entry = new Entry(0, 0, bdd.getITE(), X);
        pq.add(entry);

        while (!pq.isEmpty() && result.size() < k) {
            Entry entryLeft = pq.poll();
            Entry entryRight = entryLeft.copy();

            ITE gate = entryLeft.gate;
            if (gate instanceof Value) {
                // End reached. Add if 1-leaf.
                Value value = (Value) gate;
                if (value.x) result.add(entryLeft);
            } else {
                Structure structure = (Structure) gate;
                int nodeID = structure.x.x;

                // Take 1-path
                entryLeft.completeBAS(nodeID, mapping);
                entryLeft.gate = structure.left;
                pq.add(entryLeft);

                // Take 0-path
                entryRight.gate = structure.right;
                pq.add(entryRight);
            }
        }
        return result;
    }
}
