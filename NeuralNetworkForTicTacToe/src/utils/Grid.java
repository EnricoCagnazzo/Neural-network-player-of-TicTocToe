package utils;

public class Grid {
	int[][] cells;
	int symbols;
	public static final int CROSS=-1;
	public static final int EMPTY=0;
	public static final int NOUGHTS=1;
	public static final int C_WINS=-1;
	public static final int DRAW=0;
	public static final int N_WINS=1;
	public static final int NOT_FINAL=2;
	
	
	
	public Grid(){
		cells=new int[3][3];
		symbols=0;
	}
	
	public Grid(Grid grid) {
		this.symbols=grid.symbols;
		this.cells=new int[3][3];
		for (int i=0;i<3;i++)
			for (int j=0;j<3;j++)
				this.cells[i][j]=grid.getCell(i, j);
	}

	public void put(int symbol, int x, int y){
		if (cells[x][y]!=EMPTY)
			System.err.println("ERROR!!! Trying to put a symbol in a not-empty cell");
		cells[x][y]=symbol;
		symbols++;
	}
	
	public int state(){
		for (int i=0;i<3;i++){
			//checks rows and columns
			if (cells[i][0]!=EMPTY && cells[i][0]==cells[i][1] && cells[i][1]==cells[i][2])
				return cells[i][0]==CROSS?C_WINS:N_WINS;
			if (cells[0][i]!=EMPTY && cells[0][i]==cells[1][i] && cells[1][i]==cells[2][i])
				return cells[0][i]==CROSS?C_WINS:N_WINS;
		}
		//checks diagonals
		if (cells[0][0]!=EMPTY && cells[0][0]==cells[1][1] && cells[1][1]==cells[2][2])
			return cells[0][0]==CROSS?C_WINS:N_WINS;
		if (cells[0][2]!=EMPTY && cells[0][2]==cells[1][1] && cells[1][1]==cells[2][0])
			return cells[0][2]==CROSS?C_WINS:N_WINS;
		return symbols==9?DRAW:NOT_FINAL;
	}

	public int getCell(int i, int j) {
		return cells[i][j];
	}

	public int[] getArrayCells() {
		int [] v=new int[9];
		for (int i=0;i<3;i++)
			for (int j=0;j<3;j++)
				v[i*3+j]=cells[i][j];
		return v;
	}
	
	public boolean isEqual(Grid g){
		for (int i=0;i<3;i++) for (int j=0;j<3;j++) if (this.cells[i][j]!=g.getCell(i, j)) return false;
		return true;
	}
	
	public boolean beforeOf(Grid g){ //is the actual grid emptier then g?
		for (int i=0;i<3;i++) for (int j=0;j<3;j++) 
			if (this.cells[i][j]!=g.getCell(i, j) && this.cells[i][j]!=EMPTY) return false;
		return true;
	}

	private String printCell(int x,int y){
		return this.cells[x][y]==CROSS?"X":this.cells[x][y]==EMPTY?" ":"O";
	}
	
	public void print() {
		System.out.println(printCell(0,0)+"|"+printCell(0,1)+"|"+printCell(0,2));
		System.out.println("-------");
		System.out.println(printCell(1,0)+"|"+printCell(1,1)+"|"+printCell(1,2));
		System.out.println("-------");
		System.out.println(printCell(2,0)+"|"+printCell(2,1)+"|"+printCell(2,2));
		System.out.println();
	}
}
