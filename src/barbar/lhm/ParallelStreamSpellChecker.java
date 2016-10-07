package barbar.lhm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParallelStreamSpellChecker extends ASpellChecker {
    public ParallelStreamSpellChecker(String words) {
        super(words);
    }

    /**
     * Identify all unrecognized words in a body of text.  Runs in multiple threads.
     *
     * @param text A body of text that is to be scoured for spelling concerns
     * @return A list of suspicious words, in the order they are encountered.  May be empty.
     */
    @Override
    public List<SuspiciousWord> findAllSpellingErrors(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<SuspiciousWord> textAsList = new ArrayList<>();
            String[] rawWords = text.split("[\\W]+");
            int i = 0;
            for (String rawWord : rawWords) {
                textAsList.add(new SuspiciousWord(i++, rawWord));
            }
            List<SuspiciousWord> oddWords = textAsList.parallelStream()
                    .filter(w -> !isWord(w.getWord().toLowerCase()))
                    .collect(Collectors.toList());

            return oddWords;
        }
    }
}
