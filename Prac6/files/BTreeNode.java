@SuppressWarnings("unchecked")
class BTreeNode<T extends Comparable<T>> {
	boolean leaf;
	int keyTally;
	Comparable<T> keys[];
	BTreeNode<T> references[];
	int m;
	static int level = 0;

	// Constructor for BTreeNode class
	public BTreeNode(int order, boolean leaf1)
	{
    		// Copy the given order and leaf property
		m = order;
    		leaf = leaf1;
 
    		// Allocate memory for maximum number of possible keys
    		// and child pointers
    		keys =  new Comparable[2*m-1];
    		references = new BTreeNode[2*m];
 
    		// Initialize the number of keys as 0
    		keyTally = 0;
	
			//*/ Set references to null
			for(int i=1; i<=2*m; i++){
				if(i<=2*m-1) keys[i-1] = null; 
				references[i-1] = null;
			}
			//*/
	}

	// Function to print all nodes in a subtree rooted with this node
	public void print()
	{
		level++;
		if (this != null) {
			System.out.print("Level " + level + " ");
			this.printKeys();
			System.out.println();

			// If this node is not a leaf, then 
        		// print all the subtrees rooted with this node.
        		if (!this.leaf)
			{	
				for (int j = 0; j < 2*m; j++)
    				{
        				if (this.references[j] != null)
						this.references[j].print();
    				}
			}
		}
		level--;
	}

	// A utility function to print all the keys in this node
	private void printKeys()
	{
		System.out.print("[");
    		int j=0;
			for (int i = 0; i < this.keyTally; i++)
    		{
        		j=i;
				System.out.print(" " +((references[i]!=null)?references[i]:"")+this.keys[i]);
    		}
			System.out.print(" " +((references[j+1]!=null)?references[j+1]:""));
 		System.out.print("]");
	}

	// A utility function to give a string representation of this node
	public String toString() {
		String out = "[";
		for (int i = 1; i <= (this.keyTally-1); i++)
			out += keys[i-1] + ",";
		out += keys[keyTally-1] + "] ";
		return out;
	}

	////// You may not change any code above this line //////

	////// Implement the functions below this line //////

	// Function to insert the given key in tree rooted with this node
	public BTreeNode<T> insert(T key)
	{
        	// Your code goes here
			if(search(key) != null) return this; //already in the tree
		
			if(this.keyTally == 2*m-1 && this.insertingIntoFullLeafNode(key)){ //if root is full
				BTreeNode<T> newRoot = new BTreeNode(m,false);
				newRoot.references[0]=this;
				split(newRoot,0,this);
				
				
				insertNonFullRoot(newRoot,key);
				
				
				return newRoot;
			}
			else{
				insertNonFullRoot(this,key);
				return this;
			}	
	}

	// Function to search a key in a subtree rooted with this node
    public BTreeNode<T> search(T key)
    {  
		// Your code goes here
		if(this != null){
			int i =0;
			while(i<keyTally && keys[i].compareTo(key) < 0){i++;}
			//System.out.println(keys[i]);
			if(i<2*m-1){if(keys[i] == key) return this;}
			else return (references[i] != null)?references[i].search(key):null;
		}
		return null;
	}

	// Function to traverse all nodes in a subtree rooted with this node
	public void traverse(){
		System.out.println(this.traverseRec());
	}
	
	public String traverseRec()
	{
			String s = "";
			// Your code goes here
			if (this==null) return "";
			else{
				for(int i=0; i<keyTally+1; i++){
					if(i<keyTally){
						s+=((references[i]==null)?"":references[i].traverseRec())+" "+keys[i];
					}
					else{
						s+=((references[i]==null)?"":references[i].traverseRec());
					}
				}
				return s;
			}
			
	}

	//======================== HELPERS ==================================
	public void insertNonFullRoot(BTreeNode<T> x, T key){
		if (x==null) return;

		int i = x.keyTally;

		if(x.leaf)
		{
			//System.out.println("Called for "+key+" in node "+x);
			while(i >= 1 && x.keys[i-1].compareTo(key)>0)
			{
				x.keys[i] = x.keys[i-1]; //shift values to make room
				i--;
			}
			x.keys[i] = key;
			x.keyTally++;
		}
		else
		{
			int j = 0;
			while(j < x.keyTally  && x.keys[j].compareTo(key) < 0){j++;}
			if(x.references[j].keyTally == 2*m-1)
			{
				split(x,j,x.references[j]);//call split on node x's ith child
				j=(x.keys[j].compareTo(key) <0)?j+1:j;
			}
			insertNonFullRoot(x.references[j],key);
		}
	}


	public boolean setLeafStatus(){
		//if any non null entry encountered, change leaf status
		leaf = true;
		for(int i=0; i<=keyTally+1; i++){
			if(references[i-1] != null){
				leaf =false;
				break;
			}
		}
		return leaf;
	}

	private void split(BTreeNode<T> x, int i, BTreeNode<T> y){
		BTreeNode<T> z = new BTreeNode(m,y.leaf);
		z.keyTally = m-1;

		for(int j = 0; j < m-1; j++) //use m-1 as the midpoint
		{
			z.keys[j] = y.keys[j+m]; //copy right of y into left of z

		}
		if(!y.leaf)
		{
			for(int k = 0; k < m; k++){z.references[k] = y.references[k+m]; }
		}

		y.keyTally = m-1; 

		for(int j = x.keyTally ; j> i ; j--){x.references[j+1] = x.references[j];}
		x.references[i+1] = z;

		for(int j = x.keyTally; j> i; j--){x.keys[j + 1] = x.keys[j];}
		x.keys[i] = y.keys[m-1];

		y.keys[m-1] = null; //erase values

		for(int j = 0; j < m-1; j++){y.keys[j+m] = null;}
		x.keyTally++;
	}

	private boolean insertingIntoFullLeafNode(T key){
		//find the node youre going to insert it in
		int i = 0;
		while(i<this.keyTally && keys[i].compareTo(key)<0){i++;}
		if(references[i]==null){
			//inserting in this node
			return (this.leaf)&&(this.keyTally==2*m-1);

		}
		else return references[i].insertingIntoFullLeafNode(key);
	}


}