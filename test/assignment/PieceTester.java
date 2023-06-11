package assignment;

import assignment.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by tejasmehta on 10/4/19.
 */
public class PieceTester {
    @BeforeClass
    public static void setUp() {

    }

    @Test
    public void TestGetPieceType() {
        Piece.PieceType square = Piece.PieceType.SQUARE;
        Piece piece = new TetrisPiece(square);
        Assert.assertEquals(square,piece.getType());
    }
    @Test
    public void TestGetRotationIndex() {
        Piece.PieceType square = Piece.PieceType.SQUARE;
        Piece piece = new TetrisPiece(square);
        Assert.assertEquals(0,piece.getRotationIndex());
        Piece piece1 = new TetrisPiece(square,1,piece.getBody());
        Assert.assertEquals(1,piece1.getRotationIndex());
    }
    @Test
    public void TestClockwiseandCounterClockwisePiece() {
        Piece.PieceType square = Piece.PieceType.SQUARE;
        Piece piece = new TetrisPiece(square);
        piece = piece.clockwisePiece();
        Assert.assertEquals(1,piece.getRotationIndex());
        piece = piece.counterclockwisePiece();
        Assert.assertEquals(0,piece.getRotationIndex());
        piece = piece.counterclockwisePiece();
        Assert.assertEquals(3,piece.getRotationIndex());
        piece = piece.clockwisePiece();
        Assert.assertEquals(0,piece.getRotationIndex());
        piece = piece.clockwisePiece();
        Assert.assertEquals(1,piece.getRotationIndex());
        piece = piece.clockwisePiece();
        Assert.assertEquals(2,piece.getRotationIndex());
        piece = piece.clockwisePiece();
        Assert.assertEquals(3,piece.getRotationIndex());
        piece = piece.clockwisePiece();
        Assert.assertEquals(0,piece.getRotationIndex());
    }
    @Test
    public void TestGetWidthAndHeight() {
        Piece.PieceType square = Piece.PieceType.SQUARE;
        Piece piece = new TetrisPiece(square);
        Assert.assertEquals(square.getBoundingBox().width,piece.getWidth());
        Assert.assertEquals(square.getBoundingBox().height,piece.getHeight());
    }
    @Test
    public void TestGetBody() {
        Piece.PieceType square = Piece.PieceType.SQUARE;
        Piece piece = new TetrisPiece(square);
        Assert.assertEquals(square.getSpawnBody(),piece.getBody());
    }
    @Test
    public void TestSkirt(){
        Piece.PieceType square = Piece.PieceType.SQUARE;

        //Square
        Piece piece = new TetrisPiece(square);
        int[] skirt = piece.getSkirt();
        Assert.assertEquals(0,skirt[0]);
        Assert.assertEquals(0,skirt[1]);
        Assert.assertEquals(2,skirt.length);

        //T
        piece = new TetrisPiece(Piece.PieceType.T);
        skirt = piece.getSkirt();
        Assert.assertEquals(1,skirt[0]);
        Assert.assertEquals(1,skirt[1]);
        Assert.assertEquals(1,skirt[2]);
        Assert.assertEquals(3,skirt.length);

        //Stick
        piece = new TetrisPiece(Piece.PieceType.STICK);
        skirt = piece.getSkirt();
        Assert.assertEquals(2,skirt[0]);
        Assert.assertEquals(2,skirt[1]);
        Assert.assertEquals(2,skirt[2]);
        Assert.assertEquals(2,skirt[3]);
        Assert.assertEquals(4,skirt.length);

        //LeftL
        piece = new TetrisPiece(Piece.PieceType.LEFT_L);
        skirt = piece.getSkirt();
        Assert.assertEquals(1,skirt[0]);
        Assert.assertEquals(1,skirt[1]);
        Assert.assertEquals(1,skirt[2]);
        Assert.assertEquals(3,skirt.length);

        //RightL
        piece = new TetrisPiece(Piece.PieceType.RIGHT_L);
        skirt = piece.getSkirt();
        Assert.assertEquals(1,skirt[0]);
        Assert.assertEquals(1,skirt[1]);
        Assert.assertEquals(1,skirt[2]);
        Assert.assertEquals(3,skirt.length);

        //LeftDog
        piece = new TetrisPiece(Piece.PieceType.LEFT_DOG);
        skirt = piece.getSkirt();
        Assert.assertEquals(2,skirt[0]);
        Assert.assertEquals(1,skirt[1]);
        Assert.assertEquals(1,skirt[2]);
        Assert.assertEquals(3,skirt.length);

        //RightDog
        piece = new TetrisPiece(Piece.PieceType.RIGHT_DOG);
        skirt = piece.getSkirt();
        Assert.assertEquals(1,skirt[0]);
        Assert.assertEquals(1,skirt[1]);
        Assert.assertEquals(2,skirt[2]);
        Assert.assertEquals(3,skirt.length);

        //RightDogClockwise
        piece = new TetrisPiece(Piece.PieceType.RIGHT_DOG);
        piece = piece.clockwisePiece();
        skirt = piece.getSkirt();
        Assert.assertEquals(Integer.MAX_VALUE,skirt[0]);
        Assert.assertEquals(1,skirt[1]);
        Assert.assertEquals(0,skirt[2]);
        Assert.assertEquals(3,skirt.length);
    }

    @Test
    public void TestEquals(){
        Piece piece = new TetrisPiece(Piece.PieceType.RIGHT_DOG);
        Piece piece1 = new TetrisPiece(Piece.PieceType.RIGHT_DOG);
        Piece square = new TetrisPiece(Piece.PieceType.SQUARE);
        Assert.assertEquals(piece, piece1);
        Assert.assertNotEquals(piece, square);
        piece1 = piece1.clockwisePiece();
        Assert.assertNotEquals(piece, piece1);
    }
}
