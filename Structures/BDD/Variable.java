package Structures.BDD;

public class Variable  {
    public int x;
    public Variable(int x) {
        this.x = x;
    }

    /**
     * String version of a variable in the BDD structure. Converts from an ID to alphabet if possible.
     * @return string version of the variable
     */
    public String toString() {
        return "" + (char) (x + 'a');
    }
}
