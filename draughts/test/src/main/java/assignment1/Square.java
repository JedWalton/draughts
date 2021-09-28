
package assignment1;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Square extends Rectangle {
    
    private int X;
    private int Y;
    public Token token;
    public int squareColor;
    private double cursorX;
    private double cursorY;
    private int originalSqColor;
    
    //defines the size of each square on the board. This is used in GameController.java
    public Square(int X, int Y, int squareColor) {
        this.squareColor = squareColor;
        Rectangle rectangle = new Rectangle();
        this.setX(X*MainMenuController.sizeOfSquare);
        this.setY(Y*MainMenuController.sizeOfSquare);
        this.setWidth(MainMenuController.sizeOfSquare);
        this.setHeight(MainMenuController.sizeOfSquare);
        if (this.squareColor == 0) {
            this.setFill(Color.BLUE);
        }
        if (this.squareColor == 1) {
            this.setFill(Color.LIGHTBLUE);
        }
        if (this.squareColor == 2){
            this.setFill(Color.BLANCHEDALMOND);
        }
        
        
    }
    
    public void displayTokenOnSquare() {
        this.token.setCenterX((X*MainMenuController.sizeOfSquare)+(0.5*(MainMenuController.sizeOfSquare)));
        this.token.setCenterY((Y*MainMenuController.sizeOfSquare)+(0.5*(MainMenuController.sizeOfSquare)));
        this.token.setRadius(15);
    }

    public Token placeTokenOnSquare(Token token) {
        this.token = token;
        return token;
    }
    
    public void alignToken(int X, int Y) {
        this.token.setCenterX((X*MainMenuController.sizeOfSquare)+(0.5*(MainMenuController.sizeOfSquare)));
        this.token.setCenterY((Y*MainMenuController.sizeOfSquare)+(0.5*(MainMenuController.sizeOfSquare)));

    }

    public Token removeToken() {
        return this.token = null;
    }
    
    public Token getTokenOnSquare(){
        return this.token;
    }
    
    public void hoveringOverValidMove() {
        //converts raw coordinates to X and Y int value in range 1-8
        int currentX = this.token.currentX;
        int currentY = this.token.currentY;
    }
    public void exitedHoveringOverValidMove() {
        
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if((i + j) % 2 == 0) {
                    GameController.gameBoard[i][j].setFill(Color.BLUE);
                } 
            }
        }
    }
    
}
