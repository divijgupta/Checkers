=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
Checkers Project README
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

  1. 2D arrays - I created a 2D array of Type Piece (a class that I created). If the array is empty
  	 at that spot, the piece will be null. Otherwise, the Piece will keep track of the Color, 
  	 whether or not it is a King, and the location. This is an appropriate use of the concept 
  	 because a CheckerBoard is visually a 2D array and it easy to implement the repaint method 
  	 of it.

  2. Collections - I used a LinkedList of type Move (a class that I created). This is necessary 
  	because it can allow me to store the moves in order as the List would always add at the tail.
  	If the player clicks Undo, I can remove from the tail, allowing the pieces to go back where they
  	were in the previous move.

  3. JUnit - I used JUnit testing of the Board Class because it is the the part that keeps track of
  	core state of the game. I tested various scenarios including whether or not the pieces moved 
  	properly, were jumped properly, when pieces become Kings, and various edge cases. This was
  	appropriate and very useful as it allowed me to pinpoint errors efficiently.
  
  4. I/O - I used I/O to allow the User to save the game into a text file, allowing the user to
  	store the current state of the game. If the user presses save on the game, it overwrites any
  	previous storage and if the user presses load, it loads the previously stored game. I took your
  	advice and made disabled the user from undo'ing if he loads the game state. However, if he or she
  	makes moves after loading the game, it is possible to undo until the point the game was loaded. If
  	the user makes moves and decides to load a game after, you cannot press undo to get to the
  	previous state either.


=========================
=: My Implementation :=
=========================

  	I had 4 main classes, CheckerBoard, Game, Move, and Piece.

	1) CheckerBoard - This class track of the core state of the game. It keeps track of the 2D array
	of the Board as well as moves, whether or not a piece was removed, whether or not a piece turned
	into a King, and other functions related to the Board. It also allowed for the undo function,
	the reset function, the save function, and the reload function.
	
	2) Game - This class used a lot of the code in the started code Class game. It just utilizes
	java swing to allow the User see and interact with the game. It does this through various
	buttons, a label, and the game screen. Additionally, it also creates the instruction pop up in
	the beginning, as well as creating error pop ups for undo'ing when not possible and loading a
	file that doesn't exist.
	
	3) Move - This class creates an object of a specific move. It keeps track of the old position of 
	a piece, the new position of the piece, whether or not the piece was upgraded, the actual piece,
	and whether or not if a piece was removed (null if none and the reference to the piece if it
	was). This class was used as the type of the LinkedList that kept track of the moves throughout
	the game. When a User pressed Undo, the latest move from the LinkedList is removed and then 
	the pieces go back to where they were prior to the move.
	
	4) Piece - This class creates an object of type piece. This was probably my most useful class as
	it kept track of whether or not a Piece was a King, its Color, and its current coordinates. It
	was used as the type of the 2D array of the Grid and was set equal to null if the space was
	empty. It also had its own draw method that I called when I drew the board. 





  
