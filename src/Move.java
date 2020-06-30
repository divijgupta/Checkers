
public class Move {

	private int x;
	private int y;
	private int newX;
	private int newY;
	private boolean upgrade;
	private Piece piece;
	private Piece removed;

	/**
	 * This is the Constructor for the Move Object (used for undo)
	 * 
	 * @param x	            This keeps track of the Piece's old X value
	 * @param y             This keeps track of the Piece's old Y value
	 * @param newX          This keeps track of the Piece's new X value
	 * @param newY          This keeps track of the Piece's new Y value
	 * @param upgrade       This keeps track of whether or not the Piece was upgraded to a King
	 * @param piece         This keeps track of the referential value to the Piece
	 * @param removed       This keeps track of the referential value to the piece remove
	 * 						(null if no Piece was removed)
	 */
	public Move(int x, int y, int newX, int newY, boolean upgrade, Piece piece, Piece removed) {
		super();
		this.x = x;
		this.y = y;
		this.newX = newX;
		this.newY = newY;
		this.upgrade = upgrade;
		this.piece = piece;
		this.removed = removed;
	}

	@Override
	public String toString() {
		return "Move [x=" + x + ", y=" + y + ", newX=" + newX + ", newY=" + newY +
				", upgrade=" + upgrade + ", piece=" + piece + ", removed=" + removed + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + newX;
		result = prime * result + newY;
		result = prime * result + ((piece == null) ? 0 : piece.hashCode());
		result = prime * result + ((removed == null) ? 0 : removed.hashCode());
		result = prime * result + (upgrade ? 1231 : 1237);
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (newX != other.newX)
			return false;
		if (newY != other.newY)
			return false;
		if (piece == null) {
			if (other.piece != null)
				return false;
		} else if (!piece.equals(other.piece))
			return false;
		if (removed == null) {
			if (other.removed != null)
				return false;
		} else if (!removed.equals(other.removed))
			return false;
		if (upgrade != other.upgrade)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	/***
	 * GETTERS
	 **********************************************************************************/

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getNewX() {
		return newX;
	}

	public int getNewY() {
		return newY;
	}

	public boolean upgradeHappened() {
		return upgrade;
	}

	public Piece getPiece() {
		return piece;
	}

	public Piece getRemoved() {
		return removed;
	}

	/***
	 * SETTERS
	 **********************************************************************************/

	public void setUpgrade(boolean upgrade) {
		this.upgrade = upgrade;
	}
	
	/************************************************************************************/

}
