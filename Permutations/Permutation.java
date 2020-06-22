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
        ArrayList<Integer> set = new ArrayList<>(x);
        ArrayList<Integer> result = new ArrayList<>();
        for (int n = set.size(); n > 0; n--) {
            BigInteger[] div = k.divideAndRemainder(fact[n-1]);
            result.add(set.get(div[0].intValue()));
            set.remove(div[0].intValue());
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
