package game.opponent;

import java.util.List;

import game.*;
import game.MoveTypeClass.MoveType;
import game.opponent.TranspositionTable.FLAG;
import game.opponent.TranspositionTableProbing.ProbingResult;

public class Engine {

	public Move bestMove;
	public int nb_positions_reached;

	public Engine() {
		this.bestMove = null;
		this.nb_positions_reached = 0;
	}

	public double alphaBetaMax(Board board, double alpha, double beta, int depth) {
		if (depth <= 0) {
			return Evaluation.overallEvaluation(board);
		}
		
		//Implementation of transposition Tables not working for the moment; working on it
		 long zKey_position = board.zKey.getZobristHash(board);
		if (board.table.moveTable.containsKey(zKey_position)) {
			TranspositionTableProbing probe = board.table.probeHash(board, depth, alpha, beta);
			if (probe.result == ProbingResult.OK) {
				return probe.score;
			}
		}

		List<Move> allMoves = board.allLegalMoves();
		Move.sortMoves(allMoves, board);
		for (Move move : allMoves) {
			nb_positions_reached++;
			move.playMove(board);
			zKey_position = board.zKey.getZobristHash(board);
			double score = this.alphaBetaMin(board, alpha, beta, depth - 1);
			board.unmakeMove();
			if (score >= beta) {
				board.table.addToTable(zKey_position, move, score, alpha, beta, depth, FLAG.HFBETA);
				return beta;
			}
			if (score > alpha) {
				alpha = score;
				board.table.addToTable(zKey_position, move, score, alpha, beta, depth, FLAG.HFEXACT);
			} else {
				board.table.addToTable(zKey_position, move, score, alpha, beta, depth, FLAG.HFALPHA);
			}
		}
		return alpha;
	}

	public double alphaBetaMin(Board board, double alpha, double beta, int depth) {
		if (depth <= 0) {
			return Evaluation.overallEvaluation(board);
		}
		
		//Implementation of transposition Tables not working for the moment; working on it
		long zKey_position = board.zKey.getZobristHash(board);
		if (board.table.moveTable.containsKey(zKey_position)) {
			TranspositionTableProbing probe = board.table.probeHash(board, depth, alpha, beta);
			if (probe.result == ProbingResult.OK) {
				return probe.score;
			}
		} 

		List<Move> allMoves = board.allLegalMoves();
		Move.sortMoves(allMoves, board);
		for (Move move : allMoves) {
			nb_positions_reached++;
			move.playMove(board);
			zKey_position = board.zKey.getZobristHash(board);
			double score = this.alphaBetaMax(board, alpha, beta, depth - 1);
			board.unmakeMove();
			if (score <= alpha) {
				board.table.addToTable(zKey_position, move, score, alpha, beta, depth, FLAG.HFALPHA);
				return alpha;
			}
			if (score < beta) {
				beta = score;
				board.table.addToTable(zKey_position, move, score, alpha, beta, depth, FLAG.HFEXACT);
			} else {
				board.table.addToTable(zKey_position, move, score, alpha, beta, depth, FLAG.HFBETA);
			}
		}
		return beta;
	}
	
	public static Engine returnBestMove_alphaBeta(Board board, int depth, boolean color, List<Move> allMoves) {
		Engine engine = new Engine();
		Move bestMove = allMoves.get(0);
		double score_max;
		if (color) {
			score_max = Double.NEGATIVE_INFINITY;
		} else {
			score_max = Double.POSITIVE_INFINITY;
		}

		for (Move move : allMoves) {
			move.playMove(board);
			if (color) {
				double score = engine.alphaBetaMin(board, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, depth);
				if (score >= score_max) {
					score_max = score;
					bestMove = move;
				}
			} else {
				double score = engine.alphaBetaMax(board, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, depth);
				if (score <= score_max) {
					score_max = score;
					bestMove = move;
				}
			}
			board.unmakeMove();
		}
		engine.bestMove = bestMove;
		return engine;
	}
	
	public static void playBestMove_alphaBeta(Board board, int depth, boolean color) {
		long t0 = System.currentTimeMillis();
		
		List<Move> allMoves = board.allLegalMoves();
		Move.sortMoves(allMoves, board);
		Engine engine = returnBestMove_alphaBeta(board, depth, color, allMoves);
		Move bestMove = engine.bestMove;

		bestMove.playMove(board);
		long t1 = System.currentTimeMillis();
		bestMove.display();
		if ((t1-t0)/1000 > 60) {
			System.out.println("Total time to find move at a depth of " + (depth + 1) + ": " + (t1 - t0) / (1000*60)
					+ " min, " + ((t1-t0)/1000 - 60*((t1 - t0) / (1000*60))) + " s. Went through " + engine.nb_positions_reached + " positions.");
		} else {
			System.out.println("Total time to find move at a depth of " + (depth + 1) + ": " + (t1 - t0) / 1000
					+ " s. Went through " + engine.nb_positions_reached + " positions.");
		}
	}
	
	public static double searchAllCaptures(Board board, double alpha, double beta) {
		double evaluation = Evaluation.overallEvaluation(board);
		if (evaluation >= beta) {
			return beta;
		}
		alpha = Double.max(alpha, evaluation);
		
		List<Move> allCaptures = board.allCaptureMoves();
		Move.sortMoves(allCaptures, board);
		
		for(Move capture : allCaptures) {
			capture.playMove(board);
			evaluation = -searchAllCaptures(board, -beta, -alpha);
			board.unmakeMove();
			
			if (evaluation >= beta) {
				return beta;
			}
			alpha = Double.max(alpha, evaluation);
		}
		return alpha;
	}
	
	public static void playBestMove_iterativeDeepening(Board board, int depth, boolean color) {
		long t0 = System.currentTimeMillis();
		
		Move bestMove = null;
		int nb_positions_reached = 0;
		Engine en = new Engine();
		
		List<Move> allMoves = board.allLegalMoves();
		Move.sortMoves(allMoves, board);
		for (int i = 1; i<=depth; i++) {
			en = returnBestMove_alphaBeta(board, i, color, allMoves);
			bestMove = en.bestMove;
			nb_positions_reached += en.nb_positions_reached;
			for (Move move : allMoves) {
				if (move.equalsTo(bestMove)) {
					move.moveType = MoveType.FoundByAlphaBeta;
					break;
				}
			}
			Move.sortMoves(allMoves, board);
		}
		
		bestMove.playMove(board);
		long t1 = System.currentTimeMillis();
		bestMove.display();
		if ((t1-t0)/1000 > 60) {
			System.out.println("Total time to find move at a depth of " + (depth + 1) + ": " + (t1 - t0) / (1000*60)
					+ " min, " + ((t1-t0)/1000 - 60*((t1 - t0) / (1000*60))) + " s. Went through " + nb_positions_reached + " positions.");
		} else {
			System.out.println("Total time to find move at a depth of " + (depth + 1) + ": " + (t1 - t0) / 1000
					+ " s. Went through " + nb_positions_reached + " positions.");
		}
	}

}
