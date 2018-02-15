package game;

import game.players.MiniMax;
import game.players.NeuralNetwork;
import game.players.Player;
import game.players.RandomPlayer;
import utils.Grid;
import utils.MiniMaxTree;
import utils.Parameters;

public class Game {

	public static void start() {
		int nGames=Parameters.getnEpochs();
		int nGamesWithMiniMax=Parameters.getnEpochsBetTests();
		NeuralNetwork NN=new NeuralNetwork(Parameters.getnHidd(),Parameters.getnNeurLay(),Parameters.getLearnRate(),Parameters.isQuickprop());
		MiniMax MM=new MiniMax();
		RandomPlayer r=new RandomPlayer();
		MiniMaxTree.calculate();
		int totalRateA=0,partialRateA=0;
		int totalRateB=0,partialRateB=0;
		Player A,B;
		A=NN;
		B=MM;
		for (int i=1;i<=nGames;i++){
			//game NN vs MM
			Grid grid=new Grid();
			int symbol=Grid.CROSS;
			while (grid.state()==Grid.NOT_FINAL){
				int move;
				if (symbol==Grid.CROSS) move=A.move(grid, symbol);
				else move=B.move(grid,symbol);
				grid.put(symbol, move/3, move%3);
				symbol*=-1;
			}
			//game MM vs NN
			grid=new Grid();
			symbol=Grid.CROSS;
			while (grid.state()==Grid.NOT_FINAL){
				int move;
				if (symbol==Grid.CROSS) move=B.move(grid, symbol);
				else move=A.move(grid, symbol);
				grid.put(symbol, move/3, move%3);
				symbol*=-1;
			}			
			if (i%nGamesWithMiniMax==0){
				//game NN vs MM
				grid=new Grid();
				symbol=Grid.CROSS;
				int rateA=0,rateB=0;
				while (grid.state()==Grid.NOT_FINAL){
					int move;
					if (symbol==Grid.CROSS) move=A.move(grid, symbol);
					else move=B.move(grid,symbol);
					grid.put(symbol, move/3, move%3);
					symbol*=-1;
					if (grid.state()!=Grid.NOT_FINAL) {
							 if (grid.state()==Grid.C_WINS) rateA+=3; 
						else if (grid.state()==Grid.DRAW)  {rateA+=1; rateB+=1;}
						else 								rateB+=3;
					}
				}
				//game MM vs NN
				grid=new Grid();
				symbol=Grid.CROSS;
				while (grid.state()==Grid.NOT_FINAL){
					int move;
					if (symbol==Grid.CROSS) move=B.move(grid, symbol);
					else move=A.move(grid, symbol);
					grid.put(symbol, move/3, move%3);
					symbol*=-1;
					if (grid.state()!=Grid.NOT_FINAL) {
							 if (grid.state()==Grid.N_WINS) rateA+=3; 
						else if (grid.state()==Grid.DRAW)  {rateA+=1; rateB+=1;}
						else 								rateB+=3;
					}
				}
				totalRateA+=rateA; totalRateB+=rateB;
				partialRateA+=rateA; partialRateB+=rateB;
				//System.out.println(rate+" "+totalRate+" "+i);
				if (i%(nGamesWithMiniMax*10)==0) {
					System.out.println(/*i+" \t Pr "+A.getClass().getSimpleName()+ " \t"+*/partialRateA+/*"\t; "+B.getClass().getSimpleName()+*/" \t"+partialRateB);
					partialRateA=partialRateB=0;
				} 
			}

		}
		System.out.println(totalRateA+" "+totalRateB);
	}

}
