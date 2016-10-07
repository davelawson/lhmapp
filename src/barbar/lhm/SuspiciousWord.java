package barbar.lhm;

/**
 * Represents a word that was found to be suspicious within a greater text.
 */
public class SuspiciousWord implements Comparable<SuspiciousWord> {
    final private int index;
    final private String word;

    public int getIndex() {
        return index;
    }

    public String getWord() {
        return word;
    }

    public SuspiciousWord(int index, String word) {
        this.index = index;
        this.word = word;
    }

    @Override
    public int compareTo(SuspiciousWord o) {
        return index - o.getIndex();
    }
}
