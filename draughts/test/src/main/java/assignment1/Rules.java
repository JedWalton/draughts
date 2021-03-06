
package assignment1;

import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Jed
 */
public class Rules {
    
    
    public static void rules() throws IOException {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Draughts Rules");
        ScrollPane scroll = new ScrollPane();
        scroll.setPrefSize(500, 500);
        Label rulesNote = new Label("Basic Rules\n The object is to eliminate all opposing checkers or to create a situation in which it is impossible for your opponent to make any move. Normally, the victory will be due to complete elimination.Non-capturing MoveBlack moves first and play proceeds alternately. From their initial positions, checkers may only move forward. There are two types of moves that can be made, capturing moves and non-capturing moves. Non-capturing moves are simply a diagonal move forward from one square to an adjacent square. (Note that the white squares are never used.) Capturing moves occur when a player &quot;jumps&quot; an opposing piece. This is also done on the diagonal and can only happen when the square behind (on the same diagonal) is also open. This means that you may not jump an opposing piece around a corner.Capturing MoveOn a capturing move, a piece may make multiple jumps. If after a jump a player is in a position to make another jump then he may do so. This means that a player may make several jumps in succession, capturing several pieces on a single turn.Forced Captures: When a player is in a position to make a capturing move, he must make a capturing move. When he has more than one capturing move to choose from he may take whichever move suits him. ");
        rulesNote.setWrapText(true);
        rulesNote.setMaxWidth(400);
        Label CrowningKing = new Label("Crowning King in Checkers\n" +
"When a checker achieves the opponent's edge of the board (called the \"king's row\") it is crowned with another checker. This signifies that the checker has been made a king. The king now gains an added ability to move backward. The king may now also jump in either direction or even in both directions in one turn (if he makes multiple jumps).\n" +
"\n" +
"A similar idea in the game of chess occurs when a pawn reaches the opponent's end of the board it becomes a queen. There is a practical reason for these piece promotions. Without it, a piece which can only move in one direction becomes worthless. Interestingly enough it also has some social significance in that it signifies that royalty and power should not be simply endowed at birth. Nobility is something that can be and should be earned.\n" +
"\n" +
"If the player gets an uncrowned checker on the king's row because of a capturing move then he must stop to be crowned even if another capture seems to be available. He may then use his new king on his next move.");
        CrowningKing.setWrapText(true);
        CrowningKing.setMaxWidth(400);
        Label CheckerStrategy = new Label("Checker Strategy\n" +
"Checkers is a straight-forward game in many ways. Yet, play can unfold in intricate layers. Every move opens untold possibilities and closes down untold more. Thus, it is well to keep a few strategies in mind when playing, even when it is just for fun.\n" +
"\n" +
"First, always keep in mind the possibility of using the forced capture rule to maneuver your opponent into a position where he gives up two pieces for one of your own. Often a one piece advantage can make all the difference in the end game.\n" +
"\n" +
"Second, always try to keep the lanes to your own king's row blocked to your opponent. Once either side gets a king, any uncrowned checker in the open is highly vulnerable.\n" +
"\n" +
"Third, move between your own pieces and your opponent in order to move adjacent to an opposing checker without loss.\n" +
"\n" +
"Of course, these are elementary ideas to the tournament player. To move beyond the beginner stage, a player will want to acquire a book on checkers and checker strategy. An excellent place to begin is Fred Reinfeld's book, How to win at Checkers.");
        CheckerStrategy.setWrapText(true);
        CheckerStrategy.setMaxWidth(400);
        Button closeRules = new Button("Close Rules");
        closeRules.setOnAction(e -> popup.close());
        VBox box = new VBox(100);
        scroll.setContent(box);
        box.getChildren().addAll(rulesNote, CrowningKing, CheckerStrategy, closeRules);
        
        Scene rules = new Scene(scroll, 500, 500);
        
        popup.setScene(rules);
        popup.showAndWait();
    }
}
