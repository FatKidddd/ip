package tinman;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    private Label text;
    private ImageView displayPicture;

    /**
     * Creates a DialogBox with the specified text and image.
     *
     * @param text The text to display in the dialog.
     * @param img The image to display alongside the text.
     */
    public DialogBox(String text, Image img) {
        this.text = new Label(text);
        displayPicture = new ImageView(img);

        this.text.setWrapText(true);
        this.text.setMaxWidth(300);
        this.text.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 15; -fx-padding: 12; -fx-font-size: 14px;");
        
        displayPicture.setFitWidth(80.0);
        displayPicture.setFitHeight(80.0);
        displayPicture.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 2, 2);");

        this.setAlignment(Pos.TOP_RIGHT);
        this.setSpacing(12);
        this.setStyle("-fx-padding: 5;");
        this.getChildren().addAll(this.text, displayPicture);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
    }

    /**
     * Creates a dialog box for user input.
     *
     * @param text The user's input text.
     * @param img The user's avatar image.
     * @return A DialogBox representing the user's message.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    /**
     * Creates a dialog box for TinMan's response.
     *
     * @param text TinMan's response text.
     * @param img TinMan's avatar image.
     * @return A DialogBox representing TinMan's message.
     */
    public static DialogBox getTinManDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        db.text.setStyle("-fx-background-color: #f3e5f5; -fx-background-radius: 15; -fx-padding: 12; -fx-font-size: 14px;");
        return db;
    }
}