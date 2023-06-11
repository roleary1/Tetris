package assignment;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * 
 * All operations on a TetrisPiece should be constant time, except for it's
 * initial construction. This means that rotations should also be fast - calling
 * clockwisePiece() and counterclockwisePiece() should be constant time! You may
 * need to do precomputation in the constructor to make this possible.
 */
public final class TetrisPiece implements Piece {
    private PieceType pType;
    private int rotationIndex;
    private Point[] body;
    private int[] skirt;
    private int bodyWidth;
    private int bodyHeight;
    private int left;
    private int right;
    private double bottom;
    private double top;

    //Different Rotations:
    //T
    private Point[][] TRotations = {PieceType.T.getSpawnBody(),
            {new Point(1,0), new Point(1,1), new Point(1,2), new Point(2,1)},
            {new Point(0,1), new Point(1,0), new Point(1,1), new Point(2,1)},
            {new Point(0,1), new Point(1,0), new Point(1,1), new Point(1,2)}};

    //Square (Redundant)
//    Point[][] Squares = {PieceType.SQUARE.getSpawnBody(),PieceType.SQUARE.getSpawnBody(),PieceType.SQUARE.getSpawnBody(),PieceType.SQUARE.getSpawnBody()};

    //Stick
    private Point[][] StickRotations = {PieceType.STICK.getSpawnBody(),
            {new Point(2, 0), new Point(2, 1), new Point(2, 2), new Point(2, 3)},
            {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1)},
            {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3)}};

    //LeftL
    private Point[][] LeftLRotations = {PieceType.LEFT_L.getSpawnBody(),
            {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2)},
            {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0)},
            {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(1, 2)}};

    //RightL
    private Point[][] RightLRotations = {PieceType.RIGHT_L.getSpawnBody(),
            {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0)},
            {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)},
            {new Point(0, 2), new Point(1, 0), new Point(1, 1), new Point(1, 2)}};

    //LeftDog
    private Point[][] LeftDogRotations = {PieceType.LEFT_DOG.getSpawnBody(),
            {new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2)},
            {new Point(0, 1), new Point(1, 0), new Point(1, 1), new Point(2, 0)},
            {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)}};

    //RightDog
    private Point[][] RightDogRotations = {PieceType.RIGHT_DOG.getSpawnBody(),
            {new Point(1, 1), new Point(1, 2), new Point(2, 0), new Point(2, 1)},
            {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1)},
            {new Point(0, 1), new Point(0, 2), new Point(1, 0), new Point(1, 1)}};


    /**
     * Construct a tetris piece of the given type. The piece should be in it's spawn orientation,
     * i.e., a rotation index of 0.
     * 
     * You may freely add additional constructors, but please leave this one - it is used both in
     * the runner code and testing code.
     */
    public TetrisPiece(PieceType type) { //Constructor for new pieces
        //PieceType
        pType = type;
        rotationIndex = 0;
        body  = pType.getSpawnBody();
        skirt = getSkirt();
        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;
        //Get min and max x values
        for (int i = 0; i < body.length; i++) { //Get left and right
            if (body[i].getX()<min){
                min = body[i].getX();
            }
            if (body[i].getX()>max){
                max = body[i].getX();
            }
        }
        left = (int)min;
        right = (int)max;
        bodyWidth = (int)(max-min)+1;
        bottom = Integer.MAX_VALUE;
        top = Integer.MIN_VALUE;
        for (int i = 0; i < body.length; i++) { //get top and bottom
            if (body[i].getY()<bottom){
                bottom = body[i].getY();
            }
            if (body[i].getY()>top){
                top = body[i].getY();
            }
        }
        bodyHeight = (int)(top-bottom)+1;
    }

    public TetrisPiece(PieceType type, int rIndex, Point[] body) { //Constructor for rotations
        pType = type;
        this.body = body;
        rotationIndex = rIndex;
        skirt = getSkirt();
        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;
        for (int i = 0; i < body.length; i++) { //get left and right
            if (body[i].getX()<min){
                min = body[i].getX();
            }
            if (body[i].getX()>max){
                max = body[i].getX();
            }
        }
        left = (int)min;
        right = (int)max;
        bodyWidth = right-left+1;
        double bottom = Integer.MAX_VALUE;
        double top = Integer.MIN_VALUE;
        for (int i = 0; i < body.length; i++) { //get top and bottom
            if (body[i].getY()<bottom){
                bottom = body[i].getY();
            }
            if (body[i].getY()>top){
                top = body[i].getY();
            }
        }
        bodyHeight = (int)(top-bottom)+1;
    }

    @Override
    public PieceType getType() { //return piecetype
        return pType;
    }

    @Override
    public int getRotationIndex() { //return rotation index
        return rotationIndex;
    }

    @Override
    public Piece clockwisePiece() { //increment rotation index or set to 0
        if(getRotationIndex() == 3)
            rotationIndex = 0;
        else rotationIndex = rotationIndex+1;
        rotatePiece(rotationIndex);
        return new TetrisPiece(pType,rotationIndex,body);
    }

    @Override
    public Piece counterclockwisePiece() { //decrememnt rotation index or set to 3
        if(getRotationIndex() == 0)
            rotationIndex = 3;
        else rotationIndex = rotationIndex-1;
        rotatePiece(rotationIndex);
        return new TetrisPiece(pType,rotationIndex,body);
    }

    @Override
    public int getWidth() { //return width
        return bodyWidth;
    }

    @Override
    public int getHeight() { //return height
        return bodyHeight;
    }

    @Override
    public Point[] getBody() { //return body
        return body;
    }

    @Override
    public int[] getSkirt() { //Get the bottom most values of each piece per column
        int[] skirt = new int[(int)pType.getBoundingBox().getWidth()];
        Arrays.fill(skirt,Integer.MAX_VALUE);
        for(int i = 0; i < body.length; i++){
            if (skirt[body[i].x]>body[i].y) {
                skirt[body[i].x] = body[i].y;
            }
        }
        return skirt; //return the array
    }

    @Override
    public boolean equals(Object other) { //check if type and rotation index are equal for both current and given object
        // Ignore objects which aren't also tetris pieces.
        if(!(other instanceof TetrisPiece)) return false;
        TetrisPiece otherPiece = (TetrisPiece) other;
        if(otherPiece.pType == pType && otherPiece.rotationIndex == rotationIndex)
            return true;
        return false;
    }

    //helper methods
    private void rotatePiece(int index) { //change the body depending on what piece type it is
        switch (pType){
            case T:
                body = TRotations[index];
                break;
            case STICK:
                body = StickRotations[index];
                break;
            case LEFT_L:
                body = LeftLRotations[index];
                break;
            case SQUARE:
                break;
            case RIGHT_L:
                body = RightLRotations[index];
                break;
            case LEFT_DOG:
                body = LeftDogRotations[index];
                break;
            case RIGHT_DOG:
                body = RightDogRotations[index];
                break;
            default:
                break;
        }
    }

    public double getBottom() { //return bottom
        return bottom;
    }

    public double getTop() { //return the top
        return top;
    }

    public int getLeft() { //return the left
        return left;
    }

    public int getRight() { //return the right
        return right;
    }
}
