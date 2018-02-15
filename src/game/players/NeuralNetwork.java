package game.players;

import java.util.ArrayList;
import java.util.Random;

import utils.Grid;
import utils.MiniMaxTree;

public class NeuralNetwork extends Player{
	
	private double learningRate;
	private boolean quickProp;
	
	private class InputLayer{
		private double Bias=1.0;
		private double[] BiasWeights;
		private double[] Values;
		private double[][] Weights;
		private double[][] PrevDeEonDeWij;
		private double[][] PrevDeltaWeight;
		private double[] NextLayerDeltaJ;
		private int dim;
		private boolean firstCorrection;
		private double[] PrevBiasDeEonDeWij;
		private double[] PrevBiasDeltaWeight;
        
        private InputLayer(int n, int m) {
        	dim=n;
        	Random r=new Random();
			BiasWeights=new double[m];
			Values=new double[n];
			Weights=new double[n][m];
			PrevDeEonDeWij=new double[n][m];
			PrevDeltaWeight=new double[n][m];
			PrevBiasDeEonDeWij=new double[m];
			PrevBiasDeltaWeight=new double[m];
			NextLayerDeltaJ=new double[m];
			firstCorrection=true;
			for (int j=0;j<m;j++)
				BiasWeights[j]=r.nextDouble()*2-1;//get a weight between -1.0 and 1.0
			for (int i=0;i<n;i++){
				for (int j=0;j<m;j++)
					Weights[i][j]=r.nextDouble()*2-1;
			}
		}
        
        public void setValues(int[] v, int symbol){
        	for (int i=0;i<dim;i++)
        		Values[i]=v[i]*symbol;
        }

		public void correctWeights(HiddenLayer nextHidden) {
			for (int j=0;j<nextHidden.dim;j++){
				double oldNLD=NextLayerDeltaJ[j];
				NextLayerDeltaJ[j]=0.0;
				for (int k=0;k<nextHidden.NextLayerDeltaJ.length;k++)
					NextLayerDeltaJ[j]+=nextHidden.NextLayerDeltaJ[k]*nextHidden.Weights[j][k];
				NextLayerDeltaJ[j]*=nextHidden.Activation[j]*(1-nextHidden.Activation[j]);
				if (NextLayerDeltaJ[j]==Double.NaN) NextLayerDeltaJ[j]=oldNLD;
			}
			for (int j=0;j<nextHidden.dim;j++){ //correct Bias weights
				double deEonDeWij,deltaWeight;
				deEonDeWij=-NextLayerDeltaJ[j]*this.Bias;
				deltaWeight=deEonDeWij*(quickProp?((firstCorrection?1:PrevBiasDeltaWeight[j])/(firstCorrection?1:PrevBiasDeEonDeWij[j]-deEonDeWij)):1);
				if (Double.isNaN(deltaWeight)) continue;
				deltaWeight*=learningRate;
				this.BiasWeights[j]+=deltaWeight;
				PrevBiasDeEonDeWij[j]=deEonDeWij; PrevBiasDeltaWeight[j]=deltaWeight;
			}
			for (int i=0;i<this.dim;i++){
				for (int j=0;j<nextHidden.dim;j++){
					double deEonDeWij,deltaWeight;
					deEonDeWij=-NextLayerDeltaJ[j]*activation(this.Values[i]);
					 deltaWeight=deEonDeWij*(quickProp?((firstCorrection?1:PrevDeltaWeight[i][j])/(firstCorrection?1:PrevDeEonDeWij[i][j]-deEonDeWij)):1);
					if (Double.isNaN(deltaWeight)) continue;
					this.Weights[i][j]+=deltaWeight*learningRate;
					PrevDeEonDeWij[i][j]=deEonDeWij; PrevDeltaWeight[i][j]=deltaWeight;
				}
			}
			firstCorrection=false;
		}
	}
	
	private class HiddenLayer{
		private double Bias=1.0;
		private double[] Activation;
		private double[] BiasWeights;
		private double[] Net;
		private double[][] Weights;
		private double[][] PrevDeltaWeight;
		private double[][] PrevDeEonDeWij;
		private double[] NextLayerDeltaJ;
		private int dim;
		private boolean firstCorrection;
		private double[] PrevBiasDeltaWeight;
		private double[] PrevBiasDeEonDeWij;
        
        private HiddenLayer(int n,int m){
        	dim=n;
        	Random r=new Random();
        	Activation=new double[n];
			BiasWeights=new double[m];
			Net=new double[n];
			Weights=new double[n][m];
			PrevDeEonDeWij=new double[n][m];
			PrevDeltaWeight=new double[n][m];
			NextLayerDeltaJ=new double[m];
			PrevBiasDeEonDeWij=new double[m];
			PrevBiasDeltaWeight=new double[m];
			firstCorrection=true;
			for (int j=0;j<m;j++)
				BiasWeights[j]=r.nextDouble()*2-1;//get a weight between -1.0 and 1.0
			for (int i=0;i<n;i++){
				for (int j=0;j<m;j++)
					Weights[i][j]=r.nextDouble()*2-1;
			}
        }


		public void setValues(InputLayer input) {
			for (int i=0;i<dim;i++){
				Net[i]=input.Bias*input.BiasWeights[i];
				for (int j=0;j<input.dim;j++)
					Net[i]+=input.Values[j]*input.Weights[j][i];
				Activation[i]=activation(Net[i]);
			}
		}
        
		public void setValues(HiddenLayer hidden) {
			for (int i=0;i<dim;i++){
				Net[i]=hidden.Bias*hidden.BiasWeights[i];
				for (int j=0;j<hidden.dim;j++)
					Net[i]+=hidden.Activation[j]*hidden.Weights[j][i];
				Activation[i]=activation(Net[i]);
			}
		}


		public void correctWeights(OuputLayer output) {
			for (int i=0;i<output.dim;i++)
				NextLayerDeltaJ[i]=(output.Target[i]-output.Activation[i])*output.Activation[i]*(1-output.Activation[i]);
			for (int j=0;j<output.dim;j++){ //correct Bias weights
				double deEonDeWij,deltaWeight;
				deEonDeWij=-NextLayerDeltaJ[j]*this.Bias;
				deltaWeight=deEonDeWij*(quickProp?((firstCorrection?1:PrevBiasDeltaWeight[j])/(firstCorrection?1:PrevBiasDeEonDeWij[j]-deEonDeWij)):1);
				if (Double.isNaN(deltaWeight)) continue;
				deltaWeight*=learningRate;
				this.BiasWeights[j]+=deltaWeight;
				PrevBiasDeEonDeWij[j]=deEonDeWij; PrevBiasDeltaWeight[j]=deltaWeight;
			}
			for (int i=0;i<dim;i++)
				for (int j=0;j<output.dim;j++){
					double deEonDeWij,deltaWeight;
					deEonDeWij=-NextLayerDeltaJ[j]*this.Activation[i];
					deltaWeight=deEonDeWij*(quickProp?((firstCorrection?1:PrevDeltaWeight[i][j])/(firstCorrection?1:PrevDeEonDeWij[i][j]-deEonDeWij)):1);
					if (Double.isNaN(deltaWeight)) continue;
					deltaWeight*=learningRate;
					this.Weights[i][j]+=deltaWeight;
					PrevDeEonDeWij[i][j]=deEonDeWij; PrevDeltaWeight[i][j]=deltaWeight;
				}
			firstCorrection=false;
		}


		public void correctWeights(HiddenLayer nextHidden) {
			for (int j=0;j<nextHidden.dim;j++){
				double oldNLD=NextLayerDeltaJ[j];
				NextLayerDeltaJ[j]=0.0;
				for (int k=0;k<nextHidden.NextLayerDeltaJ.length;k++)
					NextLayerDeltaJ[j]+=nextHidden.NextLayerDeltaJ[k]*nextHidden.Weights[j][k];
				NextLayerDeltaJ[j]*=nextHidden.Activation[j]*(1-nextHidden.Activation[j]);
				if (NextLayerDeltaJ[j]==Double.NaN) NextLayerDeltaJ[j]=oldNLD;
			}
			for (int j=0;j<nextHidden.dim;j++){ //correct Bias weights
				double deEonDeWij,deltaWeight;
				deEonDeWij=-NextLayerDeltaJ[j]*this.Bias;
				deltaWeight=deEonDeWij*(quickProp?((firstCorrection?1:PrevBiasDeltaWeight[j])/(firstCorrection?1:PrevBiasDeEonDeWij[j]-deEonDeWij)):1);
				if (Double.isNaN(deltaWeight)) continue;
				deltaWeight*=learningRate;
				this.BiasWeights[j]+=deltaWeight;
				PrevBiasDeEonDeWij[j]=deEonDeWij; PrevBiasDeltaWeight[j]=deltaWeight;
			}
			for (int i=0;i<this.dim;i++){
				for (int j=0;j<nextHidden.dim;j++){
					double deEonDeWij,deltaWeight;
					deEonDeWij=-NextLayerDeltaJ[j]*this.Activation[i];
					deltaWeight=deEonDeWij*(quickProp?((firstCorrection?1:PrevDeltaWeight[i][j])/(firstCorrection?1:PrevDeEonDeWij[i][j]-deEonDeWij)):1);
					if (Double.isNaN(deltaWeight))
						continue;
					this.Weights[i][j]+=deltaWeight*learningRate;
					PrevDeEonDeWij[i][j]=deEonDeWij; PrevDeltaWeight[i][j]=deltaWeight;
				}
			}
			firstCorrection=false;
		}


	}
	
	private class OuputLayer{
		private double[] Activation;
	    private double[] Net;
	    private double[] Target;
	    private int dim;
	    
	    private OuputLayer(int n){
	    	dim=n;
	    	Activation=new double[n];
			Net=new double[n];
			Target=new double[n];
	    }

		public void setValues(HiddenLayer hidden) {
			for (int i=0;i<dim;i++){
				Net[i]=hidden.Bias*hidden.BiasWeights[i];
				for (int j=0;j<hidden.dim;j++)
					Net[i]+=hidden.Activation[j]*hidden.Weights[j][i];
				Activation[i]=activation(Net[i]);
			}
		}

		public void setTargetVector(double[] targetVector) {
			System.arraycopy(targetVector, 0, Target, 0, dim);
		}
	}
	
	InputLayer input;
	ArrayList<HiddenLayer> hidden;
	OuputLayer output;

	public NeuralNetwork(int nHidd, ArrayList<Integer> nNeurLay,double lR, boolean q) {
		//create the input leyer of 9 neurons
		input=new InputLayer(9, nNeurLay.get(0));
		//create the nHidd hidden layers
		hidden=new ArrayList<HiddenLayer>();
		for (int i=0;i<nHidd-1;i++)
			hidden.add(new HiddenLayer(nNeurLay.get(i), nNeurLay.get(i+1)));
		hidden.add(new HiddenLayer(nNeurLay.get(nHidd-1), 9));
		//create the output layer of 9 neurons
		output=new OuputLayer(9);
		learningRate=lR; quickProp=q;
	}

	public double activation(double net) {
		//use the standard logistic function 1/(1+e^-net)
		double slf=1/(1+Math.exp(-net));
		if (slf==Double.NaN)
			System.err.println("NAN after SLF");
		return slf;
	}

	public int move(Grid grid,int symbol){
		//first check if there is a win availablein rows, columns, first diagonal, second diagonal
		for (int i=0,t=0;i<3;i++){
			for (int j=0;j<3;j++)
				t+=grid.getCell(i, j);
			t*=symbol;
			if (t==2){// a win is available 
				for (int j=0;j<3;j++)
					if (grid.getCell(i, j)==Grid.EMPTY) return i*3+j;
			}
			t=0;
		}
		for (int j=0,t=0;j<3;j++){
			for (int i=0;i<3;i++)
				t+=grid.getCell(i, j);
			t*=symbol;
			if (t==2){// a win is available 
				for (int i=0;i<3;i++)
					if (grid.getCell(i, j)==Grid.EMPTY) return i*3+j;
			}
			t=0;
		}
		int t=0;
		for (int i=0;i<3;i++)
			t+=grid.getCell(i, i);
		t*=symbol;
		if (t==2){// a win is available 
			for (int i=0;i<3;i++)
				if (grid.getCell(i, i)==Grid.EMPTY) return i*3+i;
		}
		t=0;
		for (int i=0;i<3;i++)
			t+=grid.getCell(i, 2-i);
		t*=symbol;
		if (t==2){// a win is available 
			for (int i=0;i<3;i++)
				if (grid.getCell(i, 2-i)==Grid.EMPTY) return i*3+2-i;
		}
		forwardPropagation(grid,symbol);
		double max=-10000; int cell=0;
		for (int i=0;i<3;i++){
			for (int j=0;j<3;j++){
				if (grid.getCell(i, j)==Grid.EMPTY && output.Activation[i*3+j]*symbol>max){
					max=output.Activation[i*3+j]; cell=i*3+j;
				}
			}
		}
		//get the target vector
		double[] targetVector=MiniMaxTree.getTargetVector(grid,symbol);
		//backpropagation
		backPropagation(targetVector);
		//printWeights();
		return cell;
	}

//	private void printWeights() {
//		for (int i=0;i<input.dim;i++)
//			for (int j=0;j<hidden.get(0).dim;j++)
//				System.out.print(input.Weights[i][j]+" ");
//		for (int i=0;i<hidden.size()-1;i++)
//			for (int j=0;j<hidden.get(i).dim;j++)
//				for (int k=0;k<hidden.get(i+1).dim;k++)
//					System.out.print(hidden.get(i).Weights[j][k]+" ");
//		for (int i=0;i<hidden.get(hidden.size()-1).dim;i++)
//			for (int j=0;j<output.dim;j++)
//				System.out.print(hidden.get(hidden.size()-1).Weights[i][j]+" ");
//		System.out.println();
//	}

	private void forwardPropagation(Grid grid, int symbol) {
		this.input.setValues(grid.getArrayCells(),symbol);
		this.hidden.get(0).setValues(this.input);
		for (int i=1;i<hidden.size();i++)
			this.hidden.get(i).setValues(this.hidden.get(i-1));
		this.output.setValues(this.hidden.get(this.hidden.size()-1));	
	}

	private void backPropagation(double[] targetVector) {
		this.output.setTargetVector(targetVector);
		this.hidden.get(this.hidden.size()-1).correctWeights(this.output);
		for (int i=this.hidden.size()-2;i>=0;i--)
			this.hidden.get(i).correctWeights(this.hidden.get(i+1));
		this.input.correctWeights(this.hidden.get(0));
	}
	
}
