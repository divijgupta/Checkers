// imports necessary libraries for Java swing
import java.awt.*;

import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {

    	//create the instructions JFrame
        final JFrame instructions = new JFrame("Instructions!");
        instructions.setLocation(300, 300);
        
        //add the JPanel to instructions
        final JPanel description = new JPanel();
        instructions.add(description, BorderLayout.CENTER);
        final JTextArea words = new JTextArea("Welcome to Checkers! This is the classic game that"
        		+ " you played growing up.\nThe object of the game is to 'jump' all of your "
        		+ "opponent's pieces. You can\n only move your own color and only move when its"
        		+ " your turn.\nRules:\n1) To move, drag your piece to the new spot.\n"
        		+ "2) To jump your opponent, drag from your piece to their piece (not to your new "
        		+ "location).\n3) Red goes first. The person whose turn it is will be "
        		+ "displayed on the bottom.\n" + "4) Regular Pieces can only move forward. "
        		+ "Kings can move in either direction.\n5) To become a King, you have to reach the "
        		+ "end of the board.\n6) To save a game, press save. To load the last saved game, "
        		+ "press load.\n7) No double jumps!", 6, 5);
        words.setEditable(false);
        instructions.add(words);
        
        //create pop-up for undo
        final JFrame undoPopUp = new JFrame("Undo failed :(");
        undoPopUp.setLocation(300, 300);
        final JPanel undoDescription = new JPanel();
        undoPopUp.add(undoDescription, BorderLayout.CENTER);
        //create the text for the pop-up
        final JTextArea undoWords = new JTextArea("You have no more Moves to Undo! \n"
        		+ "NOTE: if you loaded a previously saved game, you can't undo past the loaded "
        		+ "point.\nHowever, you can undo any new moves that you may have made since the "
        		+ "load!\nIf you decide to load, you cannot undo to get to your previous game.", 
        		4, 4);
        undoWords.setEditable(false);
        undoDescription.add(undoWords);
        
      //create pop-up for load failure
        final JFrame loadFailure = new JFrame("load failed :(");
        loadFailure.setLocation(300, 300);
        final JPanel loadDescription = new JPanel();
        loadFailure.add(loadDescription, BorderLayout.CENTER);
        //create the text for the pop-up
        final JTextArea loadWords = new JTextArea("To continue a game, either save a current game"
        		+ " or please input a file that exists or one in the correct format.", 
        		2, 4);
        loadWords.setEditable(false);
        loadFailure.add(loadWords);
        	
        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Checkers");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Red's Turn");
        status_panel.add(status);

        // Main playing area
        final CheckerBoard game = new CheckerBoard(status);
        frame.add(game, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // reset button
        final JButton resetGame = new JButton("Reset");
        resetGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.reset();
            }
        });
        control_panel.add(resetGame);
        
        //undo's the previous move
        final JButton undoMove = new JButton("Undo");
        undoMove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//call undo
                if (!game.undo()) {
                	//call the undo pop-up
                	undoPopUp.pack();
                	undoPopUp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                	undoPopUp.setVisible(true);
                	
                }
            }
        });
        control_panel.add(undoMove);
        
        //used to save the state
        final JButton saveGame = new JButton("Save");
        saveGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	game.save();
            }
        });
        control_panel.add(saveGame);
        
        //used to load the old saved state
        final JButton loadGame = new JButton("Load");
        loadGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (!game.load()) {
            		//in case of a load failure
            		loadFailure.pack();
            		loadFailure.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            		loadFailure.setVisible(true);
            	}
            }
        });
        control_panel.add(loadGame);

        
        // Put the game frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        //put the instructions page on the screen (don't exit on close only dispose)
        instructions.pack();
        instructions.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        instructions.setVisible(true);

        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}