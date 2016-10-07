package barbar.lhm;

import java.util.HashSet;

public abstract class ASpellChecker implements ISpellChecker {
    final protected HashSet<String> words = new HashSet();

    /**
     * Builds the list of allowable words.  Stores them in lower case.
     * @param words A string containing recognized words separated by whitespace.
     */
    protected ASpellChecker(String words) {
        String[] wordsArray = words.split("[\\s]+");
        for (String word : wordsArray) {
            this.words.add(word.toLowerCase());
        }
    }

    /**
     * Tests if the supplied word is recognised.
     * @param word A word to be looked for.
     * @return Whether word can be found in the list of words.
     */
    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }
}
