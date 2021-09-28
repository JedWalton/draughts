package assignment1;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class MainMenuController {
        
    //AI player difficulty
    ObservableList<String> difficultyOfAIList = FXCollections.observableArrayList("Easy", "Normal", "Hard", "Very Hard", "Nightmare");

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("game");
    }
    
    
    @FXML
    public ChoiceBox<String> difficultyBox = new ChoiceBox<>();
    
    @FXML
    public static int sizeOfSquare = 50;
    
    @FXML
    public Square[][] checkerBoard = new Square[8][8];
    
    public static String value;

    @FXML
    private void initialize() {
        difficultyBox.getItems().add("Very Easy");
        difficultyBox.getItems().add("Easy");
        difficultyBox.getItems().add("Normal");
        difficultyBox.getItems().add("Hard");
        difficultyBox.getItems().add("Very Hard");
        difficultyBox.getItems().add("Extremely Hard");
        difficultyBox.getItems().add("Nightmare");
        
        //Listen for selection changes.
        difficultyBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> this.value = newValue );
    }
    

}
