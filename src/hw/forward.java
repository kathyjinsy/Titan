package hw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class forward {
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
	
	private static double run_forward(int pattern_i) {
		List<String> pattern = o.get(pattern_i);
		int M = pattern.size();
		int N = a.size();
				
		//initialize alpha table M*N
		List<Double> last_row = new ArrayList<>();
		String token0 = pattern.get(0);
		for (int i = 0; i < N; i ++) {
			String state = a_ref.get(i);
			double pi_i = pi_init.get(state);
			Map<String, Double> cur_bline = b.get(state);
			double bi_o0 = cur_bline.get(token0);
			last_row.add(Math.log(pi_i) + Math.log(bi_o0));
		}
		
		for (int i = 1; i < M; i ++) {
			List<Double> cur_row = new ArrayList<>();
			String token = pattern.get(i);
			for(int j = 0; j < N; j ++){
				String state = a_ref.get(j);
				double bi_o_cur = b.get(state).get(token);
				double sum = 1;
				for (int k = 0; k < N; k ++) {
					String prev_state = a_ref.get(k);
					double akj = a.get(prev_state).get(state);
					double curval = last_row.get(k) + Math.log(akj);
					sum = sum > 0 ? curval : logsum(sum, curval);
				}
				cur_row.add(Math.log(bi_o_cur) + sum);
			}
			last_row = cur_row;
		}
		
		double sum = 1;
		for (int i = 0; i < N; i ++) {
			sum = sum > 0 ? last_row.get(i) : logsum(sum, last_row.get(i));
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
			System.out.println(run_forward(i));
		}
	}
}
