package Permutations;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Permutation {

    private ArrayList<Integer> x;

    Permutation(ArrayList<Integer> x) {
        this.x = x;
    }

    /**
     * Get the k'th permutation where 0 is the input permutation
     * @param k, permutation number
     * @return permutation
     */
    public ArrayList<Integer> getPermutation(int k) {
        return getPermutation(new BigInteger("" + k));
    }

    /**
     * Get the k'th permutation where 0 is the input permutation
     * @param k, permutation number
     * @return permutation
     */
    public ArrayList<Integer> getPermutation(BigInteger k) {
        ArrayList<Integer> result = new ArrayList<>();
        LinkedList<Integer> next = new LinkedList<>();
        for (int i: x) next.addLast(i);
        getPermutation(k, next, result);
        return result;
    }

    /**
     * Calculate the total amount of permutations of the set
     * @return
     */
    public BigInteger getTotalPermutations() {
        return fact(x.size());
    }

    /**
     * Get the k'th permutation where 0 is the input permutation
     * @param k, permutation number
     * @param next, BASs left to consider
     * @param result, current result of the ordering containing BASs
     */
    private void getPermutation(BigInteger k, LinkedList<Integer> next, ArrayList<Integer> result) {
        Iterator<Integer> iterator = next.listIterator();
        int n = next.size();
        for (int i = 0; i < n; i++) {
            int x = iterator.next();
            BigInteger start = fact(n - 1).multiply(new BigInteger("" + i));
            BigInteger end = fact(n - 1).multiply(new BigInteger("" + (i + 1)));
            if (k.compareTo(end) < 0) {
                iterator.remove();
                result.add(x);
                getPermutation(k.subtract(start), next, result);
                return;
            }
        }
    }

    /**
     * Calculate k!
     * @param k, factorial
     * @return k!
     */
    private BigInteger fact(int k) {
        BigInteger res = BigInteger.ONE;
        for (int i = 2; i <= k; i++) res = res.multiply(new BigInteger("" + i));
        return res;
    }

    /**
     * Create string version of the permutation group
     * @return BASs ','-separated
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i : x) sb.append(i).append(',');
        return sb.toString();
    }
}
