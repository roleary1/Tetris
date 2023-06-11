package assignment;

import java.awt.*;
import java.util.Arrays;

/**
 * Represents a Tetris board -- essentially a 2-d grid of piece types (or nulls). Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {
    private Piece currentPiece = null;
    private int height, width;
    private Piece[][] pieceBoard;
    private int xPos, yPos, rightOffSet, leftOffSet, rowsCleared;
    private int maxHeight = 0;
    private Result lastResult;
    private Piece[][] testBoardGrid;
    private Action lastAction;
    private int[] columnHeights;
    private int[] rowWidths;

    // JTetris will use this constructor
    public TetrisBoard(int width, int height) { //initialize grids and arrays
        this.width = width;
        this.height = height;
        pieceBoard = new Piece[height][width];
        columnHeights = new int[width];
        rowWidths = new int[height];
    }

    @Override
    public Result move(Action act) {
        lastAction = act;
        Point[] body = currentPiece.getBody();
        int[] skirt = currentPiece.getSkirt();
        for (int i = 0; i < skirt.length; i++) { //set the left offset
            if (skirt[i] != Integer.MAX_VALUE) {
                leftOffSet = i;
                break;
            }
        }
        for (int i = skirt.length - 1; i >= 0; i--) { //set the right offset
            if (skirt[i] != Integer.MAX_VALUE) {
                rightOffSet = i;
                break;
            }
        }
      switch (act) {
            case DOWN:
                int minYPos = Integer.MAX_VALUE;
                for (int element : skirt) {  //get the lowest value
                    if (element < minYPos) {
                        minYPos = element;
                    }
                }
                if (yPos + minYPos > 0) { //if its above the bottom
                    for (int i = 0; i < skirt.length; i++) {
                        if (skirt[i] != Integer.MAX_VALUE && pieceBoard[yPos + skirt[i] - 1][xPos + i] != null) { //if it should be placed
                            return place(body);
                        }
                    }
                    lastResult = Result.SUCCESS;
                    yPos--; //move piece down
                    return Result.SUCCESS;
                } else {
                    return place(body); //place
                }

            case DROP:
                while (true) { //repeat down till the piece is placed
                    minYPos = Integer.MAX_VALUE;
                    for (int item : skirt) {
                        if (item < minYPos) {
                            minYPos = item;
                        }
                    }
                    if (yPos + minYPos > 0) {
                        for (int i = 0; i < skirt.length; i++) {
                            if (skirt[i] != Integer.MAX_VALUE && pieceBoard[yPos + skirt[i] - 1][xPos + i] != null) {
                                return place(body);
                            }
                        }
                        lastResult = Result.SUCCESS;
                        yPos--;
                    } else {
                        return place(body);
                    }
                }
            case HOLD:
                break;
            case LEFT:
                lastResult = Result.SUCCESS;
                for (Point item : body) {
                    if (xPos > 0 && pieceBoard[yPos + item.y][item.x + xPos - 1] != null) { //if left of the wall or there is piece on the left
                        lastResult = Result.OUT_BOUNDS;
                        return Result.OUT_BOUNDS;
                    }
                }
                if (xPos + leftOffSet > 0) { //if there is space to the left

                    xPos--;
                    return Result.SUCCESS;
                } else {
                    lastResult = Result.OUT_BOUNDS;
                    return Result.OUT_BOUNDS;
                }
            case RIGHT:
                lastResult = Result.SUCCESS;
                for (Point point : body) {
                    if (point.x + xPos + 1 < pieceBoard[0].length && pieceBoard[yPos + point.y][point.x + xPos + 1] != null) { //if right of the wall or there is piece on right
                        lastResult = Result.OUT_BOUNDS;
                        return Result.OUT_BOUNDS;
                    }
                }
                if (xPos + rightOffSet + 1 < width) { //if there is space on right
                    xPos++;
                    return Result.SUCCESS;
                }  else { //out of bounds
                    lastResult = Result.OUT_BOUNDS;
                    return Result.OUT_BOUNDS;
                }
            case NOTHING:
                break;
            case CLOCKWISE:
                if (shouldPlace()){ //place
                    return place(body);
                }
                TetrisPiece tempPieceCL = new TetrisPiece(currentPiece.getType(), currentPiece.getRotationIndex(), currentPiece.getBody()); //Piece rotated clockwise
                tempPieceCL = (TetrisPiece) tempPieceCL.clockwisePiece(); //rotate the piece clockwise
                if (canRotate(tempPieceCL.getBody())) { //if the rotated piece is in bounds
                    currentPiece = tempPieceCL;
                    if (shouldPlace(tempPieceCL.getBody())){ //if the piece should be placed
                        return place(tempPieceCL.getBody());
                    }
                    lastResult = Result.SUCCESS;
                    return Result.SUCCESS;
                } else {
                    Point[][] clockwiseWallKicks;  //stores wallkicks
                    if (tempPieceCL.getType() == Piece.PieceType.STICK){
                        clockwiseWallKicks = tempPieceCL.I_CLOCKWISE_WALL_KICKS;
                    }
                    else {
                        clockwiseWallKicks = tempPieceCL.NORMAL_CLOCKWISE_WALL_KICKS;
                    }
                    for (int i = 0; i < clockwiseWallKicks[currentPiece.getRotationIndex()].length; i++) {
                        if (testWallKick(tempPieceCL, clockwiseWallKicks[currentPiece.getRotationIndex()][i])) { //if wallkick is in bounds
                            int tempRot = currentPiece.getRotationIndex();
                            currentPiece = tempPieceCL;
                            wallKick(clockwiseWallKicks[tempRot][i]); //wallkick
                            if (shouldPlace(tempPieceCL.getBody())) {
                                return place(tempPieceCL.getBody()); //place
                            }
                            lastResult = Result.SUCCESS;
                            return Result.SUCCESS;
                        }
                    }
                    lastResult = Result.OUT_BOUNDS;
                    return Result.OUT_BOUNDS;
                }
            case COUNTERCLOCKWISE: //same as above except counterclockwise
                if (shouldPlace()){
                    return place(body);
                }
                TetrisPiece tempPieceCCL = new TetrisPiece(currentPiece.getType(), currentPiece.getRotationIndex(), currentPiece.getBody());
                tempPieceCCL = (TetrisPiece) tempPieceCCL.counterclockwisePiece();
                if (canRotate(tempPieceCCL.getBody())) {
                    currentPiece = tempPieceCCL;
                    if (shouldPlace(tempPieceCCL.getBody())){
                        return place(tempPieceCCL.getBody());
                    }
                    lastResult = Result.SUCCESS;
                    return Result.SUCCESS;
                } else {
                    Point[][] counterclockwiseWallKicks;
                    if (tempPieceCCL.getType() == Piece.PieceType.STICK){
                        counterclockwiseWallKicks = tempPieceCCL.I_COUNTERCLOCKWISE_WALL_KICKS;
                    }
                    else {
                        counterclockwiseWallKicks = tempPieceCCL.NORMAL_COUNTERCLOCKWISE_WALL_KICKS;
                    }
                    for (int i = 0; i < counterclockwiseWallKicks[currentPiece.getRotationIndex()].length; i++) {
                        if (testWallKick(tempPieceCCL, counterclockwiseWallKicks[currentPiece.getRotationIndex()][i])) {
                            int tempRot = currentPiece.getRotationIndex();
                            currentPiece = tempPieceCCL;
                            wallKick(counterclockwiseWallKicks[tempRot][i]);
                            if (shouldPlace(tempPieceCCL.getBody())) {
                                return place(tempPieceCCL.getBody());
                            }
                            lastResult = Result.SUCCESS;
                            return Result.SUCCESS;
                        }
                    }
                    lastResult = Result.OUT_BOUNDS;
                    return Result.OUT_BOUNDS;
                }
            default:
                break;
        }
        return Result.NO_PIECE;
    }

    @Override
    public Board testMove(Action act) {
        TetrisBoard testBoard = new TetrisBoard(this.width, this.height);
        for (int i = 0; i < pieceBoard.length; i++) { //deepcopys the board
            for (int j = 0; j < pieceBoard[0].length; j++) {
                Piece p = pieceBoard[i][j];
                if (p!=null) {
                    Piece pCopy = new TetrisPiece(p.getType(), p.getRotationIndex(), p.getBody());
                    testBoard.pieceBoard[i][j] = pCopy;
                }
                else {
                    testBoard.pieceBoard[i][j] = null;
                }
            }
        }
        testBoard.nextPiece(currentPiece,new Point(xPos,yPos)); //gets next piece
        testBoard.move(act); //moves
        return testBoard;
    }


    Piece[][] getPieceBoard(){
        return pieceBoard;
    }

    @Override
    public Piece getCurrentPiece() {
        return currentPiece;
    }

    @Override
    public Point getCurrentPiecePosition() {
        return new Point(xPos, yPos);
    }

    @Override
    public void nextPiece(Piece p, Point spawnPosition) { //updates piece position
        int xPos = (int) spawnPosition.getX();
        int yPos = (int) spawnPosition.getY();
        this.xPos = xPos;
        this.yPos = yPos;
        currentPiece = p;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof TetrisBoard) {
            TetrisBoard otherBoard = (TetrisBoard) other;
            if (otherBoard.getHeight() == getHeight() && otherBoard.getWidth() == getWidth()) {
                for (int i = 0; i < pieceBoard.length; i++) {
                    for (int j = 0; j < pieceBoard[i].length; j++) {
                        if (pieceBoard[i][j] != null && pieceBoard[i][j].getType() != otherBoard.getGrid(j, i)) {
                            return false;
                        } else if (pieceBoard[i][j] == null) {
                            return otherBoard.getGrid(j, i) == null;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public Result getLastResult() {
        return lastResult;
    }

    @Override
    public Action getLastAction() {
        return lastAction;
    }

    @Override
    public int getRowsCleared() {
        return rowsCleared;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public int dropHeight(Piece piece, int x) { //finds what height the object will drop to
        int dropHeight = Integer.MAX_VALUE;
        int dropColumn = x;
        for(int i = 0; i < piece.getSkirt().length; i++) {
            if(getColumnHeight(x + i) + piece.getSkirt()[i] < dropHeight) {
                dropHeight = getColumnHeight(x + i) + piece.getSkirt()[i];
                dropColumn = x+i;
            }
        }
        return getColumnHeight(dropColumn) + 1;
    }

    @Override
    public int getColumnHeight(int x) {
        return columnHeights[x];
    }

    @Override
    public int getRowWidth(int y) {
        return rowWidths[y];
    }

    @Override
    public Piece.PieceType getGrid(int x, int y) { //return element in the grid area
        try {
            return pieceBoard[y][x].getType();
        } catch (Exception e) {
            return null;
        }
    }

    //HELPER METHODS
    private Result place(Point[] body){
        for (Point point : body) { //updates the grid to include current piece
            pieceBoard[yPos + point.y][xPos + point.x] = currentPiece;
        }

        int maxPointHeight = Integer.MIN_VALUE;
        for(Point p: currentPiece.getBody()){
            if (p.y>maxPointHeight){
                maxPointHeight = p.y;
            }
            if (p.y+yPos>=columnHeights[p.x+xPos]){
                columnHeights[p.x+xPos] = p.y+yPos+1; //updates column height
            }
            rowWidths[p.y+yPos]++; //updates rowwidth
        }
        maxPointHeight+= yPos + 1;
        if (maxPointHeight>maxHeight){
            maxHeight = maxPointHeight; //updates max height
        }

        lastResult = Result.PLACE;
        removeRows(); //removes the rows
        return Result.PLACE; //returns place
    }

    private void removeRows() { //removes rows that are full
        boolean rowFull = true;
        int deletedRowIndex = -1, numDeletedRows = 0;
        Piece[][] pieceBoardCopy = new Piece[getHeight()][getWidth()];
        for (int i = pieceBoard.length - 1; i >= 0; i--) {
            for (int j = 0; j < pieceBoard[i].length; j++) {
                pieceBoardCopy[i][j] = pieceBoard[i][j];
                if (pieceBoard[i][j] == null)
                    rowFull = false;
            }
            if (rowFull) {
                numDeletedRows++;
                deletedRowIndex = i;
            }
            rowFull = true;
        }
        rowsCleared = numDeletedRows;
        while (deletedRowIndex != -1 && numDeletedRows > 0) {
            for (int i = deletedRowIndex; i < pieceBoard.length - 1; i++) {
                System.arraycopy(pieceBoardCopy[i + 1], 0, pieceBoard[i], 0, pieceBoard[i].length);
            }
            Arrays.fill(pieceBoard[pieceBoard.length - 1], null);
            for (int i = pieceBoard.length - 1; i >= 0; i--) {
                System.arraycopy(pieceBoard[i], 0, pieceBoardCopy[i], 0, pieceBoard[i].length);
            }
            numDeletedRows--;
        }
    }

    private void wallKick(Point wallKick) { //increments ypos and xpos by the wallkick
        Point[] body = currentPiece.getBody();
        yPos += wallKick.y;
        xPos += wallKick.x;
    }

    private boolean testWallKick(Piece temp, Point wallKick) { //checks if the wall kick is in bounds by editing th ebody
        Point[] body = temp.getBody();
        Point[] tempBody = new Point[body.length];
        for (int i = 0; i < body.length; i++) {
            Point x = new Point(body[i].x, body[i].y);
            tempBody[i] = x;
        }
        for (Point point : tempBody) {
            point.setLocation(point.x + wallKick.x, point.y + wallKick.y);
        }
        return canRotate(tempBody);
    }

    private boolean shouldPlace(){ //checks if there a piece or ground below current piece
        Point[] body = currentPiece.getBody();
        for(int i = 0; i < body.length; i++){
            if(yPos+body[i].y-1 < 0 || pieceBoard[yPos+body[i].y-1][xPos+body[i].x] != null){
                return true;
            }
        }
        return false;
    }
    private boolean shouldPlace(Point[] body){ //same as shouldplace but uses custom body
        for(int i = 0; i < body.length; i++){
            if(yPos+body[i].y-1 < 0 || pieceBoard[yPos+body[i].y-1][xPos+body[i].x] != null){
                return true;
            }
        }
        return false;
    }


    private boolean canRotate(Point[] body) { //checks if the points in the body are in bounds and not over a piece
        for (Point point : body) {
            if (point.x + xPos < 0 || point.x + xPos >= pieceBoard[0].length || point.y + yPos < 0 || point.y + yPos >= pieceBoard.length || pieceBoard[point.y + yPos][point.x + xPos] != null) {
                return false;
            }
        }
        return true;
    }
}
