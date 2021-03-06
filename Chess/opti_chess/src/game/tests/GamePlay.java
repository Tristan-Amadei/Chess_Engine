package game.tests;

import java.util.HashSet;

import game.Board;
import game.Move;
import game.MoveTypeClass.MoveType;
import game.pieces.*;
import game.pieces.PieceTypeClass.PieceType;

public class GamePlay extends Thread {

	public Board board;
	public int line;
	public int col;
	public int depth;
	public int nbOfMoves;

	public GamePlay(Board board, int line, int col, int depth, int nbOfMoves) {
		this.board = board;
		this.line = line;
		this.col = col;
		this.depth = depth;
		this.nbOfMoves = nbOfMoves;
	}

	@Override
	public void run() {
		if (board.board[line][col] != null && board.board[line][col].getColor() == board.colorToPlay) {
			HashSet<Short> moves = board.board[line][col].legalMoves(board, line, col);
			if (moves.size() > 0) {
				for (short move : moves) {
					try {
						board.board[line][col].movePiece(board, line, col, move / 10, move - 10 * (move / 10));
						nbOfMoves += numberOfMoves(board, depth - 1);
						board.unmakeMove();
					} catch (Exception e) {
						// System.out.println("depth: " + depth + " | " +
						// board.board[line][col].getType() + " : [" + line + ", " + col + "] -> [" +
						// move/10 + ", " + (move-10*(move/10)) + "]");
					}
				}
			}
		}
	}
	/*
	 * public void run() { if (depth == 0) { nbOfMoves += 1; } if
	 * (board.board[line][col] != null && board.board[line][col].getColor() ==
	 * board.colorToPlay) { HashSet<Short> moves =
	 * board.board[line][col].legalMoves(board, line, col); if (moves.size() > 0) {
	 * for (short move : moves) { try { board.board[line][col].movePiece(board,
	 * line, col, move/10, move-10*(move/10)); GamePlay new_thread = new
	 * GamePlay(board, move/10, move-10*(move/10), depth-1, 0); new_thread.start();
	 * nbOfMoves += new_thread.nbOfMoves; board.unmakeMove(); } catch (Exception e)
	 * { //System.out.println("depth: " + depth + " | " +
	 * board.board[line][col].getType() + " : [" + line + ", " + col + "] -> [" +
	 * move/10 + ", " + (move-10*(move/10)) + "]"); } } } } }
	 */

	public static long numberOfMoves(Board board, int depth) {
		long nbOfMoves = 0;
		if (depth == 0) {
			return 1;
		}
		for (int line = 0; line < 8; line++) {
			for (int col = 0; col < 8; col++) {
				if (board.board[line][col] != null && board.board[line][col].getColor() == board.colorToPlay) {
					HashSet<Short> moves = board.board[line][col].legalMoves(board, line, col);
					if (moves.size() > 0) {
						for (short move : moves) {
							try {
								board.board[line][col].movePiece(board, line, col, move / 10, move - 10 * (move / 10));
								nbOfMoves += numberOfMoves(board, depth - 1);
								board.unmakeMove();
							} catch (Exception e) {
								// System.out.println("depth: " + depth + " | " +
								// board.board[line][col].getType() + " : [" + line + ", " + col + "] -> [" +
								// move/10 + ", " + (move-10*(move/10)) + "]");
							}
						}
					}
				}
			}
		}
		return nbOfMoves;
	}

	/*
	 * public Piece[][] board; public boolean colorToPlay; public List<Move>
	 * movesPlayed; // list here, and note hashset bc we need to know what the moves
	 * contained are w/o knowing them by advance public List<Short> inGamePieces;
	 * public short whiteKingPosition; public short blackKingPosition;
	 */

	public static Board position3() {
		Board b = new Board();
		b.colorToPlay = true;

		b.board[3][0] = new King(true);
		((King) b.board[3][0]).hasMoved = true;
		b.inGamePieces.add((short) 30);
		b.board[3][1] = new Pawn(true);
		b.inGamePieces.add((short) 31);
		b.board[4][1] = new Rook(true);
		b.inGamePieces.add((short) 41);
		b.board[1][2] = new Pawn(false);
		b.inGamePieces.add((short) 12);
		b.board[2][3] = new Pawn(false);
		b.inGamePieces.add((short) 23);
		b.board[6][4] = new Pawn(true);
		b.inGamePieces.add((short) 64);
		b.board[4][5] = new Pawn(false);
		b.inGamePieces.add((short) 45);
		b.board[6][6] = new Pawn(true);
		b.inGamePieces.add((short) 66);
		b.board[3][7] = new Rook(false);
		b.inGamePieces.add((short) 37);
		b.board[4][7] = new King(false);
		((King) b.board[4][7]).hasMoved = true;
		b.inGamePieces.add((short) 47);

		Move move = new Move(PieceType.Pawn, false, (short) 35, (short) 45, null, MoveType.Normal);
		b.movesPlayed.add(move);

		b.whiteKingPosition = (short) 30;
		b.blackKingPosition = (short) 47;

		b.display();
		return b;
	}

	public static Board position5() {
		Board b = new Board();
		b.colorToPlay = true;

		b.board[0][0] = new Rook(false);
		b.inGamePieces.add((short) 00);
		b.board[0][1] = new Knight(false);
		b.inGamePieces.add((short) 01);
		b.board[0][2] = new Bishop(false);
		b.inGamePieces.add((short) 02);
		b.board[0][3] = new Queen(false);
		b.inGamePieces.add((short) 03);
		b.board[0][5] = new King(false);
		((King) b.board[0][5]).hasMoved = true;
		b.inGamePieces.add((short) 05);
		b.board[0][7] = new Rook(false);
		b.inGamePieces.add((short) 07);
		b.board[1][0] = new Pawn(false);
		b.inGamePieces.add((short) 10);
		b.board[1][1] = new Pawn(false);
		b.inGamePieces.add((short) 11);
		b.board[1][3] = new Pawn(true);
		b.inGamePieces.add((short) 13);
		b.board[1][4] = new Bishop(false);
		b.inGamePieces.add((short) 14);
		b.board[1][5] = new Pawn(false);
		b.inGamePieces.add((short) 15);
		b.board[1][6] = new Pawn(false);
		b.inGamePieces.add((short) 16);
		b.board[1][7] = new Pawn(false);
		b.inGamePieces.add((short) 17);
		b.board[2][2] = new Pawn(false);
		b.inGamePieces.add((short) 22);
		b.board[4][2] = new Bishop(true);
		b.inGamePieces.add((short) 42);
		b.board[6][0] = new Pawn(true);
		b.inGamePieces.add((short) 60);
		b.board[6][1] = new Pawn(true);
		b.inGamePieces.add((short) 61);
		b.board[6][2] = new Pawn(true);
		b.inGamePieces.add((short) 62);
		b.board[6][4] = new Knight(true);
		b.inGamePieces.add((short) 64);
		b.board[6][5] = new Knight(false);
		b.inGamePieces.add((short) 65);
		b.board[6][6] = new Pawn(true);
		b.inGamePieces.add((short) 66);
		b.board[6][7] = new Pawn(true);
		b.inGamePieces.add((short) 67);
		b.board[7][0] = new Rook(true);
		b.inGamePieces.add((short) 70);
		b.board[7][1] = new Knight(true);
		b.inGamePieces.add((short) 71);
		b.board[7][2] = new Bishop(true);
		b.inGamePieces.add((short) 72);
		b.board[7][3] = new Queen(true);
		b.inGamePieces.add((short) 73);
		b.board[7][4] = new King(true);
		b.inGamePieces.add((short) 74);
		b.board[7][7] = new Rook(true);
		b.inGamePieces.add((short) 77);

		Move move = new Move(PieceType.Pawn, false, (short) 12, (short) 22, null, MoveType.Normal);
		b.movesPlayed.add(move);

		b.whiteKingPosition = (short) 74;
		b.blackKingPosition = (short) 05;

		b.display();
		return b;
	}
}
