import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class swapsort {
    private static List<Integer> parseArray(String input) {
        String[] nums = input.split(" ");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < nums.length; i ++) {
            list.add(Integer.parseInt(nums[i]));
        }
        return list;
    }
    
    private static List<List<Integer>> swap(List<Integer> input) {
        List<List<Integer>> output = new ArrayList<>();
        
        Set<Integer> set = new HashSet<>();
        int idx = 0;
        while (idx < input.size()) {
            if (set.contains(idx) || input.get(idx) == idx) {
                set.add(idx);
                idx ++;
            } else {
                int temp = input.get(idx);
                List<Integer> cur = new ArrayList<>();
                cur.add(idx);
                cur.add(temp);
                output.add(cur);
                input.add(idx, input.get(temp));
                input.add(temp, temp);
            }
        }
        
        return output;
    }
    
    /**
     * The algorithm is as follows:
     *       Scan the input array from index 0 :
     *         If the element on the current position is at its right position,
     *         continue to check the next element(i ++). Else, swap it with the element
     *         on the position it should be and repeat the process on the current
     *         current position(i unchanged).
     * It runs in O(n) because we read each element only once by using a set to
     * record the sorted position.
     */
    public static void main(String[] args) {
        //read and parse the input array
    Scanner scan = new Scanner(System.in);
        String line = scan.nextLine();
        line = scan.nextLine();
        List<Integer> input = parseArray(line);
        
        List<List<Integer>> output = swap(input);
        System.out.println(output.size());
        for(List<Integer> pair: output) {
            System.out.println(pair.get(0) + " " + pair.get(1));
        }
    }
}
