package game.pieces;

import java.util.HashSet;
import java.util.List;

import game.Board;
import game.Move;
import game.pieces.PieceTypeClass.PieceType;

public class Pawn extends Piece {

	public Pawn(final boolean color) {
		super(PieceType.Pawn, color);
	}

	public void display() {
		if (color) {
			System.out.print(" W Pawn ");
		} else {
			System.out.print(" B Pawn ");
		}
	}

	public HashSet<Short> possibleMoves(Board board, int line, int colonne) {
		HashSet<Short> moves = new HashSet<>();
		short new_coordinate;

		if (color) {
			if (line - 1 >= 0 && board.board[line - 1][colonne] == null) { // le pion peut avancer
				new_coordinate = (short) (10 * (line - 1) + colonne);
				moves.add(new_coordinate);
			}
			if (line - 1 >= 0 && colonne - 1 >= 0 && board.board[line - 1][colonne - 1] != null
					&& board.board[line - 1][colonne - 1].color != color) { // capture a gauche
				new_coordinate = (short) (10 * (line - 1) + colonne - 1);
				moves.add(new_coordinate);
			}
			if (line - 1 >= 0 && colonne + 1 < 8 && board.board[line - 1][colonne + 1] != null
					&& board.board[line - 1][colonne + 1].color != color) { // capture a  droite
				new_coordinate = (short) (10 * (line - 1) + colonne + 1);
				moves.add(new_coordinate);
			}
			if (line == 6 && board.board[line - 1][colonne] == null && board.board[line - 2][colonne] == null) { // pion
																													// a
																													// sa
																													// position
																													// de
																													// depart,
																													// peut
																													// avancer
																													// de
																													// 2
																													// cases
				new_coordinate = (short) (10 * (line - 2) + colonne);
				moves.add(new_coordinate);
			}
		} else {
			if (line + 1 < 8 && board.board[line + 1][colonne] == null) {
				new_coordinate = (short) (10 * (line + 1) + colonne);
				moves.add(new_coordinate);
			}
			if (line + 1 < 8 && colonne - 1 >= 0 && board.board[line + 1][colonne - 1] != null
					&& board.board[line + 1][colonne - 1].color != color) {
				new_coordinate = (short) (10 * (line + 1) + colonne - 1);
				moves.add(new_coordinate);
			}
			if (line + 1 < 8 && colonne + 1 < 8 && board.board[line + 1][colonne + 1] != null
					&& board.board[line + 1][colonne + 1].color != color) {
				new_coordinate = (short) (10 * (line + 1) + colonne + 1);
				moves.add(new_coordinate);
			}
			if (line == 1 && board.board[line + 1][colonne] == null && board.board[line + 2][colonne] == null) {
				new_coordinate = (short) (10 * (line + 2) + colonne);
				moves.add(new_coordinate);
			}
		}
		// HashSet<Short> movesEnPassant = enPassant(board, line, colonne);
		// moves.addAll(movesEnPassant);
		return moves;
	}

	public HashSet<Short> enPassant(Board board, int line, int colonne) {
		List<Move> movesPlayed = board.movesPlayed;
		Move lastMove;
		if (movesPlayed.size() == 0) {
			lastMove = null; // ne need to worry for the conditions further down won't be met hence the
								// method will just finish immediatly and return null
		} else {
			lastMove = movesPlayed.get(movesPlayed.size() - 1);
		}

		HashSet<Short> movesEP = new HashSet<>(); // movesEP = moves_En_Passant
		short new_coordinate;

		if (board.colorToPlay && line == 3) {
			if (lastMove.getMovedPiece() == PieceType.Pawn
					&& lastMove.getStartSquare() == (short) (10 * 1 + colonne - 1)
					&& lastMove.getLandingSquare() == (short) (10 * 3 + colonne - 1)) {
				new_coordinate = (short) (10 * (2) + colonne - 1);
				movesEP.add(new_coordinate);
			}
			if (lastMove.getMovedPiece() == PieceType.Pawn
					&& lastMove.getStartSquare() == (short) (10 * 1 + colonne + 1)
					&& lastMove.getLandingSquare() == (short) (10 * 3 + colonne + 1)) {
				new_coordinate = (short) (10 * (2) + colonne + 1);
				movesEP.add(new_coordinate);
			}
		}
		if (!board.colorToPlay && line == 4) {
			if (lastMove.getMovedPiece() == PieceType.Pawn
					&& lastMove.getStartSquare() == (short) (10 * 6 + colonne - 1)
					&& lastMove.getLandingSquare() == (short) (10 * 4 + colonne - 1)) {
				new_coordinate = (short) (10 * (5) + colonne - 1);
				movesEP.add(new_coordinate);
			}
			if (lastMove.getMovedPiece() == PieceType.Pawn
					&& lastMove.getStartSquare() == (short) (10 * 6 + colonne + 1)
					&& lastMove.getLandingSquare() == (short) (10 * 4 + colonne + 1)) {
				new_coordinate = (short) (10 * (5) + colonne + 1);
				movesEP.add(new_coordinate);
			}
		}
		return movesEP;
	}

	public HashSet<Short> legalMoves(Board board, int i, int j) {
		HashSet<Short> legal_moves = new HashSet<Short>();
		HashSet<Short> moves = board.board[i][j].possibleMoves(board, i, j);
		HashSet<Short> movesEP = ((Pawn) board.board[i][j]).enPassant(board, i, j);
		moves.addAll(movesEP);
		for (short square_move : moves) {
			try {
				if (!checkMoveIsIllegal(board, i, j, square_move / 10, square_move - 10 * (square_move / 10))) {
					legal_moves.add(square_move);
				}
			} catch (Exception e) {
				// nothing to do, bc the way we did it, no exception can actually be cuaght
			}
		}
		return legal_moves;
	}
}
