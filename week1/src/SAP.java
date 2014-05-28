import java.util.ArrayList;

public class SAP {

    private static final int UNPROCESSED = 0;
    private static final int VISITED = 1;
    private static final int DONE = 2;
    private byte[] dfsState;
    private int root = -1;

    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        digraph = new Digraph(G);

        dfsState = new byte[digraph.V()];
        isRootedDag();
    }

    private void isRootedDag() {
        for (int v = 0; v < digraph.V(); v++) {
            if (dfsState[v] == UNPROCESSED) {
                dfs(v);
            }
        }
    }


    private void dfs(int v) {

        dfsState[v] = VISITED;

        Iterable<Integer> adj = digraph.adj(v);

        int outDegree = 0;
        for (int w : adj) {
            outDegree++;
            if (dfsState[w] == UNPROCESSED) {
                dfs(w);
            } else if (dfsState[w] == VISITED) {
                //find cycles
                throw new IllegalArgumentException();
            }
        }

        //check for multiple roots
//        if (outDegree == 0) {
//            if (root > -1) {
//                throw new IllegalArgumentException();
//            } else {
//                root = v;
//            }
//
//        }

        dfsState[v] = DONE;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return ancestorAndLength(v, w)[1];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return ancestorAndLength(v, w)[0];
    }


    private int[] ancestorAndLength(int v, int w) {
        ArrayList<Integer> vList = new ArrayList<>();
        vList.add(v);
        ArrayList<Integer> wList = new ArrayList<>();
        wList.add(w);

        return ancestorAndLength(vList, wList);
    }

    private int[] ancestorAndLength(Iterable<Integer> v, Iterable<Integer> w) {

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);

        boolean[] marked = new boolean[digraph.V()];
        Queue<Integer> q = new Queue<>();

        int length = Integer.MAX_VALUE;
        int ancestor = -1;

        for (int s : v) {
            marked[s] = true;
            q.enqueue(s);
        }

        while (!q.isEmpty()) {
            int v1 = q.dequeue();
            for (int w1 : digraph.adj(v1)) {
                if (!marked[w1]) {
                    marked[w1] = true;
                    q.enqueue(w1);
                }
            }

            if (bfsW.hasPathTo(v1)) {
                int currentLenght = bfsW.distTo(v1) + bfsV.distTo(v1);
                if (currentLenght < length) {
                    ancestor = v1;
                    length = currentLenght;
                }
            }
        }

        if (ancestor == -1) {
            length = -1;
        }

        int[] result = {ancestor, length};
        return result;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return ancestorAndLength(v, w)[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return ancestorAndLength(v, w)[0];
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        In file = new In("D:\\myProjects\\coursera\\alg2\\week1\\wordnet\\digraph1.txt");
        Digraph digraph = new Digraph(file);

        SAP sap = new SAP(digraph);
        System.out.println(sap.ancestor(12, 9));
    }
}
