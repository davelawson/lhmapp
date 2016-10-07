package barbar.lhm;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ManualThreadedSpellChecker extends ASpellChecker {
    public ManualThreadedSpellChecker(String words) {
        super(words);
    }

    /**
     * Identify all unrecognized words in a body of text.  Runs in multiple threads.
     *
     * @param text A body of text that is to be scoured for spelling concerns
     * @return A list of suspicious words, in the order they are encountered.  May be empty.
     */
    @Override
    public List<SuspiciousWord> findAllSpellingErrors(String text) throws InterruptedException {
        List<SuspiciousWord> suspiciousWords = new LinkedList<>();
        if ( !text.isEmpty() ) {
            String[] textAsArray = text.split("[\\W]+");
            ExecutorService executor = Executors.newFixedThreadPool(10);
            int wordCtr = 0;
            for (String word : textAsArray) {
                executor.execute(new WordLookupRunnable(new SuspiciousWord(wordCtr++, word), words, suspiciousWords));
            }
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            Collections.sort(suspiciousWords);
        }
        return suspiciousWords;
    }
}
