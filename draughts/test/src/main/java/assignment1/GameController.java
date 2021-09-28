package assignment1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.util.concurrent.TimeUnit;


public class GameController extends App {

    
    @FXML
    private AnchorPane checkerBoard = new AnchorPane();
    
    @FXML
    public static Group allPlayerTokens = new Group();
    
    @FXML
    public static Group allAITokens = new Group();
    
    @FXML
    public static Group allSquares = new Group();
    
    @FXML
    private void openRules() throws IOException {
        Rules.rules();
    }

    private int squareColor;
    
    private int tokenType;
    
    public static Square[][] gameBoard = new Square[8][8];
    public static Square[][] tempBoard = gameBoard;
    public static int turn = 0;
    public static int forceCap = 0;
    public static boolean pieceMoved = false;
    public static boolean pieceTakenThisMove = false;
    public static boolean GameOver = false;
    
    @FXML
    public Label turnMessage;
    
    public static Label static_turnMessage;
    
    @FXML
    public Label specificMessage;
    
    public static Label static_specificMessage;
    
    @FXML
    public Label difficultyOfAI;
    
    public static Label static_difficultyOfAI;
    
    public static boolean displayMSG = false;
    
    public static int numberOfPlayerTokens;
    
    public static int numberOfAITokens;
    
    public static int numberOfPlayerKings;
    
    public static int numberOfAIKings;
    
    public static int difficulty;
    
    public static List<Position> availablePositions;
    
    public static List<PositionsAndScores> successorEvaluations;
    
    public static int deCount, seCount, pCount;
    
    public static void makeBestMove(){
        startEvaluation();
        int bestMoveCurX = GameController.getBestMove().currentX;
        int bestMoveCurY = GameController.getBestMove().currentY;
        int bestMoveNewX = GameController.getBestMove().X;
        int bestMoveNewY = GameController.getBestMove().Y;
        System.out.println(bestMoveCurX);
        System.out.println(bestMoveCurY);
        System.out.println(bestMoveNewX);
        System.out.println(bestMoveNewY);
        GameController.gameBoard[bestMoveCurX][bestMoveCurY].getTokenOnSquare().makeMove(bestMoveNewX, bestMoveNewY, bestMoveCurX, bestMoveCurY);
    }
    
    //Takes difficulty from main menu and applies it to variable 'difficulty' 
    //This is then used for the depth of search in minimax.
    public int setupDifficulty(){
        if(MainMenuController.value == null){
            difficulty = 7;
            MainMenuController.value = "Normal";
        }
        if(MainMenuController.value == "Very Easy"){
            difficulty = 2;
        }
        if(MainMenuController.value == "Easy"){
            difficulty = 3;
        }
        if(MainMenuController.value == "Normal"){
            difficulty = 7;
        }
        if(MainMenuController.value == "Hard"){
            difficulty = 11;
        }
        if(MainMenuController.value == "Very Hard"){
            difficulty = 17;
        }
        if(MainMenuController.value == "Extremely Hard"){
            difficulty = 29;
        }
        if(MainMenuController.value == "Nightmare"){
            difficulty = 47;
        }
        return this.difficulty = difficulty;
    }
    
    public static int minimax(int depth, int player, int alpha, int beta) {
        int bestScore = 0;
        int currentScore;
        
        tempBoard = gameBoard;
        List<Position> positionsAvailable = getAvailableStates();
        if(player == 1){
            bestScore = 0;
        }
        
        else if (player == 2){
            if(GameController.GameOver != true) {
                seCount++;
                currentScore = Score();       
            }
            else{
                return 0;
            }
            for (int i = 0; i < positionsAvailable.size(); i++){
                Position pos = positionsAvailable.get(i);
                currentScore = Score(); 
                if(player == 2){
                    gameBoard[pos.currentX][pos.currentY].getTokenOnSquare().makeMove(pos.X, pos.Y, pos.currentX, pos.currentY);
                    currentScore = minimax(depth + 1, 1, alpha, beta);
                    if(currentScore > bestScore){
                        bestScore = currentScore;
                    }
                    alpha = Math.max(currentScore, alpha);

                    if(depth == 0){
                        GameController.successorEvaluations.add(new PositionsAndScores(currentScore, pos));

                    }
                }
                else if(player == 1){
                    gameBoard[pos.currentX][pos.currentY].getTokenOnSquare().makeMove(pos.X, pos.Y, pos.currentX, pos.currentY);
                    currentScore = minimax(depth + 1, 0, alpha, beta);
                    if(currentScore < bestScore){
                        bestScore = currentScore;
                    }
                    beta = Math.min(currentScore, beta);
                }

                //UNDO MOVE MADE
                gameBoard = tempBoard;

                if(alpha >= beta) {
                    pCount++;
                    break;
                }
            }
        }
        return bestScore;
    }
    
    
    public static int Score(){
        int totalScore = 0;
        int seCount = 0;
        int numKingAI = GameController.numberOfAIKings;
        int numKingPlayer = GameController.numberOfPlayerKings;
        int numAI = GameController.numberOfAITokens;
        int numPlayer = GameController.numberOfPlayerTokens;
        
        int player =  -(numPlayer + numKingPlayer);
        int AI =  (numAI + numKingAI);
        
        
        totalScore = player + AI;
        
        return totalScore;
    }
    
    public static Position getBestMove(){
        int max = 0;
        int best = 0;
        for (int i = 0; i < successorEvaluations.size(); i++){
            if (max < successorEvaluations.get(i).score) {
                max = successorEvaluations.get(i).score;
                best = i;
            }
        }
        System.out.println(successorEvaluations.get(best).pos);
        return GameController.successorEvaluations.get(best).pos;
    }
    
    public static void startEvaluation(){
        successorEvaluations = new ArrayList<>();
        minimax(difficulty, 2, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    public static void placePiece(Position pos) {
        if(gameBoard[pos.currentX][pos.currentY].token != null){
            gameBoard[pos.currentX][pos.currentY].token.makeMove(pos.X, pos.Y, pos.currentX, pos.currentY);
        }
    }
    
    
    public static List<Position> getAvailableStates(){
        availablePositions = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if(gameBoard[i][j].getTokenOnSquare() != null){
                    gameBoard[i][j].getTokenOnSquare().ForceCapture();
                    int curX = gameBoard[i][j].getTokenOnSquare().currentX;
                    int curY = gameBoard[i][j].getTokenOnSquare().currentY;
                    int p = 2;
                    //}
                    
                    int newX1 = i + 1;
                    if(newX1 > 7){
                        newX1 = 7;
                    }
                    int newX2 = i + 2;
                    if(newX2 > 7){
                        newX2 = 7;
                    }
                    int newX3 = i -1;
                    if(newX3 < 0){
                        newX3 = 0;
                    }
                    int newX4 = i -2;
                    if(newX4 < 0){
                        newX4 = 0;
                    }
                    int newY1 = j + 1;
                    if(newY1 > 7){
                        newY1 = 7;
                    }
                    int newY2 = j + 2;
                    if(newY2 > 7){
                        newY2 = 7;
                    }
                    int newY3 = j - 1;
                    if(newY3 < 0){
                        newY3 = 0;
                    }
                    int newY4 = j - 2;
                    if(newY4 < 0){
                        newY4 = 0;
                    }
                            
                    int vP = 1;
                    
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX1, newY1, curX, curY, 0, p, vP) == true) {
                        availablePositions.add(new Position(newX1, newY1, curX, curY));
                    }
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX3, newY1, curX, curY, 0, p, vP) == true){
                        availablePositions.add(new Position(newX3, newY1, curX, curY));
                    }
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX3, newY3, curX, curY, 0, p, vP) == true){
                        availablePositions.add(new Position(newX3, newY3, curX, curY));
                    }
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX1, newY3, curX, curY, 0, p, vP) == true){
                        availablePositions.add(new Position(newX1, newY3, curX, curY));
                    }
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX4, newY2, curX, curY, 0, p, vP) == true){
                        availablePositions.add(new Position(newX4, newY2, curX, curY));
                    }
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX2, newY2, curX, curY, 0, p, vP) == true){
                        availablePositions.add(new Position(newX2, newY2, curX, curY));
                    }
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX4, newY4, curX, curY, 0, p, vP) == true){
                        availablePositions.add(new Position(newX4, newY4, curX, curY));
                    }
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX2, newY4, curX, curY, 0, p, vP) == true){
                        availablePositions.add(new Position(newX2, newY4, curX, curY));
                    }
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX1, newY1, curX, curY, 1, p, vP) == true){
                        availablePositions.add(new Position(newX1, newY1, curX, curY));
                    }
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX3, newY1, curX, curY, 1, p, vP) == true){
                        availablePositions.add(new Position(newX3, newY1, curX, curY));
                    }
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX3, newY3, curX, curY, 1, p, vP) == true){
                        availablePositions.add(new Position(newX3, newY3, curX, curY));
                    }
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX1, newY3, curX, curY, 1, p, vP) == true){
                        availablePositions.add(new Position(newX1, newY3, curX, curY));
                    }
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX4, newY2, curX, curY, 1, p, vP) == true){
                        availablePositions.add(new Position(newX4, newY2, curX, curY));
                    }
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX2, newY2, curX, curY, 1, p, vP) == true){
                        availablePositions.add(new Position(newX2, newY2, curX, curY));
                    }
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX4, newY4, curX, curY, 1, p, vP) == true){
                        availablePositions.add(new Position(newX4, newY4, curX, curY));
                    }
                    if(gameBoard[i][j].getTokenOnSquare().validMove(newX2, newY4, curX, curY, 1, p, vP) == true){
                        availablePositions.add(new Position(newX2, newY4, curX, curY));
                    }
                    
                }
            }
        }
        return availablePositions;
    }
    
    //@FXML
    public void initialize() throws IOException {
        static_turnMessage = turnMessage;
        static_specificMessage = specificMessage;
        static_difficultyOfAI = difficultyOfAI;
        static_turnMessage.setText("Game Started. You are red.");
        static_specificMessage.setText("It is your turn. Good luck");
        setupDifficulty();
        static_difficultyOfAI.setText(MainMenuController.value + " Difficulty");
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {

                if(((i + j) % 2 == 0)) {
                    Square node = new Square(i, j, squareColor=0);;
                    if(j > 4) {
                        int player = 1;
                        Token token = new Token(i, j, player);
                        node.placeTokenOnSquare(token);
                        numberOfPlayerTokens += 1;
                        allPlayerTokens.getChildren().addAll(token);
                        token.setOnMouseEntered(e -> 
                                token.hoveringOverTokenPlayer());
                        token.setOnMouseExited(e -> 
                                token.exitedHoveringOverTokenPlayer());
                    }
                    if(j < 3) {
                        int player = 2;
                        Token token = new Token(i, j, player);
                        node.placeTokenOnSquare(token);
                        numberOfAITokens += 1;
                        allAITokens.getChildren().addAll(token);
                        token.setOnMouseEntered(e -> 
                                token.hoveringOverTokenPlayer());
                        token.setOnMouseExited(e -> 
                                token.exitedHoveringOverTokenPlayer());
                    }
                    allSquares.getChildren().add(node);
                    gameBoard[i][j] = node;
                } else {
                    //fill light blue square
                    Square node = new Square(i, j, squareColor=1);
                    allSquares.getChildren().add(node);
                    gameBoard[i][j] = node;

                }

            }
        }
        checkerBoard.getChildren().addAll(allSquares, allAITokens, allPlayerTokens);
    }
}