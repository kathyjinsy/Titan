package hw;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class topwords {
	private static List<List<String>> examples;
	private static List<Integer> docsL;
	private static List<Integer> docsC;
	private static Set<String> dict;
	private static Map<String, Double> Pwk_C;
	private static Map<String, Double> Pwk_L;
	
	public static void main(String[] args) {
		//read training data
		dict = new HashSet<>();
		String training_files = args[0];
		FileReader fileReader;
		String line;
		examples = new ArrayList<>();
		Pwk_C = new HashMap<>();
		Pwk_L = new HashMap<>();
		docsC = new ArrayList<>();
		docsL = new ArrayList<>();
		
		try {
			fileReader = new FileReader(training_files);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int count = 0;
			while((line = bufferedReader.readLine()) != null) {
				String training_file = line.trim();
				String category = training_file.charAt(0) == 'c' ? "C" : "L";
				FileReader fileReader_traning = new FileReader(training_file);
				BufferedReader bufferedReader_training = new BufferedReader(fileReader_traning);
				String line_train;
				List<String> train_set = new ArrayList<>();
				while((line_train = bufferedReader_training.readLine()) != null) {
					String word = line_train.trim().toLowerCase();
					dict.add(word);
					Pwk_C.put(word, 0.0);
					Pwk_L.put(word, 0.0);
					train_set.add(word);
				}
				examples.add(train_set);
				if (category.equals("C")) {
					docsC.add(count);
				} else {
					docsL.add(count);
				}
				count ++;
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		//calculate P(vj) and P(wk|vj)
		//1. C:
		double Pc = docsC.size() * 1.0 / examples.size();
		int n = 0;
		for (int i = 0; i < docsC.size(); i ++) {
			int index = docsC.get(i);
			List<String> set = examples.get(index);
			n += set.size();
			for (int j = 0; j < set.size(); j ++) {
				String word = set.get(j);
				Pwk_C.put(word, Pwk_C.get(word) + 1);
			}
		}
		for (String word: Pwk_C.keySet()) {
			Double count = Pwk_C.get(word);
			double Pword_v = (count + 1) * 1.0 / (n + dict.size());
			Pwk_C.put(word, Pword_v);
		}
		
		//2. L:
		double Pl = docsL.size() * 1.0 / examples.size();
		n = 0;
		for (int i = 0; i < docsL.size(); i ++) {
			int index = docsL.get(i);
			List<String> set = examples.get(index);
			n += set.size();
			for (int j = 0; j < set.size(); j ++) {
				String word = set.get(j);
				Pwk_L.put(word, Pwk_L.get(word) + 1);
			}
		}
		for (String word: Pwk_L.keySet()) {
			Double count = Pwk_L.get(word);
			double Pword_v = (count + 1) * 1.0 / (n + dict.size());
			Pwk_L.put(word, Pword_v);
		}
		
		Map<Double, String> reverseC = new HashMap<>();
		List<Double> probC = new ArrayList<>();
		for (String str:Pwk_C.keySet()) {
			Double prob = Pwk_C.get(str);
			probC.add(prob);
			reverseC.put(prob, str);
		}
		Collections.sort(probC,Collections.reverseOrder());
		
		Map<Double, String> reverseL = new HashMap<>();
		List<Double> probL = new ArrayList<>();
		for (String str:Pwk_L.keySet()) {
			Double prob = Pwk_L.get(str);
			probL.add(prob);
			reverseL.put(prob, str);
		}
		Collections.sort(probL,Collections.reverseOrder());
		
		
		//print result
		DecimalFormat df = new DecimalFormat("0.0000");
		for(int i = 0; i < 20; i ++) {
			Double prob = probL.get(i);
			String word = reverseL.get(prob);
			System.out.println(word + " " + df.format(prob));
		}
		System.out.println();
		for(int i = 0; i < 20; i ++) {
			Double prob = probC.get(i);
			String word = reverseC.get(prob);
			System.out.println(word + " " + df.format(prob));
		}
	}
}
