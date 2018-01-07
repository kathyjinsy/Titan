package hw;

import java.util.ArrayList;
import java.util.List;

public class NN {
	private double[][] input;
	private double[] target; 
	private int input_node_num;
	private double[][] weight_l1;
	private double[] weight_l2;
	
	private final static int hidden_node_num = 4;
	private final static double alpha = 0.1;
	private final static int limit = 10;
	
	private double getRandomWeight() {
		return -1 + Math.random() * 2;
	}
	
	private double activate(double z) {
		return 1.0 / (1 + Math.exp(-z));
	}
	
	private double deriv_activate(double z) {
		return activate(z) * (1 - activate(z));
	}
	
	private double getOutDelta(double y, double h, double z) {
		return - (y - h) * deriv_activate(z);
	}
	
	private double getHiddenDelta(double delta_next, double w, double z) {
		return delta_next * w * deriv_activate(z);
	}
	
	private List<List<Double>> feedForwardPass(int idx) {
		List<List<Double>> res = new ArrayList<>();
		List<Double> h = new ArrayList<>();
		List<Double> z = new ArrayList<>();
		double[] curinput = input[idx];
		
		//compute hidden layer:
		for (int i = 0; i < hidden_node_num; i ++) {
			double sum = 0.0;
			for (int j = 0; j < input_node_num; j ++) {
				sum += weight_l1[i][j] * curinput[j];
			}
			z.add(sum);
			h.add(activate(sum));
		}
		
		//compute output layer:
		double sum = 0.0;
		for (int i = 0; i < hidden_node_num; i ++) {
			sum += weight_l2[i] * h.get(i);
		}
		z.add(sum);
		h.add(activate(sum));
		
		res.add(h);
		res.add(z);
		
		return res;
	}
	
	public NN (double[][] input, double[] target) {
		this.input = input;
		this.target = target;
		this.input_node_num = input[0].length;
		weight_l1 = new double[hidden_node_num][input_node_num];
		weight_l2 = new double[hidden_node_num];
		
		//initialize weights & bias
		for (int i = 0; i < hidden_node_num; i ++) {
			weight_l2[i] = getRandomWeight();
			for (int j = 0; j < input_node_num; j ++) {
				weight_l1[i][j] = getRandomWeight();
			}
		}
		
		for (int j = 0; j < input_node_num; j ++) {
			weight_l1[0][j] = 0.2;
			weight_l1[1][j] = 0.4;
			weight_l1[2][j] = 0.6;
		}
		for (int j = 0; j < hidden_node_num; j ++) {
			weight_l2[j] = 0.5;
		}
	}
	
	public void train() {
		int count = 0;
		while (count < limit) {
			double[][] trW_l1 = new double[hidden_node_num][input_node_num];
			double[] trW_l2 = new double[hidden_node_num];
			double trError = 0.0;
			
			for (int i = 0; i < target.length; i ++) {
				//feed forward pass
				List<List<Double>> res = feedForwardPass(i);
				List<Double> h = res.get(0);
				List<Double> z = res.get(1);
				//System.out.println("h:" + h.get(h.size() - 1));
				trError += Math.pow((target[i] - h.get(h.size() - 1)), 2);
				
				//backpropagating
				double outDelta = getOutDelta(target[i], h.get(h.size() - 1), z.get(z.size() - 1));
				double[] hiddenDelta = new double[hidden_node_num];
				for (int j = 0; j < hidden_node_num; j ++) {
					hiddenDelta[j] = getHiddenDelta(outDelta, weight_l2[j], z.get(j));
				}
				
				for (int j = 0; j < hidden_node_num; j ++) {
					trW_l2[j] += outDelta * h.get(j);
					for (int k = 0; k < input_node_num; k ++) {
						trW_l1[j][k] += hiddenDelta[j] * input[i][k];
					}
				}
			}
			
			//gradient descent
			for (int i = 0; i < hidden_node_num; i ++) {
				weight_l2[i] -= alpha * (1.0 / target.length * trW_l2[i]); 
				for (int j = 0; j < input_node_num; j ++) {
					weight_l1[i][j] -= alpha * (1.0 / target.length * trW_l1[i][j]);
				}
			} 
			double error = 1.0 / target.length * trError;
			System.out.println(error);
			
			count ++;
		}
	}
	
	public double predict(double[] dev) {
		List<Double> h = new ArrayList<>();
		
		//compute hidden layer:
		for (int i = 0; i < hidden_node_num; i ++) {
			double sum = 0.0;
			for (int j = 0; j < input_node_num; j ++) {
				sum += weight_l1[i][j] * dev[j];
			}
			h.add(activate(sum));
		}
		
		//compute output layer:
		double sum = 0.0;
		for (int i = 0; i < hidden_node_num; i ++) {
			sum += weight_l2[i] * h.get(i);
		}
		double res = activate(sum);
		return res;
	}
}
