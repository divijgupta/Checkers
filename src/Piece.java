import java.awt.*;
import java.awt.Graphics;

public class Piece implements Comparable<Piece> {

	private boolean king;
	private int colorIdx;
	private int currX;
	private int currY;
	private final Color color;

	/**
	 * This is the Constructor for the Piece Object
	 * 
	 * @param king          This is a boolean that is true when the piece is a king and false when 
	 * 						the piece isn't
	 * @param colorIdx      This keeps track of the Color that the piece is (1 for red, 2 for black)
	 * @param currX         This keeps track of the piece's current X value
	 * @param currY         This keeps track of the piece's current Y value
	 * @param color         This keeps track of the Piece's color of type Color
	 */
	public Piece(boolean king, int colorIdx, int currX, int currY, Color color) {
		super();
		this.king = king;
		this.colorIdx = colorIdx;
		this.currX = currX;
		this.currY = currY;
		this.color = color;
	}

	public void draw(Graphics g, int boardSize) {
		//draw the piece
		g.setColor(this.color);
		g.fillOval(this.getCurrX() * boardSize / 8, this.getCurrY() * boardSize / 8, 
				boardSize / 8, boardSize / 8);

		// if it is a king draw the word "KING" on it
		if (this.isKing()) {
			g.setColor(Color.WHITE);

			g.drawString("KING", (this.getCurrX() * boardSize / 8) + 4, (this.getCurrY() * 
					boardSize / 8) + 22);
		}

	}

	/***
	 * GETTERS
	 **********************************************************************************/
	public boolean isKing() {
		return king;
	}

	public Color getColor() {
		return color;
	}

	public int getColorIdx() {
		return colorIdx;
	}

	public int getCurrX() {
		return currX;
	}

	public int getCurrY() {
		return currY;
	}

	/***
	 * SETTERS
	 **********************************************************************************/
	public void setKing(boolean king) {
		this.king = king;
	}

	public void setColorIdx(int colorIdx) {
		this.colorIdx = colorIdx;
	}

	public void setCurrX(int currX) {
		this.currX = currX;
	}

	public void setCurrY(int currY) {
		this.currY = currY;
	}
	
	/*************************************************************************************/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + colorIdx;
		result = prime * result + currX;
		result = prime * result + currY;
		result = prime * result + (king ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int compareTo(Piece o) {

		if (this.currY > o.currY) {
			return 1;
		} else if (this.currY < o.currY) {
			return -1;
		} else if (this.currX < o.currX) {
			return -1;
		} else if (this.currX > o.currX) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "Piece [king=" + king + ", currX=" + currX + ", currY=" + currY 
				+ ", color=" + color + "]";
	}

}
