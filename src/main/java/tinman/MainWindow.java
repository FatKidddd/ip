package tinman;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private TinMan tinman;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/hasbulla.jpg"));
    private Image tinmanImage = new Image(this.getClass().getResourceAsStream("/images/hasbulla_glasses.jpg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the TinMan instance.
     *
     * @param tinman The TinMan instance to use.
     */
    public void setTinMan(TinMan tinman) {
        this.tinman = tinman;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing TinMan's reply
     * and then appends them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String userText = userInput.getText();
        if (userText.trim().isEmpty()) {
            return;
        }

        String tinmanText = getResponse(userText);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userImage),
                DialogBox.getTinManDialog(tinmanText, tinmanImage)
        );
        userInput.clear();
    }

    private String getResponse(String input) {
        try {
            return tinman.processCommandForGui(input);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
