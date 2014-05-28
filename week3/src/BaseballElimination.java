import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BaseballElimination {

    private final int n;
    private final Map<String, Integer> names;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] g;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();

        names = new HashMap<>(n);
        wins = new int[n];
        losses = new int[n];
        remaining = new int[n];
        g = new int[n][n];

        for (int i = 0; i < n; i++) {
            String teamName = in.readString();
            names.put(teamName, i);
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();

            for (int j = 0; j < n; j++) {
                g[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return names.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        checkName(team);
        return wins[names.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        checkName(team);
        return losses[names.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        checkName(team);
        return remaining[names.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        checkName(team1);
        checkName(team2);

        return g[names.get(team1)][names.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        checkName(team);

        Iterable<String> certificateOfElimination = certificateOfElimination(team);
        return certificateOfElimination != null;
    }

    private Set<String> trivialCertificateOfElimination(String team) {
        int x = names.get(team);
        Set<String> coe = null;

        for (String t : teams()) {
            if (team.equals(t)) {
                continue;
            }

            if (wins[x] + remaining[x] < wins[names.get(t)]) {

                if (coe == null) {
                    coe = new HashSet<>();
                }
                coe.add(t);
            }
        }
        return coe;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        checkName(team);

        Set<String> coe = trivialCertificateOfElimination(team);

        if (coe != null) {
            return coe;
        }

        FlowNetwork flowNetwork = createFlowNetwork(team);
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, flowNetwork.V() - 2, flowNetwork.V() - 1);

        for (String t : teams()) {
            if (team.equals(t)) {
                continue;
            }

            if (fordFulkerson.inCut(names.get(t))) {
                if (coe == null) {
                    coe = new HashSet<>();
                }
                coe.add(t);
            }
        }

        return coe;
    }

    private void checkName(String team) {
        if (!names.containsKey(team)) {
            throw new IllegalArgumentException();
        }
    }

    private FlowNetwork createFlowNetwork(String team) {
        int V = n + n * n + 1 + 1;
        int x = names.get(team);

        int s = V - 2;
        int t = V - 1;
        FlowNetwork flowNetwork = new FlowNetwork(V);

        for (int i = 0; i < n; i++) {
            if (i == x) {
                continue;
            }
            for (int j = i + 1; j < n; j++) {
                if (j == x) {
                    continue;
                }
                int gv = getGameVertex(i, j);

                FlowEdge flowEdge = new FlowEdge(s, gv, g[i][j]);
                flowNetwork.addEdge(flowEdge);

                flowEdge = new FlowEdge(gv, i, Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(flowEdge);

                flowEdge = new FlowEdge(gv, j, Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(flowEdge);
            }
        }

        for (int tv = 0; tv < n; tv++) {
            if (tv == x) {
                continue;
            }
            FlowEdge flowEdge = new FlowEdge(tv, t, wins[x] + remaining[x] - wins[tv]);
            flowNetwork.addEdge(flowEdge);
        }

        return flowNetwork;
    }

    private int getGameVertex(int i, int j) {
        int v = i * n + j + n;
        return v;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("D:\\myProjects\\coursera\\alg2\\week3\\baseball\\teams4.txt");
//        String team = "New_York";
//        System.out.println(division.isTriviallyEliminated(team));
//        System.out.println(division.isEliminated(team));
//        System.out.println(division.certificateOfElimination(team));

        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
