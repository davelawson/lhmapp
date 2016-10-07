package barbar.lhm;

import java.util.HashSet;
import java.util.List;

/**
 * A thread responsible for verifying a given word's presence in a list of words.
 */
public class WordLookupRunnable implements Runnable {

    final private SuspiciousWord word;
    final private HashSet<String> wordList;
    final private List<SuspiciousWord> suspiciousWords;

    /**
     * Create a new WordLookupRunnable.
     * @param word              The word that is to be searched for.
     * @param wordList          The HashSet in which to search for the word.  Contents should be in lower case.
     * @param suspiciousWords   The list to which we should add the word, provided it is not present in the list.
     */
    public WordLookupRunnable(SuspiciousWord word, HashSet<String> wordList, List<SuspiciousWord> suspiciousWords) {
        this.word = word;
        this.wordList = wordList;
        this.suspiciousWords = suspiciousWords;
    }

    @Override
    public void run() {
        if (!wordList.contains(word.getWord().toLowerCase())) {
            addResult(suspiciousWords, word);
        }
    }

    /**
     * Many concurrent threads might be trying to add results to the list of suspicious words simultaneously.  This is therefore synchronized.
     * @param suspiciousWords A list to which the new word is to be added.
     * @param word The newly discovered suspicious word.
     */
    private static synchronized void addResult(List<SuspiciousWord> suspiciousWords, SuspiciousWord word) {
        suspiciousWords.add(word);
    }
}
