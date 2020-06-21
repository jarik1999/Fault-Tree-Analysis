package Permutations;

import java.math.BigInteger;
import java.util.ArrayList;

public class Permutations {
    private int n;
    private Permutation[] permutations;
    // dp[i] indicates amount of permutations from sets with index > i
    // similar approach of base 10 -> base 2 conversion. However, here each number has a different base,
    // so we use this array
    private BigInteger[] dp;
    private BigInteger total;


    public Permutations(ArrayList<ArrayList<Integer>> sets) {
        this.n = sets.size();

        this.permutations = new Permutation[n];
        for (int i = 0; i < n; i++) {
            this.permutations[i] = new Permutation(sets.get(i));
        }

        updateTotalPermutations();
    }

    /**
     * Create the dp array, and calculate the total amount of permutations. The dp array is used to obtain the
     * amount of permutations in certain sets in O(1). dp[i] contains permutations in sets with index > i combined.
     */
    private void updateTotalPermutations() {
        dp = new BigInteger[n];
        dp[n - 1] = BigInteger.ONE;
        for (int i = n - 2; i >= 0; i--) {
            dp[i] = dp[i + 1].multiply(permutations[i+1].getTotalPermutations());
        }
        total = dp[0].multiply(permutations[0].getTotalPermutations());
    }

    /**
     * Get the k'th permutations of the attack
     * @param k, permutation number
     * @return k'th permutation
     */
    public ArrayList<Integer> getPermutation(int k) {
        return getPermutation(new BigInteger("" + k));
    }

    /**
     * Get the k'th permutations of the attack
     * @param k, permutation number
     * @return k'th permutation
     */
    public ArrayList<Integer> getPermutation(BigInteger k) {
        ArrayList<Integer> result = new ArrayList<>();
        if (k.compareTo(total) >= 0) return null;

        for (int i = 0; i < n; i++) {
            result.addAll(permutations[i].getPermutation(k.divide(dp[i])));
            k = k.mod(dp[i]);
        }

        return result;
    }

    /**
     * Get the total amount of permutations of the attack
     * @return total permutations
     */
    public BigInteger getTotalPermutations() {
        return total;
    }
}
