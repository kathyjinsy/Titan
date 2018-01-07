
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class takes in 3 or 4 command line arguments: the first three are 
 * pathnames of synonyms file, file1 and file2. The last one(optional)
 * is the tuple size. It will print out the plagiarism rate(represented by
 * the percent of tuples in file1 which appear in file2.) in percentage form.
 * 
 * Assumption1: All words in the synonyms file,file1 and file2 are separated by
 * one or more white spaces; there're no illegal characters in these three files,
 * only valid words are in these files.
 * 
 * Assumption2: A word may appear in multiple lines in the synonyms file
 * 
 * Assumption3: The same sequence of words may appear in multiple places in
 * file1 and file2. We will not double count these sequences. 
 * Example: "go go"  file1.txt   "go here" file2.txt  size = 1
 * plagiarism rate = 50%, only one "go" in file1 can be compared with "go"
 * in file2.
 * 
 * Data Structure: I used a map, whose keys are words in the synonyms file and
 * values are the words' synonyms. Suppose there're roughly m lines in the 
 * synonyms file and each line contains at most n words. The map size is roughly
 * O(mn^2). However, we will have O(1) performance if we want to tell whether two
 * words are synonyms. 
 * 
 * Time complexity: Suppose there're N tuples in file1 and M tuples in file2 and 
 * each tuple is of size k. The program will run in O(NMk).
 * 
 * 
 * @author Siying Jin(siyingj@andrew.cmu.edu)
 *
 */
public class PlagiarismDetection {
	private static final int DEFAULTSIZE = 3;
	
	//A map that associate each word that appears in synonyms file with all
	//its synonyms stored in a set.
	private static Map<String, Set<String>> synsmap;
	
	//A list that contains all words in file 1 in original order
	private static List<String> f1words;
	
	//A list that contains all words in file 1 in original order
	private static List<String> f2words;
	
	private static int tupleSize;
	
	public PlagiarismDetection(){
		synsmap = new HashMap<>();
		f1words = new ArrayList<>();
		f2words = new ArrayList<>();
		tupleSize = DEFAULTSIZE;
	}
	
	/**
	 * readTargetFile reads all words in input file into the input list
	 * @param filename path of the input file
	 * @param list the list that stores words in input file
	 */
	private static void readTargetFile(String filename, List<String> list) {
		File f = new File(filename);
		FileReader fileReader;
		try {
			fileReader = new FileReader(f);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] curarr = line.split("\\s+");
				for (String str: curarr) {
					list.add(str);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * readSyns reads all synonyms in the input file and store them
	 * in synsmap.
	 * @param filename pathname of the synonyms file
	 */
	private static void readSyns(String filename) {
		File fsyns = new File(filename);
		FileReader fileReader;
		try {
			fileReader = new FileReader(fsyns);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
      			while ((line = bufferedReader.readLine()) != null) {
				Set<String> curSet = new HashSet<>();
				String[] curarr = line.split("\\s+");
				for (String str: curarr) {
					curSet.add(str);
				}
				for (String str: curarr) {
					//merge two sets if the word appears in multiple lines
					if (synsmap.containsKey(str)) {
						Set<String> newSet = synsmap.get(str);
						newSet.addAll(curSet);
						synsmap.put(str, newSet);
					} else {
						synsmap.put(str, curSet);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean areSyns(String w1, String w2) {
		if (!synsmap.containsKey(w1)) {
			return w1.equals(w2);
		} else {
			Set<String> set = synsmap.get(w1);
			return set.contains(w2);
		}
	}
	
	public static void main(String[] args) {
		//pre-processing: read words in input files to helper data structures
		// and read the tuple size if possible
 		PlagiarismDetection pl = new PlagiarismDetection();               
		readSyns(args[0]);
		readTargetFile(args[1], f1words);
		readTargetFile(args[2], f2words);
		try{
			tupleSize = Integer.parseInt(args[3]);
		}catch (Exception e) {
			//No given tuple size -- set to default
		}
         
		
		//Ignore illegal cases when tuple size are larger than
		//file1 or file2's size
		if (tupleSize > f1words.size() || tupleSize > f2words.size()) {
			System.out.println("Tuple size is too big");
			return;
		}
		
		//Count the number of "similar" tuples in both files and 
		//compute the plagiarism rate
		boolean visited[] = new boolean[f2words.size() - tupleSize + 1];
		int counter = 0;
		for (int i = 0; i < f1words.size() - tupleSize + 1; i ++) {
			for (int j = 0; j < f2words.size() - tupleSize + 1; j ++) {
				if (visited[j]) {
					continue;
				}
				boolean foundPlag = true;
				for (int k = 0; k < tupleSize; k ++) {
					if (!areSyns(f1words.get(i + k), f2words.get(j + k))) {
						foundPlag = false;
						break;
					}
				}
				if (foundPlag) {
					counter ++;
					visited[j] = true;
				}
			}
		}
		double plagRate = 1.0 * counter / (f1words.size() - tupleSize + 1);
		System.out.println("The plagiarism rate is :" + plagRate * 100 + "%");
	}
}
