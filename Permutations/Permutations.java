package Permutations;

import java.math.BigInteger;
import java.util.ArrayList;

public class Permutations {
    private int n;
    private Permutation[] permutations;
    // dp[i] indicates amount of permutations from sets with index > i
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

    private void updateTotalPermutations() {
        dp = new BigInteger[n];
        dp[n - 1] = BigInteger.ONE;
        for (int i = n - 2; i >= 0; i--) {
            dp[i] = dp[i + 1].multiply(permutations[i+1].getTotalPermutations());
        }
        total = dp[0].multiply(permutations[0].getTotalPermutations());
    }

    public ArrayList<Integer> getPermutation(int k) {
        return getPermutation(new BigInteger("" + k));
    }

    public ArrayList<Integer> getPermutation(BigInteger k) {
        ArrayList<Integer> result = new ArrayList<>();
        if (k.compareTo(total) >= 0) return null;

        for (int i = 0; i < n; i++) {
            result.addAll(permutations[i].getPermutation(k.divide(dp[i])));
            k = k.mod(dp[i]);
        }

        return result;
    }

    public BigInteger getTotalPermutations() {
        return total;
    }
}
