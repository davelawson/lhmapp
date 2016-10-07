package barbar.lhm;

import java.util.List;

public interface ISpellChecker {
    boolean isWord(String word);
    List<SuspiciousWord> findAllSpellingErrors(String text) throws InterruptedException;
}
