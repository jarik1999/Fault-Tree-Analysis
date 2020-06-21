package AttackTree;

import Examples.ATExamples;
import Structures.AttackTree.AttackTree;
import Structures.AttackTree.Gate;
import Structures.AttackTree.Leaf;
import Structures.AttackTree.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class AT {
    private AttackTree at;
    private String name;

    public AT(String name, AttackTree at) {
        this.at = at;
        this.name = name;
    }

    /**
     * Get the AttackTree structure
     * @return AttackTree structure
     */
    public AttackTree getAttackTree() {
        return at;
    }

    /**
     * Get a mapping of ID -> AttackTree
     * @return mapping
     */
    public AttackTree[] getMapping() {
        int nodes = getTotalNodes(at);
        AttackTree[] result = new AttackTree[nodes];
        traverseMapping(at, result);
        return result;
    }

    /**
     * Creates a mapping by traversal
     * @param at, current AttackTree
     * @param mapping, mapping result
     */
    private void traverseMapping(AttackTree at, AttackTree[] mapping) {
        mapping[at.getID()] = at;
        if (at instanceof Gate) {
            Gate gate = (Gate) at;
            traverseMapping(gate.left, mapping);
            traverseMapping(gate.right, mapping);
        }
    }

    /**
     * Converts an AttackTree to an UPPAAL model.
     * @return [System declarations, Declarations] to insert into the framework file
     */
    public ArrayList<String> toUPPAAL() {
        ArrayList<String> result = new ArrayList<>(2);
        int N = getTotalNodes(at);

        StringBuilder nodes = new StringBuilder();
        StringBuilder system = new StringBuilder("system\n");
        StringBuilder declarations = new StringBuilder();

        int[] isAttack = new int[N];
        Arrays.fill(isAttack, 1);
        int[] time = new int[N];
        int[] cost = new int[N];

        traverseUPPAAL(at, nodes, system, new HashSet<>(), time, cost);

        // System declarations
        nodes.append("TLE = TopLevel(").append(at.getID()).append(");\n");
        system.append("TLE;\n");
        result.add(nodes.toString() + system);

        // Declarations
        declarations.append("const int N = ").append(N).append(";\n");
        declarations.append("const bool isattack[N]={").append(arrayString(isAttack)).append("};\n");
        declarations.append("const int mintime[N]={").append(arrayString(time)).append("};\n");
        declarations.append("const int maxtime[N]={").append(arrayString(time)).append("};\n");
        declarations.append("const int costs[N]={").append(arrayString(cost)).append("};\n");
        result.add(declarations.toString());

        return result;
    }

    /**
     * Converts an array to an UPPAAL string for arrays
     * @param a, array
     * @return string version of the array
     */
    private String arrayString(int[] a) {
        return Arrays.stream(a)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
    }

    /**
     * Create the UPPAAL model by traversal of the Attack Tree
     * @param at, current AttackTree
     * @param nodes, string for the Declaration
     * @param system, string for the System Declaration
     * @param visited, visited nodes of the AttackTree
     * @param time, ID -> time for the UPPAAL model
     * @param cost, ID -> cost for the UPPAAL model
     */
    private void traverseUPPAAL(AttackTree at, StringBuilder nodes, StringBuilder system, HashSet<String> visited, int[] time, int[] cost) {
        if (visited.contains(at.getUniqueName())) return;
        visited.add(at.getUniqueName());

        if (at instanceof Leaf) {
            Leaf leaf = (Leaf) at;
            String ID = leaf.name + "_" + leaf.ID;

            // System declarations
            nodes.append(ID).append(" = Leaf(").append(leaf.ID).append(");\n");
            system.append(ID).append(",");

            // Declarations
            time[leaf.ID] = leaf.time;
            cost[leaf.ID] = leaf.cost;
        } else {
            Gate gate = (Gate) at;
            String ID = gate.name + "_" + gate.ID;

            // System declarations
            nodes
                    .append(ID)
                    .append(" = ")
                    .append(gate.type)
                    .append("(")
                    .append(gate.ID)
                    .append(",")
                    .append(gate.left.getID())
                    .append(",")
                    .append(gate.right.getID())
                    .append(");\n");
            system.append(ID).append(",");

            traverseUPPAAL(gate.left, nodes, system, visited, time, cost);
            traverseUPPAAL(gate.right, nodes, system, visited, time, cost);
        }
    }

    /**
     * Converts the Attack Tree model to a DOT for visualisation
     * @return DOT string
     */
    public String toDOT() {
        StringBuilder edges = new StringBuilder();
        traverseDOT(at, edges);
        return "digraph {\n" + edges + "}";
    }

    /**
     * Create the DOT model by traversal
     * @param node, current node in the Attack Tree
     * @param edges, edges for the DOT model
     */
    private void traverseDOT(AttackTree node, StringBuilder edges) {
        if (node instanceof Leaf) return;
        Gate gate = (Gate) node;

        edges.append(gate.getUniqueName()).append("->").append(gate.left.getUniqueName()).append("\n");
        edges.append(gate.getUniqueName()).append("->").append(gate.right.getUniqueName()).append("\n");
        traverseDOT(gate.left, edges);
        traverseDOT(gate.right, edges);
    }

    /**
     * Determine an ordering of the BASs such that each SAND gate is satisfied.
     * X = BASs contained in the left subtree of a SAND
     * Y = BASs contained in the right subtree of a SAND
     * The resulting order has x < y for each x,y in X and Y.
     * @return valid ordering for the Attack Tree
     */
    public Ordering attackTreeOrdering() {
        int totalBAS = getTotalBAS(at);

        ArrayList<Integer>[] topo = getOrderingDAGDefinition1();
        int[] in_degree = getIndegree(topo);

        // BAS with in_degree zero can be first
        int index = 0;
        int[] ordering = new int[totalBAS];
        for (int i = 0; i < totalBAS; i++) {
            if (in_degree[i] == 0) {
                ordering[index++] = i;
            }
        }

        // Add BAS when their indegree becomes zero
        for (int i = 0; i < index; i++) {
            for (int node: topo[ordering[i]]) {
                if (--in_degree[node] == 0) {
                    ordering[index++] = node;
                }
            }
        }

        // Test whether ordering was indeed a DAG
        for (int i = 0; i < totalBAS; i++) {
            if (in_degree[i] > 0) {
                throw new RuntimeException("Found cyclic ordering dependencies");
            }
        }

        return new Ordering(ordering);
    }

    /**
     * Get the topology sets for the ordering. Used to find additional ordering solutions.
     * @return topology ordering sets
     */
    public ArrayList<ArrayList<Integer>> getTopologySets() {
        int totalBAS = getTotalBAS(at);
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        boolean[] visited = new boolean[totalBAS];

        ArrayList<Integer>[] topo = getOrderingDAGDefinition1();
        int[] in_degree = getIndegree(topo);

        boolean change = true;
        int added = 0;
        while (change) {
            change = false;
            ArrayList<Integer> set = new ArrayList<>();
            for (int i = 0; i < totalBAS; i++) {
                if (in_degree[i] == 0 && !visited[i]) {
                    set.add(i);
                    added += 1;
                    visited[i] = true;
                    change = true;
                }
            }
            for (int i: set) {
                for (int j: topo[i]) {
                    in_degree[j]--;
                }
            }
            if (change) result.add(set);
        }

        if (added < totalBAS) {
            throw new RuntimeException("Found cyclic ordering dependencies");
        }

        return result;
    }

    /**
     * Create the topology sets for a certain solution. These can be used to create the additional orderings for
     * solutions.
     * @param topologySets, topology sets create by the topological sort
     * @param solution, solution for the AT
     * @return topology sets for the solution
     */
    public ArrayList<ArrayList<Integer>> getSolutionSets(ArrayList<ArrayList<Integer>> topologySets, ArrayList<Integer> solution) {
        // Create mapping of BAS -> Set
        HashMap<Integer, Integer> setMapping = new HashMap<>();
        for (int i = 0; i < topologySets.size(); i++) {
            for (int j = 0; j < topologySets.get(i).size(); j++) {
                setMapping.put(topologySets.get(i).get(j), i);
            }
        }

        // Create solution sets
        ArrayList<ArrayList<Integer>> sets = new ArrayList<>();
        for (int i = 0; i < topologySets.size(); i++) sets.add(new ArrayList<>());
        for (int bas: solution) {
            int set = setMapping.get(bas);
            sets.get(set).add(bas);
        }

        // Create result
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        for (int i = 0; i < topologySets.size(); i++) {
            if (sets.get(i).size() > 0) {
                result.add(sets.get(i));
            }
        }

        return result;
    }

    /**
     * Construct the Ordering Graph from the AT according to definition one (used in the paper).
     * Definition 1: For each SAND-gate l < r for each SAND-gate (L, R\L)
     * @return Ordering Graph of the AT
     */
    private ArrayList<Integer>[] getOrderingDAGDefinition1() {
        int totalBAS = getTotalBAS(at);
        AttackTree[] mapping = getMapping();
        ArrayList<Integer>[] topo = new ArrayList[totalBAS];
        for (int i = 0; i < totalBAS; i++) topo[i] = new ArrayList<>();
        for (int i = 0; i < mapping.length; i++) {
            AttackTree node = mapping[i];
            if (node instanceof Gate) {
                Gate gate = (Gate) node;
                if (gate.type == Type.Sand) {
                    HashSet<Integer> leftBAS = new HashSet<>();
                    HashSet<Integer> rightBAS = new HashSet<>();
                    getBAS(gate.left, leftBAS);
                    getBAS(gate.right, rightBAS);

                    for (int left: leftBAS) {
                        if (!rightBAS.contains(left)) {
                            for (int right: rightBAS) {
                                topo[left].add(right);
                            }
                        }
                    }
                }
            }
        }
        for (ArrayList<Integer> set: topo) System.out.println(set);
        return topo;
    }

    /**
     * Construct the Ordering Graph from the AT according to definition two (alternative to the paper).
     * Definition 2: For each SAND-gate l < r for each SAND-gate (L, R)
     * @return Ordering Graph of the AT
     */
    private ArrayList<Integer>[] getOrderingDAGDefinition2() {
        int totalBAS = getTotalBAS(at);

        // Create a graph where topo[i] contains the out edges of i
        // If there is an out_edge it means x < y
        ArrayList<Integer>[] topo = new ArrayList[totalBAS];
        for (int i = 0; i < totalBAS; i++) topo[i] = new ArrayList<>();
        traverse(at, topo, new ArrayList<>());

        return topo;
    }

    /**
     * Construct the indegree array from the Ordering Graph of the AT
     * @param topo, Ordering Graph
     * @return indegree array of the graph
     */
    private int[] getIndegree(ArrayList<Integer>[] topo) {
        int totalBAS = getTotalBAS(at);

        int[] in_degree = new int[totalBAS];
        for (int i = 0; i < totalBAS; i++) {
            for (int node: topo[i]) {
                in_degree[node]++;
            }
        }

        return in_degree;
    }

    /**
     * Create a DAG of BASs for the Attack Tree to be used for ordering.
     * @param at, current Attack Tree node
     * @param topo, DAG of x -> y of the BASs. Where x -> y indicates that x should occur before y
     * @param nodes, if current at X then X should occur before all of Nodes.
     */
    private void traverse(AttackTree at, ArrayList<Integer>[] topo, ArrayList<Integer> nodes) {
        if (at instanceof Gate) {
            Gate gate = (Gate) at;
            if (gate.type == Type.Sand) {
                ArrayList<Integer> rightBAS = new ArrayList<>(nodes);
                getBAS(gate.right, rightBAS);
                traverse(gate.left, topo, rightBAS);
                traverse(gate.right, topo, nodes);
            } else {
                traverse(gate.left, topo, nodes);
                traverse(gate.right, topo, nodes);
            }
        } else {
            Leaf leaf = (Leaf) at;
            for (int node: nodes) topo[leaf.ID].add(node);
        }
    }

    /**
     * Get the IDs of all BASs in the Attack Tree.
     * @param at, current Attack Tree in traversal
     * @param bas, set of IDs
     */
    private void getBAS(AttackTree at, ArrayList<Integer> bas) {
        if (at instanceof Leaf) {
            bas.add(((Leaf) at).ID);
        } else {
            Gate gate = (Gate) at;
            getBAS(gate.left, bas);
            getBAS(gate.right, bas);
        }
    }

    /**
     * Get the IDs of all BASs in the Attack Tree
     * @param at, current Attack Tree in traversal
     * @param bas, set of IDs
     */
    private void getBAS(AttackTree at, HashSet<Integer> bas) {
        if (at instanceof Leaf) {
            bas.add(((Leaf) at).ID);
        } else {
            Gate gate = (Gate) at;
            getBAS(gate.left, bas);
            getBAS(gate.right, bas);
        }
    }

    /**
     * Searches for highest ID leaf node. LeafID < GateID should hold.
     * @param at, Attack Tree (Top Node)
     * @return Number of BAS in the AT
     */
    private int getTotalBAS(AttackTree at) {
        if (at instanceof Leaf) return at.getID() + 1;
        Gate gate = (Gate) at;
        return Math.max(getTotalBAS(gate.left), getTotalBAS(gate.right));
    }

    /**
     * Searches for highest ID node to determine amount of nodes in the Attack Tree
     * @param at, current Attack Tree node
     * @return number of nodes in the Attack Tree
     */
    public int getTotalNodes(AttackTree at) {
        if (at instanceof Leaf) return at.getID() + 1;
        Gate gate = (Gate) at;
        int max = Math.max(getTotalNodes(gate.left), getTotalNodes(gate.right));
        return Math.max(max, gate.ID + 1);
    }

    /**
     * Increments the IDs of all nodes in the Attack Tree. Used for example for combining different
     * ATs.
     */
    public void incrementIDs(int increment) {
        AttackTree[] mapping = getMapping();
        for (AttackTree at: mapping) {
            if (at instanceof Leaf) ((Leaf) at).ID += increment;
            else ((Gate) at).ID += increment;
        }
    }

    /**
     * Create a copy of the Attack Tree from the original JSON.
     * @return copy of the Attack Tree
     */
    public AT copy() {
        return ATExamples.openJSON(name);
    }
}
