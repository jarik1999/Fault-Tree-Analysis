package Tests;

import Algorithms.Algorithm;
import AttackTree.AT;
import Examples.ATExamples;
import Permutations.Permutations;
import Structures.AttackTree.AttackTree;
import Structures.Traversal.Entry;
import Structures.Traversal.EntryOrdering;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigInteger;
import java.util.ArrayList;

public class PermutationTest {

    @Test
    /* Test whether we have the correct amount of permutations */
    public void testPermutationCount() {
        // Single sets
        testPermutationCount(getLists(4), new BigInteger("24"));
        testPermutationCount(getLists(6), new BigInteger("720"));
        testPermutationCount(getLists(10), new BigInteger("3628800"));
        // Multiple sets
        testPermutationCount(getLists(4, 10), new BigInteger("87091200"));
        testPermutationCount(getLists(4, 5, 1), new BigInteger("2880"));
        testPermutationCount(getLists(4, 3, 5), new BigInteger("17280"));
        // Large results
        testPermutationCount(getLists(10, 20), new BigInteger("8828514807271391232000000"));
        testPermutationCount(getLists(5, 7, 6, 8), new BigInteger("17557585920000"));
        testPermutationCount(getLists(2, 12, 3, 1, 1), new BigInteger("5748019200"));
    }

    private void testPermutationCount(ArrayList<ArrayList<Integer>> sets, BigInteger expected) {
        Permutations permutations = new Permutations(sets);
        assertEquals(expected, permutations.getTotalPermutations());
    }

    @Test
    /* Test whether the permutations indeed find all possible permutations */
    public void testPermutationSolutions() {
        // Check whether all result permutations are unique
        testPermutationsUnique(getLists(3, 2, 1, 2));
        testPermutationsUnique(getLists(1));
        testPermutationsUnique(getLists(2, 2, 3));
        testPermutationsUnique(getLists(5));
        testPermutationsUnique(getLists(4, 2, 1));
    }

    @Test
    public void testTopologySets() {
        testTopologySetsUppaal();
        testTopologySetsFigure1();
        testTopologySetsFigure3();


    }

    private void testTopologySetsUppaal() {
        // In the subsumed Uppaal tree, ech solution has only one possible ordering
        AT uppaal = ATExamples.openJSON("Uppaal_Example.json");
        testTopologyOrderings(uppaal, new int[] {1, 1, 1});
    }

    private void testTopologySetsFigure1() {
        // In the subsumed Figure 1 tree, we have solution of size 1, 2, 2. No sand gates so 1!, 2!, 2! orderings
        AT figure1 = ATExamples.openJSON("Paper_Figure_1.json");
        testTopologyOrderings(figure1, new int[] {1, 2, 2});
    }

    private void testTopologySetsFigure3() {
        // In the subsumed Figure 3 tree, we have solutions of size 2, 2, 2, 2. No sand gates so 2!, 2!, 2!, 2!
        AT at = ATExamples.openJSON("Paper_Figure_3.json");
        testTopologyOrderings(at, new int[] {2, 2, 2, 2});
    }

    private void testTopologyOrderings(AT at, int[] totalOrderings) {
        AttackTree[] mapping = at.getMapping();
        ArrayList<ArrayList<Integer>> topologySets = at.getTopologySets();
        ArrayList<Entry> solutions = Algorithm.evaluate(at, EntryOrdering.time, true, false);
        assert(solutions.size() == totalOrderings.length);
        for (int i = 0; i < totalOrderings.length; i++) {
            ArrayList<Integer> bas = solutions.get(i).getCompleted(mapping);
            Permutations permutations = new Permutations(at.getSolutionSets(topologySets, bas));
            assert(permutations.getTotalPermutations().intValue() == totalOrderings[i]);
        }
    }

    private void testPermutationsUnique(ArrayList<ArrayList<Integer>> sets) {
        ArrayList<ArrayList<Integer>> results = new ArrayList<>();
        Permutations permutations = new Permutations(sets);

        int i = 0;
        ArrayList<Integer> permutation = permutations.getPermutation(i);
        do {
            results.add(permutation);
            permutation = permutations.getPermutation(++i);
        } while (permutation != null);

        for (i = 0; i < results.size(); i++) {
            for (int j = i + 1; j < results.size(); j++) {
                assert(unequalArrayLists(results.get(i), results.get(j)));
            }
        }
    }

    private boolean unequalArrayLists(ArrayList<Integer> a, ArrayList<Integer> b) {
        if (a.size() != b.size()) return true;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) return true;
        }
        return false;
    }


    private ArrayList<ArrayList<Integer>> getLists(int... sizes) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        for (int size: sizes) result.add(getList(size));
        return result;
    }

    private ArrayList<Integer> getList(int size) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < size; i++) result.add(i);
        return result;
    }
}
