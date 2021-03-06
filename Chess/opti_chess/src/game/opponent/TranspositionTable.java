package game.opponent;

import java.util.*;

import game.*;
import game.opponent.TranspositionTableProbing.ProbingResult;

public class TranspositionTable {
	
	public enum FLAG {
		HFNONE, 
		HFALPHA, 
		HFBETA, 
		HFEXACT
	}
	
	public Hashtable<Long, Move> moveTable;
	public Hashtable<Long, Double> evalTable;
	public Hashtable<Long, Double> alphaTable;
	public Hashtable<Long, Double> betaTable;
	public Hashtable<Long, Integer> depthTable;
	public Hashtable<Long, FLAG> flagTable; 
	
	public TranspositionTable() {
		moveTable = new Hashtable<>();
		evalTable = new Hashtable<>();
		alphaTable = new Hashtable<>();
		betaTable = new Hashtable<>();
		depthTable = new Hashtable<>();
		flagTable = new Hashtable<>();		
	}
	
	public void addToTable(long ZobristKey, Move move, Double eval, Double alpha, Double beta, int depth, FLAG flag) {
		moveTable.put(ZobristKey, move);
		evalTable.put(ZobristKey, eval);
		alphaTable.put(ZobristKey, alpha);
		betaTable.put(ZobristKey, beta);
		depthTable.put(ZobristKey, depth);
		flagTable.put(ZobristKey, flag);
	}
	
	public void reset() {
		moveTable = new Hashtable<>();
		evalTable = new Hashtable<>();
		alphaTable = new Hashtable<>();
		betaTable = new Hashtable<>();
		depthTable = new Hashtable<>();
		flagTable = new Hashtable<>();
	}
	
	public TranspositionTableProbing probeHash(Board board, int depth, double alpha, double beta) {
		TranspositionTableProbing returning = new TranspositionTableProbing();
		
		long zKey_position = board.zKey.getZobristHash(board);
		if (moveTable.containsKey(zKey_position)) { //then all other tables do possess this key too
			
			double eval = evalTable.get(zKey_position);
			int depth_table = depthTable.get(zKey_position);
			FLAG flag = flagTable.get(zKey_position);
			
			if (depth_table >= depth) {
				returning.result = ProbingResult.OK;
				switch(flag) {
				case HFEXACT:
					returning.score = eval;
					return returning;
				case HFALPHA:
					if (Math.abs(eval) <= Math.abs(alpha)) {
						returning.score = alpha;
						return returning;
					}
					returning.score = eval;
					//returning.result = ProbingResult.NotOK;
					return returning;
				case HFBETA:
					if (Math.abs(eval) >= Math.abs(beta)) {
						returning.score = beta;
						return returning;
					}
					returning.score = eval;
					//returning.result = ProbingResult.NotOK;
					return returning;
				default:
					returning.result = ProbingResult.NotOK;
					returning.score = eval;
					return returning;
				}
			} else {
				returning.result = ProbingResult.NotOK;
				return returning;
			}
		} else {
			returning.result = ProbingResult.NotOK;
			return returning;
		}
	}

}
