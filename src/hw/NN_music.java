package hw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NN_music {
	private static double normalizeInput(String a, int idx) {
		if (idx == 0) {
			double d = Double.parseDouble(a);
			return (d - 1900.0) * 0.01 * 2 - 1;
		} else if (idx == 1) {
			double d = Double.parseDouble(a);
			return d / 7.0 * 2 - 1;
		} else {
			if (a.equals("yes")) {
				return 1.0;
			} else {
				return -1.0;
			}
		}
	}
	
	private static double normalizeOutput(String a) {
		if (a.equals("yes")) {
			return 1.0;
		} else {
			return 0.0;
		}
	}
	
	private static String parseOutput(double d) {
		if (Math.abs(d - 1) < Math.abs(d)) {
			return "yes";
		} else {
			return "no";
		}
	}
	
	public static void main(String[] args) {
		//pre-processing 1.get and normalize input
		String file1 = args[0];
		FileReader fileReader;
		String line;
		boolean isFirstLine = true;
		List<List<Double>> list = new ArrayList<>();
		try {
			fileReader = new FileReader(file1);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			try {
				while((line = bufferedReader.readLine()) != null) {
					if (!isFirstLine) {
						String[] curarr = line.split(",");
						List<Double> curlist = new ArrayList<>();
						for (int i = 0; i < curarr.length; i ++) {
							curlist.add(normalizeInput(curarr[i], i));
						}
						list.add(curlist);
					}
					isFirstLine = false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		double[][] input = new double[list.size()][list.get(0).size()];
		for (int i = 0; i < list.size(); i ++) {
			for (int j = 0; j < list.get(0).size(); j ++) {
				input[i][j] = list.get(i).get(j);
			}
		}
		
		// 2. get and normalize output
		double[] target = new double[list.size()];
		String file2 = args[1];
		int count = 0;
		try {
			fileReader = new FileReader(file2);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			try {
				while((line = bufferedReader.readLine()) != null) {
					target[count] = normalizeOutput(line);
					count ++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// Applying NN to train
		NN nn = new NN(input, target);
		nn.train();
		System.out.println("TRAINING COMPLETED! NOW PREDICTING.");
		
		// Predicting result
		String file3 = args[2];
		isFirstLine = true;
		try {
			fileReader = new FileReader(file3);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			try {
				while((line = bufferedReader.readLine()) != null) {
					if (! isFirstLine) {
						String[] curarr = line.split(",");
						List<Double> curlist = new ArrayList<>();
						for (int i = 0; i < curarr.length; i ++) {
							curlist.add(normalizeInput(curarr[i], i));
						}
						double[] dev = new double[curlist.size()];
						for (int i = 0; i < curlist.size(); i ++) {
							dev[i] = curlist.get(i);
						}
						double predicted = nn.predict(dev);
						System.out.println(parseOutput(predicted));
					}
					isFirstLine = false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
