package game.players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import utils.Grid;

public class Human extends Player {
	public int move(Grid grid, int symbol){
		grid.print();
		System.out.println(symbol==Grid.CROSS?"X":"O");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int m=0;
        try{
            m = Integer.parseInt(br.readLine());
        }catch(NumberFormatException | IOException nfe){
            System.err.println("Invalid Format!");
        }
		return m;
	}
}
