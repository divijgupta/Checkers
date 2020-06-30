
import java.awt.*;

import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;

@SuppressWarnings("serial")
public class CheckerBoard extends JPanel {
	// creates the gameBoard for the game
	private Piece[][] gameBoard;

	// keeps track of the moves that both players made
	private LinkedList<Move> moves;

	// board dimensions
	private final int BOARD_MAX = 7;
	private final int BOARD_MIN = 0;
	private static final int BOARD_SIZE = 300;

	private JLabel status;

	// keeps track of mouse-clicks
	private int oldX = -1;
	private int oldY = -1;
	private int newX = -1;
	private int newY = -1;

	// player 1 goes first
	private boolean player1Turn = true;

	// keeps track of whether or not the game is over
	private String gameWinner = null;

	static final String SAVED_GAME_PATH = "files/saved_game.txt";

	// constructor
	public CheckerBoard(JLabel status) {

		// initializes the 8 by 8 grid (upper left corner will represent piece)
		gameBoard = new Piece[8][8];

		// initializes moves
		moves = new LinkedList<Move>();

		// Make sure that this component has the focus
		requestFocusInWindow();

		// initialize game
		initialize();

		// initialize status
		this.status = status;

		// create a mouse listener to listen for clicks
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				oldX = event.getX() * 8 / 300;
				oldY = event.getY() * 8 / 300;

			}

			public void mouseReleased(MouseEvent event) {
				newX = event.getX() * 8 / 300;
				newY = event.getY() * 8 / 300;

				// if move happens change turns
				if (move(oldX, oldY, newX - oldX, newY - oldY, player1Turn)) {
					player1Turn = !player1Turn;

				}
			}
		});

	}

	/**
	 * This allows the pieces on the board to move, if it is his or her turn
	 *
	 * @param x           The x coordinate of the piece on the board
	 * @param y           The y coordinate of the piece on the board
	 * @param dx          The change in x for the move
	 * @param dy          The change in y for the move
	 * @param player1Turn Whether or not it is player 1's turn
	 * 
	 * @return A Boolean of whether or not the move occurred or not
	 */
	public boolean move(int x, int y, int dx, int dy, boolean player1Turn) {

		// gets the piece associated with the move (will be null if no piece)
		Piece p = gameBoard[x][y];

		// checks if the piece is there
		if (gameBoard[x][y] == null) {
			return false;

		}
		// if its player 1's turn, only red can move
		else if (player1Turn && p.getColor().equals(Color.BLACK)) {
			return false;
		} // if its player two's turn, only black can move
		else if (!player1Turn && p.getColor().equals(Color.RED)) {
			return false;
		}
		// pieces can only move diagonally
		else if (Math.abs(dx * dy) != 1) {
			return false;
		}
		// don't allow a piece to move off the board
		else if ((x + dx) > BOARD_MAX) {
			return false;
		} else if ((x + dx) < BOARD_MIN) {
			return false;
		} else if ((y + dy) > BOARD_MAX) {
			return false;
		} else if ((y + dy) < BOARD_MIN) {
			return false;
		}

		// don't allow a non king red to move backwards
		else if ((!p.isKing()) && (p.getColor().equals(Color.RED)) && (dy < 0)) {
			return false;
		}
		// don't allow a non king black to move backwards
		else if ((!p.isKing()) && (p.getColor().equals(Color.BLACK)) && (dy > 0)) {
			return false;
		}
		// allow regular movement
		else if (gameBoard[x + dx][y + dy] == null) {
			// update gameBoard
			gameBoard[x + dx][y + dy] = p;
			gameBoard[x][y] = null;

			// update piece
			p.setCurrX(x + dx);
			p.setCurrY(y + dy);

			// create new move
			Move regularMove = new Move(x, y, x + dx, y + dy, false, p, null);

			// check for upgrade and update move if needed
			if (checkUpgrade(p.getCurrX(), p.getCurrY())) {
				regularMove.setUpgrade(true);

			}

			// add move to the move list
			moves.add(regularMove);

			// redraws canvas
			repaint();

			return true;
		}

		// can't allow a piece to move or jump on a piece possessed by the same team
		else if (gameBoard[x + dx][y + dy].getColor().equals(p.getColor())) {
			return false;

		}
		// allow jump for red
		else if (p.getColor().equals(Color.RED) && 
				(gameBoard[x + dx][y + dy].getColor().equals(Color.BLACK))) {

			if (jump(x, y, dx, dy, p)) {

				// check if red has won
				String winner = winner();

				// if black hasn't lost then continue game as planned
				if (winner == null) {
					return true;
				} else {
					// change status if red wins
					gameWinner = "Red wins!";
					return true;

				}

			} else {
				return false;
			}

			// allow jump for black
		} else if (p.getColor().equals(Color.BLACK) && 
				(gameBoard[x + dx][y + dy].getColor().equals(Color.RED))) {

			if (jump(x, y, dx, dy, p)) {

				// check if black has won
				String winner = winner();

				// if red hasn't lost then continue game as planned
				if (winner == null) {
					return true;
				} else {
					// change status if black wins
					gameWinner = "Black wins!";
					return true;

				}
			} else {
				return false;
			}

		}
		// otherwise return false
		else {

			return false;

		}

	}

	/**
	 * This allows the pieces on the board to jump and take the opponent's piece
	 *
	 * @param x  The x coordinate of the piece on the board
	 * @param y  The y coordinate of the piece on the board
	 * @param dx The change in x for the move
	 * @param dy The change in y for the move
	 * @param p  The reference to the piece will move and steal the opponent's piece
	 * 
	 * @return A Boolean of whether or not the jump occurred or not
	 */
	public boolean jump(int x, int y, int dx, int dy, Piece p) {
		// check if the piece where it would be moved to is in bounds
		if (x + dx + dx > BOARD_MAX || x + dx + dx < BOARD_MIN || y + dy + dy > BOARD_MAX 
				|| y + dy + dy < BOARD_MIN) {

			return false;

		}
		// check if another piece is in the spot it will move to
		else if (gameBoard[x + dx + dx][y + dy + dy] != null) {

			return false;
		} else {

			Piece removed = gameBoard[x + dx][y + dy];

			// create new move
			Move jumpMove = new Move(x, y, x + dx + dx, y + dy + dy, false, p, removed);

			p.setCurrX(x + dx + dx);
			p.setCurrY(y + dy + dy);

			// update board
			gameBoard[x][y] = null;
			gameBoard[x + dx][y + dy] = null;
			// add piece to new location
			gameBoard[x + dx + dx][y + dy + dy] = p;

			// check to see if piece should be upgraded to a King status
			if (checkUpgrade(p.getCurrX(), p.getCurrY())) {
				// set upgrade to true
				jumpMove.setUpgrade(true);

			}

			// add move to move list
			moves.add(jumpMove);

			// redraws canvas
			repaint();

			return true;

		}

	}

	/**
	 * This checks whether or not a piece needs to be updated to a king and updates
	 * the piece after and returns whether or not the upgrade happened
	 *
	 * @param x The x coordinate of the piece on the board
	 * @param y The y coordinate of the piece on the board
	 * 
	 * @return A Boolean of whether or not the piece was upgraded
	 */
	public boolean checkUpgrade(int x, int y) {

		// checks if piece is a non-king red piece
		if (gameBoard[x][y].getColor().equals(Color.RED) && !gameBoard[x][y].isKing() && (y == 7)) {

			// updates necessary information
			gameBoard[x][y].setKing(true);
			return true;

		}
		// checks if piece is a non-king black piece
		else if (gameBoard[x][y].getColor().equals(Color.BLACK) 
				&& !gameBoard[x][y].isKing() && (y == 0)) {

			// updates necessary information
			gameBoard[x][y].setKing(true);
			return true;

		}
		// return false if piece is not upgraded
		return false;

	}

	/**
	 * This initializes the pieces on the board, used to place all of the pieces in
	 * the right location at the start
	 */
	public void initialize() {

		/// adds all of the red pieces to the board and set of pieces
		gameBoard[1][0] = new Piece(false, 1, 1, 0, Color.RED);
		gameBoard[3][0] = new Piece(false, 1, 3, 0, Color.RED);
		gameBoard[5][0] = new Piece(false, 1, 5, 0, Color.RED);
		gameBoard[7][0] = new Piece(false, 1, 7, 0, Color.RED);
		gameBoard[0][1] = new Piece(false, 1, 0, 1, Color.RED);
		gameBoard[2][1] = new Piece(false, 1, 2, 1, Color.RED);
		gameBoard[4][1] = new Piece(false, 1, 4, 1, Color.RED);
		gameBoard[6][1] = new Piece(false, 1, 6, 1, Color.RED);
		gameBoard[1][2] = new Piece(false, 1, 1, 2, Color.RED);
		gameBoard[3][2] = new Piece(false, 1, 3, 2, Color.RED);
		gameBoard[5][2] = new Piece(false, 1, 5, 2, Color.RED);
		gameBoard[7][2] = new Piece(false, 1, 7, 2, Color.RED);

		// adds all of the black pieces to the board
		gameBoard[0][5] = new Piece(false, 2, 0, 5, Color.BLACK);
		gameBoard[2][5] = new Piece(false, 2, 2, 5, Color.BLACK);
		gameBoard[4][5] = new Piece(false, 2, 4, 5, Color.BLACK);
		gameBoard[6][5] = new Piece(false, 2, 6, 5, Color.BLACK);
		gameBoard[1][6] = new Piece(false, 2, 1, 6, Color.BLACK);
		gameBoard[3][6] = new Piece(false, 2, 3, 6, Color.BLACK);
		gameBoard[5][6] = new Piece(false, 2, 5, 6, Color.BLACK);
		gameBoard[7][6] = new Piece(false, 2, 7, 6, Color.BLACK);
		gameBoard[0][7] = new Piece(false, 2, 0, 7, Color.BLACK);
		gameBoard[2][7] = new Piece(false, 2, 2, 7, Color.BLACK);
		gameBoard[4][7] = new Piece(false, 2, 4, 7, Color.BLACK);
		gameBoard[6][7] = new Piece(false, 2, 6, 7, Color.BLACK);

	}

	/**
	 * This returns the referential value to a piece, given its x and y coordinate.
	 * It was mostly used during testing
	 *
	 * @param x The x coordinate of the piece on the board
	 * @param y The y coordinate of the piece on the board
	 * 
	 * @return The referential value to the piece on the board
	 */
	public Piece getPiece(int x, int y) {
		return gameBoard[x][y];
	}

	/**
	 * This allows the a user to add a new piece to the board. This was used during
	 * testing
	 *
	 * @param king     Whether or not a piece is a King (false at the start)
	 * @param colorIdx Was used to keep track of the Piece's color (1 for red, 2 for
	 *                 black)
	 * @param x        The x coordinate of the piece on the board
	 * @param y        The y coordinate of the piece on the board
	 * @param color    The color of the piece on the board of type Color
	 * 
	 * @return N/A
	 */
	public void addPiece(boolean king, int colorIdx, int currX, int currY, Color color) {
		gameBoard[currX][currY] = new Piece(king, colorIdx, currX, currY, color);
	}

	/**
	 * This clears the game board. This was used during testing
	 */
	public void clear() {

		// iterate through the board
		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard[i].length; j++) {
				gameBoard[i][j] = null;
			}
		}
	}

	/**
	 * This function checks whether or not a team has won in the game
	 * 
	 * @return This function returns null if no Winner, "BLACK" if black has lost,
	 *         and "RED" if red has lost
	 */
	public String winner() {

		// will count number of black and red pieces
		int numBlack = 0;
		int numRed = 0;

		// count how many pieces each player has left
		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard[i].length; j++) {
				if (gameBoard[i][j] == null) {
					continue;
				} else if (gameBoard[i][j].getColor().equals(Color.BLACK)) {
					numBlack++;
				} else {
					numRed++;
				}
			}
		}

		// if either player has 0 pieces left return the player otherwise null
		if (numBlack == 0) {
			return "BLACK";
		} else if (numRed == 0) {
			return "RED";
		} else {
			return null;
		}
	}

	/**
	 * This allows the the game to be reset. It is used in the Game class
	 */
	public void reset() {

		// resets the gameBoard
		gameBoard = new Piece[8][8];

		// resets the list of moves
		moves.clear();

		// initialize the gameBoard
		initialize();

		// Make sure that this component has the focus
		requestFocusInWindow();

		// player 1 goes first
		player1Turn = true;

		gameWinner = null;

		// repaint the board
		repaint();
	}

	/**
	 * This uses the Writer Class and allows the the game format to be saved. It is
	 * used in the Game class. If a game is saved, it overwrites any previously
	 * saved state. As a result, it can only keep track of one save
	 */
	public void save() {

		// create the buffered writer and the output file
		BufferedWriter bufWr = null;
		File savedGame = Paths.get(SAVED_GAME_PATH).toFile();

		// initialize the writer
		try {
			bufWr = new BufferedWriter(new FileWriter(savedGame, false));
		} catch (IOException e1) {
			System.out.println("I/O Error");
		}

		// iterate through the board to write to the file
		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard[i].length; j++) {

				// if location is null then 0
				if (gameBoard[i][j] == null) {

					try {
						bufWr.write("EMPTY");
						// create a new line after each piece
						bufWr.newLine();
					} catch (IOException e) {

						e.printStackTrace();
					}

				} else {
					// write the position of each piece and whether or not it is a king
					try {
						bufWr.write("" + gameBoard[i][j].getColorIdx() + gameBoard[i][j].isKing());
						// create a new line after each piece
						bufWr.newLine();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

			}

		}

		// write whose turn it is & whether or not someone won
		try {
			bufWr.write("" + player1Turn);
			bufWr.newLine();
			bufWr.write("" + gameWinner);
		} catch (IOException e) {

			e.printStackTrace();
		}

		// make sure to close and flush the buffered writer
		try {
			bufWr.flush();
			bufWr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This uses the Reader Class and allows the last saved format to be loaded. It
	 * is used in the Game class. However, if a user presses load, he can't undo
	 * past the part that he loaded from
	 */
	public boolean load() {
		// create the buffered reader and the output file
		BufferedReader buffR = null;

		try {
			buffR = new BufferedReader(new FileReader(SAVED_GAME_PATH));
			// if path is found
		} catch (FileNotFoundException e) {
			// if file is not found
			return false;
		}

		// current indices of what the reader is reading
		int currX = 0;
		int currY = 0;

		int firstTime = 0;

		String str = "";
		// read the first line
		try {
			str = buffR.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while (str != null) {

			// reset the game the first time around
			if (firstTime == 0) {
				reset();
			}
			// find the type of piece at each point
			if (str.equals("EMPTY")) {
				gameBoard[currX][currY] = null;

			} else if (str.equals("1false")) {
				// create a nonKing red Piece
				gameBoard[currX][currY] = new Piece(false, 1, currX, currY, Color.RED);

			} else if (str.equals("1true")) {
				// create a King red Piece
				gameBoard[currX][currY] = new Piece(true, 1, currX, currY, Color.RED);

			} else if (str.equals("2false")) {
				// create a nonKing Black Piece
				gameBoard[currX][currY] = new Piece(false, 2, currX, currY, Color.BLACK);

			} else if (str.equals("2true")) {
				// create a King Black Piece
				gameBoard[currX][currY] = new Piece(true, 2, currX, currY, Color.BLACK);

			} else if (str.equals("true")) {
				// set it to player 1's turn
				player1Turn = true;

			} else if (str.equals("false")) {
				// set it to player 2's turn
				player1Turn = false;

			} // see if nobody won
			else if (str.equals("null")) {
				gameWinner = null;
				// see if red won
			} else if (str.equals("Red wins!")) {
				gameWinner = "Red wins!";
				// see if black won
			} else if (str.equals("Black wins!")) {
				gameWinner = "Black wins!";
			} else {
				// if some other text exists
				try {
					buffR.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}

			// its no longer the first time
			firstTime++;

			// make sure to keep track of the position within the array
			if (currY < 7) {
				currY++;
			} else {
				currX++;
				currY = 0;
			}

			// read the next line
			try {
				str = buffR.readLine();

			} catch (IOException e1) {

				e1.printStackTrace();
			}

		}

		// repaint with the new additions
		repaint();

		// close the reader when you're done reading it
		try {
			buffR.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// if everything goes well
		return true;

	}

	/**
	 * This allows a move to be undo'd.
	 * 
	 * NOTE: If a game is loaded from I/O, you cannot undo past the point it was
	 * loaded at. Any new moves from that state can be undo'd
	 */
	public boolean undo() {
		// get the last move
		Move m = null;
		try {
			m = moves.removeLast();
		} catch (NoSuchElementException e) {
			// if undo didn't happen
			return false;
		}

		// prevents a null pointer exception
		if (m != null) {

			// last thing to figure out
			// resets the list of moves
			// moves.clear();

			Piece p = m.getPiece();
			// set the position of the piece back to its old spot
			p.setCurrX(m.getX());
			p.setCurrY(m.getY());

			// move piece back to old location
			gameBoard[m.getX()][m.getY()] = p;

			// sets new location to null
			gameBoard[m.getNewX()][m.getNewY()] = null;

			// if it was upgraded, setKing to false
			if (m.upgradeHappened()) {
				p.setKing(false);
			}

			// gets the piece that might have been removed
			if (m.getRemoved() != null) {

				Piece removed = m.getRemoved();
				int removedX = (m.getX() + m.getNewX()) / 2;
				int removedY = (m.getY() + m.getNewY()) / 2;
				gameBoard[removedX][removedY] = removed;

			}

			// repaint with undo'd stuff
			repaint();

			// Make sure that this component has the focus
			requestFocusInWindow();

			// reset turn
			player1Turn = !player1Turn;

			// if game ended change status of game back to ongoing
			if (gameWinner != null) {
				gameWinner = null;
			}
			// if undo happened
			return true;

		}
		// if undo didn't happen
		return false;

	}

	/**
	 * This allows the CheckerBoard to be drawn
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// iterate through the 2D array to draw squares
		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard[i].length; j++) {

				// if it should be a Gray square
				if ((i + j) % 2 == 0) {
					g.setColor(Color.GRAY);
				}
				// otherwise make it a blue square
				else {
					g.setColor(Color.BLUE);
				}

				g.fillRect(i * BOARD_SIZE / 8, j * BOARD_SIZE / 8, BOARD_SIZE / 8, BOARD_SIZE / 8);

				// draw pieces
				if (gameBoard[i][j] == null) {
					continue;
				} else {
					gameBoard[i][j].draw(g, BOARD_SIZE);

				}

			}
		}

		// change the status of Label if needed and the game isn't over
		if (gameWinner == null) {
			if (player1Turn) {
				status.setText("Red's Turn");
			} else {
				status.setText("Black's Turn");
			}
		} else {
			status.setText(gameWinner);
		}
	}

	/**
	 * This returns the Dimension of the Board
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(BOARD_SIZE, BOARD_SIZE);
	}

}
