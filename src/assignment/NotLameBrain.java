package assignment;

import java.util.*;

public class NotLameBrain implements Brain{
    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMoves;

    /**
     * Decide what the next move should be based on the state of the board.
     */
    public Board.Action nextMove(Board currentBoard) {
        // Fill the our options array with versions of the new Board
        options = new ArrayList<>();
        firstMoves = new ArrayList<>();
        enumerateOptions(currentBoard);

        //Do same for clockwise piece
        Board clockwise1 = currentBoard.testMove(Board.Action.CLOCKWISE);
        enumerateOptions(clockwise1);
        Board clockwise2 = clockwise1.testMove(Board.Action.CLOCKWISE);
        enumerateOptions(clockwise2);
        Board clockwise3 = clockwise2.testMove(Board.Action.CLOCKWISE);
        enumerateOptions(clockwise3);

        //Do same for counterclockwise piece
        Board counterclockwise1 = currentBoard.testMove(Board.Action.COUNTERCLOCKWISE);
        enumerateOptions(counterclockwise1);
        Board counterclockwise2 = counterclockwise1.testMove(Board.Action.COUNTERCLOCKWISE);
        enumerateOptions(counterclockwise2);
        Board counterclockwise3 = counterclockwise2.testMove(Board.Action.COUNTERCLOCKWISE);
        enumerateOptions(counterclockwise3);
        int best = 0;
        int bestIndex = 0;

        // Check all of the options and get the one with the highest score
        for (int i = 0; i < options.size(); i++) {
            int score = scoreBoard(options.get(i));
            if (score > best) {
                best = score;
                bestIndex = i;
            }
        }

        int rotationDifference = options.get(bestIndex).getCurrentPiece().getRotationIndex()-options.get(0).getCurrentPiece().getRotationIndex();
        // We want to return the first move on the way to the best Board
        if (rotationDifference==1){
            return Board.Action.CLOCKWISE;
        }
        else if (rotationDifference==-1 || rotationDifference == 3){
            return Board.Action.COUNTERCLOCKWISE;
        }
        else {
            return firstMoves.get(bestIndex);
        }
    }

    private void enumerateOptions(Board currentBoard) {
        options.add(currentBoard.testMove(Board.Action.DROP));
        firstMoves.add(Board.Action.DROP);

        Board left = currentBoard.testMove(Board.Action.LEFT);
        while (left.getLastResult() == Board.Result.SUCCESS) {
            options.add(left.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.LEFT);
            left.move(Board.Action.LEFT);
        }

        Board right = currentBoard.testMove(Board.Action.RIGHT);
        while (right.getLastResult() == Board.Result.SUCCESS) {
            options.add(right.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.RIGHT);
            right.move(Board.Action.RIGHT);
        }
    }

    int scoreBoard(Board newBoard) {
        return (100 - newBoard.getMaxHeight()*5) + getVerticalStick(newBoard) + (newBoard.getRowsCleared()*10) - (getNumHoles(newBoard)*5);
    }

    int getNumHoles(Board newBoard) {
        //point boost for placement that minimizes total # holes
        int numHoles = 0;
        for(int i = 0; i < newBoard.getHeight(); i++) {
            for(int j = 0; j < newBoard.getWidth(); j++) {
                if(newBoard.getGrid(j, i) == null && newBoard.getColumnHeight(j) > i)
                    numHoles++;
            }
        }
        return numHoles;
    }

    int getVerticalStick(Board newBoard) {
        //point boost for vertical sticks because more likely to clear rows
        int verticalStickBoost = 0;
        if (newBoard.getCurrentPiece().getType() == Piece.PieceType.STICK){
            if (newBoard.getMaxHeight()<newBoard.getHeight()-4) {
                if (newBoard.getCurrentPiece().getRotationIndex() == 1 || newBoard.getCurrentPiece().getRotationIndex() == 3){
                    verticalStickBoost = 20;
                }
            }
        }
        return verticalStickBoost;
    }
//    int getBlockades(Board newBoard) {
//        //point deduction for blocks placed above existing holes
//        int numBlockades = 0;
//        for(int i = 0; i < newBoard.getCurrentPiece().getSkirt().length; i++) {
//            for(int j = 0; j < newBoard.getCurrentPiecePosition().y; j++) {
//                if(newBoard.getCurrentPiecePosition().x+i >= 0
//                        && newBoard.getGrid(newBoard.getCurrentPiecePosition().x+i, j) == null
//                        && newBoard.getColumnHeight(newBoard.getCurrentPiecePosition().x+i) > j) {
//                    numBlockades++;
//                }
//            }
//        }
//        return numBlockades;
//    }
}
