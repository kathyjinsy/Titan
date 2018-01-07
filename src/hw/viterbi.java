package hw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class viterbi {
	//transition probability matrix
	private static Map<String, Map<String, Double>> a;
	private static Map<Integer, String> a_ref;
	//emission probability matrix
	private static Map<String, Map<String, Double>> b;
	//initial state probability distribution
	private static Map<String, Double> pi_init;
	//observed sequence
	private static List<List<String>> o;
	
	private static String run_viterbi(int pattern_i) {
		List<String> pattern = o.get(pattern_i);
		int M = pattern.size();
		int N = a.size();
				
		//initialize alpha table M*N
		Map<String, List<String>> old_path = new HashMap<>();
		Map<String, List<String>> cur_path = new HashMap<>();
		List<Double> last_row = new ArrayList<>();
		String token0 = pattern.get(0);
		for (int i = 0; i < N; i ++) {
			String state = a_ref.get(i);
			double pi_i = pi_init.get(state);
			Map<String, Double> cur_bline = b.get(state);
			double bi_o0 = cur_bline.get(token0);
			double curval = Math.log(pi_i) + Math.log(bi_o0);
			last_row.add(curval);
			List<String> list = new ArrayList<>();
			list.add(state);
			old_path.put(state, list);
		}
		
		for (int i = 1; i < M; i ++) {
			List<Double> cur_row = new ArrayList<>();
			String token = pattern.get(i);
			for(int j = 0; j < N; j ++){
				String state = a_ref.get(j);
				double bi_o_cur = b.get(state).get(token);
				double maxVal = Integer.MIN_VALUE;
				String maxState = null;
				for (int k = 0; k < N; k ++) {
					String prev_state = a_ref.get(k);
					double akj = a.get(prev_state).get(state);
					double curval = last_row.get(k) + Math.log(akj);
					if (curval > maxVal) {
						maxVal = curval;
						maxState = prev_state;
					}
				}
				double curStateVal = Math.log(bi_o_cur) + maxVal;
				cur_row.add(curStateVal);
				List<String> curlist = new ArrayList<>(old_path.get(maxState));
				curlist.add(state);
				cur_path.put(state, curlist);
			}
			last_row = cur_row;
			old_path = cur_path;
			cur_path = new HashMap<>();
		}

		String maxState = null;
		double maxVal = Integer.MIN_VALUE;
		for (int i = 0; i < N; i ++) {
			if (last_row.get(i) > maxVal) {
				maxVal = last_row.get(i);
				maxState = a_ref.get(i);
			}
		}
		List<String> maxPath = old_path.get(maxState);
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < M; i ++) {
			String token = pattern.get(i);
			String tag = maxPath.get(i);
			sb.append(token);
			sb.append("_");
			sb.append(tag);
			if (i < M - 1) {
				sb.append(" ");
			}
		}
		
		return sb.toString();
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
			System.out.println(run_viterbi(i));
		}
	}
}
