package game.tests;

import game.*;
import game.opponent.Engine;

public class RunningTests {

	public static void main(String[] args) {
		/*
		 * int depth_thread = 4; List<Board> boards = new ArrayList<>();
		 * 
		 * Board board1 = new Board(); board1.newGameBoard(); Board board2 = new
		 * Board(); board1.newGameBoard(); Board board3 = new Board();
		 * board1.newGameBoard(); Board board4 = new Board(); board1.newGameBoard();
		 * Board board5 = new Board(); board1.newGameBoard(); Board board6 = new
		 * Board(); board1.newGameBoard(); Board board7 = new Board();
		 * board1.newGameBoard(); Board board8 = new Board(); board1.newGameBoard();
		 * Board board9 = new Board(); board1.newGameBoard(); Board board10 = new
		 * Board(); board1.newGameBoard(); Board board11 = new Board();
		 * board1.newGameBoard(); Board board12 = new Board(); board1.newGameBoard();
		 * Board board13 = new Board(); board1.newGameBoard(); Board board14 = new
		 * Board(); board1.newGameBoard(); Board board15 = new Board();
		 * board1.newGameBoard(); Board board16 = new Board(); board1.newGameBoard();
		 * boards.add(board1); boards.add(board2); boards.add(board3);
		 * boards.add(board4); boards.add(board5); boards.add(board6);
		 * boards.add(board7); boards.add(board8); boards.add(board9);
		 * boards.add(board10); boards.add(board11); boards.add(board12);
		 * boards.add(board13); boards.add(board14); boards.add(board15);
		 * boards.add(board16);
		 * 
		 * long t0 = System.currentTimeMillis(); int final_nbMoves = 0; int i=0; for
		 * (int line=6; line<8; line++) { for (int col=0; col<8; col++) {
		 * 
		 * GamePlay new_thread = new GamePlay(boards.get(i), line, col, depth_thread,
		 * 0); i++; new_thread.start(); final_nbMoves += new_thread.nbOfMoves; } } long
		 * t1 = System.currentTimeMillis(); System.out.println("Total time: " + (t1-t0)
		 * + " ms for depth " + depth_thread);
		 * System.out.println("Number of moves for depth " + depth_thread + ": " +
		 * final_nbMoves + "\n");
		 */

		/*
		 * for (int depth = 1; depth<10; depth++) { long t0 =
		 * System.currentTimeMillis();
		 * 
		 * Board testBoard = new Board(); testBoard.newGameBoard(); long nbOfMoves =
		 * GamePlay.numberOfMoves(testBoard, depth); long t1 =
		 * System.currentTimeMillis(); System.out.println("Total time: " + (t1-t0) +
		 * " ms for depth " + depth); System.out.println("Number of moves for depth " +
		 * depth + ": " + nbOfMoves + "\n"); }
		 */

		Board board = new Board();
		board.newGameBoard();
		int depth = 6;

		try {
			board.board[6][4].movePiece(board, 6, 4, 4, 4);
			board.board[1][4].movePiece(board, 1, 4, 3, 4);
			board.board[7][6].movePiece(board, 7, 6, 5, 5);
			board.board[0][1].movePiece(board, 0, 1, 2, 2);
			board.board[7][5].movePiece(board, 7, 5, 4, 2);
			board.board[0][5].movePiece(board, 0, 5, 3, 2);
			long t0 = System.currentTimeMillis();
			long nbOfMoves = GamePlay.numberOfMoves(board, depth);
			long t1 = System.currentTimeMillis();
			System.out.println("Total time: " + (t1 - t0) + " ms for depth " + depth);
			System.out.println("Number of moves for depth " + depth + ": " + nbOfMoves + "\n");
			Engine.playBestMove_alphaBeta(board, 5, board.colorToPlay);
		} catch (Exception e) {

		}
		board.display();

	}

}
