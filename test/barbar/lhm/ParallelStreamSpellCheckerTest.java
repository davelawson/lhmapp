package barbar.lhm;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class ParallelStreamSpellCheckerTest {

    private String typicalWordList() {
        return "one two three four five six seven eight nine ten eleven twelve thirteen fourteen fifteen sixteen seventeen eighteen nineteen twenty";
    }

    private String textWithOnlyTypicalWords() {
        return "One, two three ten.  Six!  Four; five ten ten ten    eight...";
    }

    private String textWithSomeAtypicalWords() {
        return "One, two three ten.  Bob!  Four; five ten ten sally    eight...";
    }

    private List<SuspiciousWord> search(ParallelStreamSpellChecker checker, String text) {
        List<SuspiciousWord> result = null;
        result = checker.findAllSpellingErrors(text);
        return result;
    }

    @Test
    public void emptyTextEmptyWordList() {
        ParallelStreamSpellChecker checker = new ParallelStreamSpellChecker("");
        List<SuspiciousWord> results = search(checker, "");
        assertTrue(results.isEmpty());
    }

    @Test
    public void typicalTextEmptyWordList() {
        ParallelStreamSpellChecker checker = new ParallelStreamSpellChecker("");
        List<SuspiciousWord> results = search(checker, textWithOnlyTypicalWords());

        String[] wordsInText = textWithOnlyTypicalWords().split("[\\W]+");
        assertTrue(results.size() == wordsInText.length);
        for( int i = 0; i < wordsInText.length; i++ ) {
            assertTrue(results.get(i).getWord().equals(wordsInText[i]));
        }
    }

    @Test
    public void emptyTextTypicalWordList() {
        ParallelStreamSpellChecker checker = new ParallelStreamSpellChecker(typicalWordList());
        List<SuspiciousWord> results = search(checker, "");
        assertTrue(results.isEmpty());
    }

    @Test
    public void typicalTextTypicalWordList() {
        ParallelStreamSpellChecker checker = new ParallelStreamSpellChecker(typicalWordList());
        List<SuspiciousWord> results = search(checker, typicalWordList());
        assertTrue(results.isEmpty());
    }

    @Test
    public void atypicalTextTypicalWordList() {
        ParallelStreamSpellChecker checker = new ParallelStreamSpellChecker(typicalWordList());
        List<SuspiciousWord> results = search(checker, textWithSomeAtypicalWords());
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).getWord().equals("Bob"));
        assertTrue(results.get(1).getWord().equals("sally"));
    }
}
