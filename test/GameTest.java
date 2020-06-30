import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import javax.swing.JLabel;

import org.junit.Test;


public class GameTest {

	@Test
	public void regularMovementRed() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 1, 2, Color.RED);
		game.move(1, 2, 1, 1, true);

		// makes sure that piece that moves is no longer in the same spot
		assertTrue(game.getPiece(1, 2) == null);

		assertEquals(game.getPiece(2, 3).getColor(), Color.RED);
	}

	@Test
	public void regularMovementBlack() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 1, 2, Color.RED);
		game.addPiece(false, 2, 2, 5, Color.BLACK);

		game.move(1, 2, 1, 1, true);
		game.move(2, 5, -1, -1, false);

		// makes sure that piece that moves is no longer in the same spot
		assertTrue(game.getPiece(2, 5) == null);
		assertEquals(game.getPiece(1, 4).getColor(), Color.BLACK);
	}

	@Test
	public void canOnlyMovePieces() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 1, 2, Color.RED);
		game.addPiece(false, 2, 2, 5, Color.BLACK);

		game.move(2, 2, 1, 1, true);
		game.move(2, 4, -1, -1, false);

		// makes sure that pieces didn't move
		assertEquals(game.getPiece(1, 2).getColor(), Color.RED);
		assertEquals(game.getPiece(2, 5).getColor(), Color.BLACK);
	}

	@Test
	public void redCantMoveBlack() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 1, 2, Color.RED);
		game.addPiece(false, 2, 2, 5, Color.BLACK);

		// red tries moving black (doesn't work)
		game.move(2, 5, 1, -1, true);

		// makes sure that pieces didn't move
		assertEquals(game.getPiece(1, 2).getColor(), Color.RED);
		assertEquals(game.getPiece(2, 5).getColor(), Color.BLACK);
	}

	@Test
	public void blackCantMoveRed() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 1, 2, Color.RED);
		game.addPiece(false, 2, 2, 5, Color.BLACK);

		// red moves red (works)
		game.move(1, 2, 1, 1, true);

		// black tries moving red (doesn't work)
		game.move(2, 3, 1, 1, false);

		// makes sure that pieces are in right spot
		assertEquals(game.getPiece(2, 3).getColor(), Color.RED);
		assertEquals(game.getPiece(2, 5).getColor(), Color.BLACK);
	}

	@Test
	public void redPieceJumpsBlack() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 1, 2, Color.RED);
		game.addPiece(false, 2, 4, 5, Color.BLACK);
		game.move(1, 2, 1, 1, true);
		game.move(4, 5, -1, -1, false);
		game.move(2, 3, 1, 1, true);
		// tests that reds piece moves to the right spot
		assertEquals(game.getPiece(4, 5).getColor(), Color.RED);
		// tests that black piece was deleted
		assertTrue(game.getPiece(3, 4) == null);
		// makes sure that the piece that jumps is no longer in the same spot
		assertTrue(game.getPiece(2, 3) == null);
	}

	@Test
	public void blackPieceJumpsRed() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 1, 2, Color.RED);
		game.addPiece(false, 2, 2, 5, Color.BLACK);
		game.addPiece(false, 1, 3, 2, Color.RED);
		game.move(1, 2, 1, 1, true);
		game.move(4, 5, -1, -1, false);
		game.move(3, 2, 1, 1, true);
		game.move(3, 4, -1, -1, false);
		// tests that black piece moves to the right spot
		assertEquals(game.getPiece(1, 2).getColor(), Color.BLACK);
		// tests that red piece was deleted
		assertTrue(game.getPiece(2, 3) == null);
		// makes sure piece that jumps is no longer in the same spot
		assertTrue(game.getPiece(3, 4) == null);
	}

	@Test
	public void pieceStaysWithinLeftBoundary() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 0, 1, Color.RED);
		game.move(0, 1, -1, 1, true);
		assertEquals(game.getPiece(0, 1).getColor(), Color.RED);
	}

	@Test
	public void pieceStaysWithinRightBoundary() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 7, 2, Color.RED);
		game.move(7, 2, 1, 1, true);
		assertEquals(game.getPiece(7, 2).getColor(), Color.RED);
	}

	@Test
	public void pieceStaysWithinBottomBoundary() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 2, 7, Color.RED);
		game.move(2, 7, 1, 1, true);
		assertEquals(game.getPiece(2, 7).getColor(), Color.RED);
	}

	@Test
	public void pieceStaysWithinTopBoundary() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 4, 5, Color.RED);
		game.addPiece(false, 2, 3, 0, Color.BLACK);
		game.move(4, 5, 1, 1, true);
		game.move(3, 0, 1, -1, true);
		assertEquals(game.getPiece(3, 0).getColor(), Color.BLACK);

	}

	@Test
	public void nonKingRedCantMoveBackwards() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 1, 2, Color.RED);
		game.addPiece(false, 2, 2, 5, Color.BLACK);
		game.move(1, 2, 1, 1, true);
		game.move(2, 5, 1, -1, false);
		game.move(2, 3, -1, -1, true);

		// piece is still in new spot
		assertEquals(game.getPiece(2, 3).getColor(), Color.RED);

		// old spot is still empty
		assertTrue(game.getPiece(1, 2) == null);
	}

	@Test
	public void nonKingBlackCantMoveBackwards() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 1, 2, Color.RED);
		game.addPiece(false, 2, 2, 5, Color.BLACK);
		game.move(1, 2, 1, 1, true);
		game.move(2, 5, 1, -1, false);
		game.move(2, 3, 1, 1, true);
		game.move(3, 4, -1, 1, false);

		// piece is still in new spot
		assertEquals(game.getPiece(3, 4).getColor(), Color.BLACK);

		// old spot is still empty
		assertTrue(game.getPiece(2, 5) == null);
	}

	@Test
	public void blackPieceCantJumpIfPieceBehindExists() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 1, 2, Color.RED);
		game.addPiece(false, 2, 2, 5, Color.BLACK);
		game.addPiece(false, 1, 0, 1, Color.RED);
		game.move(1, 2, 1, 1, true);
		game.move(4, 5, -1, -1, false);
		game.move(0, 1, 1, 1, true);
		game.move(3, 4, -1, -1, false);
		// tests that no pieces were deleted
		assertEquals(game.getPiece(3, 4).getColor(), Color.BLACK);
		assertEquals(game.getPiece(2, 3).getColor(), Color.RED);
		assertEquals(game.getPiece(1, 2).getColor(), Color.RED);
	}

	@Test
	public void RedPieceCantJumpIfPieceBehindExists() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 1, 2, Color.RED);
		game.addPiece(false, 2, 2, 3, Color.BLACK);
		game.addPiece(false, 2, 3, 4, Color.BLACK);
		game.move(1, 2, 1, 1, true);
		// tests that no pieces were deleted
		assertEquals(game.getPiece(3, 4).getColor(), Color.BLACK);
		assertEquals(game.getPiece(2, 3).getColor(), Color.BLACK);
		assertEquals(game.getPiece(1, 2).getColor(), Color.RED);
	}

	@Test
	public void blackPieceCantJumpIfCorner() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 6, 3, Color.RED);
		game.addPiece(false, 2, 6, 5, Color.BLACK);
		game.move(6, 3, 1, 1, true);
		game.move(6, 5, 1, -1, false);

		// tests that no pieces were deleted
		assertEquals(game.getPiece(6, 5).getColor(), Color.BLACK);
		assertEquals(game.getPiece(7, 4).getColor(), Color.RED);
	}

	@Test
	public void redPieceCantJumpIfCorner() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 6, 3, Color.RED);
		game.addPiece(false, 2, 7, 4, Color.BLACK);
		game.move(6, 3, 1, 1, true);

		// tests that no pieces were deleted
		assertEquals(game.getPiece(6, 3).getColor(), Color.RED);
		assertEquals(game.getPiece(7, 4).getColor(), Color.BLACK);
	}

	@Test
	public void redTurnsIntoKingAtEnd() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		
		game.clear();

		// add the piece to the game
		game.addPiece(false, 1, 3, 6, Color.RED);
		game.addPiece(false, 2, 6, 5, Color.BLACK);

		// piece is not king before movement
		assertFalse(game.getPiece(3, 6).isKing());

		game.move(3, 6, 1, 1, true);

		//piece is king after movement
		assertTrue(game.getPiece(4, 7).isKing());

	}

	// check this one
	@Test
	public void redKingCanMoveBackwards() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.clear();
		game.addPiece(true, 1, 1, 2, Color.RED);
		game.move(1, 2, -1, -1, true);

		// Red King able to move backwards
		assertEquals(game.getPiece(0, 1).getColor(), Color.RED);

		// old spot is now null
		assertTrue(game.getPiece(1, 2) == null);

	}

	@Test
	public void blackKingCanMoveBackwards() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.clear();
		game.addPiece(false, 1, 1, 2, Color.RED);
		game.addPiece(true, 1, 5, 5, Color.BLACK);
		game.move(1, 2, 1, 1, true);
		game.move(5, 5, 1, 1, false);

		// Black King able to move backwards
		assertEquals(game.getPiece(6, 6).getColor(), Color.BLACK);

		// old spot is now null
		assertTrue(game.getPiece(5, 5) == null);

	}

	@Test
	public void blackTurnsIntoKingAtEnd() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		// clear board
		game.clear();
		game.addPiece(false, 1, 5, 2, Color.RED);
		game.addPiece(false, 2, 2, 1, Color.BLACK);
		Piece p = game.getPiece(2, 1);

		// piece wasn't a king before
		assertFalse(p.isKing());

		game.move(5, 2, 1, 1, true);
		game.move(2, 1, -1, -1, false);

		// piece is a King now
		assertTrue(p.isKing());

	}

	@Test
	public void redUndo() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 1, 2, Color.RED);
		game.move(1, 2, 1, 1, true);
		game.undo();

		// makes sure that piece that moves is no longer in the same spot
		assertTrue(game.getPiece(2, 3) == null);

		assertEquals(game.getPiece(1, 2).getColor(), Color.RED);
	}

	@Test
	public void blackUndo() {
		final JLabel status = new JLabel("Red's Turn");
		CheckerBoard game = new CheckerBoard(status);
		game.addPiece(false, 1, 1, 2, Color.RED);
		game.addPiece(false, 2, 2, 5, Color.BLACK);

		// perform the moves
		game.move(1, 2, 1, 1, true);
		game.move(2, 5, -1, -1, false);

		// undo the most recent move
		game.undo();

		// makes sure that piece that moves is no longer in the same spot
		assertTrue(game.getPiece(1, 4) == null);
		assertEquals(game.getPiece(2, 5).getColor(), Color.BLACK);
	}

}
