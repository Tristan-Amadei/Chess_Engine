package game;

import game.pieces.PieceTypeClass.PieceType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import game.MoveTypeClass.MoveType;
import game.opponent.Evaluation;
import game.pieces.*;

public class Move {

	private PieceType movedPiece;
	private PieceType capturedPiece;
	private boolean colorOfPiece;
	private short startSquare;
	private short landingSquare;
	public MoveType moveType;

	public Move(final PieceType movedPiece, final boolean colorOfPiece, final short startSquare,
			final short landingSquare, final PieceType capturedPiece, final MoveType moveType) {
		this.movedPiece = movedPiece;
		this.colorOfPiece = colorOfPiece;
		this.startSquare = startSquare;
		this.landingSquare = landingSquare;
		this.capturedPiece = capturedPiece;
		this.moveType = moveType;
	}

	public PieceType getMovedPiece() {
		return movedPiece;
	}

	public boolean getColorOfPiece() {
		return colorOfPiece;
	}

	public short getStartSquare() {
		return startSquare;
	}

	public short getLandingSquare() {
		return landingSquare;
	}

	public PieceType getCapturedPiece() {
		return capturedPiece;
	}

	public MoveType getMoveType() {
		return moveType;
	}

	public void display() {
		System.out.println(movedPiece + ": [" + startSquare / 10 + ", " + (startSquare - 10 * (startSquare / 10))
				+ "] -> [" + landingSquare / 10 + ", " + (landingSquare - 10 * (landingSquare / 10)) + "], moveType: " + moveType);
	}
	
	public static List<Short> MiddleBoardSquares() {
		List<Short> middleSquares = new ArrayList<Short>();
		middleSquares.add((short)22);
		middleSquares.add((short)23);
		middleSquares.add((short)24);
		middleSquares.add((short)25);
		middleSquares.add((short)32);
		middleSquares.add((short)33);
		middleSquares.add((short)34);
		middleSquares.add((short)35);
		middleSquares.add((short)42);
		middleSquares.add((short)43);
		middleSquares.add((short)44);
		middleSquares.add((short)45);
		middleSquares.add((short)52);
		middleSquares.add((short)53);
		middleSquares.add((short)54);
		middleSquares.add((short)55);
		
		return middleSquares;
	}
	
	public static List<Short> EdgesBoardSquares() {
		List<Short> edgeSquares = new ArrayList<Short>();
		edgeSquares.add((short)00);
		edgeSquares.add((short)10);
		edgeSquares.add((short)20);
		edgeSquares.add((short)30);
		edgeSquares.add((short)40);
		edgeSquares.add((short)50);
		edgeSquares.add((short)60);
		edgeSquares.add((short)70);
		edgeSquares.add((short)07);
		edgeSquares.add((short)17);
		edgeSquares.add((short)27);
		edgeSquares.add((short)37);
		edgeSquares.add((short)47);
		edgeSquares.add((short)57);
		edgeSquares.add((short)67);
		edgeSquares.add((short)77);
		
		return edgeSquares;
	}

	public void playMove(Board board) {
		int start_line = startSquare / 10;
		int start_col = startSquare - 10 * start_line;
		int land_line = landingSquare / 10;
		int land_col = landingSquare - 10 * land_line;

		try {
			board.board[start_line][start_col].legalMovePiece(board, start_line, start_col, land_line, land_col);
		} catch (Exception e) {
			System.out.println("Move impossible: " + this.movedPiece + " [" + start_line + ", " + start_col + "] -> ["
					+ land_line + ", " + land_col + "]\n");
		}
	}
	
	public boolean moveProvokesCheck(Board board) {
		this.playMove(board);
		short king = board.getKingPosition(board.colorToPlay);
		boolean kingInCheck = ((King) board.board[king/10][king - 10*(king/10)]).isInCheck(board, king/10, king - 10*(king/10));
		board.unmakeMove();
		return kingInCheck;
	}
	
	public Move returnMoveFromHashSet(Board board, PieceType type, boolean color, short piece_square, short move_square) {
		
		if (type == PieceType.King) {
			if (piece_square - move_square == 2) {
				return new Move(type, color, piece_square, move_square, null, MoveType.KingSideCastle);
			}
			if (piece_square - move_square == -2) {
				return new Move(type, color, piece_square, move_square, null, MoveType.QueenSideCastle);
			} 
		}
		
		if (type == PieceType.Pawn) {
			if (move_square == 0 || move_square == 7)  {
				if (board.inGamePieces.contains((Object) move_square)) {
					return new Move(type, color, piece_square, move_square, 
							board.board[move_square/10][move_square-10*(move_square/10)].getType(), MoveType.Promotion);
				}
				return new Move(type, color, piece_square, move_square, null, MoveType.Promotion);
			}
		}
		
		if (this.moveProvokesCheck(board)) {
			if (board.inGamePieces.contains((Object) move_square)) {
				return new Move(type, color, piece_square, move_square, 
						board.board[move_square/10][move_square-10*(move_square/10)].getType(), MoveType.Check);
			}
			return new Move(type, color, piece_square, move_square, null, MoveType.Check);
		}
		
		if (board.inGamePieces.contains((Object) move_square)) {
			return new Move(type, color, piece_square, move_square, 
					board.board[move_square/10][move_square-10*(move_square/10)].getType(), MoveType.Capture);
		} 
		return new Move(type, color, piece_square, move_square, null, MoveType.Normal);
	}
	
	public boolean pieceToCaptureIsProtected(Board board) { 
		if (capturedPiece == null) {
			return true;
		}
		HashSet<Short> moveSquaresOfOpponent = board.allPossibleMoveSquares(!board.colorToPlay);
		return moveSquaresOfOpponent.contains((Object)landingSquare);
	}
	
	public double score(Board board) {
		switch(moveType) {
		case FoundByAlphaBeta:
			return 1000;
		case KingSideCastle:
			return 200;
		case QueenSideCastle:
			return 190;
		case Capture:
			if (this.pieceToCaptureIsProtected(board)) {
				return (Evaluation.pieceTypeEvaluation(capturedPiece) - Evaluation.pieceTypeEvaluation(movedPiece)) + 9.1;
				//+9.1 so that capture equal trades are still sorted before standard moves
			}
			return Evaluation.pieceTypeEvaluation(capturedPiece);
			
		case Promotion:
			return 700;
		case Check:
			return 210;
		default:
			if (MiddleBoardSquares().contains((Object)landingSquare)) {
				if (movedPiece == PieceType.Pawn) {
					return 0;
				}
				return Evaluation.pieceTypeEvaluation(movedPiece)/100;
			}
			if (EdgesBoardSquares().contains((Object)landingSquare)) {
				if (movedPiece == PieceType.Pawn) {
					return 0;
				}
				return -Evaluation.pieceTypeEvaluation(movedPiece)/100;
			}
			return 0;
		}
	}
	
	public static void sortMoves(List<Move> moves, Board board) {
		Collections.sort(moves, new Comparator<Move>() {

			@Override
			public int compare(Move m1, Move m2) {
				return Double.compare(m2.score(board), m1.score(board));
			}
		});
	}
	
	public boolean equalsTo(Move otherMove) {
		return (movedPiece == otherMove.movedPiece) && (colorOfPiece == otherMove.colorOfPiece) && (startSquare == otherMove.startSquare) &&
				(landingSquare == otherMove.landingSquare);
	}
}
