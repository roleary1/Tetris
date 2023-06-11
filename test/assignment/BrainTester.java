package assignment;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

public class BrainTester {
    private static TetrisBoard board1, board2, board3;
    private static ArrayList<Piece> tetrisPieces;
    private static NotLameBrain brainAI;

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

        brainAI = new NotLameBrain();
    }

    @Test
    public void TestGetNumHoles() {
        board1 = new TetrisBoard(10, 24);
        Piece nextPiece = new TetrisPiece(Piece.PieceType.SQUARE);
        board1.nextPiece(nextPiece, new Point(0, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(nextPiece, new Point(2, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(nextPiece, new Point(6, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(nextPiece, new Point(8, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(new TetrisPiece(Piece.PieceType.STICK), new Point(4, 20));
        board1.move(Board.Action.DROP);

        Assert.assertEquals(4, brainAI.getNumHoles(board1));
    }
    @Test
    public void TestGetVerticalStick() {
        board1 = new TetrisBoard(10, 24);
        Piece nextPiece = new TetrisPiece(Piece.PieceType.STICK);
        board1.nextPiece(nextPiece, new Point(board1.getWidth()/2 - nextPiece.getWidth()/2, 20));
        board1.move(Board.Action.CLOCKWISE);

        Assert.assertEquals(20 ,brainAI.getVerticalStick(board1));
    }
    @Test
    public void TestScoreBoard() {
        board1 = new TetrisBoard(10, 24);
        Piece nextPiece = new TetrisPiece(Piece.PieceType.SQUARE);
        board1.nextPiece(nextPiece, new Point(0, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(nextPiece, new Point(2, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(nextPiece, new Point(6, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(nextPiece, new Point(8, 20));
        board1.move(Board.Action.DROP);
        nextPiece = new TetrisPiece(Piece.PieceType.STICK);
        board1.nextPiece(nextPiece, new Point(4, 20));
        board1.move(Board.Action.DROP);
        board1.nextPiece(nextPiece, new Point(board1.getWidth()/2 - nextPiece.getWidth()/2, 20));
        board1.move(Board.Action.CLOCKWISE);

        Assert.assertEquals(85, brainAI.scoreBoard(board1));
    }
}
