package hw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class backward {
	//transition probability matrix
	private static Map<String, Map<String, Double>> a;
	private static Map<Integer, String> a_ref;
	//emission probability matrix
	private static Map<String, Map<String, Double>> b;
	//initial state probability distribution
	private static Map<String, Double> pi_init;
	//observed sequence
	private static List<List<String>> o;
	
	private static double logsum(double left, double right) {
		if (right < left) {
			return left + Math.log1p(Math.exp(right - left));
		} else if (left < right) {
			return right + Math.log1p(Math.exp(left - right));
		} else {
			return left + Math.log1p(1);
		}
	}
	
	private static double run_backward(int pattern_i) {
		List<String> pattern = o.get(pattern_i);
		int M = pattern.size();
		int N = a.size();
				
		//initialize beta table M*N
		List<Double> next_row = new ArrayList<>();
		String token0 = pattern.get(0);
		for (int i = 0; i < N; i ++) {
			next_row.add(Math.log(1));
		}
		
		for (int i = M - 2; i >= 0; i --) {
			List<Double> cur_row = new ArrayList<>();
			String token = pattern.get(i + 1);
			for(int j = 0; j < N; j ++){
				String state = a_ref.get(j);
				double sum = 1;
				for (int k = 0; k < N; k ++) {
					String next_state = a_ref.get(k);
					double ajk = a.get(state).get(next_state);
					double nextval = next_row.get(k) + Math.log(ajk);
					double beta_next = b.get(next_state).get(token);
					double curval = nextval + Math.log(beta_next);
					sum = sum > 0 ? curval : logsum(sum, curval);
				}
				cur_row.add(sum);
			}
			next_row = cur_row;
		}
		
		double sum = 1;
		for (int i = 0; i < N; i ++) {
			String state = a_ref.get(i);
			String token = pattern.get(0);
			double curval = Math.log(pi_init.get(state)) +
					Math.log(b.get(state).get(token)) + next_row.get(i);
			sum = sum > 0 ? curval : logsum(sum, curval);
		}
		
		return sum;
	}
	
	public static void main(String[] args) {
		//init data structures
		a = new HashMap<>();
		a_ref = new HashMap<>();
		b = new HashMap<>();
		pi_init = new HashMap<>();
		o = new ArrayList<>();
		
		//read in a, b, pi_init and o
		FileReader fileReader;
		String line;
		try {
			fileReader = new FileReader(args[1]);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int count = 0;
			while((line = bufferedReader.readLine()) != null) {
				String[] splited = line.split("\\s+");
				String tag = splited[0];
				Map<String, Double> inner = new HashMap<>();
				for (int i = 1; i < splited.length; i ++) {
					String curline = splited[i];
					String[] seperated = curline.split(":");
					String innertag = seperated[0];
					Double innerval = Double.parseDouble(seperated[1]);
					inner.put(innertag, innerval);
				}
				a.put(tag, inner);
				a_ref.put(count, tag);
				count ++;
			}
			
			fileReader = new FileReader(args[2]);
			bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				String[] splited = line.split("\\s+");
				String tag = splited[0];
				Map<String, Double> inner = new HashMap<>();
				for (int i = 1; i < splited.length; i ++) {
					String curline = splited[i];
					String[] seperated = curline.split(":");
					String innertag = seperated[0];
					Double innerval = Double.parseDouble(seperated[1]);
					inner.put(innertag, innerval);
				}
				b.put(tag, inner);
			}
			
			fileReader = new FileReader(args[3]);
			bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				String[] splited = line.split("\\s+");
				String tag = splited[0];
				Double val = Double.parseDouble(splited[1]);
				pi_init.put(tag, val);
			}
			
			fileReader = new FileReader(args[0]);
			bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				String[] splited = line.split("\\s+");
				List<String> inner = new ArrayList<>();
				for (int i = 0; i < splited.length; i ++) {
					inner.add(splited[i]);
				}
				o.add(inner);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < o.size(); i ++) {
			System.out.println(run_backward(i));
		}
	}
}
