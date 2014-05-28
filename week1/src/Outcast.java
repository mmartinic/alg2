public class Outcast {

    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {

        String outcast = null;
        int maxDistance = -1;
        for (int i = 0; i < nouns.length; i++) {
            String noun1 = nouns[i];

            int distance = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i == j) {
                    continue;
                }
                String noun2 = nouns[j];
                distance += wordnet.distance(noun1, noun2);
            }

            if (distance > maxDistance) {
                outcast = noun1;
                maxDistance = distance;
            }
        }
        return outcast;
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("D:\\myProjects\\coursera\\alg2\\week1\\synsets.txt", "D:\\myProjects\\coursera\\alg2\\week1\\wordnet\\hypernymsInvalidTwoRoots.txt");
        Outcast outcast = new Outcast(wordnet);
        String[] array = {"horse", "zebra", "cat", "bear", "table"};
        StdOut.println(outcast.outcast(array));
    }
}
