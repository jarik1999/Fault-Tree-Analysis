package Permutations;

import java.math.BigInteger;
import java.util.ArrayList;

public class Permutation {


    private ArrayList<Integer> x;
    private BigInteger[] fact;

    Permutation(ArrayList<Integer> x, BigInteger[] fact) {
        this.x = x;
        this.fact = fact;
    }

    /**
     * Calculate the total amount of permutations of the set
     * @return total permutations in the set
     */
    BigInteger getTotalPermutations() {
        return fact[x.size()];
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
        if (k.compareTo(getTotalPermutations()) >= 0) throw new RuntimeException("Permutation index out of bounds");

        int n = x.size();
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) indices[i] = i;
        SegmentTreeLazy st = new SegmentTreeLazy(indices);

        ArrayList<Integer> result = new ArrayList<>();
        for (int size = n; size > 0; size--) {
            BigInteger[] div = k.divideAndRemainder(fact[size - 1]);

            // Value in the segment tree to locate
            int value = div[0].intValue();
            int left = 0, right = n - 1;
            while (left + 1 < right) {
                int mid = (right - left) >> 1;
                int temp = st.queryMax(mid, mid);
                if (temp < value) {
                    left = mid + 1;
                } else if (temp == value) {
                    right = mid;
                } else {
                    right = mid - 1;
                }
            }
            if (st.queryMax(left, left) != value) left = right;

            // Add the value to the result
            result.add(x.get(left));
            // Update indices of elements
            st.update(left, n - 1, -1);
            k = div[1];
        }
        return result;
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
