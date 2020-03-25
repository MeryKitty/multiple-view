package test;

import java.util.Arrays;

import com.aparapi.Kernel;
import com.aparapi.Range;

public class Matrix {
	private final int rowNum, columnNum;
	private final double[][] data;
	
	private Matrix(int rowNum, int columnNum) {
		this.rowNum = rowNum;
		this.columnNum = columnNum;
		this.data = new double[rowNum][columnNum];
	}
	
	public static void main(String[] args) {
		int n = 1000;
		final float inA[][] = new float[n][n]; // get a float array of data from somewhere
		final float inB[][] = new float[n][n]; // get a float array of data from somewhere
		assert (inA.length == inB.length);
		final float[][] result = new float[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				inA[i][j] = (float) Math.random();
			}
		}
		long start = System.currentTimeMillis();
/*		for (int r = 0; r < n; r++) {
			for (int c = 0; c < n; c++) {
				int temp = 0;
				for (int j = 0; j < n; j++) {
		        	temp += inA[r][j] * inB[j][c];
		        }
		        result[r][c] = temp;
			}
		} */
		long middle1 = System.currentTimeMillis();
		for (int time = 0; time < 1; time++) {
			Kernel kernel = new Kernel() {
			    @Override
			    public void run() {
			        int i = getGlobalId();
			        int r = i / n;
			        int c = i % n;
			        int temp = 0;
			        for (int j = 0; j < n; j++) {
			        	temp += inA[r][j] * inB[j][c];
			        }
			        result[r][c] = temp;
			    }
			};
			Range range = Range.create2D(n, n);
			kernel.execute(range);
		}
		long end = System.currentTimeMillis();
		
		//System.out.println("AparAPI initialsation: " + (middle2 - middle1));
		//System.out.println("GPU: " + (end - middle2));
		System.out.println("CPU: " + (middle1 - start));
		
	}
}

