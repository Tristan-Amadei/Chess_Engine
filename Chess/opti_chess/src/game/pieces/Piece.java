package game.pieces;

import java.util.HashSet;
import game.*;
import game.MoveTypeClass.MoveType;
import game.pieces.PieceTypeClass.PieceType;

public abstract class Piece {

	protected PieceType type;

	/**
	 * 1 corresponds to Black; 0 to White
	 */
	protected boolean color;

	public Piece(final PieceType type, final boolean color) {
		this.type = type;
		this.color = color;
	}

	public PieceType getType() {
		return type;
	}

	public boolean getColor() {
		return color;
	}

	public abstract void display();

	public abstract HashSet<Short> possibleMoves(Board board, int i, int j);

	public abstract HashSet<Short> legalMoves(Board board, int i, int j);

	public void movePiece(Board board, int src_line, int src_column, int dest_line, int dest_column)
			throws IllegalMoveException {
		if (checkMoveIsIllegal(board, src_line, src_column, dest_line, dest_column)) { // the move is not legal bc it
																						// lets the king be captured
			// System.out.println("The King is in check");
			throw new IllegalMoveException();
		}
		try {
			pawnPromotion_white(board, src_line, src_column, dest_line, dest_column);
		} catch (IllegalMoveException promotionWhiteIllegal) {
			try {
				pawnPromotion_black(board, src_line, src_column, dest_line, dest_column);
			} catch (IllegalMoveException promotionBlackIllegal) {
				try {
					this.normalMove(board, src_line, src_column, dest_line, dest_column);
				} catch (IllegalMoveException normalMoveIllegal) {
					try {
						this.enPassant(board, src_line, src_column, dest_line, dest_column);
					} catch (IllegalMoveException enPassantIllegal) {
						try {
							this.kingSideCastle(board, src_line, src_column, dest_line, dest_column);
						} catch (IllegalMoveException kingSideCastleIllegal) {
							try {
								this.queenSideCastle(board, src_line, src_column, dest_line, dest_column);
							} catch (IllegalMoveException queenSideCastleIllegal) {
								// System.out.println("The move " + this.type + ": [" + src_line + ", "
								// + src_column + "] -> [" + dest_line + ", " + dest_column + "] is illegal");
								throw new IllegalMoveException();
							}
						}
					}
				}
			}
		}
	}

	public void legalMovePiece(Board board, int src_line, int src_column, int dest_line, int dest_column)
			throws IllegalMoveException {
		// to move pieces of which the move put in parameters is already known to be
		// legal
		try {
			pawnPromotion_white(board, src_line, src_column, dest_line, dest_column);
		} catch (IllegalMoveException promotionWhiteIllegal) {
			try {
				pawnPromotion_black(board, src_line, src_column, dest_line, dest_column);
			} catch (IllegalMoveException promotionBlackIllegal) {
				try {
					this.normalMove(board, src_line, src_column, dest_line, dest_column);
				} catch (IllegalMoveException normalMoveIllegal) {
					try {
						this.enPassant(board, src_line, src_column, dest_line, dest_column);
					} catch (IllegalMoveException enPassantIllegal) {
						try {
							this.kingSideCastle(board, src_line, src_column, dest_line, dest_column);
						} catch (IllegalMoveException kingSideCastleIllegal) {
							try {
								this.queenSideCastle(board, src_line, src_column, dest_line, dest_column);
							} catch (IllegalMoveException queenSideCastleIllegal) {
								// System.out.println("The move " + this.type + ": [" + src_line + ", "
								// + src_column + "] -> [" + dest_line + ", " + dest_column + "] is illegal");
								throw new IllegalMoveException();
							}
						}
					}
				}
			}
		}
	}

	public void normalMove(Board board, int src_line, int src_column, int dest_line, int dest_column)
			throws IllegalMoveException {
		HashSet<Short> moves = this.possibleMoves(board, src_line, src_column);
		Object start_square = (short) (10 * src_line + src_column);
		short landing_square = (short) (10 * dest_line + dest_column);

		if (!moves.contains(landing_square) || color != board.colorToPlay) {
			throw new IllegalMoveException();
		}

		PieceType hypotheticalCapturedPieceType;
		if (board.board[dest_line][dest_column] == null) { // no piece was captured
			hypotheticalCapturedPieceType = null;
			board.inGamePieces.add(landing_square);
			board.inGamePieces.remove(start_square);
		} else {
			hypotheticalCapturedPieceType = board.board[dest_line][dest_column].type;
			// no need to add landing_square to the list for it is already in it, as a piece
			// there is getting captured
			board.inGamePieces.remove((Object) ((short) (10 * src_line + src_column)));
			// must force them as Object before removing them, otherwise Java thinks we want
			// to remove an index, not an object
		}
		Move move = new Move(type, color, (short) (10 * src_line + src_column), landing_square,
				hypotheticalCapturedPieceType, MoveType.Normal);
		board.movesPlayed.add(move);

		board.board[dest_line][dest_column] = this;
		board.board[src_line][src_column] = null;
		board.colorToPlay = !board.colorToPlay;

		if (type == PieceType.King) {
			((King) this).hasMoved = true;
			if (color) {
				board.whiteKingPosition = landing_square;
			} else {
				board.blackKingPosition = landing_square;
			}
		}
		if (type == PieceType.Rook) {
			((Rook) this).hasMoved = true;
		}
	}

	public void enPassant(Board board, int src_line, int src_column, int dest_line, int dest_column)
			throws IllegalMoveException {
		if (type != PieceType.Pawn) { // not a pawn, hence cannot execute this special move
			throw new IllegalMoveException();
		} else {
			HashSet<Short> movesEP = ((Pawn) this).enPassant(board, src_line, src_column);
			short landing_square = (short) (10 * dest_line + dest_column);

			if (!movesEP.contains(landing_square) || color != board.colorToPlay) {
				throw new IllegalMoveException();
			}

			board.board[dest_line][dest_column] = this;
			board.inGamePieces.add(landing_square);
			// delete the captured pawn from the board
			if (color) {
				board.board[dest_line + 1][dest_column] = null;
				board.inGamePieces.remove((Object) ((short) (10 * (dest_line + 1) + dest_column)));
			} else {
				board.board[dest_line - 1][dest_column] = null;
				board.inGamePieces.remove((Object) ((short) (10 * (dest_line - 1) + dest_column)));
			}
			board.board[src_line][src_column] = null;
			board.inGamePieces.remove((Object) ((short) (10 * src_line + src_column)));

			board.colorToPlay = !board.colorToPlay;
			Move move = new Move(type, color, (short) (10 * src_line + src_column), landing_square, PieceType.Pawn,
					MoveType.EnPassant);
			board.movesPlayed.add(move);

		}
	}

	public void kingSideCastle(Board board, int src_line, int src_column, int dest_line, int dest_column)
			throws IllegalMoveException {
		if (dest_column != src_column + 2) { // not a king side castle
			throw new IllegalMoveException();
		}
		if (type != PieceType.King || ((King) this).hasMoved) { // either the piece is not a King, or it has already
																// moved hence cannot castle
			throw new IllegalMoveException();
		}
		if (((King) this).isInCheck(board, src_line, src_column)) { // cannot castle if king's in check
			throw new IllegalMoveException();
		}
		if (board.board[src_line][src_column + 3].type != PieceType.Rook) { // no rook on the right corner (either up or
																			// down) of the board
			throw new IllegalMoveException();
		}
		Rook corner_rook = (Rook) board.board[src_line][src_column + 3];
		if (corner_rook.hasMoved) {
			throw new IllegalMoveException();
		}
		if (!((King) this).kingSideCastlePossible_squares(board, src_line, src_column)) { // some squares on the right
																							// of the king are
																							// controlled by the
																							// opponent
			throw new IllegalMoveException();
		}

		// finally, after all these tests, castling is possible
		// moving the king
		board.board[dest_line][dest_column] = this;
		board.inGamePieces.add((short) (10 * dest_line + dest_column));
		board.board[src_line][src_column] = null;
		board.inGamePieces.remove((Object) ((short) (10 * src_line + src_column)));
		// moving the rook
		board.board[src_line][src_column + 1] = corner_rook;
		board.inGamePieces.add((short) (10 * src_line + src_column + 1));
		board.board[src_line][src_column + 3] = null;
		board.inGamePieces.remove((Object) ((short) (10 * src_line + src_column + 3)));

		((King) board.board[dest_line][dest_column]).hasMoved = true;
		((Rook) board.board[src_line][src_column + 1]).hasMoved = true;

		if (color) {
			board.whiteKingPosition = (short) (10 * dest_line + dest_column);
		} else {
			board.blackKingPosition = (short) (10 * dest_line + dest_column);
		}

		board.colorToPlay = !board.colorToPlay;
		Move move = new Move(type, color, (short) (10 * src_line + src_column), (short) (10 * dest_line + dest_column),
				null, MoveType.KingSideCastle);
		board.movesPlayed.add(move);
	}

	public void queenSideCastle(Board board, int src_line, int src_column, int dest_line, int dest_column)
			throws IllegalMoveException {
		if (dest_column != src_column - 2) { // not a queen side castle
			throw new IllegalMoveException();
		}
		if (type != PieceType.King || ((King) this).hasMoved) { // either the piece is not a King, or it has already
																// moved hence cannot castle
			throw new IllegalMoveException();
		}
		if (((King) this).isInCheck(board, src_line, src_column)) { // cannot castle if king's in check
			throw new IllegalMoveException();
		}
		if (board.board[src_line][0].type != PieceType.Rook) { // no rook on the right corner (either up or down) of the
																// board
			throw new IllegalMoveException();
		}
		Rook corner_rook = (Rook) board.board[src_line][0];
		if (corner_rook.hasMoved) {
			throw new IllegalMoveException();
		}
		if (!((King) this).queenSideCastlePossible_squares(board, src_line, src_column)) { // some squares on the right
																							// of the king are
																							// controlled by the
																							// opponent
			throw new IllegalMoveException();
		}

		// finally, after all these tests, castling is possible
		// moving the king
		board.board[dest_line][dest_column] = this;
		board.inGamePieces.add((short) (10 * dest_line + dest_column));
		board.board[src_line][src_column] = null;
		board.inGamePieces.remove((Object) ((short) (10 * src_line + src_column)));
		// moving the rook
		board.board[src_line][src_column - 1] = corner_rook;
		board.inGamePieces.add((short) (10 * src_line + src_column - 1));
		board.board[src_line][0] = null;
		board.inGamePieces.remove((Object) ((short) (10 * src_line + 0)));

		((King) board.board[dest_line][dest_column]).hasMoved = true;
		((Rook) board.board[src_line][src_column - 1]).hasMoved = true;

		if (color) {
			board.whiteKingPosition = (short) (10 * dest_line + dest_column);
		} else {
			board.blackKingPosition = (short) (10 * dest_line + dest_column);
		}

		board.colorToPlay = !board.colorToPlay;
		Move move = new Move(type, color, (short) (10 * src_line + src_column), (short) (10 * dest_line + dest_column),
				null, MoveType.QueenSideCastle);
		board.movesPlayed.add(move);
	}

	public void pawnPromotion_white(Board board, int src_line, int src_column, int dest_line, int dest_column)
			throws IllegalMoveException {
		if (color == false || type != PieceType.Pawn || src_line != 1) {
			throw new IllegalMoveException();
		}

		HashSet<Short> moves = this.possibleMoves(board, src_line, src_column);
		short landing_square = (short) (10 * dest_line + dest_column);

		if (!moves.contains(landing_square) || color != board.colorToPlay) {
			throw new IllegalMoveException();
		}

		PieceType hypotheticalCapturedPieceType;
		if (board.board[dest_line][dest_column] == null) {
			hypotheticalCapturedPieceType = null;
			board.inGamePieces.add(landing_square);
			board.inGamePieces.remove((Object) ((short) (10 * src_line + src_column)));
		} else {
			hypotheticalCapturedPieceType = board.board[dest_line][dest_column].type;
			// no need to add landing_square to the list for it is already in it, as a piece
			// there is getting captured
			board.inGamePieces.remove((Object) ((short) (10 * src_line + src_column)));
		}
		Move move = new Move(type, color, (short) (10 * src_line + src_column), landing_square,
				hypotheticalCapturedPieceType, MoveType.Promotion);
		board.movesPlayed.add(move);

		board.board[dest_line][dest_column] = new Queen(color);
		board.board[src_line][src_column] = null;
		board.colorToPlay = !board.colorToPlay;
	}

	public void pawnPromotion_black(Board board, int src_line, int src_column, int dest_line, int dest_column)
			throws IllegalMoveException {
		if (color == true || type != PieceType.Pawn || src_line != 6) {
			throw new IllegalMoveException();
		}

		HashSet<Short> moves = this.possibleMoves(board, src_line, src_column);
		short landing_square = (short) (10 * dest_line + dest_column);

		if (!moves.contains(landing_square) || color != board.colorToPlay) {
			throw new IllegalMoveException();
		}

		PieceType hypotheticalCapturedPieceType;
		if (board.board[dest_line][dest_column] == null) {
			hypotheticalCapturedPieceType = null;
			board.inGamePieces.add(landing_square);
			board.inGamePieces.remove((Object) ((short) (10 * src_line + src_column)));
		} else {
			hypotheticalCapturedPieceType = board.board[dest_line][dest_column].type;
			// no need to add landing_square to the list for it is already in it, as a piece
			// there is getting captured
			board.inGamePieces.remove((Object) ((short) (10 * src_line + src_column)));
		}
		Move move = new Move(type, color, (short) (10 * src_line + src_column), landing_square,
				hypotheticalCapturedPieceType, MoveType.Promotion);
		board.movesPlayed.add(move);

		board.board[dest_line][dest_column] = new Queen(color);
		board.board[src_line][src_column] = null;
		board.colorToPlay = !board.colorToPlay;
	}

	public boolean checkMoveIsIllegal(Board board, int src_line, int src_column, int dest_line, int dest_column)
			throws IllegalMoveException {
		if (board.board[src_line][src_column].color != board.colorToPlay) {
			return true;
		}
		try {
			pawnPromotion_white(board, src_line, src_column, dest_line, dest_column);
		} catch (IllegalMoveException promotionWhiteIllegal) {
			try {
				pawnPromotion_black(board, src_line, src_column, dest_line, dest_column);
			} catch (IllegalMoveException promotionBlackIllegal) {
				try {
					this.normalMove(board, src_line, src_column, dest_line, dest_column);
				} catch (IllegalMoveException normalMoveIllegal) {
					try {
						this.enPassant(board, src_line, src_column, dest_line, dest_column);
					} catch (IllegalMoveException enPassantIllegal) {
						try {
							this.kingSideCastle(board, src_line, src_column, dest_line, dest_column);
						} catch (IllegalMoveException kingSideCastleIllegal) {
							try {
								this.queenSideCastle(board, src_line, src_column, dest_line, dest_column);
							} catch (IllegalMoveException queenSideCastleIllegal) {
								// System.out.println("The move " + this.type + ": [" + src_line + ", "
								// + src_column + "] -> [" + dest_line + ", " + dest_column + "] is illegal");
								throw new IllegalMoveException();
							}
						}
					}
				}
			}
		}
		short king_position = board.getKingPosition(!board.colorToPlay);
		int king_line = king_position / 10;
		int king_column = king_position - 10 * king_line;
		boolean king_is_in_check = ((King) board.board[king_line][king_column]).isInCheck(board, king_line,
				king_column);
		board.unmakeMove();
		return king_is_in_check;
	}

}
