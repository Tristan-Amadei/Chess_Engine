package game.pieces;

import java.util.HashSet;

import game.Board;
import game.pieces.PieceTypeClass.PieceType;

public class Knight extends Piece {

	public Knight(final boolean color) {
		super(PieceType.Knight, color);
	}

	public void display() {
		if (color) {
			System.out.print("W Knight");
		} else {
			System.out.print("B Knight");
		}
	}

	public HashSet<Short> possibleMoves(Board board, int line, int colonne) {
		HashSet<Short> moves = new HashSet<>();
		short new_coordinate;

		int[][] positions_atteignables = { { line - 1, colonne - 2 }, { line - 2, colonne - 1 },
				{ line - 2, colonne + 1 }, { line - 1, colonne + 2 }, { line + 1, colonne + 2 },
				{ line + 2, colonne + 1 }, { line + 2, colonne - 1 }, { line + 1, colonne - 2 } };
		for (int k = 0; k < positions_atteignables.length; k++) {
			int i_atteignable = positions_atteignables[k][0];
			int j_atteignable = positions_atteignables[k][1];
			if (i_atteignable >= 0 && i_atteignable < 8 && j_atteignable >= 0 && j_atteignable < 8) { // donc si la case
																										// est bien sur
																										// l'echiquier
				if (board.board[i_atteignable][j_atteignable] == null
						|| board.board[i_atteignable][j_atteignable].color != color) {
					new_coordinate = (short) (10 * i_atteignable + j_atteignable);
					moves.add(new_coordinate);
				}
			}
		}
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
