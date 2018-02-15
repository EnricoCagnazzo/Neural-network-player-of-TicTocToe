package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parameters {

	/**
	 * #hidden layers
	 * #neurons for layer
	 * #epochs
	 * #epochs between tests
	 */
	static private int nHidd;
	static private ArrayList<Integer> nNeurLay;
	static private double learnRate;
	static private int nEpochs;
	static private int nEpochsBetTests;
	static private boolean quickprop;
	
	public static void setParameters(){
		try (BufferedReader br = new BufferedReader(new FileReader("config.txt")))
		{
			nHidd=getNumber(br.readLine());
			nNeurLay=getNnumbers(br.readLine(),nHidd);
			learnRate=getDouble(br.readLine());
			nEpochs=getNumber(br.readLine());
			nEpochsBetTests=getNumber(br.readLine());
			quickprop=br.readLine().startsWith("quickprop");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private static ArrayList<Integer> getNnumbers(String s, int n) {
		ArrayList<Integer> array=new ArrayList<Integer>();
		int b=0, e=s.indexOf(' ');
		for (int i=0;i<n;i++){
			array.add(Integer.parseInt(s.substring(b, e)));
			b=e+1; e=s.indexOf(' ', b);
		}
		return array;
	}

	public static int getnHidd() {
		return nHidd;
	}
	public static ArrayList<Integer> getnNeurLay() {
		return nNeurLay;
	}
	public static int getnEpochs() {
		return nEpochs;
	}
	public static int getnEpochsBetTests() {
		return nEpochsBetTests;
	}
	
	public static int getNumber(String s) {
		return Integer.parseInt(s.substring(0, s.indexOf(' ')));
	}	
	
	public static double getDouble(String s) {
		return Double.parseDouble(s.substring(0, s.indexOf(' ')));
	}

	public static double getLearnRate() {
		return learnRate;
	}

	public static boolean isQuickprop() {
		return quickprop;
	}
	
}
