package tinman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tinman.exception.TinManException;
import tinman.parser.CommandType;
import tinman.parser.Parser;

/**
 * JavaFX GUI for TinMan chatbot application.
 */
public class Main extends Application {

    private TinMan tinman;
    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/hasbulla.jpg"));
    private Image tinmanImage = new Image(this.getClass().getResourceAsStream("/images/hasbulla_glasses.jpg"));

    /**
     * Default constructor for JavaFX Application.
     */
    public Main() {
        tinman = new TinMan("./data/tinman.txt");
    }

    @Override
    public void start(Stage stage) {
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        scene = new Scene(mainLayout);

        stage.setScene(scene);
        stage.show();

        stage.setTitle("TinMan - Your Personal Task Assistant");
        stage.setResizable(true);
        stage.setMinHeight(600.0);
        stage.setMinWidth(450.0);

        mainLayout.setPrefSize(450.0, 600.0);
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        scrollPane.setPrefSize(435, 535);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent;");

        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        dialogContainer.setSpacing(15);
        dialogContainer.setStyle("-fx-padding: 15; -fx-background-color: transparent;");

        userInput.setPrefWidth(365.0);
        userInput.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-padding: 10;");
        userInput.setPromptText("Type your message here...");

        sendButton.setPrefWidth(60.0);
        sendButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 15; -fx-font-weight: bold;");

        AnchorPane.setTopAnchor(scrollPane, 1.0);

        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);

        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);

        sendButton.setOnMouseClicked((event) -> {
            handleUserInput();
        });

        userInput.setOnAction((event) -> {
            handleUserInput();
        });

        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));
    }

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