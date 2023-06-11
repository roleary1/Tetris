package assignment;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BoardTester {
    //TetrisBoards to test basic movement
    private static TetrisBoard board1, board2, board3;
    private static ArrayList<Piece> tetrisPieces;

    @BeforeClass
    public static void setUp() {
        tetrisPieces = new ArrayList<>();
        tetrisPieces.add(new TetrisPiece(Piece.PieceType.SQUARE));
        tetrisPieces.add(new TetrisPiece(Piece.PieceType.STICK));
        tetrisPieces.add(new TetrisPiece(Piece.PieceType.LEFT_L));
        tetrisPieces.add(new TetrisPiece(Piece.PieceType.RIGHT_L));
        tetrisPieces.add(new TetrisPiece(Piece.PieceType.LEFT_DOG));
        tetrisPieces.add(new TetrisPiece(Piece.PieceType.RIGHT_DOG));
        tetrisPieces.add(new TetrisPiece(Piece.PieceType.T));

        board2 = new TetrisBoard(10, 24);
        board2.nextPiece(new TetrisPiece(Piece.PieceType.SQUARE), new Point(0, 20)); board2.move(Board.Action.DROP);
        board2.nextPiece(new TetrisPiece(Piece.PieceType.SQUARE), new Point(2, 20)); board2.move(Board.Action.DROP);
        board2.nextPiece(new TetrisPiece(Piece.PieceType.SQUARE), new Point(4, 20)); board2.move(Board.Action.DROP);
        board2.nextPiece(new TetrisPiece(Piece.PieceType.SQUARE), new Point(6, 20)); board2.move(Board.Action.DROP);
        board2.nextPiece(new TetrisPiece(Piece.PieceType.SQUARE), new Point(8, 20));
    }

    @Test
    public void TestMoveDownUnobstructed() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(10, 24);
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2, 20));

            Point expectedPosition = new Point(board1.getCurrentPiecePosition().x,board1.getCurrentPiecePosition().y-1);
            Board.Result result = board1.move(Board.Action.DOWN);
            Point actualPosition = board1.getCurrentPiecePosition();

            Assert.assertEquals(Board.Result.SUCCESS, result);
            Assert.assertEquals(expectedPosition, actualPosition);
        }
    }
    @Test
    public void TestMoveDownObstructed() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(10, 4);

            int lowestSkirt = Integer.MAX_VALUE;
            for(int j = 0; j < tetrisPieces.get(i).getSkirt().length; j++){
                if(tetrisPieces.get(i).getSkirt()[j] < lowestSkirt)
                    lowestSkirt = tetrisPieces.get(i).getSkirt()[j];
            }
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2, 0-lowestSkirt));

            Point expectedPosition = board1.getCurrentPiecePosition();
            Board.Result result = board1.move(Board.Action.DOWN);
            Point actualPosition = board1.getCurrentPiecePosition();

            Assert.assertEquals(Board.Result.PLACE, result);
            Assert.assertEquals(expectedPosition, actualPosition);
        }
    }
    @Test
    public void TestMoveLeftUnObstructed() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(10, 24);
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2, 20));

            Point expectedPosition = new Point(board1.getCurrentPiecePosition().x-1,board1.getCurrentPiecePosition().y);
            Board.Result result = board1.move(Board.Action.LEFT);
            Point actualPosition = board1.getCurrentPiecePosition();

            Assert.assertEquals(Board.Result.SUCCESS, result);
            Assert.assertEquals(expectedPosition, actualPosition);
        }
    }
    @Test
    public void TestMoveLeftObstructedWall() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(2, 24);
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2, 20));

            Point expectedPosition = board1.getCurrentPiecePosition();
            Board.Result result = board1.move(Board.Action.LEFT);
            Point actualPosition = board1.getCurrentPiecePosition();

            Assert.assertEquals(Board.Result.OUT_BOUNDS, result);
            Assert.assertEquals(expectedPosition, actualPosition);
        }
    }
    @Test
    public void TestMoveLeftObstructedPiece() {
        board1 = new TetrisBoard(10, 24);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.SQUARE), new Point(board1.getWidth()/2, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.SQUARE), new Point(board1.getWidth()/2 + 2, 0));

        Point expectedPosition = board1.getCurrentPiecePosition();
        Board.Result result = board1.move(Board.Action.LEFT);
        Point actualPosition = board1.getCurrentPiecePosition();

        Assert.assertEquals(Board.Result.OUT_BOUNDS, result);
        Assert.assertEquals(expectedPosition, actualPosition);
    }
    @Test
    public void TestMoveRightUnObstructed() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(10, 24);
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2, 20));

            Point expectedPosition = new Point(board1.getCurrentPiecePosition().x+1,board1.getCurrentPiecePosition().y);
            Board.Result result = board1.move(Board.Action.RIGHT);
            Point actualPosition = board1.getCurrentPiecePosition();

            Assert.assertEquals(Board.Result.SUCCESS, result);
            Assert.assertEquals(expectedPosition, actualPosition);
        }
    }
    @Test
    public void TestMoveRightObstructedWall() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(2, 24);
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2, 20));

            Point expectedPosition = board1.getCurrentPiecePosition();
            Board.Result result = board1.move(Board.Action.RIGHT);
            Point actualPosition = board1.getCurrentPiecePosition();

            Assert.assertEquals(Board.Result.OUT_BOUNDS, result);
            Assert.assertEquals(expectedPosition, actualPosition);
        }
    }
    @Test
    public void TestMoveRightObstructedPiece() {
        board1 = new TetrisBoard(10, 24);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.SQUARE), new Point(board1.getWidth()/2, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.SQUARE), new Point(board1.getWidth()/2 - 2, 0));

        Point expectedPosition = board1.getCurrentPiecePosition();
        Board.Result result = board1.move(Board.Action.RIGHT);
        Point actualPosition = board1.getCurrentPiecePosition();

        Assert.assertEquals(Board.Result.OUT_BOUNDS, result);
        Assert.assertEquals(expectedPosition, actualPosition);
    }
    @Test
    public void TestMoveClockwiseUnObstructed() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(10, 24);
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2, 20));

            Piece pieceCopy = new TetrisPiece(board1.getCurrentPiece().getType(), board1.getCurrentPiece().getRotationIndex(), board1.getCurrentPiece().getBody());

            Point[] expectedBody = pieceCopy.clockwisePiece().getBody();
            Board.Result result = board1.move(Board.Action.CLOCKWISE);
            Point[] actualBody = board1.getCurrentPiece().getBody();

            Assert.assertEquals(Board.Action.CLOCKWISE, board1.getLastAction());
            Assert.assertEquals(Board.Result.SUCCESS, result);
            Assert.assertEquals(expectedBody, actualBody);
        }
    }
    @Test
    public void TestMoveClockwiseWallKick() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(10, 24);
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth() - tetrisPieces.get(i).getWidth(), 20));
            board1.move(Board.Action.COUNTERCLOCKWISE);
            board1.move(Board.Action.RIGHT);

            Piece pieceCopy = new TetrisPiece(board1.getCurrentPiece().getType(), board1.getCurrentPiece().getRotationIndex(), board1.getCurrentPiece().getBody());

            Point[] expectedBody = pieceCopy.clockwisePiece().getBody();
            Board.Result result = board1.move(Board.Action.CLOCKWISE);
            Point[] actualBody = board1.getCurrentPiece().getBody();

            Assert.assertEquals(Board.Action.CLOCKWISE, board1.getLastAction());
            Assert.assertEquals(Board.Result.SUCCESS, result);
            Assert.assertEquals(expectedBody, actualBody);
        }
    }
    @Test
    public void TestMoveCounterClockwiseUnObstructed() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(10, 24);
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2, 20));

            Piece pieceCopy = new TetrisPiece(board1.getCurrentPiece().getType(), board1.getCurrentPiece().getRotationIndex(), board1.getCurrentPiece().getBody());

            Point[] expectedBody = pieceCopy.counterclockwisePiece().getBody();
            Board.Result result = board1.move(Board.Action.COUNTERCLOCKWISE);
            Point[] actualBody = board1.getCurrentPiece().getBody();

            Assert.assertEquals(Board.Action.COUNTERCLOCKWISE, board1.getLastAction());
            Assert.assertEquals(Board.Result.SUCCESS, result);
            Assert.assertEquals(expectedBody, actualBody);
        }
    }
    @Test
    public void TestMoveCounterClockwiseWallKick() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(10, 24);
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth() - tetrisPieces.get(i).getWidth(), 20));
            board1.move(Board.Action.COUNTERCLOCKWISE);
            board1.move(Board.Action.RIGHT);

            Piece pieceCopy = new TetrisPiece(board1.getCurrentPiece().getType(), board1.getCurrentPiece().getRotationIndex(), board1.getCurrentPiece().getBody());

            Point[] expectedBody = pieceCopy.counterclockwisePiece().getBody();
            Board.Result result = board1.move(Board.Action.COUNTERCLOCKWISE);
            Point[] actualBody = board1.getCurrentPiece().getBody();

            Assert.assertEquals(Board.Action.COUNTERCLOCKWISE, board1.getLastAction());
            Assert.assertEquals(Board.Result.SUCCESS, result);
            Assert.assertEquals(expectedBody, actualBody);
        }
    }
    @Test
    public void TestMoveDrop() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(10, 24);
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2, 20));

            int dropDistance = Integer.MIN_VALUE;
            for(int j = 0; j < tetrisPieces.get(i).getSkirt().length; j++){
                if(board1.getColumnHeight(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2 + j) - tetrisPieces.get(i).getSkirt()[j] > dropDistance)
                    dropDistance = board1.getColumnHeight(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2 + j) - tetrisPieces.get(i).getSkirt()[j];
            }
            Point expectedPosition = new Point(board1.getCurrentPiecePosition().x, dropDistance);
            Board.Result result = board1.move(Board.Action.DROP);
            Point actualPosition = board1.getCurrentPiecePosition();

            Assert.assertEquals(Board.Result.PLACE, result);
            Assert.assertEquals(expectedPosition, actualPosition);
        }
    }
    @Test
    public void TestMoveNothing() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(10, 24);
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2, 20));

            Point expectedPosition = board1.getCurrentPiecePosition();
            Board.Result result = board1.move(Board.Action.NOTHING);
            Point actualPosition = board1.getCurrentPiecePosition();

            Assert.assertEquals(Board.Result.NO_PIECE, result);
            Assert.assertEquals(expectedPosition, actualPosition);
        }
    }

    @Test
    public void TestGetCurrentPiece() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(10, 24);
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2, 20));
            Assert.assertEquals(tetrisPieces.get(i).getType(), board1.getCurrentPiece().getType());
            Assert.assertEquals(tetrisPieces.get(i).getRotationIndex(), board1.getCurrentPiece().getRotationIndex());
            Assert.assertEquals(tetrisPieces.get(i).getBody(), board1.getCurrentPiece().getBody());
        }
    }
    @Test
    public void TestGetNextPiece() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(10, 24);
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2, 20));
            Assert.assertEquals(tetrisPieces.get(i), board1.getCurrentPiece());
            Assert.assertEquals(new Point(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2, 20), board1.getCurrentPiecePosition());
        }
    }
    @Test
    public void TestEquals() {
        TetrisBoard identicalBoard;
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(10, 24);
            identicalBoard = new TetrisBoard(10, 24);

            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2, 20));
            board1.move(Board.Action.DROP);
            identicalBoard.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth()/2 - tetrisPieces.get(i).getWidth()/2, 20));
            board1.move(Board.Action.DROP);

            Assert.assertTrue(board1.equals(identicalBoard));
        }
    }
    @Test
    public void TestGetLastResult() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(10, 24);
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth() / 2 - tetrisPieces.get(i).getWidth() / 2, 20));

            board1.move(Board.Action.DOWN);
            Assert.assertEquals(Board.Result.SUCCESS, board1.getLastResult());
            board1.move(Board.Action.LEFT);
            Assert.assertEquals(Board.Result.SUCCESS, board1.getLastResult());
            board1.move(Board.Action.RIGHT);
            Assert.assertEquals(Board.Result.SUCCESS, board1.getLastResult());
            board1.move(Board.Action.DROP);
            Assert.assertEquals(Board.Result.PLACE, board1.getLastResult());
        }
    }
    @Test
    public void TestGetLastAction() {
        for(int i = 0; i < tetrisPieces.size(); i++) {
            board1 = new TetrisBoard(10, 24);
            board1.nextPiece(tetrisPieces.get(i), new Point(board1.getWidth() / 2 - tetrisPieces.get(i).getWidth() / 2, 20));

            board1.move(Board.Action.DOWN);
            Assert.assertEquals(Board.Action.DOWN, board1.getLastAction());
            board1.move(Board.Action.LEFT);
            Assert.assertEquals(Board.Action.LEFT, board1.getLastAction());
            board1.move(Board.Action.RIGHT);
            Assert.assertEquals(Board.Action.RIGHT, board1.getLastAction());
            board1.move(Board.Action.NOTHING);
            Assert.assertEquals(Board.Action.NOTHING, board1.getLastAction());
        }
    }
    @Test
    public void TestGetRowsCleared() {
        board2.move(Board.Action.DROP);
        Assert.assertEquals(2, board2.getRowsCleared());
    }
    @Test
    public void TestGetWidth() {
        board1 = new TetrisBoard(10, 24);
        Assert.assertEquals(10, board1.getWidth());
        board1 = new TetrisBoard(0, 24);
        Assert.assertEquals(0, board1.getWidth());
        board1 = new TetrisBoard(100, 24);
        Assert.assertEquals(100, board1.getWidth());
    }
    @Test
    public void TestGetHeight() {
        board1 = new TetrisBoard(10, 24);
        Assert.assertEquals(24, board1.getHeight());
        board1 = new TetrisBoard(10, 0);
        Assert.assertEquals(0, board1.getHeight());
        board1 = new TetrisBoard(10, 100);
        Assert.assertEquals(100, board1.getHeight());
    }
    @Test
    public void TestGetMaxHeight() {
        board1 = new TetrisBoard(10, 24);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.RIGHT_L), new Point(board1.getWidth() / 2, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.LEFT_L), new Point(board1.getWidth() / 2, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.RIGHT_L), new Point(board1.getWidth() / 2, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.LEFT_L), new Point(board1.getWidth() / 2, 20));
        board1.move(Board.Action.DROP);

        Assert.assertEquals(8, board1.getMaxHeight());
    }
    @Test
    public void TestGetDropHeight() {
        board1 = new TetrisBoard(10, 24);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.RIGHT_L), new Point(board1.getWidth() / 2, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.LEFT_L), new Point(board1.getWidth() / 2, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.RIGHT_L), new Point(board1.getWidth() / 2, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.LEFT_L), new Point(board1.getWidth() / 2, 20));

        Assert.assertEquals(6, board1.dropHeight(board1.getCurrentPiece(), board1.getCurrentPiecePosition().x));
    }
    @Test
    public void TestGetColumnHeight() {
        board1 = new TetrisBoard(10, 24);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.RIGHT_L), new Point(board1.getWidth() / 2, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.LEFT_L), new Point(board1.getWidth() / 2, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.RIGHT_L), new Point(board1.getWidth() / 2, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.LEFT_L), new Point(board1.getWidth() / 2, 20));
        Assert.assertEquals(5, board1.getColumnHeight(board1.getWidth() / 2));
    }
    @Test
    public void TestGetRowWidth() {
        board1 = new TetrisBoard(15, 24);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.T), new Point(0, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.T), new Point(3, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.T), new Point(6, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.T), new Point(9, 20));
        board1.move(Board.Action.DROP);

        Assert.assertEquals(12, board1.getRowWidth(0));
        Assert.assertEquals(4, board1.getRowWidth(1));
    }
    @Test
    public void TestGetGrid() {
        board1 = new TetrisBoard(10, 24);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.T), new Point(board1.getWidth() / 2, 20));
        //shouldn't take current piece into consideration
        Assert.assertEquals(null, board1.getGrid(board1.getCurrentPiecePosition().x, board1.getCurrentPiecePosition().y));
        //returns correct type of previous piece
        board1.move(Board.Action.DROP);
        board1.move(Board.Action.NOTHING);
        Assert.assertEquals(Piece.PieceType.T, board1.getGrid(board1.getWidth() / 2, 0));
    }
    @Test
    public void TestTestMove() {
        board1 = new TetrisBoard(10, 24);
        TetrisBoard boardCopy = new TetrisBoard(board1.getWidth(), board1.getHeight());
        board1.nextPiece(new TetrisPiece(Piece.PieceType.T), new Point(board1.getWidth() / 2, 20));
        boardCopy.nextPiece(new TetrisPiece(Piece.PieceType.T), new Point(board1.getWidth() / 2, 20));
        board1.move(Board.Action.DOWN);

        Assert.assertEquals(board1, boardCopy.testMove(Board.Action.DOWN));
    }
}
