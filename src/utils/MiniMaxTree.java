package utils;

import java.util.ArrayList;

public class MiniMaxTree {

	private static class TreeNode{
		private final static int NOVALUE=10;
		
		private Grid grid;
		private int value;
		private int lastInserted;
		
		private ArrayList<TreeNode> children;
		
		private TreeNode(){
			grid=new Grid();
			children=new ArrayList<TreeNode>();
			value=NOVALUE;
			lastInserted=-1;
		}
		
		private TreeNode(Grid grid, TreeNode parent, int li){
			this.grid=grid;
			parent.addChild(this);
			children=new ArrayList<TreeNode>();
			value=NOVALUE;
			lastInserted=li;
		}
		
		private void addChild(TreeNode treeNode) {
			this.children.add(treeNode);
		}

		private void setValue(int v) {
			value=v;
		}

		public void addChild(int i, int j, int symbol, int li) {
			Grid grid=new Grid(this.grid);
			grid.put(symbol, i, j);
			new TreeNode(grid, this, li);
		}

		public TreeNode getLastChild() {
			return children.get(children.size()-1);
		}
	}
	
	private static TreeNode tree;
	
	private static void recoursiveTreeCreation(TreeNode actual, int symbol){
		if (actual.grid.state()==Grid.NOT_FINAL){
			for (int i=0;i<3;i++){
				for (int j=0;j<3;j++){
					if (actual.grid.getCell(i,j)==Grid.EMPTY){
						actual.addChild(i,j,symbol,i*3+j);
						recoursiveTreeCreation(actual.getLastChild(), symbol*(-1));
					}
				}
			}
		} else
			actual.setValue(actual.grid.state() *(10-actual.grid.symbols)); //in this way the player will try to win as soon as possible
	}
	
	public static void calculate() {
		tree=new TreeNode();
		int symbol=Grid.CROSS;
		recoursiveTreeCreation(tree, symbol);
		calculateValors(tree,symbol);
	}

	private static void calculateValors(TreeNode actual, int symbol) {
		ArrayList<TreeNode> children=actual.children;
		for (int i=0;i<children.size();i++){
			if (children.get(i).value==TreeNode.NOVALUE)
				calculateValors(children.get(i), symbol*(-1));//the opposite symbol
			if (actual.value==TreeNode.NOVALUE){
				actual.setValue(children.get(i).value);
				continue;
			}	//								  *symbol is to calculate the max or the min if its cross or nought
			if (children.get(i).value*symbol>actual.value*symbol) actual.setValue(children.get(i).value);
		}
	}

	public static double[] getTargetVector(Grid grid, int symbol) {
		TreeNode actual=tree;
		double[] target=new double[9];
		while (!actual.grid.isEqual(grid)){
			ArrayList<TreeNode> children=actual.children;
			for (int i=0;i<children.size();i++){
				if (children.get(i).grid.beforeOf(grid)){
					actual=children.get(i);
					continue;
				}
			}
		}
		ArrayList<TreeNode> children=actual.children;
		double rif=-10;
		//find the best result for the actual grid
		for (int i=0;i<children.size();i++){
			if (children.get(i).value*symbol >rif )
				rif=children.get(i).value;
		}
		//set target to 1 for the moves that lead to that result
		for (int i=0;i<children.size();i++)
			if (children.get(i).value==rif)
				target[children.get(i).lastInserted]=1;
		return target;
	}

}
