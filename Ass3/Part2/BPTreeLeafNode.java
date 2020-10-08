/**
 * A B+ tree leaf node
 * @param <TKey> the data type of the key
 * @param <TValue> the data type of the value
 */
class BPTreeLeafNode<TKey extends Comparable<TKey>, TValue> extends BPTreeNode<TKey, TValue> {
	
	protected Object[] values;
	
	public BPTreeLeafNode(int order) {
		this.m = order;
		// The strategy used here first inserts and then checks for overflow. 
		// Thus an extra space is required i.e. m instead of m-1.
		// You can change this if needed.
		this.keys = new Object[m];
		this.values = new Object[m];
	}

	@SuppressWarnings("unchecked")
	public TValue getValue(int index) {
		return (TValue)this.values[index];
	}

	public void setValue(int index, TValue value) {
		this.values[index] = value;
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}

	////// You should not change any code above this line //////

	////// Implement functions below this line //////
	public boolean isFull(){
		return (this.keyTally == m);
	}

	

	public void insertNew(TKey key, TValue value){
		
		//find the part in the array that preserves ordering
		int index = 0;
		while(index<getKeyCount() && this.getKey(index).compareTo(key)<0) index++;
		//index now points to where the value must go - move all the remaining values up;
		//move other values to fit in
		for(int p=this.getKeyCount()-1; p>=index; p--){this.setKey(p+1,this.getKey(p)); this.setValue(p+1,this.getValue(p));}


		//now safely assign values
		this.keys[index] =  key;
		this.values[index] =  value;
		//System.out.println("index for "+key+" is "+index);
		//this.printKeys();
		//have inserted into leaf node - raise key count
		keyTally++;

		//return this;
		//now to work on adjusting

	}

	public BPTreeNode<TKey, TValue> rootSplit(){
		BPTreeInnerNode<TKey, TValue> newRoot = new BPTreeInnerNode(m);
		//two smaller subarrays 
		BPTreeLeafNode<TKey, TValue> left = new BPTreeLeafNode(m);
		BPTreeLeafNode<TKey, TValue> right = new BPTreeLeafNode(m);
		left.rightSibling = right;
		right.leftSibling = left;
		int splitIndex = this.keyTally/2;



		newRoot.setChild(0,left); newRoot.setChild(1,right);
		left.parentNode = newRoot; right.parentNode = newRoot;
		newRoot.setKey(0,this.getKey(splitIndex));
		newRoot.keyTally = 1;

		//System.out.println(newRoot.getKey(0));
		for(int i=0; i<splitIndex; i++) left.insertNew(this.getKey(i),this.getValue(i));
		for(int j=splitIndex; j<keyTally; j++ ) right.insertNew(this.getKey(j),this.getValue(j));

		//newRoot.printKeys();
		return newRoot;


	}

	public BPTreeNode<TKey, TValue> splitIntoParent(){
		
		
		BPTreeLeafNode<TKey, TValue> right = new BPTreeLeafNode(m);
		this.rightSibling = right;
		right.rightSibling = this.rightSibling;
		right.leftSibling = this;
		
		int splitIndex = this.keyTally/2;
		TKey newKey = this.getKey(splitIndex);
		//this time around around, only move things from the latter half of the new array upwards;

		//System.out.println(newRoot.getKey(0));
		int rightBound = this.keyTally;
		for(int j=splitIndex; j<rightBound; j++ ){
			//move elements into new right subarray
			right.insertNew(this.getKey(j),this.getValue(j));
			//delete them from old one
			this.setKey(j,null); keyTally--;
		}

		//now add the new key and reference to the parent
		BPTreeInnerNode<TKey, TValue> parent = (BPTreeInnerNode<TKey, TValue>) this.parentNode;
		parent = parent.insertNew(newKey,right);
		BPTreeNode<TKey, TValue> highest = parent;
		while(highest.parentNode!=null) highest=highest.parentNode;
		return highest;
	}




}
