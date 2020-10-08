public class Trie {	
	protected char[] letters;
	protected Node root = null;
	private int numPtrs;

	public Trie(char[] letters) {
		this.letters = letters;
		this.numPtrs = letters.length + 1;
	}


	//Provided Helper functions
	
	private int index(char c) {
		for (int k = 0; k < letters.length; k++) {
			if (letters[k] == (c)) {
				return k+1;
			}
		}
		return -1;
	}
	
	private char character(int i) {
		if (i == 0) {
			return '#';
		} else {
			return letters[i-1];
		}
	}
	
	private String nodeToString(Node node, boolean debug) {
		if (node.isLeaf) {
			return node.key;
		}
		else {
			String res = "";
			for (int k = 0; k < node.ptrs.length; k++) {
				if (node.ptrs[k] != null) {
					res += " (" + character(k) + ",1) ";
				} else if (debug) {
					res += " (" + character(k) + ",0) ";
				}
			}
			return res;
		}
	}

	public void print(boolean debug) {
		Queue queue = new Queue();
		Node n = root;
		if (n != null) {
			n.level = 1;
			queue.enq(n);
			while (!queue.isEmpty()){
				n = queue.deq();
				System.out.print("Level " + n.level + " [");
				System.out.print(nodeToString(n, debug));
				System.out.println("]");
				for (int k = 0; k < n.ptrs.length; k++) {
					if (n.ptrs[k] != null) {
						n.ptrs[k].level = n.level+1;
						queue.enq(n.ptrs[k]);
					}
				}
			}
		}
	}


	////// You may not change any code above this line //////

	////// Implement the functions below this line //////

	
	// Function to insert the given key into the trie at the correct position.
	public void insert(String key) {
		
		// Your code goes here
		if(true){
			if(this.root==null){
				this.root = new Node(numPtrs); //create a node for the root
				this.root.ptrs[index(key.charAt(0))] = new Node(key,numPtrs); //create a new leaf immediatley
			}
			else{
				Node curLayer = this.root, prevLayer = null; 
				int layer = 0; //corresponds to the n+1 char in the key
				//traverse to the node where insertion happens
				//System.out.println("Key being inserted: "+key);
				while(curLayer.ptrs[index(key.charAt(layer))] != null && layer < key.length()-1){
					prevLayer = curLayer; curLayer = curLayer.ptrs[index(key.charAt(layer))]; layer++;
				}
				//case 1 - simple leaf insertion of a leaf node to the node
				if(!curLayer.isLeaf && curLayer.ptrs[index(key.charAt(layer))] == null){
					//System.out.println("CASE 1 - at layer "+layer);
					curLayer.ptrs[index(key.charAt(layer))] = new Node(key,numPtrs);
				}
				//case 2 - end of word reached before leaf
				else if(!curLayer.isLeaf && layer == key.length()-1){
					//the word has finished in this block, so go to the next node and add the end word
					curLayer.ptrs[index(key.charAt(key.length()-1))].ptrs[0] = new Node(key,numPtrs); //make a new node corresponding to the end symbol
				}
				//case 3 - leaf node reached - the leaf is not the word
				else if(curLayer.isLeaf && curLayer.key != key){
					//System.out.println("CASE 3");
					String temp = curLayer.key;
					//prev points to the parent - can never be null if there is a root
					//remove the current leaf, add a non-leaf node and then insert both values, first the original key. This is recursive, so if it needs be done multiple times, it will
					prevLayer.ptrs[index(key.charAt(layer-1))] = new Node(numPtrs);
					this.insert(temp);
					this.insert(key);
				}
			} 
			

		}


	}
	

	// Function to determine if a node with the given key exists.
	public boolean contains(String key) {
		//System.out.println("looking for "+key);
		if(this.root==null) return false;
		Node s = this.root;
		int layer = 0;
		while(s!=null && !s.isLeaf){
			int letter = (layer<key.length())?index(key.charAt(layer)):0;
			s = (letter==-1)?null:s.ptrs[letter];//go to the character specified,
			layer++;
		}
		//either points to leaf or null
		//System.out.println(layer);
		if (s==null) return false;
		else if (!s.isLeaf) return false;
		else return (s.key==key);
		
	}

	static int tally;
	// Function to print all the keys in the trie in alphabetical order.
	public void printKeyList(){
		tally=0;
		System.out.println(this.printKeyList(this.root));
	}

	
	//Helper functions
	private String printKeyList(Node n){
		String result = "";
		if(n!=null){
			if(n.isLeaf){result+=n.key+" "; tally++;}
			else for(int i=0; i<numPtrs; i++){
				result+=printKeyList(n.ptrs[i]);
			}
		}
		return result;
	}


}