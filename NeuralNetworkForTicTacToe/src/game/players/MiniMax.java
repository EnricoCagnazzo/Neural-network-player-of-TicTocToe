package game.players;

import java.util.ArrayList;
import java.util.Random;

import utils.Grid;
import utils.MiniMaxTree;

public class MiniMax extends Player {

	public int move(Grid grid, int symbol) {
		double[] moves=MiniMaxTree.getTargetVector(grid, symbol);
		double max=-10; ArrayList<Integer> bestMoves=new ArrayList<>();
		for (int i=0;i<moves.length;i++){
			if (moves[i]==max){
				bestMoves.add(i);
			} else if (moves[i]>max){
				bestMoves.clear();
				bestMoves.add(i);
				max=moves[i];
			}
		}
		return bestMoves.get(new Random().nextInt(bestMoves.size()));
	}

}
