package BDD;

import AttackTree.Ordering;
import Structures.BDD.ITE;
import Structures.BDD.Structure;
import Structures.BDD.Value;

import java.util.HashMap;

public class Subsuming {

    private Ordering ordering;
    private HashMap<TableEntry, ITE> without;

    public Subsuming(Ordering ordering) {
        this.without = new HashMap<>();
        this.ordering = ordering;
    }

    // Figure 5 from the paper
    public ITE without(ITE F, ITE G) {
        //System.out.println("SUBSUMING: " + F.toString() + " - " + G.toString());
        // F = 0 => 0
        if (F instanceof Value && !((Value) F).x) return F;
        // G = 1 => 0
        if (G instanceof Value && ((Value) G).x) return new Value(false);
        // G = 0 => F
        if (G instanceof Value && !((Value) G).x) return F;
        // F = 1 => 1
        if (F instanceof Value && ((Value) F).x) return F;
        // ({F, G}, R) contained => R
        TableEntry entry = new TableEntry(F, G);
        if (without.containsKey(entry)) return without.get(entry);

        Structure F_ITE = (Structure) F;
        Structure G_ITE = (Structure) G;
        int compare = ordering.compare(F_ITE.x.x, G_ITE.x.x);
        // x < y
        if (compare < 0) {
            ITE U = without(F_ITE.left, G);
            ITE V = without(F_ITE.right, G);

            ITE R = new Structure(F_ITE.x, U, V);
            TableEntry result = new TableEntry(F, G);
            without.put(result, R);
            return R;
        }
        // x > y
        if (compare > 0) {
            return without(F, G_ITE.right);
        }
        // x = y
        else {
            ITE U = without(F_ITE.left, G_ITE.left);
            ITE V = without(F_ITE.right, G_ITE.right);

            ITE R = new Structure(F_ITE.x, U, V);
            TableEntry result = new TableEntry(F, G);
            without.put(result, R);
            return R;
        }
    }


    private class TableEntry {

        private String F;
        private String G;
        private int hashCode;

        TableEntry(ITE F, ITE G) {
            this.F = F.toString();
            this.G = G.toString();
            this.hashCode = this.F.hashCode() + this.G.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            TableEntry entry = (TableEntry) o;
            return this.F.equals(entry.F) && this.G.equals(entry.G);
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }
}
