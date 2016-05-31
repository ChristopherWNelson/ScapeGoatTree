// Christopher Nelson
// CS 450 Design & Analysis of Algorithms

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

public class ScapeGoatTree {
	
	// Nodes of the scapegoat tree
	static class Node { 
        public int key;
        public Node left, right, parent;
        public Node(int k) {
            key = k;
            left = right = parent = null;
        }
    }
	
	// The scapegoat tree itself
	static class SGT {
		public Node root;
		public float alpha;
		public int size, max_size;
		public SGT(float a, int k){
			root = new Node(k);
	    	alpha = a;
	    	size = 0;
	    	max_size = 0;	
		}
		
		// returns the size of a node
		private int sizeOf(Node x){
			if (x == null)
				return 0;
			return (1 + sizeOf(x.left) + sizeOf(x.right));
		}
		
		// find highest Scapegoat
		private Node findScapeGoat(Node x, int key){
			Node current = x;
			Float awt = alpha*sizeOf(current);
			if (awt > alpha*sizeOf(current.left) || awt > alpha*sizeOf(current.right)){
					return current;
			}
			else{
				if (key > current.key)
					return findScapeGoat(current.right, key);
				else
					return findScapeGoat(current.left, key);
			}
		}
		
		// helper function to determine if tree is a height balanced
		private double logb(double a, double b){
			return Math.log(a)/Math.log(b);
		}
		
		// a height balanced function
		private int aHeightBalanced(){
			return (int) Math.floor(logb(size, 1.0/alpha));
		}

		// convert bst to array
		private ArrayList<Node> inorderArray(Node node, ArrayList<Node> array){
			if(node == null)
				return null;
			inorderArray(node.left, array);
			array.add(node);
			inorderArray(node.right, array);
			return array;
		}
		
		// convert the array to BST
		private Node BSTArray(ArrayList<Node> array, int start, int end){
			if (start > end)
				return null;
			int mid = (int) Math.floor((start+end)/2);
			Node node = array.get(mid);
			node.left = BSTArray(array, start, mid-1);
			if (node.left != null)
				node.left.parent = node;
			node.right = BSTArray(array, mid+1, end);
			if (node.right != null)
				node.right.parent = node;
			return node;
		}
		
		// rebuild tree from scapegoat
		private Node rebuildTree(Node scapegoat){
			ArrayList<Node> array = new ArrayList<Node>();
			ArrayList<Node> inorder = inorderArray(scapegoat, array);
			Node newNode = BSTArray(inorder, 0, inorder.size()-1);
			return newNode;
		}
		
		// given an integer key, create a new node with key value
		// and insert it into the tree
		public void insert(int key){
			int depth = 0;
			Node node = new Node(key);
			Node current = root;
			Node previous = null;
			
			// insert node into tree
			while (current != null){
				previous = current;
				if (key > current.key)
					current = current.right;
				else
					current = current.left;
				depth += 1;
			}
			if (previous == null)
				root = node;
			else if (key > previous.key){
				previous.right = node;
				node.parent = previous;
			}
			else {
				previous.left = node;
				node.parent = previous;
			}
			size += 1;
			max_size = Math.max(size, max_size);
			
			// if node's count violates the a-height-balance
			// rebuild the tree
			if (depth > aHeightBalanced()){
				Node scapegoat = findScapeGoat(root, key);
				Node parentSG = scapegoat.parent;
				// if the scapegoat is the root
				if( scapegoat == root){
					root = rebuildTree(scapegoat);
					root.parent = null;
					max_size = size;
				}
				else {
					scapegoat = rebuildTree(scapegoat);
					if(scapegoat.key > parentSG.key){
						parentSG.right = scapegoat;
						scapegoat.parent = parentSG;
					}
					else{
						parentSG.left = scapegoat;
						scapegoat.parent = parentSG;
					}
				}
			}
		}
		
		// find a specified key in the tree if it exists
		// by iterating through the tree
		public boolean search(int key){
			Node x = root;
			boolean found = false;
			if (x == null)
				return found;
			while (x != null && !found){
				if (key < x.key)
					x = x.left;
				else if (key > x.key)
					x = x.right;
				else {
					found = true;
					break;
				}
			}
			return found;
		}
		
		// finds min node
		private Node min(Node x){
			Node current = x;
			while(current.left != null){
				current = current.left;
			}
			return current;
		}
		
		// helper function for delete by replacing x with y
		private void replace(Node x, Node y){
			if(x.parent == null)
				root = y;
			else if(x == x.parent.left)
				x.parent.left = y;
			else
				x.parent.right = y;
			if(y != null)
				y.parent = x.parent;
		}
		
		// delete the specified key from the tree
		public void delete (int key){
			Node current = root;
			if(current == null){
				return;
			}
			while(current != null){
				if(key == current.key){
					// right child only/no children
					if(current.left == null){
						replace(current, current.right);
						size -= 1;
					}
					// left child only
					else if(current.right == null){
						replace(current,current.left);
						size -= 1;
					}
					// two children
					else{
						Node minNode = min(current.right);
						if(minNode.parent != current){
							replace(minNode,minNode.right);
							minNode.right = current.right;
							minNode.right.parent = minNode;
							replace(current,minNode);
							minNode.left = current.left;
							minNode.left.parent = minNode;
							size -= 1;
						}
						else{
							replace(current,minNode);
							size -= 1;
						}
					}
					if(size <= alpha*max_size){
						root = rebuildTree(root);
						root.parent = null;
						max_size = size;
					}
					return;
				}
				else if(key > current.key)
					current = current.right;
				else
					current = current.left;
			}
			return;
		}
		
		// serialize to print tree
	    private void serialize(Node tree, Vector<String> vec) {
	        if (tree == null)
	            vec.addElement(null);
	        else {
	            vec.addElement(Integer.toString(tree.key));
	            serialize(tree.left, vec);
	            serialize(tree.right, vec);
	        }
	    }
	        
	    public Vector<String> serialize() {
	        Vector<String> vec = new Vector<>();
	        serialize(root, vec);
	        return vec;
	    }
		
	}
    
    public static void main(String[] args) throws IOException{
    	// read text file "tree.txt"
    	FileInputStream fstream = new FileInputStream("tree.txt");
    	BufferedReader br = new BufferedReader(new InputStreamReader(fstream));    	
    	String strLine;
    	
    	// read first line for BuildTree command
    	strLine = br.readLine();
    	String [] tokensBT = strLine.split("\\s+");
    	
    	// first command is BuildTree, continue
    	if (tokensBT[0].equals("BuildTree")){
    		float alpha = Float.parseFloat(tokensBT[1]);
    		int keyBT = Integer.parseInt(tokensBT[2]);
    		SGT tree = new SGT(alpha, keyBT);
    	
    		while ((strLine = br.readLine()) != null){
    			int key;
    			String[] tokens = strLine.split("\\s+");	
    			
    			// insert
    			if (tokens[0].equals("Insert")){
    				key = Integer.parseInt(tokens[1]);
    				tree.insert(key);
    			}
    			
    			//search
    			else if (tokens[0].equals("Search")){
    				key = Integer.parseInt(tokens[1]);
    				if(tree.search(key))
    					System.out.println(key + " is in the scapegoat tree!");
    				else
    					System.out.println(key + " not found!");
    			}
    			
    			//delete
    			else if (tokens[0].equals("Delete")){
    				key = Integer.parseInt(tokens[1]);
    				tree.delete(key);
    			}
    			
    			// prints a tree
    			else if (tokens[0].equals("Print")){
    				// serialize and print tree
    	    		// Works in Firefox
    	    		Vector<String> st = tree.serialize();
    	            TreePrinter treePrinter = new TreePrinter(st);
    	            try {
    	                FileOutputStream out  = new FileOutputStream("tree.svg");
    	                PrintStream ps = new PrintStream(out);
    	                treePrinter.printSVG(ps);
    	            } catch (FileNotFoundException e) {}
    	            System.out.println("Tree has been printed to file tree.svg!");
    			}
    			
    			//
    			else if (tokens[0].equals("Done")){
    				br.close();
    				System.out.println("Done command read. Exiting...");
    				System.exit(1);
    			}
    		}
    		br.close();
    		
    	}
    	
    	// otherwise can't build tree, exit
    	else{
    		System.out.println("Error: First line must be BuildTree command");
    		System.exit(1);
    	}
    }
}
