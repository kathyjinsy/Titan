package hw;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Algorithm : Linear Programming 
 *  Minimize s
 *  s.t.
 *  Replace each h(u) with x(u) - x(u)'
 *  for each edge e = (u,v), -s*l(e) <= h(u)-h(v) <= s*l(e)
 *  <==> -s*l(e) <= (x(u)-x(u)')-(x(v)-x(v)') <= s*l(e)
 *  for each vertex u with known height, h(u)<= x(u)-x(u)'<= h(u)
 *  for all vertex u, x(u),x(u),s >= 0
 *  
 *  Transfer this LP to its dual and use Simplex to solve for optimum.
 *  
 *  Analysis:
 *  O((m+n)n) to transfer to the dual LP.
 *  Since Simplex is exponential, the time complexity is exponential.
 */
public class slope {
	private static double A[][];
	private static int[] basic;    // size m.  indices of basic vars
    private static int[] nonbasic; // size n.  indices of non-basic vars
    final static double INF = 1e100;
    final static double EPS = 1e-9;
    final int FEASIBLE = 1;
    final int INFEASIBLE = 0;
    final int UNBOUNDED = -1;
    private static double z;
    private static int row;
    private static int col;
	
    private static void Pivot(int r, int c) {
    	int t = basic[r];
    	basic[r] = nonbasic[c];
    	nonbasic[c] = t;
    	
    	A[r][c] = 1 / A[r][c];
    	for(int j=0; j<=col; j++) {
    	    if (j!=c) A[r][j] *= A[r][c];
    	}
    	for(int i=0; i<=row; i++) {
    	    if (i != r) {
    		for (int j=0; j<=col; j++) {if (j!=c) A[i][j] -= A[i][c] * A[r][j];}
    		A[i][c] = -A[i][c] * A[r][c];
    	    }
    	}
        }
    
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
        String line = scan.nextLine();
        String[] splited = line.split("\\s+");
        //number of vertices
        int n = Integer.parseInt(splited[0]);
        //number of edges
        int m = Integer.parseInt(splited[1]);
		//vertex height
		List<Integer> heights = new ArrayList<>();
		//vertex height known
		List<Boolean> known = new ArrayList<>();
		for (int i = 0; i < n; i ++) {
        	line = scan.nextLine();
        	if (line.equals("*")) {
        		heights.add(0);
        		known.add(false);
        	} else {
        		heights.add(Integer.parseInt(line));
        		known.add(true);
        	}
        }
		//edge length
		List<List<Integer>> lengths = new ArrayList<>();
		for (int i = 0; i < m; i ++) {
			List<Integer> curl = new ArrayList<>();
			line = scan.nextLine();
			String[] splited1 = line.split("\\s+");
			curl.add(Integer.parseInt(splited1[0]));
			curl.add(Integer.parseInt(splited1[1]));
			curl.add(Integer.parseInt(splited1[2]));
			lengths.add(curl);
		}
		
		Map<Integer, Integer> var_map = new HashMap<>();
		List<Integer> known_heights = new ArrayList<>();
		List<Integer> known_vertex = new ArrayList<>();
		int count = 0;
		for (int i = 0; i < heights.size(); i ++) {
			if (known.get(i)) {
				known_vertex.add(i);
				known_heights.add(heights.get(i));
			} else {
				var_map.put(i, count);
				count ++;
			}
		}
		int num_known = known_heights.size();
		
		row = 2 * (n - num_known) + 1;
		col = 2 * m;
		
		double[] C = new double[col];
		
		A = new double[row + 1][col + 1];
		for (int i = 0; i < m ; i ++) {
			List<Integer> length = lengths.get(i);
			int v1 = length.get(0);
			int v2 = length.get(1);
			int l = length.get(2);
			if (known.get(v1)) {
				v2 = var_map.get(v2);
				int h1 = heights.get(v1);
				A[2 * v2][2 * i] = -1.0;
				A[2 * v2][2 * i + 1] = 1.0;
				A[2 * v2 + 1][2 * i] = 1.0;
				A[2 * v2 + 1][2 * i + 1] = -1.0;
				C[2 * i] = -1.0 * h1;
				C[2 * i + 1] = 1.0 * h1;
			} else if (known.get(v2)) {
				v1 = var_map.get(v1);
				int h2 = heights.get(v2);
				A[2 * v1][2 * i] = 1.0;
				A[2 * v1][2 * i + 1] = -1.0;
				A[2 * v1 + 1][2 * i] = -1.0;
				A[2 * v1 + 1][2 * i + 1] = 1.0;
				C[2 * i] = 1.0 * h2;
				C[2 * i + 1] = -1.0 * h2;
			} else {
				v1 =  var_map.get(v1);
				v2 =  var_map.get(v2);
				A[2 * v1][2 * i] = 1.0;
				A[2 * v1][2 * i + 1] = -1.0;
				A[2 * v1 + 1][2 * i] = -1.0;
				A[2 * v1 + 1][2 * i + 1] = 1.0;
				A[2 * v2][2 * i] = -1.0;
				A[2 * v2][2 * i + 1] = 1.0;
				A[2 * v2 + 1][2 * i] = 1.0;
				A[2 * v2 + 1][2 * i + 1] = -1.0;
				C[2 * i] = 0.0;
				C[2 * i + 1] = 0.0;
			}
			A[2 * (n - num_known)][2 * i] = 1.0 * l;
			A[2 * (n - num_known)][2 * i + 1] = 1.0 * l;
		}
		
		double[] B = new double[row];
		for(int i = 0; i < 2 * (n - num_known); i ++) {
			B[i] = 0;
		}
		B[row - 1] = 1;
		
		basic = new int[row];
		nonbasic = new int[col];
		for (int j=0; j<row; j++) basic[j] = col+j;
		for (int i=0; i<col; i++) nonbasic[i] = i;
		for (int i=0; i<row; i++) {
		    A[i][col] = B[i];
		}

		for (int j=0; j<col; j++) A[row][j] = C[j];
		while(true) {
		    int r=0, c=0;
		    double p = 0.0;
		    for (int i=0; i<col; i++) {if (A[row][i] > p) p = A[row][c=i];}
		    if(p < EPS) {
			z = -A[row][col];
			break;
		    }
		    p = INF;
		    for (int i=0; i<row; i++) {
			if (A[i][c] > EPS) {
			    double v = A[i][col] / A[i][c];
			    if (v < p) {p = v; r = i;}
			}
		    }
		    Pivot(r,c);
		}
		
		DecimalFormat formatter = new DecimalFormat("#0.000000");
		System.out.println("slope = " + formatter.format(z));
	}
}
