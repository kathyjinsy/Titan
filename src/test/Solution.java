package test;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Solution {
	private static void swap(char[] ch, int i, int j) {
	      char temp = ch[i];
	      ch[i] = ch[j];
	      ch[j] = temp;
	  }
	  
	  private static void reverse(char[] ch, int i, int j) {
	      while (i < j) {
	          swap(ch, i, j);
	          i ++;
	          j --;
	      }
	  }
	  
	  public static String reverseWords(String input) {
	    if (input == null || input.length() == 0) {
	        return input;
	    }
	    
	    char[] ch = input.toCharArray();
	    reverse(ch, 0, input.length() - 1);
	    
	    
	    int start = 0;
	    int end = 1;
	    while (end <= input.length()) {
	        if (end >= input.length() || ch[end] == ' ') {
	        	System.out.println(start);
	        	System.out.println(end);
	            reverse(ch, start, end - 1);
	            if (end == input.length()) {
	                break;
	            }
	            start = end + 1;
	            end = start;
	        } else {
	            end ++;
	        }
	    }
	    
	    return new String(ch);
	  }
	  
    public static void main(String[] args) {
    	String a = "abcdefghijklmnopqrstuvwxyzzabcdefghijklmnopqrstu";
    	String b = "qrstuvwxyzzabcdefghijklmnopqrstu";
    	System.out.println(reverseWords("an apple"));
    	
    	int [] arr1 = new int[]{1,0,1,0,0};
    	int[] arr2 = new int[]{1,0,1,1,1};
    	int[] arr3 = new int[]{1,1,1,1,1};
    	int[] arr4 = new int[]{1,0,0,1,0};
    	int[][] matrix = new int[4][4];
    	matrix[0] = arr1;
    	matrix[1] = arr2;
    	matrix[2] = arr3;
    	matrix[3] = arr4;
    	
    }
}
