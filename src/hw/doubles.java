package hw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class doubles {
	
	private static int cl(String s1, String s2) {
		int idx = 0;
		while (idx < Math.min(s1.length(), s2.length())) {
			if (s1.charAt(idx) != s2.charAt(idx)) {
				return idx;
			}
			idx ++;
		}
		return idx;
	}
	
	private static int findStartIndex(String s) {
		StringBuilder sb = new StringBuilder();
		for (int j = s.length() - 1; j >= 0; j --) {
			char c = s.charAt(j);
			if(c == '$') {
				break;
			}
			sb.insert(0, c);
		}
		return Integer.parseInt(sb.toString());
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
	    String input = scan.nextLine();
		
		//get all suffixes
		int length = input.length();
		List<String> list = new ArrayList<>();
		for (int i = 0; i < length; i ++) {
			list.add(input.substring(i) + "$" + i);
		}
		Collections.sort(list);
		
		//Find lcp
		List<Integer> lcp = new ArrayList<>();
		for (int i = 0; i < length - 1; i ++) {
			String s1 = list.get(i);
			String s2 = list.get(i + 1);
			lcp.add(cl(s1, s2));
		}
		
		//suffix array
		List<Integer> sa = new ArrayList<>();
		for(String str : list) {
			sa.add(findStartIndex(str));
		}	
		
		int maxLength = 0;
		int start = -1;
		for (int i = 0; i < length - 1; i ++) {
			int tempMax = 0;
			int tempMin = Integer.MAX_VALUE;
			int tempstart = -1;
			int begin1 = sa.get(i);
			for (int j = i; j < length - 1; j ++) {
				tempMin = Math.min(lcp.get(j), tempMin);
				if (tempMin > tempMax) {
					int begin2 = sa.get(j + 1);
					int b1 = Math.min(begin1, begin2);
					int b2 = b1 == begin1 ? begin2 : begin1;
					if (b1 + tempMin >= b2 && tempMin > tempMax) {
						tempMax = b2 - b1;
						tempstart = b1;
					}
				}
			}
			if (tempMax > maxLength) {
				maxLength = tempMax;
				start = tempstart;
			} else if (tempMax == maxLength) {
				start = start == -1 ? tempstart : Math.min(tempstart, start);
			}
		}
		
		if(start == -1) {
			System.out.println(-1);
		} else {
			System.out.println(start + " " + maxLength);
		}
	}
}
