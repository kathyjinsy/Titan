package hw;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 * Algorithm: 
 * Step1: construct a network graph: Find all 'c','a','t','s' in the graph
 * and make a graph node for each of these letters. Add an edge between a
 * pair of c-a, a-t, t-s if this pair of letters are adjacent on the input
 * board. Connect all c's with source and all s's with sink. All edges are
 * of capacity 1.(Note that in case of repeated use of some letters on 
 * multiple "cats", we add a helper node for vertex 'a' and 't').
 * For example, input = xcxx
 * 						cats
 * 						xtxx
 * 						xsxx
 * Corresponding network:
 * 		  --- c(0,1) 						--- t(1,2) --- t'(1,2) --- s(1,3)
 * source 			 --- a(1,1) --- a'(1,1) 								  --- sink
 * 		  --- c(1,0)						--- t(2,1) --- t'(2,1) --- s(3,1)
 * Step2: Apply Ford-Fulkerson Algorithm to find the max flow from source 
 * to sink.
 * 
 * Space Complexity:
 * Number of nodes in network = O(∑#'c','a','t','s') = V
 * Number of edges in network = O(V^2) = E
 * 
 * Time Complexity:
 * Construct the graph in O(rc); FF in O(max_flow * E)
 * 
 * @author siyingj@andrew.cmu.edu
 *
 */

class GraphNode{
	public char ch;
	public int index;
	public List<Integer> neighbors;
	public List<Integer> capacities;
	public int row;
	public int col;
	
	GraphNode(char ch, int index, List<Integer> neighbors, List<Integer> capacities, int row, int col) {
		this.ch = ch;
		this.index = index;
		this.neighbors = neighbors;
		this.capacities = capacities;
		this.row = row;
		this.col = col;
	}
}

public class cats {
	private static List<GraphNode> network;
	
	private static boolean hasEdge(GraphNode cur, GraphNode prev) {
		char cur_char = cur.ch;
		boolean next_to = (cur.row == prev.row && Math.abs(cur.col - prev.col) == 1)
				|| (cur.col == prev.col && Math.abs(cur.row - prev.row) == 1);
		if (!next_to) {
			return false;
		}
		if (cur_char == 'a') {
			return prev.ch == 'c';
		} else if (cur_char == 't') {
			return prev.ch == 'b';
		} else if (cur_char == 's') {
			return prev.ch == 'u';
		} else {
			System.out.println("error char: " + cur_char);
			return false;
		}
	}
	
	private static void connect(GraphNode cur, GraphNode prev) {
		prev.neighbors.add(cur.index);
		prev.capacities.add(1);
		cur.neighbors.add(prev.index);
		cur.capacities.add(0);
	}
	
	private static void construct(GraphNode source, List<String> input) {
		//add c,a,t,s
		int count = 1;
		for(int i = 0; i < input.size(); i ++) {
			String line = input.get(i);
			for (int j = 0; j < line.length(); j ++) {
				char c = line.charAt(j);
				if (c == 'c' || c == 'a' || c == 't' || c == 's') {
					List<Integer> curlist = new ArrayList<>();
					List<Integer> caps = new ArrayList<>();
					GraphNode node = new GraphNode(c, count, curlist, caps, i, j);
					network.add(node);
					count ++;
					//add helper node b,u
					if (c == 'a' || c == 't') {
						GraphNode prev = network.get(count - 1);
						
						char c_next = (char)(c + 1);
						List<Integer> curnei = new ArrayList<>();
						List<Integer> curcap = new ArrayList<>();
						GraphNode cur = new GraphNode(c_next, count, curnei, curcap, i, j);
						network.add(cur);
						connect(cur, prev);
						count ++;
					}
				}
			}
		}
		
		List<Integer> nei = new ArrayList<>();
		List<Integer> cap = new ArrayList<>();
		GraphNode sink = new GraphNode('1', count, nei, cap, -1, -1);
		network.add(sink);
		
		//add b,u as helper node to ensure a and t are used only once.
		for (int i = 1; i < count; i ++) {
			GraphNode curNode = network.get(i);
			if (curNode.ch == 'c') {
				connect(curNode, source);
				continue;
			} else if (curNode.ch == 's') {
				connect(sink, curNode);
			} else if (curNode.ch == 'b' || curNode.ch == 'u') {
				continue;
			} 
			
			// c-a, b-t, u-s
			for (int j = 0; j < count; j ++) {
				if (j == i) {
					continue;
				}
				
				GraphNode prevNode = network.get(j);
				if (hasEdge(curNode, prevNode)) {
					connect(curNode, prevNode);
				}
			}
		}
	}
	
	private static boolean bfs(int end, List<Integer> path) {
		boolean[] visited = new boolean[network.size()];
		visited[0] = true;
		
		Queue<Integer> q = new LinkedList<>();
		q.offer(0);
		while (!q.isEmpty()) {
			Integer cur = q.poll();
			GraphNode curNode = network.get(cur);
			for (int i = 0; i < curNode.neighbors.size(); i ++) {
				Integer neighbor = curNode.neighbors.get(i);
				if (!visited[neighbor] && curNode.capacities.get(i) > 0) {
					visited[neighbor] = true;
					q.add(neighbor);
					path.set(neighbor, cur);
				}
			}
		}
		
		return visited[network.size() - 1];
	}
	
	private static int ff(int end) {
		List<Integer> path = new ArrayList<>();
		for (int i = 0; i < network.size(); i ++) {
			path.add(-1);
		}
		
		int max = 0;
		while (bfs(end, path)) {
			//find minimum residual capacity on the path
			int temp_cap = Integer.MAX_VALUE;
			int cur = network.size() - 1;
			while (cur > 0) {
				int prev = path.get(cur);
				GraphNode prevNode = network.get(prev);
				int cur_cap_index = prevNode.neighbors.indexOf(cur);
				temp_cap = Math.min(temp_cap, prevNode.capacities.get(cur_cap_index));
				cur = prev;
			}
			
			//update residual capacity on the path
			cur = network.size() - 1;
			while (cur > 0) {
				int prev = path.get(cur);
				GraphNode prevNode = network.get(prev);
				int cur_cap_index = prevNode.neighbors.indexOf(cur);
				int new_cap = prevNode.capacities.get(cur_cap_index) - temp_cap;
				prevNode.capacities.set(cur_cap_index, new_cap);
				GraphNode curNode = network.get(cur);
				cur_cap_index = curNode.neighbors.indexOf(prev);
				new_cap = curNode.capacities.get(cur_cap_index) + temp_cap;
				curNode.capacities.set(cur_cap_index, new_cap);
				cur = prev;
			}
			
			//update max flow
			max += temp_cap;
		}
		return max;
	}

	public static void main(String[] args) {
		
		/**
		Scanner scan = new Scanner(System.in);
        String line = scan.nextLine();
        String[] rc = line.split("\\s+");
        int r = Integer.parseInt(rc[0]);
        List<String> input = new ArrayList<>();
        for (int i = 0; i < r; i ++) {
        	line = scan.nextLine();
        	input.add(line);
        }
        */
		List<String> input = new ArrayList<>();
		input.add("cax");
		input.add("ats");
		input.add("csx");
		
		//construct the network
		network = new ArrayList<>();
		
		List<Integer> nlist = new ArrayList<>();
		List<Integer> clist = new ArrayList<>();
		GraphNode source = new GraphNode('0', 0, nlist, clist, -1, -1);
		network.add(source);
		construct(source, input);
		
		int res = ff(network.size() - 1);
		System.out.println(res + " cats");
	}
}
