package AttackTree;

public class Ordering {

    private int[] ordering;
    private int[] indexes;

    public Ordering(int[] ordering) {
        this.ordering = ordering;
        indexes = new int[ordering.length];
        for (int i = 0; i < ordering.length; i++) {
            indexes[ordering[i]] = i;
        }
    }

    /**
     * Get order of BAS
     * @return list of ID in alphabetical representation
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i: ordering) {
            sb.append((char) (i + 'a')).append(",");
        }
        return sb.toString();
    }

    /** Compare two variables
     *
     * @param a : first variable
     * @param b : second variable
     * @return : result similar to compareTo of Java
     */
    public int compare(int a, int b) {
        return indexes[a] - indexes[b];
    }
}
