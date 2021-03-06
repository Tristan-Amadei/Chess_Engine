package game.pieces;

import java.util.HashSet;

import game.Board;
import game.pieces.PieceTypeClass.PieceType;

public class Rook extends Piece {

	public boolean hasMoved;

	public Rook(final boolean color) {
		super(PieceType.Rook, color);
		this.hasMoved = false;
	}

	public void display() {
		if (color) {
			System.out.print(" W Rook ");
		} else {
			System.out.print(" B Rook ");
		}
	}

	public HashSet<Short> possibleMoves(Board board, int line, int colonne) {
		HashSet<Short> moves = new HashSet<>();
		short new_coordinate;

		int k = line - 1;
		while (k >= 0 && board.board[k][colonne] == null) { // on remonte selon la colonne
			new_coordinate = (short) (10 * k + colonne);
			moves.add(new_coordinate);
			k--;
		}
		if (k >= 0) { /*
						 * on s'est arrete parce qu'on a rencontre une case non vide, et non pas parce
						 * qu'on est arrive au bord de l'echiquier. on regarde donc si la case est
						 * occupee par une piece qu'on peut capturer ou non
						 */
			if (board.board[k][colonne].color != color) { // la piece est de couleur differente de la piece en parametre
															// donc on peut la capturer
				new_coordinate = (short) (10 * k + colonne);
				moves.add(new_coordinate);
			}
		}

		k = line + 1;
		while (k < 8 && board.board[k][colonne] == null) { // on descend selon la colonne
			new_coordinate = (short) (10 * k + colonne);
			moves.add(new_coordinate);
			k++;
		}
		if (k < 8) {
			if (board.board[k][colonne].color != color) {
				new_coordinate = (short) (10 * k + colonne);
				moves.add(new_coordinate);
			}
		}

		int l = colonne - 1;
		while (l >= 0 && board.board[line][l] == null) { // on va a gauche sur la line
			new_coordinate = (short) (10 * line + l);
			moves.add(new_coordinate);
			l--;
		}
		if (l >= 0) {
			if (board.board[line][l].color != color) {
				new_coordinate = (short) (10 * line + l);
				moves.add(new_coordinate);
			}
		}

		l = colonne + 1;
		while (l < 8 && board.board[line][l] == null) { // on va a droite sur la line
			new_coordinate = (short) (10 * line + l);
			moves.add(new_coordinate);
			l++;
		}
		if (l < 8) {
			if (board.board[line][l].color != color) {
				new_coordinate = (short) (10 * line + l);
				moves.add(new_coordinate);
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
