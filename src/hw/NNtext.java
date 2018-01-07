package hw;

public class NNtext {
	public static void main(String[] args) {
		double[][] d1 = new double[1][3];
		double[] d3 = new double[]{1.5, 2.0, 3.0};
		d1[0] = d3;
		double[] d2 = new double[]{1};
		NN n = new NN(d1, d2);
		n.train();
	}
}
