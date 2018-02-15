import game.Game;
import utils.Parameters;

public class Main {

	public static void main(String[] args) {
		Parameters.setParameters();
		Game.start();
		System.out.println("OK");
	}

}
