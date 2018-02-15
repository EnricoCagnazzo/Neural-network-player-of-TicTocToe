package game.players;

import java.util.Random;

import utils.Grid;

public class RandomPlayer extends Player{
	public int move(Grid g, int symbol){
		int m; Random r=new Random();
		m=r.nextInt(9);
		while (g.getCell(m/3, m%3)!=Grid.EMPTY){
			m=r.nextInt(9);
		}
		return m;
	}
}
