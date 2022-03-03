package game;

public class MoveTypeClass {

	public enum MoveType {
		Normal, 
		EnPassant, 
		KingSideCastle, 
		QueenSideCastle, 
		Promotion, 
		Check,
		Capture, 
		FoundByAlphaBeta
	}

}
