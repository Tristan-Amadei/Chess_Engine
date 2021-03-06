package game.opponent;

import java.security.*;
import java.util.List;

import game.Board;
import game.Move;
import game.pieces.Piece;

public class ZobristKey {

	long zArray[][][] = new long[2][6][64];
    long zEnPassant[] = new long[8];
    long zCastle[] = new long[4];
    long zBlackMove;
    SecureRandom random = new SecureRandom();
    
    public long random64() {
        return random.nextLong();
    }
    
    public void zobristFillArray() {
        for (int color = 0; color < 2; color++) {
            for (int pieceType = 0; pieceType < 6; pieceType++) {
                for (int square = 0; square < 64; square++) {
                    zArray[color][pieceType][square] = random64();
                }
            }
        }
        for (int column = 0; column < 8; column++) {
            zEnPassant[column] = random64();
        }
        for (int i = 0; i < 4; i++) {
            zCastle[i] = random64();
        }
        zBlackMove = random64();
    }
    
    public long getZobristHash(Board board) {
    	long returnZKey = 0;
    	int line;
    	int col;
    	for (Short piece_square : board.inGamePieces) {
    		line = piece_square/10;
    		col = piece_square-10*line;
    		int square = 7*line + col;
    		Piece piece = board.board[line][col];
    		switch(piece.getType()) {
    		case Pawn:
    			if (piece.getColor()) {
    				returnZKey ^= zArray[0][0][square];
    			} else {
    				returnZKey ^= zArray[1][0][square];
    			}
    			break;
    		case Knight:
    			if (piece.getColor()) {
    				returnZKey ^= zArray[0][1][square];
    			} else {
    				returnZKey ^= zArray[1][1][square];
    			}
    			break;
    		case Bishop:
    			if (piece.getColor()) {
    				returnZKey ^= zArray[0][2][square];
    			} else {
    				returnZKey ^= zArray[1][2][square];
    			}
    			break;
    		case Rook:
    			if (piece.getColor()) {
    				returnZKey ^= zArray[0][3][square];
    			} else {
    				returnZKey ^= zArray[1][3][square];
    			}
    			break;
    		case Queen:
    			if (piece.getColor()) {
    				returnZKey ^= zArray[0][4][square];
    			} else {
    				returnZKey ^= zArray[1][4][square];
    			}
    			break;
    		case King:
    			if (piece.getColor()) {
    				returnZKey ^= zArray[0][5][square];
    			} else {
    				returnZKey ^= zArray[1][5][square];
    			}
    			break;
    		}
    	}
    	
    	List<Move> moves = board.allLegalMoves();
		for (Move move : moves) {
			switch(move.getMoveType()) {
			case EnPassant:
				col = move.getLandingSquare() - 10*(move.getLandingSquare()/10);
				returnZKey ^= zEnPassant[col];
				break;
			case KingSideCastle:
				if (move.getColorOfPiece()) {
					returnZKey ^= zCastle[0];
				} else {
					returnZKey ^= zCastle[2];
				}
				break;
			case QueenSideCastle:
				if (move.getColorOfPiece()) {
					returnZKey ^= zCastle[1];
				} else {
					returnZKey ^= zCastle[3];
				}
				break;
			default: break;
			}
		}
		if (!board.colorToPlay) {
			returnZKey ^= zBlackMove;
		}
		return returnZKey;
    }
}
