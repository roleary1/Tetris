package assignment;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;

import assignment.Piece.PieceType;

/**
 * Created by tejasmehta on 10/11/19.
 */
public class JBrainTetris extends JTetris {
    private Brain brain;
    JBrainTetris(){
        super(); //JTetris constructor
        brain = new NotLameBrain();
        for (KeyStroke keyStroke : super.getRegisteredKeyStrokes()){ //removes keystrokes
            unregisterKeyboardAction(keyStroke);
        }
        timer = new javax.swing.Timer(DELAY, new ActionListener() { //timer to tick
            public void actionPerformed(ActionEvent e) {
                tick(brain.nextMove(JBrainTetris.super.board));
            }
        });
    }
    public static void main(String[] args) {
        createGUI(new JBrainTetris()); //creates the gui
    }
}
