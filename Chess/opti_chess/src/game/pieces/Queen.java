package game.pieces;

import java.util.HashSet;

import game.Board;
import game.pieces.PieceTypeClass.PieceType;

public class Queen extends Piece {

	public Queen(final boolean color) {
		super(PieceType.Queen, color);
	}

	public void display() {
		if (color) {
			System.out.print("W Queen ");
		} else {
			System.out.print("B Queen ");
		}
	}

	public HashSet<Short> possibleMoves(Board board, int ligne, int colonne) {
		Bishop fake_bishop = new Bishop(color);
		Rook fake_rook = new Rook(color);
		HashSet<Short> moves = fake_bishop.possibleMoves(board, ligne, colonne);
		moves.addAll(fake_rook.possibleMoves(board, ligne, colonne));

		fake_bishop = null;
		fake_rook = null;
		return moves;
	}

	public HashSet<Short> legalMoves(Board board, int i, int j) {
		HashSet<Short> legal_moves = new HashSet<Short>();
		HashSet<Short> moves = board.board[i][j].possibleMoves(board, i, j);
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
