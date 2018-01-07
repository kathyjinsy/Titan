package hw;

import java.util.ArrayList;
import java.util.List;


/**
 * Algorithm: 2D-DP
 * 
 * Base case: all substring of length 1 has a compression of length 1
 * Induction rule: for a substring s[i,j) with i < j - 1, matrix[i,j] = the shortest
 * compression of compress(s[i,k), s[k, j)) for all i < k < j.
 * 
 * compress(s[i,k), s[k, j)) only if s[i,k) is multiple copy of s[k, j)
 * or s[k, j) is multiple copy of s[i,k).
 */
public class repcount {
	private static List<List<String>> matrix;
	private static List<List<Integer>> copy_matrix;
	
	private static String getRawExpr(String expr) {
		if (Character.isDigit(expr.charAt(0))) {
			int count = 0;
			while (Character.isDigit(expr.charAt(count))) {
				count ++;
			}
			if (expr.charAt(count) == '(' && expr.charAt(expr.length() - 1) == ')') {
				return expr.substring(count + 1, expr.length() - 1);
			} else {
				String inner = expr.substring(count, expr.length());
				if (inner.length() == 1) {
					return inner;
				} else {
					return expr;
				}
			}
		} else {
			return expr;
		}
	}
	
	public static void main(String[] args) {
		
		String input = "helllooo";
		if (input.length() <= 1) {
			System.out.println(1);
			System.out.println(input);
		}
		int n = input.length();
		
		//n * n matrix, each element is a 3-tuple(length, #copy, cutting index)
		matrix = new ArrayList<>();
		copy_matrix= new ArrayList<>();
		for (int i = 0; i < n; i ++) {
			List<String> list = new ArrayList<>();
			List<Integer> list1 = new ArrayList<>();
			for (int j = 0; j < n; j ++) {
				list.add("");
				list1.add(0);
			}
			matrix.add(list);
			copy_matrix.add(list1);
		}
		
		//Base case: size 1 substring:
		for (int i = 0; i < n; i ++) {
			List<String> list = matrix.get(i);
			list.set(i, input.substring(i, i + 1));
			List<Integer> curlist = copy_matrix.get(i);
			curlist.set(i, 1);
		}
		
		//Induction : substring with size > 1
		for (int l = 2; l <= n; l ++) {
			// i = starting index of substring
			for (int i = 0; i <= n - l; i ++) {
				//j = ending index of substring: s[i,j] (j included)
				int j = i + l - 1;
				
				int min_length = l;
				String expr = input.substring(i, j + 1);
				
				//k = cutting index: s[i,k] and s[k+1,j]
				for (int k = i; k <= j - 1; k ++) {
					String leftExpr = matrix.get(i).get(k);
					String rightExpr = matrix.get(k + 1).get(j);
					String combineExpr;
					
					// compress left and right if possible
					String leftRawExpr = getRawExpr(leftExpr);
					String rightRawExpr = getRawExpr(rightExpr);
					int leftRawNum = copy_matrix.get(i).get(k);
					int rightRawNum = copy_matrix.get(k + 1).get(j);
					
					
					if (leftRawExpr.equals(rightRawExpr)) {
						int totalRawNum = leftRawNum + rightRawNum;
						if (leftRawExpr.length() == 1) {
							combineExpr = Integer.toString(totalRawNum) + leftRawExpr;
						} else {
							combineExpr = Integer.toString(totalRawNum) + "(" + leftRawExpr + ")";
						}
					} else {
						combineExpr = leftExpr + rightExpr;
					}
					
					if (combineExpr.length() < min_length) {
						if(i == 0 && j == 11) {
						//System.out.println(leftExpr  + " "+ rightExpr + " " + combineExpr + " " +  leftRawExpr + " " + rightRawExpr + " " + leftRawNum + " " + rightRawNum);
						}
						min_length = combineExpr.length();
						expr = combineExpr;
					}
				}
				List<String> curlist = matrix.get(i);
				curlist.set(j, expr);
 			}
			
		}
		String res = matrix.get(0).get(n - 1);
		System.out.println(res.length());
		System.out.println(res);
		
	}
}
