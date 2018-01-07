package hw;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Algorithm: 
 * For every point from the set, do Angular Sweep from this point.
 * 
 * Angular Sweep from Point P:
 * For every other point Q, construct two circles C1 and C2 where C1 and C2 
 * are the center of the circle. P and Q should be both on the circles.
 * For each of the C1, compute the angle theta1 between PC1 and the positive
 * x-axis and for each of the C2, compute the angle theta2(theta1 and theta2
 * should be in counterclockwise order). 
 * Sort all theta1 and theta2 for computed from all Q's. Keep a counter to
 * count the number of points in the circle with theta for
 * each sorted angle theta. If theta is theta1, counter ++; other wise, 
 * counter --. Return the maximum value of the counter.
 * 
 * Analysis:
 * For each fixed P:O(1) to compute theta1 and theta2 for each each pair of 
 * P and Q. There are n-1 Q's for each P, thus O(n) theta's. Sorting them 
 * is O(nlogn). It takes O(n) to get maximum count of points.
 * There are n such P, so total time is O(n^2logn)
 * 
 * @author siyingj@andrew.cmu.edu
 *
 */
public class circle {
	private static List<Integer> xs;
	private static List<Integer> ys;
	private static int r;
	
	private static double getDistance(int x1, int y1, int x2, int y2) {
		int x = Math.abs(x1 - x2);
		int y = Math.abs(y1 - y2);
		return Math.sqrt(1.0 * (Math.pow(x, 2) + Math.pow(y, 2)));
	}
	
	private static int getNumPointsIn(int pointId) { 
		int curx = xs.get(pointId);
		int cury = ys.get(pointId);
		//an angle is marked true if it's an entry angle
		List<List<Double>> entryTable; 
		entryTable = new ArrayList<>();
		
		//Find all entry and leaving angles between input
		//point and any other point
		for (int i = 0; i < xs.size(); i ++) {
			if (i == pointId) {
				continue;
			}
			int x = xs.get(i);
			int y = ys.get(i);
			double d = getDistance(curx, cury, x, y);
			if (d > 2 * r) {
				continue;
			}
			
			double pqpx;
			if ((cury - y) < 0) {
				pqpx = 2 * Math.PI - Math.atan2(y - cury, curx - x);
			} else {
				pqpx = Math.atan2(cury - y, curx - x);
			}
			double pqpc = Math.acos(1.0 * d / (2.0 * r));
			double entry = pqpx - pqpc;
			DecimalFormat df2 = new DecimalFormat("###.####");
			entry = Double.valueOf(df2.format(entry));
			double leaving = pqpx + pqpc;
			List<Double> inner = new ArrayList<>();
			leaving = Double.valueOf(df2.format(leaving));
			
			inner.add(entry);
			inner.add(1.0);
			inner.add(i*1.0);
			entryTable.add(inner);
			List<Double> inner2 = new ArrayList<>();
			inner2.add(leaving);
			inner2.add(-1.0);
			inner2.add(i*1.0);
			entryTable.add(inner2);
		}
		
		Collections.sort(entryTable, new Comparator<List<Double>>() {
            public int compare(List<Double> s1, List<Double> s2) {
					if (s1.get(0) < s2.get(0)) {
						return -1;
					} else if (s1.get(0) > s2.get(0)) {
						return 1;
					} else {
						if (s1.get(1) > s2.get(1)) {
							return -1;
						} else if (s1.get(1) < s2.get(1)) {
							return 1;
						} else {
							return 0;
						}
					}
                 }
             });
		
		int cur = 1;
		int max = 1;
		for (int i = 0; i < entryTable.size(); i ++) {
			List<Double> inner = entryTable.get(i);
			double k = inner.get(1);
			cur += (int)k;
			max = Math.max(max, cur);
		}
		return max;
	}
	
	public static void main(String[] args) {
		xs = new ArrayList<>();
		ys = new ArrayList<>();
		
		Scanner scan = new Scanner(System.in);
        String line = scan.nextLine();
        String[] splited = line.split("\\s+");
        int n = Integer.parseInt(splited[0]);
        r = Integer.parseInt(splited[1]);
        for (int i = 0; i < n; i ++) {
        	line = scan.nextLine();
        	String[] splited1 = line.split("\\s+");
        	xs.add(Integer.parseInt(splited1[0]));
        	ys.add(Integer.parseInt(splited1[1]));
        }
        
		int max = 0;
		for (int i = 0; i < xs.size(); i ++) {
			max = Math.max(max, getNumPointsIn(i));
		}
		
		System.out.println(max);
	}
}
