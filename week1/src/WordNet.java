import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordNet {

    private final SAP sap;
    private final Map<Integer, Synset> synsetIdMap;
    private final Map<String, Set<Integer>> nounMap;

    // constructor takes the name of the two input files
    public WordNet(String synsetsFile, String hypernymsFile) {
        In synSetsIn = new In(synsetsFile);
        synsetIdMap = new HashMap<>();
        nounMap = new HashMap<>();
        while (synSetsIn.hasNextLine()) {
            String line = synSetsIn.readLine();
            Synset synset = new Synset(line);
            synsetIdMap.put(synset.id, synset);

            String[] nouns = synset.synset.split("\\s");
            for (String noun : nouns) {
                Set<Integer> nounSet = nounMap.get(noun);
                if (nounSet == null) {
                    nounSet = new HashSet<>();
                    nounMap.put(noun, nounSet);
                }
                nounSet.add(synset.id);
            }
        }

        Digraph digraph = new Digraph(synsetIdMap.size());

        In hypernymsIn = new In(hypernymsFile);
        while (hypernymsIn.hasNextLine()) {
            String line = hypernymsIn.readLine();
            String[] strings = line.split(",");

            int v = Integer.parseInt(strings[0]);
            for (int i = 1; i < strings.length; i++) {
                int w = Integer.parseInt(strings[i]);
                digraph.addEdge(v, w);
            }
        }

        sap = new SAP(digraph);
    }

    private static class Synset {
        private final int id;
        private final String synset;

        private Synset(String line) {
            String[] strings = line.split(",");

            id = Integer.parseInt(strings[0]);
            synset = strings[1];
        }
    }

    // the nouns of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nounMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {

        Set<Integer> v = nounMap.get(nounA);
        Set<Integer> w = nounMap.get(nounB);

        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        int length = sap.length(v, w);

        return length;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        Set<Integer> v = nounMap.get(nounA);
        Set<Integer> w = nounMap.get(nounB);

        int ancestorId = sap.ancestor(v, w);

        String ancestor = null;
        if (ancestorId > -1) {
            Synset synset = synsetIdMap.get(ancestorId);
            ancestor = synset.synset;
        }

        return ancestor;
    }

    // for unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("D:\\myProjects\\coursera\\alg2\\week1\\synsets.txt", "D:\\myProjects\\coursera\\alg2\\week1\\wordnet\\hypernymsInvalidCycle.txt");

        String ancestor = wordNet.sap("word", "raven");
        System.out.println(ancestor);
    }
}
