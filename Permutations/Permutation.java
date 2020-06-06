package Permutations;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class Permutation {
    /**
     * TODO optimize by storing n! for important n
     * TODO to support larger sets of BAS create a function k < n! function(k, n) that tries to evaluate without calculating n completely
     * TODO implement wrapping class to calculate permutations for sets
     */

    private ArrayList<Integer> x;

    public Permutation(ArrayList<Integer > x) {
        this.x = x;
    }

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

    public BigInteger getTotalPermutations() {
        return fact(x.size());
    }

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

    private BigInteger fact(int k) {
        BigInteger res = BigInteger.ONE;
        for (int i = 2; i <= k; i++) res = res.multiply(new BigInteger("" + i));
        return res;
    }



    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i : x) sb.append(i).append(',');
        return sb.toString();
    }

    public static void main(String[] args) {
        ArrayList<Integer> a = new ArrayList<>(){{add(0);add(1);add(2);add(3);}};
        Permutation perm = new Permutation(a);

        for (int i = 0; i < 24; i++) {
            System.out.println(perm.getPermutation(i).toString());
        }
    }
}
