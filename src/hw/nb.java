package hw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class nb {
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
		
		//read test data
		List<String> real_label = new ArrayList<>();
		List<String> estimated_label = new ArrayList<>();
		List<List<String>> positions = new ArrayList<>();
		String test_files = args[1];
		try {
			fileReader = new FileReader(test_files);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				String test_file = line.trim();
				String category = test_file.charAt(0) == 'c' ? "C" : "L";
				real_label.add(category);
				FileReader fileReader_test = new FileReader(test_file);
				BufferedReader bufferedReader_test = new BufferedReader(fileReader_test);
				String line_test;
				List<String> test_set = new ArrayList<>();
				while((line_test = bufferedReader_test.readLine()) != null) {
					String word = line_test.trim().toLowerCase();
					if (dict.contains(word)) {
						test_set.add(word);
					}
				}
				positions.add(test_set);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		//process test data
		for (int i = 0; i < positions.size(); i ++) {
			List<String> set = positions.get(i);
			double probC = Math.log(Pc);
			double probL = Math.log(Pl);
			for (int j = 0; j < set.size(); j ++) {
				String word = set.get(j);
				double pjc = Pwk_C.get(word);
				double pjl = Pwk_L.get(word);
				probC += Math.log(pjc);
				probL += Math.log(pjl);
			}
			if (probC > probL) {
				estimated_label.add("C");
			} else {
				estimated_label.add("L");
			}
		}
		
		//print result
		int count = 0;
		for (int i = 0; i < estimated_label.size(); i ++) {
			String label = estimated_label.get(i);
			System.out.println(label);
			if (label.equals(real_label.get(i))) {
				count ++;
			}
		}
		DecimalFormat df = new DecimalFormat("0.0000");
		System.out.println("Accuracy: " + df.format(count * 1.0 / real_label.size()));
	}
}
