
package assignment1;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import static javafx.scene.transform.Transform.translate;


public class Token extends Circle {
    
    
    private int X;
    private int Y;
    private int lastPosX;
    private int lastPosY;
    private double cursorX;
    private double cursorY;
    public int player;
    public int tokenType = 0;
    public int currentX;
    public int currentY;
    public int lastPlayerMoved;
    private int validationPass = 0;
    private boolean canMove = false;
    

    
    public Token(int X, int Y, int player) {
        currentX = X;
        currentY = Y;
        /*this.tokenType = tokenType*/;
        this.player = player;
        Circle circle = new Circle();
        drawToken(X, Y, circle);
        
        if (this.player == 1) {
            if (this.tokenType == 0) {
                this.setFill(Color.DARKRED); //REGULAR PIECE
            }
            if (this.tokenType == 1) {
                this.setFill(Color.RED); //KING
            }
        }
        if (this.player == 2) {
            if (this.tokenType == 0) {
                this.setFill(Color.GREEN); //REGULAR PIECE
            }
            if (this.tokenType == 1) {
                this.setFill(Color.LAWNGREEN); //KING
            }             
        }
        
        setOnMousePressed( e -> { 
            cursorX = e.getSceneX();
            cursorY = e.getSceneY();
        });
        setOnMouseDragged ( e-> {
            relocate(e.getSceneX() + lastPosX - 15, e.getSceneY() + lastPosY - 15);
        });
        setOnMouseReleased(e -> {
            cursorX = e.getSceneX();
            cursorY = e.getSceneY();

            //converts raw coordinates to X and Y int value in range 1-8
            int newX = convertCursorToBoardCoords(cursorX);
            int newY = convertCursorToBoardCoords(cursorY);

            //aligns index to 0-7 from 1-8
            newX = newX - 1;
            newY = newY - 1;

            //Used in error message function
            lastPlayerMoved = this.player;
            
            makeMove(newX, newY, currentX, currentY);
            turnMessages(newX, newY);
            


        });
    }

    public void makeBestMove(){
        GameController.startEvaluation();
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
    
    
    
    public void makeMove(int newX, int newY, int currentX, int currentY){
        if(validMove(newX, newY, this.currentX, this.currentY, this.tokenType, this.player, validationPass=0) == true) {
            relocate(X*MainMenuController.sizeOfSquare + (0.2*MainMenuController.sizeOfSquare), Y*MainMenuController.sizeOfSquare + (0.2*MainMenuController.sizeOfSquare));
            makeMoveOnBoard(newX, newY, this.currentX, this.currentY);
            relocate(newX*MainMenuController.sizeOfSquare + (0.2*MainMenuController.sizeOfSquare), newY*MainMenuController.sizeOfSquare + (0.2*MainMenuController.sizeOfSquare));
            this.X = newX;
            this.Y = newY;
            GameController.displayMSG = false;
            GameController.static_specificMessage.setText("");
            GameController.pieceMoved = true;

            //if piece has been taken, and the just taken a piece user can jump a piece then user must jump piece. otherwise change turn
            if(GameController.pieceTakenThisMove == false){
                if(GameController.pieceMoved){
                    if(GameController.turn == 0){
                        GameController.turn = 1;
                        GameController.forceCap = 0;
                        GameController.pieceMoved = false;
                        //GameController.makeBestMove();
                    }else if(GameController.turn == 1){
                        GameController.turn = 0;
                        GameController.forceCap = 0;
                        GameController.pieceMoved = false;
                    }
                }
            }

            if(GameController.pieceTakenThisMove == true){
                if(canForceCaptureMoves(this.X, this.Y) == false){
                  if(GameController.turn == 0){
                    GameController.turn = 1;
                    GameController.forceCap = 0;
                    GameController.pieceTakenThisMove = false;
                    //GameController.makeBestMove();
                    }else if(GameController.turn == 1){
                        GameController.turn = 0;
                        GameController.forceCap = 0; 
                        GameController.pieceTakenThisMove = false;
                    }
                }
            }

            GameOver();

        }
        if(validMove(newX, newY, this.currentX, this.currentY, this.tokenType, this.player, validationPass=0) == false) {
            relocate(this.currentX*MainMenuController.sizeOfSquare + (0.2*MainMenuController.sizeOfSquare), this.currentY*MainMenuController.sizeOfSquare + (0.2*MainMenuController.sizeOfSquare));
            GameController.displayMSG = true;
        }
    }
    
    //Checks if game is over
    public void GameOver(){
        if(GameController.numberOfAITokens == 0){
            GameController.GameOver = true;
            GameController.static_turnMessage.setText("GAME OVER");
            GameController.static_specificMessage.setText("You Beat The AI! ... WELL DONE.");
            //Stops users from being able to take turn. 404 Turn not found.
            GameController.turn = 404;
        }
        if(GameController.numberOfPlayerTokens == 0){
            GameController.GameOver = true;
            GameController.static_turnMessage.setText("GAME OVER");
            GameController.static_specificMessage.setText("You lost! Please restart game to play again.");
            //Stops users from being able to take turn. 404 Turn not found.
            GameController.turn = 404;
        }
    }
    
    public void makeMoveOnBoard(int newX, int newY, int curX, int curY) {
        GameController.gameBoard[newX][newY].placeTokenOnSquare(this);
        //System.out.println(GameController.gameBoard[newX][newY].placeTokenOnSquare(this));
        GameController.gameBoard[curX][curY].removeToken();
        this.currentX = newX;
        this.currentY = newY;
    }
    
    public void drawToken(int X, int Y, Circle circle){
        this.setCenterX((X*MainMenuController.sizeOfSquare)+(0.5*(MainMenuController.sizeOfSquare)));
        this.setCenterY((Y*MainMenuController.sizeOfSquare)+(0.5*(MainMenuController.sizeOfSquare)));
        this.setRadius(15);
    }
    
    /*
        Method allows validation of moves.
    */

    public boolean validMove(int X, int Y, int currentX, int currentY, int tokenType, int player, int validationPass){
        boolean vM = false;
        
        /* Allows player and AI to move regular piece to empty square */
        if((X >= 0 && X <= 8) && (Y >= 0 && Y <= 8)){
            if(((X + Y) % 2) == 0 && GameController.gameBoard[X][Y].token == null){
                /*Move To Empty Square*/
                if(canMove == true){
                    if(GameController.pieceTakenThisMove == false){
                        if(GameController.forceCap == 0){
                            if(GameController.turn == 0){
                                if(Y-currentY == (-1)){
                                    if(tokenType == 0){
                                        if(player == 1){
                                            if(X-currentX == 1){
                                                vM = true;
                                            }
                                            if(X-currentX == -1){
                                                vM = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if(canMove == true){
                    if(GameController.pieceTakenThisMove == false){
                        if(GameController.forceCap == 0){
                            if(GameController.turn == 1){
                                if(Y-currentY == (1)){
                                    if(tokenType == 0){
                                        if(player == 2){
                                            if(X-currentX == 1){   
                                                vM = true;
                                            }
                                            if(X-currentX == -1) {
                                                vM = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                /* Allows player and AI to take a token with regular piece */
                //(m)
                if(GameController.turn == 0){
                    if(Y-currentY == (-2)){
                        if(tokenType == 0){
                            if(player == 1){
                                if(X-currentX == 2){
                                    if(GameController.gameBoard[currentX+1][currentY-1].token != null){
                                        if(GameController.gameBoard[currentX+1][currentY-1].getTokenOnSquare().tokenType == 1){
                                            if(validationPass == 0){
                                                GameController.gameBoard[currentX][currentY].token.tokenType = 1;
                                                GameController.gameBoard[currentX][currentY].token.player = 1;
                                                GameController.turn = 1;
                                            }
                                        }
                                        if(GameController.gameBoard[currentX+1][currentY-1].getTokenOnSquare().player == 2){
                                            if(validationPass == 0){
                                                GameController.allAITokens.getChildren().remove(GameController.gameBoard[currentX+1][currentY-1].getTokenOnSquare());
                                                GameController.gameBoard[currentX+1][currentY-1].removeToken();
                                                GameController.numberOfAITokens -= 1;
                                                ForceCapture();
                                                GameController.pieceTakenThisMove = true;
                                            }
                                            vM = true;
                                        }
                                    }
                                }
                                if(X-currentX == -2){
                                    if(GameController.gameBoard[currentX-1][currentY-1].token != null){
                                        if(GameController.gameBoard[currentX-1][currentY-1].getTokenOnSquare().tokenType == 1){
                                            if(validationPass == 0){
                                                GameController.gameBoard[currentX][currentY].token.tokenType = 1;
                                                GameController.gameBoard[currentX][currentY].token.player = 1;
                                            }
                                        }
                                        if(GameController.gameBoard[currentX-1][currentY-1].getTokenOnSquare().player == 2){
                                            if(validationPass == 0){
                                                GameController.allAITokens.getChildren().remove(GameController.gameBoard[currentX-1][currentY-1].getTokenOnSquare());
                                                GameController.gameBoard[currentX-1][currentY-1].removeToken();
                                                GameController.numberOfAITokens -= 1;
                                                ForceCapture();
                                                GameController.pieceTakenThisMove = true;
                                            }
                                            vM = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if(GameController.turn == 1){
                    if(Y-currentY == (2)){
                        if(tokenType == 0){
                            if(player == 2){
                                if(X-currentX == 2){
                                    if(GameController.gameBoard[currentX+1][currentY+1].token != null){
                                        if(GameController.gameBoard[currentX+1][currentY+1].getTokenOnSquare().tokenType == 1){
                                            if(validationPass == 0){
                                                GameController.gameBoard[currentX][currentY].token.tokenType = 1;
                                                GameController.gameBoard[currentX][currentY].token.player = 2;
                                                GameController.turn = 0;
                                            }
                                        }
                                        if(GameController.gameBoard[currentX+1][currentY+1].getTokenOnSquare().player == 1){
                                            if(validationPass == 0){
                                                GameController.allPlayerTokens.getChildren().remove(GameController.gameBoard[currentX+1][currentY+1].getTokenOnSquare());
                                                GameController.gameBoard[currentX+1][currentY+1].removeToken();
                                                GameController.numberOfPlayerTokens -= 1;
                                                ForceCapture();
                                                GameController.pieceTakenThisMove = true;
                                            }
                                            vM = true;
                                        }
                                    }
                                }
                                if(X-currentX == -2){
                                    if(GameController.gameBoard[currentX-1][currentY+1].token != null){
                                        if(GameController.gameBoard[currentX-1][currentY+1].getTokenOnSquare().tokenType == 1){
                                            if(validationPass == 0){
                                                GameController.gameBoard[currentX][currentY].token.tokenType = 1;
                                                GameController.gameBoard[currentX][currentY].token.player = 2;
                                            }
                                        }
                                        if(GameController.gameBoard[currentX-1][currentY+1].getTokenOnSquare().player == 1){
                                            if(validationPass == 0){
                                                GameController.allPlayerTokens.getChildren().remove(GameController.gameBoard[currentX-1][currentY+1].getTokenOnSquare());
                                                GameController.gameBoard[currentX-1][currentY+1].removeToken();
                                                GameController.numberOfPlayerTokens -= 1;
                                                ForceCapture();
                                                GameController.pieceTakenThisMove = true;
                                            }
                                            vM = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // KING
                //This section allows king to move to empty square.
                //This section creates Player Kings.
                if(player == 1){
                    if(Y == 0){
                        if(validationPass == 0){
                            this.tokenType = 1;
                            GameController.gameBoard[currentX][currentY].removeToken();
                            GameController.gameBoard[currentX][currentY].placeTokenOnSquare(this);
                            GameController.allPlayerTokens.getChildren().remove(GameController.gameBoard[currentX][currentY].getTokenOnSquare());
                            GameController.allPlayerTokens.getChildren().add(GameController.gameBoard[currentX][currentY].placeTokenOnSquare(this));
                            vM = true;
                        }
                    }
                }
                    //player 1 turn
                if(GameController.turn == 0) {
                    //king can move to empty
                    if(canMove == true){
                        if(GameController.forceCap == 0){
                            if(GameController.pieceTakenThisMove == false){
                                if((Y-currentY == (-1)) || (Y-currentY == (1))){
                                    if(tokenType == 1){
                                        if(player == 1){
                                            vM = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if(GameController.turn == 0){
                    //player king taking tokens in any direction.
                    if(Y-currentY == (+2)){
                        if(tokenType == 1){
                            if(player == 1){
                                if(X-currentX == 2){
                                    if(GameController.gameBoard[currentX+1][currentY+1].token != null){
                                        if(GameController.gameBoard[currentX+1][currentY+1].getTokenOnSquare().player == 2){
                                            if(validationPass == 0){
                                                GameController.allAITokens.getChildren().remove(GameController.gameBoard[currentX+1][currentY+1].getTokenOnSquare());
                                                GameController.gameBoard[currentX+1][currentY+1].removeToken();
                                                GameController.numberOfAITokens -= 1;
                                                ForceCapture();
                                                GameController.pieceTakenThisMove = true;
                                            }
                                            vM = true;
                                        }
                                    }
                                }
                                if(X-currentX == -2){
                                    if(GameController.gameBoard[currentX-1][currentY+1].token != null){
                                        if(GameController.gameBoard[currentX-1][currentY+1].getTokenOnSquare().player == 2){
                                            if(validationPass == 0){
                                                GameController.allAITokens.getChildren().remove(GameController.gameBoard[currentX-1][currentY+1].getTokenOnSquare());
                                                GameController.gameBoard[currentX-1][currentY+1].removeToken();
                                                GameController.numberOfAITokens -= 1;
                                                ForceCapture();
                                                GameController.pieceTakenThisMove = true;
                                            }
                                            vM = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(Y-currentY == (-2)){
                        if(tokenType == 1){
                            if(player == 1){
                                if(X-currentX == 2){
                                    if(GameController.gameBoard[currentX+1][currentY-1].token != null){
                                        if(GameController.gameBoard[currentX+1][currentY-1].getTokenOnSquare().player == 2){
                                            if(validationPass == 0){
                                                GameController.allAITokens.getChildren().remove(GameController.gameBoard[currentX+1][currentY-1].getTokenOnSquare());
                                                GameController.gameBoard[currentX+1][currentY-1].removeToken();
                                                GameController.numberOfAITokens -= 1;
                                                ForceCapture();
                                                GameController.pieceTakenThisMove = true;
                                            }
                                            vM = true;
                                        }
                                    }
                                }
                                if(X-currentX == -2){
                                    if(GameController.gameBoard[currentX-1][currentY-1].token != null){
                                        if(GameController.gameBoard[currentX-1][currentY-1].getTokenOnSquare().player == 2){
                                            if(validationPass == 0){
                                                GameController.allAITokens.getChildren().remove(GameController.gameBoard[currentX-1][currentY-1].getTokenOnSquare());
                                                GameController.gameBoard[currentX-1][currentY-1].removeToken();
                                                GameController.numberOfAITokens -= 1;
                                                //GameController.pieceTaken = true;
                                                ForceCapture();
                                                GameController.pieceTakenThisMove = true;
                                            }
                                            vM = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //This section creates AI Kings.
                if(player == 2){
                    if(Y == 7){
                        if(validationPass == 0){
                            this.tokenType = 1;
                            GameController.gameBoard[currentX][currentY].removeToken();
                            GameController.gameBoard[currentX][currentY].placeTokenOnSquare(this);
                            GameController.allAITokens.getChildren().remove(GameController.gameBoard[currentX][currentY].getTokenOnSquare());
                            GameController.allAITokens.getChildren().add(GameController.gameBoard[currentX][currentY].placeTokenOnSquare(this));
                            vM = true;
                            
                        }
                    }
                }
                if(GameController.turn == 1) {
                    if(canMove == true){
                        if(GameController.forceCap == 0){
                            if(GameController.pieceTakenThisMove == false){
                                if((Y-currentY == (-1)) || (Y-currentY == (1))){
                                    if(tokenType == 1){
                                        if(player == 2){
                                            if((X-currentX == 1)||(X-currentX == -1)){
                                                vM = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //AI king taking tokens in any direction.
                if(GameController.turn == 1) {
                    if(Y-currentY == (2)){
                        if(tokenType == 1){
                            if(player == 2){
                                if(X-currentX == 2){
                                    if(GameController.gameBoard[currentX+1][currentY+1].token != null){
                                    if(GameController.gameBoard[currentX+1][currentY+1].getTokenOnSquare().player == 1){
                                        if(validationPass == 0){
                                            GameController.allPlayerTokens.getChildren().remove(GameController.gameBoard[currentX+1][currentY+1].getTokenOnSquare());
                                            GameController.gameBoard[currentX+1][currentY+1].removeToken();
                                            GameController.numberOfPlayerTokens -= 1;
                                            //GameController.pieceTaken = true;
                                            GameController.pieceTakenThisMove = true;
                                            ForceCapture();
                                        }
                                        vM = true;
                                    }}
                                }
                                if(X-currentX == -2){
                                    if(GameController.gameBoard[currentX-1][currentY+1].token != null){
                                    if(GameController.gameBoard[currentX-1][currentY+1].getTokenOnSquare().player == 1){
                                        if(validationPass == 0){
                                            GameController.allPlayerTokens.getChildren().remove(GameController.gameBoard[currentX-1][currentY+1].getTokenOnSquare());
                                            GameController.gameBoard[currentX-1][currentY+1].removeToken();
                                            GameController.numberOfPlayerTokens -= 1;
                                            GameController.pieceTakenThisMove = true;
                                            ForceCapture();
                                        }
                                        vM = true;
                                    }}
                                }
                            }
                        }
                    }
                    if(Y-currentY == (-2)){
                        if(tokenType == 1){
                            if(player == 2){
                                if(X-currentX == 2){
                                    if(GameController.gameBoard[currentX+1][currentY-1].token != null){
                                    if(GameController.gameBoard[currentX+1][currentY-1].getTokenOnSquare().player == 1){
                                        if(validationPass == 0){
                                            GameController.allPlayerTokens.getChildren().remove(GameController.gameBoard[currentX+1][currentY-1].getTokenOnSquare());
                                            GameController.gameBoard[currentX+1][currentY-1].removeToken();
                                            GameController.numberOfPlayerTokens -= 1;
                                            ForceCapture();
                                            GameController.pieceTakenThisMove = true;
                                        }
                                        vM = true;
                                    }}
                                }
                                if(X-currentX == -2){
                                    
                                    if(GameController.gameBoard[currentX-1][currentY-1].token != null){
                                        if(GameController.gameBoard[currentX-1][currentY-1].getTokenOnSquare().player == 1){
                                            if(validationPass == 0){
                                                GameController.allPlayerTokens.getChildren().remove(GameController.gameBoard[currentX-1][currentY-1].getTokenOnSquare());
                                                GameController.gameBoard[currentX-1][currentY-1].removeToken();
                                                GameController.numberOfPlayerTokens -= 1;
                                                ForceCapture();
                                                GameController.pieceTakenThisMove = true;
                                            }
                                            vM = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return vM;
    }
            
       
    public void turnMessages(int newX, int newY){
                /* This section will return error messages for any invalid user move. */
        //Displays Who's turn
        if(GameController.turn == 0){
            GameController.static_turnMessage.setText("It is your turn");
        }
        if(GameController.turn == 1){
            GameController.static_turnMessage.setText("AI is thinking.");
        }
        
        if(GameController.displayMSG == true){
        //Error message if player makes a move and it's not your turn.
            if(GameController.turn == 0 && lastPlayerMoved == 1 && this.tokenType == 0){
                GameController.static_specificMessage.setText("Invalid Move.");
                GameController.displayMSG = false;
            }
        }
            //Error message if player makes a move and it's not your turn.
        if(GameController.turn == 0 && lastPlayerMoved == 1 && this.tokenType == 0){
            GameController.static_specificMessage.setText("Invalid Move. Valid moves are highlighted when hovering cursor\n over a token. This piece moves in one direction by one square per\nmove unless capturing(see rules). ");
            GameController.displayMSG = false;
        }
        if((newX + newY) % 2 == 1){
            GameController.static_specificMessage.setText("Token can only be moved to dark(highlighted) squares. Valid moves\n are highlighted when hovering cursor over a token.");
        }
        if((newX > 7) || (newX < 0)){
            GameController.static_specificMessage.setText("Token must be placed on board.");
        }
        if((newY > 7) || (newY < 0)){
            GameController.static_specificMessage.setText("Token must be placed on board.");
        }
        if(this.player == 1 && GameController.turn != 0 && (this.currentX != newX || this.currentY != newY)){
            GameController.static_specificMessage.setText("It is not your turn.");
        }
       // if(GameController.gameBoard[newX][newY].getTokenOnSquare() != null){
        //    GameController.static_specificMessage.setText("You cannot place pieces on top of other pieces. Valid Moves Are\n Highlighted when hovering cursor over token.");
        //}

        if(GameController.forceCap == 1){
            GameController.static_specificMessage.setText("Force Capture. You must take oppositions token.");
        }
    }

    public static int convertCursorToBoardCoords(double loc){
        int boardCoord = (int) ((loc + MainMenuController.sizeOfSquare) / MainMenuController.sizeOfSquare);//(loc / MainMenuController.sizeOfSquare);
        return boardCoord;
    }

    public void hoveringOverTokenPlayer() {
        if (this.player == 1) {
            if (this.tokenType == 0) {
                this.setFill(Color.DARKORCHID); //REGULAR PIECE
            }
            if (this.tokenType == 1) {
                this.setFill(Color.FIREBRICK); //KING
            }
        }
        if (this.player == 2) {
            if (this.tokenType == 0) {
                this.setFill(Color.GREEN); //REGULAR PIECE
            }
            if (this.tokenType == 1) {
                this.setFill(Color.LAWNGREEN); //KING
            }
        }
        ForceCapture();       
        getAllValidMoves();
    }

    public void ForceCapture(){
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(GameController.gameBoard[i][j].token != null){
                    GameController.gameBoard[i][j].token.canMove = GameController.gameBoard[i][j].token.canForceCaptureMoves(GameController.gameBoard[i][j].token.currentX, GameController.gameBoard[i][j].token.currentY); 
                    if(GameController.gameBoard[i][j].token.canMove == true){
                        GameController.forceCap = 1;
                        
                    }
                }
            }
        }
        if(GameController.forceCap == 0){
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    if(GameController.gameBoard[i][j].token != null){
                        GameController.gameBoard[i][j].token.canMove = true;
                    }
                }
            }
        }
    }

    public void exitedHoveringOverTokenPlayer() {
        if (this.player == 1) {
            if (this.tokenType == 0) {
                this.setFill(Color.DARKRED); //REGULAR PIECE
            }
            if (this.tokenType == 1) {
                this.setFill(Color.RED); //KING
            }
        }
        if (this.player == 2) {
            if (this.tokenType == 0) {
                this.setFill(Color.GREEN); //REGULAR PIECE
            }
            if (this.tokenType == 1) {
                this.setFill(Color.LAWNGREEN); //KING
            }             
        }
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if((i + j) % 2 == 0) {
                    GameController.gameBoard[i][j].setFill(Color.BLUE);
                }
            } 
        }
    }
    
    public void getAllValidMoves(){
        int newX1 = currentX + 1;
        if(newX1 > 7){
            newX1 = 7;
        }
        int newX2 = currentX + 2;
        if(newX2 > 7){
            newX2 = 7;
        }
        int newX3 = currentX -1;
        if(newX3 < 0){
            newX3 = 0;
        }
        int newX4 = currentX -2;
        if(newX4 < 0){
            newX4 = 0;
        }
        int newY1 = currentY + 1;
        if(newY1 > 7){
            newY1 = 7;
        }
        int newY2 = currentY + 2;
        if(newY2 > 7){
            newY2 = 7;
        }
        int newY3 = currentY - 1;
        if(newY3 < 0){
            newY3 = 0;
        }
        int newY4 = currentY - 2;
        if(newY4 < 0){
            newY4 = 0;
        }
        int validationPass = 1;
        
        
        if(this.validMove(newX1, newY1, currentX, currentY, tokenType, player, validationPass)==true){
            GameController.gameBoard[newX1][newY1].setFill(Color.BLANCHEDALMOND);
        }
        if(this.validMove(newX2, newY2, currentX, currentY, tokenType, player, validationPass)==true){
            GameController.gameBoard[newX2][newY2].setFill(Color.BLANCHEDALMOND);
        }
        if(this.validMove(newX3, newY3, currentX, currentY, tokenType, player, validationPass)==true){
            GameController.gameBoard[newX3][newY3].setFill(Color.BLANCHEDALMOND);
        }
        if(this.validMove(newX4, newY4, currentX, currentY, tokenType, player, validationPass)==true){
            GameController.gameBoard[newX4][newY4].setFill(Color.BLANCHEDALMOND);
        }
        if(this.validMove(newX1, newY3, currentX, currentY, tokenType, player, validationPass)==true){
            GameController.gameBoard[newX1][newY3].setFill(Color.BLANCHEDALMOND);
        }
        if(this.validMove(newX2, newY4, currentX, currentY, tokenType, player, validationPass)==true){
            GameController.gameBoard[newX2][newY4].setFill(Color.BLANCHEDALMOND);
        }
        if(this.validMove(newX3, newY1, currentX, currentY, tokenType, player, validationPass)==true){
            GameController.gameBoard[newX3][newY1].setFill(Color.BLANCHEDALMOND);
        }
        if(this.validMove(newX4, newY2, currentX, currentY, tokenType, player, validationPass)==true){
            GameController.gameBoard[newX4][newY2].setFill(Color.BLANCHEDALMOND);
        }
    }
    
    
    public boolean canForceCaptureMoves(int currentX, int currentY){
        boolean canForceCap = false;
        int newX2 = currentX + 2;
        if(newX2 > 7){
            newX2 = 7;
        }
        int newX4 = currentX -2;
        if(newX4 < 0){
            newX4 = 0;
        }
        int newY2 = currentY + 2;
        if(newY2 > 7){
            newY2 = 7;
        }
        int newY4 = currentY - 2;
        if(newY4 < 0){
            newY4 = 0;
        }
        int validationPass = 1;
        
        if(this.validMove(newX2, newY2, currentX, currentY, tokenType, player, validationPass)){
            canForceCap = true;
        }
        if(this.validMove(newX4, newY4, currentX, currentY, tokenType, player, validationPass)){
            canForceCap = true;
        }
        if(this.validMove(newX2, newY4, currentX, currentY, tokenType, player, validationPass)){
            canForceCap = true;
        }
        if(this.validMove(newX4, newY2, currentX, currentY, tokenType, player, validationPass)){
            canForceCap = true;
        }
        
        return canForceCap;
    }

}