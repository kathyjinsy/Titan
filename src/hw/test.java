package hw;

public class test {
	public static void getIntegerComplement(int n) {
        if (n == 0) {
            System.out.print(1);
            return;
        }
        
        int sum = 0;
        int count = 0;
        while (n != 0) {
            int curbit = n & 1;
            int flippedbit = 1 - curbit;
            System.out.print(curbit + " " + flippedbit);
            sum += flippedbit *Math.pow(2, count);
            n = n >> 1;
            count ++;
        }
        
        System.out.println(sum);
    }
	
	public static void main(String[] args){
		getIntegerComplement(100);
	}
}
