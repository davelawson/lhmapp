package barbar.lhm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class Controller {
    @FXML
    private Text textFileName;
    @FXML
    private Text dictionaryFileName;
    @FXML
    private TextArea textBody;
    @FXML
    private Button loadDictionaryButton;
    @FXML
    private Button loadTextButton;
    @FXML
    private TextField correctWordText;
    @FXML
    private Button correctButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button exitButton;

    private Optional<ParallelStreamSpellChecker> spellCheckerParallelStream = Optional.empty();
    private Optional<ManualThreadedSpellChecker> spellCheckerManualThreaded = Optional.empty();
    private Optional<List<SuspiciousWord>> suspiciousWords = Optional.empty();
    private Optional<SuspiciousWord> selectedWord = Optional.empty();
    private int suspiciousWordIndex = 0;

    @FXML
    protected void loadDictionaryButtonOnClick(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Dictionary File");
        fileChooser.setInitialDirectory(new File("."));
        Optional<File> dictionaryFile = Optional.ofNullable(fileChooser.showOpenDialog(loadDictionaryButton.getScene().getWindow()));

        if (dictionaryFile.isPresent()) {
            dictionaryFileName.setText(dictionaryFile.get().getAbsoluteFile().getAbsolutePath());
            String words = loadDictionary(dictionaryFile.get());

            spellCheckerParallelStream = Optional.of(new ParallelStreamSpellChecker(words));
            spellCheckerManualThreaded = Optional.of(new ManualThreadedSpellChecker(words));
        }
    }

    @FXML
    protected void loadTextButtonOnClick(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Text File");
        fileChooser.setInitialDirectory(new File("."));
        Optional<File> textFile = Optional.ofNullable(fileChooser.showOpenDialog(loadTextButton.getScene().getWindow()));

        if (textFile.isPresent()) {
            textFileName.setText(textFile.get().getAbsoluteFile().getAbsolutePath());
            byte[] encoded = Files.readAllBytes(Paths.get(textFile.get().getAbsolutePath()));
            textBody.setText(new String(encoded));
        }
    }

    private String loadDictionary(File file) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        return new String(encoded);
    }

    private void selectFirstSuspiciousWord() {
        textBody.selectRange(0,0);
        selectSuspiciousWord(0);
    }

    private void selectSuspiciousWord(int index) {
        suspiciousWordIndex = index;
        selectedWord = Optional.of(suspiciousWords.get().get(index));
        correctWordText.setText(selectedWord.get().getWord());
        correctButton.setDisable(false);
        nextButton.setDisable(index >= suspiciousWords.get().size() - 1);

        int newAnchor = textBody.getText().indexOf(selectedWord.get().getWord(), textBody.getAnchor());
        textBody.selectRange( newAnchor, newAnchor+selectedWord.get().getWord().length() );
    }

    @FXML
    protected void scanParallelStreamButtonOnClick(ActionEvent actionEvent) throws InterruptedException {
        if (spellCheckerParallelStream.isPresent() && !textBody.getText().isEmpty()) {
            suspiciousWords = Optional.of(spellCheckerParallelStream.get().findAllSpellingErrors(textBody.getText()));
            if (suspiciousWords.get().isEmpty()) {
                correctButton.setDisable(true);
                nextButton.setDisable(true);
            } else {
                selectFirstSuspiciousWord();
            }
        } else {
            suspiciousWords = Optional.empty();
        }
    }

    @FXML
    public void scanThreadPoolButtonOnClick(ActionEvent actionEvent) throws InterruptedException {
        if (spellCheckerManualThreaded.isPresent() && !textBody.getText().isEmpty()) {
            suspiciousWords = Optional.of(spellCheckerManualThreaded.get().findAllSpellingErrors(textBody.getText()));
            if (suspiciousWords.get().isEmpty()) {
                correctButton.setDisable(true);
                nextButton.setDisable(true);
            } else {
                selectFirstSuspiciousWord();
            }
        } else {
            suspiciousWords = Optional.empty();
        }
    }

    @FXML
    protected void saveTextButtonOnClick(ActionEvent actionEvent) throws IOException {
        if (!textBody.getText().isEmpty()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Text File");
            Optional<File> textFile = Optional.ofNullable(fileChooser.showSaveDialog(loadTextButton.getScene().getWindow()));
            if (textFile.isPresent()) {
                Files.write(Paths.get(textFile.get().getAbsolutePath()), textBody.getText().getBytes());
            }
        }
    }

    @FXML
    protected void exitButtonOnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void nextButtonOnClick(ActionEvent actionEvent) {
        selectSuspiciousWord(suspiciousWordIndex+1);
    }

    @FXML
    public void correctButtonOnClick(ActionEvent actionEvent) {
        if (selectedWord.isPresent()) {
            int anchor = textBody.getAnchor();
            String newTextBody = textBody.getText(0, anchor) +
                    correctWordText.getText() +
                    textBody.getText(anchor + selectedWord.get().getWord().length(), textBody.getText().length());
            textBody.setText(newTextBody);
            textBody.selectRange(anchor, anchor + correctWordText.getText().length());
        }
    }
}
