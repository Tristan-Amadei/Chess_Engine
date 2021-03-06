package game.pieces;

import java.util.HashSet;

import game.*;
import game.pieces.PieceTypeClass.PieceType;

public class King extends Piece {

	public boolean hasMoved;

	public King(final boolean color) {
		super(PieceType.King, color);
		this.hasMoved = false;
	}

	public void display() {
		if (color) {
			System.out.print(" W King ");
		} else {
			System.out.print(" B King ");
		}
	}

	public boolean getHasMoved() {
		return hasMoved;
	}

	public HashSet<Short> possibleMoves(Board board, int line, int colonne) {
		HashSet<Short> moves = new HashSet<>();
		short new_coordinate;

		int[][] positions_atteignables = { { line - 1, colonne - 1 }, { line - 1, colonne }, { line - 1, colonne + 1 },
				{ line, colonne + 1 }, { line + 1, colonne + 1 }, { line + 1, colonne }, { line + 1, colonne - 1 },
				{ line, colonne - 1 } };
		for (int k = 0; k < positions_atteignables.length; k++) {

			int i_att = positions_atteignables[k][0];
			int j_att = positions_atteignables[k][1];

			if (i_att >= 0 && i_att < 8 && j_att >= 0 && j_att < 8) {
				if (board.board[i_att][j_att] == null) {
					new_coordinate = (short) (10 * i_att + j_att);
					moves.add(new_coordinate);
				} else if (board.board[i_att][j_att].color != color) {
					new_coordinate = (short) (10 * i_att + j_att);
					moves.add(new_coordinate);
				}
			}
		}
		return moves;
	}

	public boolean isInCheck(Board board, int line, int column) { // checks if the square of the King is reachable by an
																	// opponent's piece
		short king_square = (short) (10 * line + column);
		for (short i : board.inGamePieces) {
			// System.out.println("i: " + i + " -> [" + (i/10) + ", " + (i - (i/10)*10) +
			// "]");
			if (board.board[i / 10][i - (i / 10) * 10].color != color) { // enemy piece on this square
				HashSet<Short> opponentsMoves = board.board[i / 10][i - 10 * (i / 10)].possibleMoves(board, i / 10,
						i - 10 * (i / 10));
				if (opponentsMoves.contains(king_square)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean kingSideCastlePossible_squares(Board board, int line, int column) {
		if (board.board[line][column + 1] != null || board.board[line][column + 2] != null) {
			return false;
		}
		short square_right_from_king = (short) (10 * line + column + 1);
		short square_2_right_from_king = (short) (10 * line + column + 2);
		for (short i : board.inGamePieces) {
			if (board.board[i / 10][i - (i / 10) * 10].color != color) { // enemy piece on this square
				HashSet<Short> opponentsMoves = board.board[i / 10][i - (i / 10) * 10].possibleMoves(board, i / 10,
						i - (i / 10) * 10);
				if (opponentsMoves.contains(square_right_from_king)
						|| opponentsMoves.contains(square_2_right_from_king)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean queenSideCastlePossible_squares(Board board, int line, int column) {
		if (board.board[line][column - 1] != null || board.board[line][column - 2] != null
				|| board.board[line][column - 3] != null) {
			return false;
		}
		short square_left_from_king = (short) (10 * line + column + 1);
		short square_2_left_from_king = (short) (10 * line + column + 2);
		for (short i : board.inGamePieces) {
			if (board.board[i / 10][i - (i / 10) * 10].color != color) { // enemy piece on this square
				HashSet<Short> opponentsMoves = board.board[i / 10][i - (i / 10) * 10].possibleMoves(board, i / 10,
						i - (i / 10) * 10);
				if (opponentsMoves.contains(square_left_from_king)
						|| opponentsMoves.contains(square_2_left_from_king)) {
					return false;
				}
			}
		}
		return true;
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

		if (board.board[i][j].color && board.whiteKingPosition == ((short) 74)) {
			try {
				boolean king_side_illegal = board.board[i][j].checkMoveIsIllegal(board, i, j, 7, 6);
				if (!king_side_illegal) {
					legal_moves.add((short) 76);
				}
			} catch (Exception e) {

			}
			try {
				boolean queen_side_illegal = board.board[i][j].checkMoveIsIllegal(board, i, j, 7, 2);
				if (!queen_side_illegal) {
					legal_moves.add((short) 72);
				}
			} catch (Exception e) {

			}
		} else if (board.board[i][j].color == false && board.blackKingPosition == ((short) 04)) {
			try {
				boolean king_side_illegal = board.board[i][j].checkMoveIsIllegal(board, i, j, 0, 6);
				if (!king_side_illegal) {
					legal_moves.add((short) 06);
				}
			} catch (Exception e) {

			}
			try {
				boolean queen_side_illegal = board.board[i][j].checkMoveIsIllegal(board, i, j, 0, 2);
				if (!queen_side_illegal) {
					legal_moves.add((short) 02);
				}
			} catch (Exception e) {

			}
		}
		return legal_moves;
	}

}
