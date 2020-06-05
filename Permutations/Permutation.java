package Permutations;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class Permutation {
    /**
     * TODO optimize by storing n! for important n
     * TODO to support larger sets of BAS create a function k < n! function(k, n) that tries to evaluate without calculating n completely
     * TODO implement wrapping class to calculate permutations for sets
     */

    private int[] x;

    public Permutation(int[] BASs) {
        this.x = BASs;
    }

    /**
     * Get the k'th permutation where 0 is the input permutation
     * @param k, permutation number
     * @return permutation
     */
    public Permutation getPermutation(int k) {
        Stack<Integer> stack = new Stack<>();
        LinkedList<Integer> next = new LinkedList<>();
        for (int i: x) next.addLast(i);
        getPermutation(k, next, stack);

        int[] result = new int[x.length];
        for (int i = 0; i < x.length; i++) {
            result[x.length - 1 - i] = stack.pop();
        }
        return new Permutation(result);
    }

    private void getPermutation(int k, LinkedList<Integer> next, Stack<Integer> stack) {
        Iterator<Integer> iterator = next.listIterator();
        int n = next.size();
        for (int i = 0; i < n; i++) {
            int x = iterator.next();
            int start = i * fact(n - 1);
            int end = (i + 1) * fact(n - 1);
            if (start <= k && k < end) {
                iterator.remove();
                stack.add(x);
                getPermutation(k - start, next, stack);
                return;
            }
        }
    }

    private int fact(int k) {
        if (k == 0 || k == 1) return 1;
        return k * fact(k-1);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i : x) sb.append(i).append(',');
        return sb.toString();
    }

    public static void main(String[] args) {
        Permutation perm = new Permutation(new int[] {0, 1, 2, 3});

        for (int i = 0; i < 24; i++) {
            System.out.println(perm.getPermutation(i).toString());
        }
    }
}
