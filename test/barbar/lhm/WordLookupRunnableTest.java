package barbar.lhm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WordLookupRunnableTest {
    private static final int MAX_THREAD_DELAY_MILLISECONDS = 1000;

    private HashSet<String> typicalWordList() {
        HashSet<String> wordList = new HashSet();
        wordList.add("one");
        wordList.add("two");
        wordList.add("three");
        return wordList;
    }

    private void run(WordLookupRunnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join(MAX_THREAD_DELAY_MILLISECONDS);
        } catch (InterruptedException e) {
            fail("WordLookupRunnable exceeded allowable time.");
        }
    }

    @Test
    public void emptyWordList() {
        SuspiciousWord word = new SuspiciousWord(1,"notfound");
        HashSet<String> wordList = new HashSet();
        List<SuspiciousWord> resultsList = new ArrayList();
        run (new WordLookupRunnable(word, wordList, resultsList));
        assertTrue("New suspicious word was not added to an empty results list.", resultsList.contains(word));
    }
    @Test
    public void wordNotFoundInTypicalWordList() {
        SuspiciousWord word = new SuspiciousWord(1,"notfound");
        HashSet<String> wordList = typicalWordList();
        List<SuspiciousWord> resultsList = new ArrayList();
        run(new WordLookupRunnable(word, wordList, resultsList));
        assertTrue("New suspicious word was not added to an empty results list.", resultsList.contains(word));
    }
    @Test
    public void wordFoundInTypicalWordList() {
        SuspiciousWord word = new SuspiciousWord(1,"two");
        HashSet<String> wordList = typicalWordList();
        List<SuspiciousWord> resultsList = new ArrayList();
        run(new WordLookupRunnable(word, wordList, resultsList));
        assertFalse("New suspicious word was added to an empty results list.", resultsList.contains(word));
    }
}
