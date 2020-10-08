/**
 * A B+ tree internal node
 * @param <TKey> the data type of the key
 * @param <TValue> the data type of the value
 */
class BPTreeInnerNode<TKey extends Comparable<TKey>, TValue> extends BPTreeNode<TKey, TValue> {
	
	protected Object[] references; 
	
	public BPTreeInnerNode(int order) {
		this.m = order;
		// The strategy used here first inserts and then checks for overflow. 
		// Thus an extra space is required i.e. m instead of m-1/m+1 instead of m.
		// You can change this if needed. 
		this.keys = new Object[m];
		this.references = new Object[m + 1];
	}
	
	@SuppressWarnings("unchecked")
	public BPTreeNode<TKey, TValue> getChild(int index) {
		return (BPTreeNode<TKey, TValue>)this.references[index];
	}

	public void setChild(int index, BPTreeNode<TKey, TValue> child) {
		this.references[index] = child;
		if (child != null)
			child.setParent(this);
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}

	////// You should not change any code above this line //////

	////// Implement functions below this line //////

	public boolean isFull(){
		return (this.getKeyCount() == m);
	}

	public BPTreeInnerNode<TKey, TValue> insertNew(TKey key, BPTreeNode<TKey, TValue> right ){
		//find the part in the array that preserves ordering
		int index = 0;
		while(index<getKeyCount() && this.getKey(index).compareTo(key)<0) index++;
		//index now points to where the value must go - move all the remaining values up;
		
		//adjust other keys and their right pointers
		for(int p=this.getKeyCount()-1; p>=index; p--){this.setKey(p+1,this.getKey(p)); this.setChild(p+2,this.getChild(p+1));}

		//add the new key and its right child
		this.setKey(index,key);
		this.setChild(index+1, right);

		this.keyTally++;

		//adjust if full
		if(this.isFull() && this.parentNode==null){ 
			
			BPTreeInnerNode<TKey, TValue> root = this.splitRoot();
			//root.printKeys();
			return root;
		}
		else if(this.isFull() && this.parentNode!=null){
			BPTreeInnerNode<TKey, TValue> root = this.splitIntoParent();
			//root.printKeys();
			while(root.parentNode!=null) root= (BPTreeInnerNode<TKey, TValue>) root.parentNode;
			return root;
		}
		
		else return this;

	}

	public BPTreeInnerNode<TKey, TValue> splitRoot(){
		//System.out.println("Working");
		//break up and make a new 
		BPTreeInnerNode<TKey, TValue> newRoot = new BPTreeInnerNode(m);
		//two smaller subarrays 
		BPTreeInnerNode<TKey, TValue> left = new BPTreeInnerNode(m);
		BPTreeInnerNode<TKey, TValue> right = new BPTreeInnerNode(m);
		left.rightSibling = right;
		right.leftSibling = left;
		int splitIndex = this.keyTally/2;

		newRoot.setChild(0,left); newRoot.setChild(1,right);
		left.parentNode = newRoot; right.parentNode = newRoot;

		//set the new root key as the split element
		newRoot.setKey(0,this.getKey(splitIndex));
		newRoot.keyTally = 1;

		//set the initial pointer of the right subarray to what the right reference of the splitindex was
		right.setChild(0,this.getChild(splitIndex+1));
		//set initial of left child to intial reference of first array
		left.setChild(0,this.getChild(0));

		//move the elements - but not the one in splitindex itself 
		for(int i=0; i<splitIndex; i++) left.insertNew(this.getKey(i),this.getChild(i+1));
		for(int j=splitIndex+1; j<keyTally; j++ ) right.insertNew(this.getKey(j),this.getChild(j+1)); 
		return newRoot;
	}

	public BPTreeInnerNode<TKey, TValue> splitIntoParent(){
		BPTreeInnerNode<TKey, TValue> right = new BPTreeInnerNode(m);
		
		int splitIndex = this.keyTally/2;
		TKey newKey = this.getKey(splitIndex);
		//this time around around, only move things from the latter half of the new array upwards;

		//System.out.println(newRoot.getKey(0));
		int rightBound = this.keyTally;
		right.setChild(0,this.getChild(splitIndex+1));

		for(int j=splitIndex; j<rightBound; j++ ){
			//move elements into new right subarray
			if(j!=splitIndex) right.insertNew(this.getKey(j),this.getChild(j+1));
			//delete them from old one
			this.setKey(j,null); keyTally--;
			this.setChild(j+1,null);
		}

		//now add the new key and reference to the parent
		BPTreeInnerNode<TKey, TValue> parent = (BPTreeInnerNode<TKey, TValue>) this.parentNode;
		parent = parent.insertNew(newKey,right);
		return parent;
	}


}