
package assignment1;

public class Position {
    int X, Y, player;
    int currentX, currentY;
    
    // 0 = piece not taken. 1 = UPLEFTCAPTURE. 2 = UPRIGHTCAPTURE. 3 = DOWNLEFTCAPTURE 4 = DOWNRIGHTCAPTURE
    // 5 =  UPLEFT. 6 = UPRIGHT. 7 = DOWNLEFT. 8 = DOWNRIGHT.
    
    public Position(int newX, int newY, int currentX, int currentY) {
        this.X = newX;
        this.Y = newY;
        this.currentX = currentX;
        this.currentY = currentY;

        //this.moveDirection = moveDirection;
    }
}
