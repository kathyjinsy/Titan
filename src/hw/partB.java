package hw;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class partB {
	private static List<Integer> parseToList(String line) {
		List<Integer> myList = new ArrayList<>();
		//Gender
		int curidx = line.indexOf("Gender") + 7;
		int bit = line.charAt(curidx) == 'M' ? 1 : 0;
		myList.add(bit);
		
		//Age
		curidx = line.indexOf("Age") + 4;
		bit = line.charAt(curidx) == 'Y' ? 1 : 0;
		myList.add(bit);
		
		//Student?
		curidx = line.indexOf("Student?") + 9;
		bit = line.charAt(curidx) == 'Y' ? 1 : 0;
		myList.add(bit);
		
		//PreviouslyDeclined?
		curidx = line.indexOf("PreviouslyDeclined?") + 20;
		bit = line.charAt(curidx) == 'Y' ? 1 : 0;
		myList.add(bit);
		
		//Risk
		curidx = line.indexOf("Risk") + 5;
		bit = line.charAt(curidx) == 'h' ? 1 : 0;
		myList.add(bit);
		
		return myList;
 	}
	
	public static void main(String[] args) {
		//P1 - P2
		System.out.println(16);
		System.out.println(65536);
		
		//P3
		List<List<Integer>> res = new ArrayList<>();
		String line;
		try {
			FileReader fileReader = new FileReader("4Cat-Train.labeled");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			try {
				while((line = bufferedReader.readLine()) != null) {
					List<Integer> list = parseToList(line);
					boolean isIn = false;
					for (List<Integer> l : res) {
						if (l.equals(list)) {
							isIn = true;
							break;
						}
					}
					if (!isIn) {
						res.add(list);
					}
				}
				System.out.println((int)Math.pow(2, 16 - (res.size())));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//P4
		try {
			FileReader fileReader = new FileReader(args[0]);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			try {
				while((line = bufferedReader.readLine()) != null) {
					List<Integer> curlist = parseToList(line);
					int high = 16;
					for (List<Integer> list: res) {
						if (list.get(0) == curlist.get(0) && list.get(1) == curlist.get(1)
								&& list.get(2) == curlist.get(2) && list.get(3) == curlist.get(3)) {
							high = 32 * list.get(4);
							break;
						}
					}
					System.out.println(high + " " + (32 - high));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
