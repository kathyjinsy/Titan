package hw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class decisionTree {
	private static String[] attributes;
	private static List<List<String>> info;
	private static List<List<String>> info2;
	private static double entropy;
	private static String attr1;
	private static String attr2;
	private static double gain1;
	private static double gain2;
	private static int y;
	private static int n;
	private static int y_plus;
	private static int y_minus;
	private static int n_plus;
	private static int n_minus;
	private static String f1;
	private static String f2;
	private static String f3;
	private static String f4;
	private static String f5;
	private static String f6;
	private static String data1;
	private static String data2;
	private static int index1;
	private static int index2;
	private static int index3;
	
	private static String convertYes(String s) {
		if (s.equals("Anti_satellite_test_ban") || s.equals("Export_south_africa")
		|| s.equals("Aid_to_nicaraguan_contras") || s.equals("Mx_missile")
		|| s.equals("Immigration") || s.equals("Superfund_right_to_sue") 
		|| s.equals("Duty_free_exports")) {
			return "y";
		} else if (s.equals("M1") || s.equals("M2") || s.equals("M3")
				|| s.equals("M4") || s.equals("M5") || s.equals("P1")
				|| s.equals("P2") || s.equals("P3") || s.equals("P4")
				|| s.equals("F")) {
			return "A";
		} else if (s.equals("year")) {
			return "before1950";
		} else if (s.equals("length")) {
			return "morethan3min";
		} else if (s.equals("tempo")) {
			return "fast";
		} else if (s.equals("buying")) {
			return "expensive";
		} else if (s.equals("maint") || s.equals("safety")) {
			return "high";
		} else if (s.equals("doors") || s.equals("person")) {
			return "Two";
		} else if (s.equals("boot")) {
			return "large";
		}
		return "yes";
	}
	
	private static String convertNo(String s) {
		if (s.equals("Anti_satellite_test_ban") || s.equals("Export_south_africa")
				|| s.equals("Aid_to_nicaraguan_contras") || s.equals("Mx_missile")
				|| s.equals("Immigration") || s.equals("Superfund_right_to_sue") 
				|| s.equals("Duty_free_exports")) {
					return "n";
				} else if (s.equals("M1") || s.equals("M2") || s.equals("M3")
						|| s.equals("M4") || s.equals("M5") || s.equals("P1")
						|| s.equals("P2") || s.equals("P3") || s.equals("P4")
						|| s.equals("F")) {
					return "not A";
				} else if (s.equals("year")) {
					return "after1950";
				} else if (s.equals("length")) {
					return "lessthan3min";
				} else if (s.equals("tempo")) {
					return "slow";
				} else if (s.equals("buying")) {
					return "cheap";
				} else if (s.equals("maint") || s.equals("safety")) {
					return "low";
				} else if (s.equals("doors") || s.equals("person")) {
					return "MoreThanTwo";
				} else if (s.equals("boot")) {
					return "small";
				}
				return "no";
	}
	
	private static void totalEntropy() {
		int plus = 0;
		int minus = 0;
		for (List<String> curlist: info) {
			if (curlist.get(curlist.size() - 1) == "+") {
				plus ++;
			} else {
				minus ++;
			}
		}
		System.out.println("[" + plus + data1 + "/" + minus + data2 +"]");
		if (plus == info.size() || minus == info.size()) {
			entropy = 0.0;
		}else {
			entropy = plus * 1.0 / info.size() * Math.log10(info.size()*1.0 / plus) / Math.log10(2.0)
				+ minus * 1.0 / info.size() * Math.log10(info.size()*1.0 / minus) / Math.log10(2.0);
		}
	}
	
	private static boolean isYes(String s) {
		return s.equals("y") || s.equals("A") || s.equals("before1950")
			|| s.equals("yes") || s.equals("morethan3min")
			|| s.equals("fast") || s.equals("expensive")
			|| s.equals("high") || s.equals("high")
			|| s.equals("Two") || s.equals("large");
	}
	
	private static void count(int i) {
		y = 0;
		n = 0;
		y_plus = 0;
		y_minus = 0;
		n_plus = 0;
		n_minus = 0;
		for (List<String> curlist: info) {
			if (isYes(curlist.get(i))) {
				y ++;
				if (curlist.get(curlist.size() - 1).equals("+")){
					y_plus ++;
				}
			} else {
				if (curlist.get(curlist.size() - 1).equals("+")) {
					n_plus ++;
				}
			}
		}
		y_minus = y - y_plus;
		n = info.size() - y;
		n_minus = n - n_plus;
	}
	
	private static void sortEntropy() {
		Map<String, Double> map = new HashMap<>();
		for (int i = 0; i < attributes.length - 1; i ++) {
			String curattr = attributes[i];
			//compute information gain known that attribute
			count(i);
			double h1;
			if (y_plus == y || y_minus == y) {
				h1 = 0;
			} else {
				h1 = y_plus*1.0/y*Math.log10(y*1.0/y_plus)/Math.log10(2.0)
						+ y_minus*1.0/y*Math.log10(y*1.0/y_minus)/Math.log10(2.0);
			}
			double h2;
			if (n_plus == n || n_minus == n) {
				h2 = 0;
			} else {
				h2 = n_plus*1.0/n*Math.log10(n*1.0/n_plus)/Math.log10(2.0)
						+ n_minus*1.0/n*Math.log10(n*1.0/n_minus)/Math.log10(2.0);
			}
			double curentropy = y*1.0/info.size() * h1 + n*1.0/info.size() * h2;
			double curgain = entropy - curentropy;
			map.put(curattr, curgain);
		}
		
		for (String str: map.keySet()) {
			if (map.get(str) > gain1) {
				gain1 = map.get(str);
				attr1 = str;
			}
		}
	}
	
	private static int[] count2y(int idx1, int idx2) {
		int[] arr = new int[6];
		int yy = 0;
		int yy_plus = 0;
		int yn_plus = 0;
		
		for (List<String> curlist: info) {
			if (isYes(curlist.get(idx1)) && isYes(curlist.get(idx2))) {
				yy ++;
				if (curlist.get(curlist.size() - 1).equals("+")){
					yy_plus ++;
				}
			} else if (isYes(curlist.get(idx1))){
				if (curlist.get(curlist.size() - 1).equals("+")) {
					yn_plus ++;
				}
			}
		}
		int yn = y - yy;
		int yy_minus = yy - yy_plus;
		int yn_minus = yn - yn_plus;
		arr[0] = yy;
		arr[1] = yy_plus;
		arr[2] = yy_minus;
		arr[3] = yn;
		arr[4] = yn_plus;
		arr[5] = yn_minus;
		return arr;
	}
	
	private static int[] count2n(int idx1, int idx2) {
		int[] arr = new int[6];
		int ny = 0;
		int ny_plus = 0;
		int nn_plus = 0;
		
		for (List<String> curlist: info) {
			if (!isYes(curlist.get(idx1)) && isYes(curlist.get(idx2))) {
				ny ++;
				if (curlist.get(curlist.size() - 1).equals("+")){
					ny_plus ++;
				}
			} else if (!isYes(curlist.get(idx1))){
				if (curlist.get(curlist.size() - 1).equals("+")) {
					nn_plus ++;
				}
			}
		}
		int nn = n - ny;
		int ny_minus = ny - ny_plus;
		int nn_minus = nn - nn_plus;
		arr[0] = ny;
		arr[1] = ny_plus;
		arr[2] = ny_minus;
		arr[3] = nn;
		arr[4] = nn_plus;
		arr[5] = nn_minus;
		return arr;
	}
	
	private static int sortEntropy2(int idx1, int flag) {
		gain2 = 0;
		attr2 = null;
		Map<String, Double> map = new HashMap<>();
		for (int i = 0; i < attributes.length - 1; i ++) {
			if (idx1 == i) {
				continue;
			}
			
			String curattr = attributes[i];
			int[] arr = flag == 0 ? count2y(idx1, i) : count2n(idx1, i);
			int yy = arr[0];
			int yy_plus = arr[1];
			int yy_minus = arr[2];
			int yn = arr[3];
			int yn_plus = arr[4];
			int yn_minus = arr[5];
			
			double h1;
			if (yy_plus == yy || yy_minus == yy) {
				h1 = 0;
			} else {
				h1 = yy_plus*1.0/yy*Math.log10(yy*1.0/yy_plus)/Math.log10(2.0)
						+ yy_minus*1.0/yy*Math.log10(yy*1.0/yy_minus)/Math.log10(2.0);
			}
			double h2;
			if (yn_plus == yn || yn_minus == yn) {
				h2 = 0;
			} else {
				h2 = yn_plus*1.0/yn*Math.log10(yn*1.0/yn_plus)/Math.log10(2.0)
						+ yn_minus*1.0/yn*Math.log10(yn*1.0/yn_minus)/Math.log10(2.0);
			}
			double curgain;
			if (flag == 0) {
			double curentropy =  yy*1.0/y * h1 + yn*1.0/y * h2;
			double oldentropy = y_plus * 1.0 / y * Math.log10(y*1.0 / y_plus) / Math.log10(2.0)
					+ y_minus * 1.0 / y * Math.log10(y*1.0 / y_minus) / Math.log10(2.0);
			curgain = oldentropy - curentropy;
			} else {
				double curentropy =  yy*1.0/n * h1 + yn*1.0/n * h2;
				double oldentropy = n_plus * 1.0 / n * Math.log10(n*1.0 / n_plus) / Math.log10(2.0)
						+ n_minus * 1.0 / n * Math.log10(n*1.0 / n_minus) / Math.log10(2.0);
				curgain = oldentropy - curentropy;
			}
			map.put(curattr, curgain);
		}
		for (String str: map.keySet()) {
			if (map.get(str) > gain2) {
				gain2 = map.get(str);
				attr2 = str;
			}
		}
		int idx2 = -1;
		for (int i = 0; i < attributes.length - 1; i ++) {
			if (attributes[i].equals(attr2)) {
				idx2 = i;
				break;
			}
		}
		return idx2;
	}
	
	private static void train(String filename) {
		File f = new File(filename);
		FileReader fileReader;
		info = new ArrayList<>();
		try {
			fileReader = new FileReader(f);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			boolean isTitle = true;
			while ((line = bufferedReader.readLine()) != null) {
				if(!isTitle) {
					String[] curarr = line.split(",");
					List<String> curlist = new ArrayList<>();
					for (int i = 0; i < curarr.length - 1; i ++) {
						curlist.add(curarr[i]);
					}
					if (curarr[curarr.length - 1].equals("democrat") || curarr[curarr.length - 1].equals("A")
							|| curarr[curarr.length - 1].equals("yes")) {
						curlist.add("+");
						data1 = curarr[curarr.length - 1].equals("yes") ? "yes" : "+";
					} else {
						curlist.add("-");
						data2 = curarr[curarr.length - 1].equals("no") ? "no" : "-";
					}
					info.add(curlist);
				} else {
					attributes = line.split(",");
					for (int i = 0; i < attributes.length; i ++) {
						attributes[i] = attributes[i].trim();
					}
					isTitle = false;
				}
			}
			
			totalEntropy();
			sortEntropy();
			boolean hasSecond = true;
			if(gain1 >= 0.1) {
				int idx1 = -1;
				for (int i = 0; i < attributes.length - 1; i ++) {
					if (attributes[i].equals(attr1)) {
						idx1 = i;
						break;
					}
				}
				index1 = idx1;
				count(idx1);
				System.out.println(attr1 + " = " + convertYes(attr1) + ": [" + y_plus + data1 +"/"+ y_minus + data2 + "]");
				int idx2 = sortEntropy2(idx1, 0);
				index2 = idx2;
				
				hasSecond = y_plus != 0 && y_minus != 0 && gain2 >= 0.1;
				if (hasSecond) {
					int arr[] = count2y(idx1, idx2);
					int yy_plus = arr[1];
					int yy_minus = arr[2];
					int yn_plus = arr[4];
					int yn_minus = arr[5];
					
					f1 = yy_plus > yy_minus ? "+" : "-";
					f2 = yn_plus > yn_minus ? "+" : "-";
					System.out.println("| " + attr2 + " = " + convertYes(attr2) +  ": [" + yy_plus + data1 +"/"+ yy_minus + data2+"]");
					System.out.println("| " + attr2 + " = " + convertNo(attr2)+ ": [" + yn_plus + data1 +"/"+ yn_minus + data2+ "]");
				} else {
					index2 = -1;
					f5 = y_plus > y_minus ? "+" : "-";
				}
				System.out.println(attr1 + " = " + convertNo(attr1)+ ": [" + n_plus + data1 +"/"+ n_minus + data2+ "]");
				idx2 = sortEntropy2(idx1, 1);
				index3 = idx2;
				hasSecond = n_plus != 0 && n_minus != 0 && gain2 >= 0.1;
				if (hasSecond) {
					int arr[] = count2n(idx1, idx2);
					int ny_plus = arr[1];
					int ny_minus = arr[2];
					int nn_plus = arr[4];
					int nn_minus = arr[5];
					f3 = ny_plus > ny_minus ? "+" : "-";
					f4 = nn_plus > nn_minus ? "+" : "-";
					System.out.println("| " + attr2 + " = " + convertYes(attr2) +  ": [" + ny_plus + data1 +"/"+ ny_minus + data2+"]");
					System.out.println("| " + attr2 + " = " + convertNo(attr2) +  ": [" + nn_plus + data1 +"/"+ nn_minus + data2+"]");
				} else {
					index3 = -1;
					f6 = n_plus > n_minus ? "+" : "-";
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static double computeError(List<List<String>> mylist) {
		int count = 0;
		for (List<String> curlist : mylist) {
			if (f5 == null && f1 == null) {
				break;
			} 
			String realSign = curlist.get(curlist.size() - 1);
			int idx1 = index1;
			int idx2 = index2;
			int idx3 = index3;
			
            if (idx2 == -1 && isYes(curlist.get(idx1))) {
            	if (!f5.equals(realSign)) {
					count ++;
				}
            } else if (isYes(curlist.get(idx1))) {
            	if (isYes(curlist.get(idx2))) {
            		if (f5 != null) {
    					if (!f5.equals(realSign)) {
    						count ++;
    					}
    				} else{
    					if (!f1.equals(realSign)) {
    						count ++;
    					}
    				}
            	} else {
            		if (f5 != null) {
    					if (!f5.equals(realSign)) {
    						count ++;
    					}
    				} else{
    					if (!f2.equals(realSign)) {
    						count ++;
    					}
    				}
            	}
            } else if (idx3 == -1 && !isYes(curlist.get(idx1))) {
            	if (!f6.equals(realSign)) {
					count ++;
				}
            } else if (!isYes(curlist.get(idx1))){
            	if (isYes(curlist.get(idx3))) {
            		if (f6 != null) {
    					if (!f6.equals(realSign)) {
    						count ++;
    					}
    				} else{
    					if (!f3.equals(realSign)) {
    						count ++;
    					}
    				}
            	} else {
            		if (f6 != null) {
    					if (!f6.equals(realSign)) {
    						count ++;
    					}
    				} else{
    					if (!f4.equals(realSign)) {
    						count ++;
    					}
    				}
            	}
            } 
		}
		return count * 1.0 / mylist.size();
	}
	
	public static void main(String[] args) {
		//process training data
		train(args[0]);
		double e1 = computeError(info);
		System.out.println("error(train): " + e1);
		File f = new File(args[1]);
		FileReader fileReader;
		info2 = new ArrayList<>();
		try {
			fileReader = new FileReader(f);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			boolean isTitle = true;
			while ((line = bufferedReader.readLine()) != null) {
				if(!isTitle) {
					String[] curarr = line.split(",");
					List<String> curlist = new ArrayList<>();
					for (int i = 0; i < curarr.length - 1; i ++) {
						curlist.add(curarr[i]);
					}
					if (curarr[curarr.length - 1].equals("democrat") || curarr[curarr.length - 1].equals("A")
							|| curarr[curarr.length - 1].equals("yes")) {
						curlist.add("+");
					} else {
						curlist.add("-");
					}
					info2.add(curlist);
				} else {
					isTitle = false;
				}
			}
			double e2 = computeError(info2);
			System.out.println("error(test): " + e2);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
