/**
 * A B+ tree generic node
 * Abstract class with common methods and data. Each kind of node implements this class.
 * @param <TKey> the data type of the key
 * @param <TValue> the data type of the value
 */
 import java.util.*;

abstract class BPTreeNode<TKey extends Comparable<TKey>, TValue> {
	
	protected Object[] keys;
	protected int keyTally;
	protected int m;
	protected BPTreeNode<TKey, TValue> parentNode;
	protected BPTreeNode<TKey, TValue> leftSibling;
	protected BPTreeNode<TKey, TValue> rightSibling;
	protected static int level = 0;
	

	protected BPTreeNode() 
	{
		this.keyTally = 0;
		this.parentNode = null;
		this.leftSibling = null;
		this.rightSibling = null;
	}

	public int getKeyCount() 
	{
		return this.keyTally;
	}
	
	@SuppressWarnings("unchecked")
	public TKey getKey(int index) 
	{
		return (TKey)this.keys[index];
	}

	public void setKey(int index, TKey key) 
	{
		this.keys[index] = key;
	}

	public BPTreeNode<TKey, TValue> getParent() 
	{
		return this.parentNode;
	}

	public void setParent(BPTreeNode<TKey, TValue> parent) 
	{
		this.parentNode = parent;
	}	
	
	public abstract boolean isLeaf();
	
	/**
	 * Print all nodes in a subtree rooted with this node
	 */
	@SuppressWarnings("unchecked")
	public void print(BPTreeNode<TKey, TValue> node)
	{
		level++;
		if (node != null) {
			System.out.print("Level " + level + " ");
			node.printKeys();
			System.out.println();

			// If this node is not a leaf, then 
        		// print all the subtrees rooted with this node.
        		if (!node.isLeaf())
			{	BPTreeInnerNode inner = (BPTreeInnerNode<TKey, TValue>)node;
				for (int j = 0; j < (node.m); j++)
    				{
        				this.print((BPTreeNode<TKey, TValue>)inner.references[j]);
    				}
			}
		}
		level--;
	}

	/**
	 * Print all the keys in this node
	 */
	protected void printKeys()
	{
		System.out.print("[");
    		for (int i = 0; i < this.getKeyCount(); i++)
    		{
        		System.out.print(" " + this.keys[i]);
    		}
 		System.out.print("]");
	}


	////// You may not change any code above this line //////

	////// Implement the functions below this line //////
	
	
	
	/**
	 * Search a key on the B+ tree and return its associated value. If the given key 
	 * is not found, null should be returned.
	 */
	public TValue search(TKey key) 
	{
		if(this.getKeyCount() == 0) return null;
		//System.out.println("Searching for "+key);
		// Your code goes here
		if(isLeaf()){
			BPTreeLeafNode<TKey, TValue> sample = (BPTreeLeafNode<TKey, TValue>) this;
			for(int i=0; i<sample.getKeyCount(); i++){
				//System.out.println("Searching for "+key+" looking at "+sample.getKey(i)+" in a leaf, evaluates "+(sample.getKey(i) == key)+" has value "+sample.getValue(i));
				if(sample.getKey(i).compareTo(key)==0) return sample.getValue(i);
			}
			return null;
		}
		else{
			BPTreeInnerNode<TKey, TValue> sample = (BPTreeInnerNode<TKey, TValue>) this;
			int i=0;
			while(i<sample.getKeyCount() && sample.getKey(i).compareTo(key) <= 0) i++;
			//System.out.println("Searching for "+key+" following inner index "+i);
			return sample.getChild(i).search(key);
		}

	}



	/**
	 * Insert a new key and its associated value into the B+ tree. The root node of the
	 * changed tree should be returned.
	 */
	public BPTreeNode<TKey, TValue> insert(TKey key, TValue value) 
	{
		// Your code goes here
		if(this.search(key) != null) return this; //already in the tree
		//System.out.println(key+" isnt in the tree");
		if(this.isLeaf()){
			BPTreeLeafNode<TKey, TValue> node = (BPTreeLeafNode<TKey, TValue>) this;
			//first Insert
			node.insertNew(key,value);
			//if its full, do stuff
			if(node.isFull()){
				//System.out.println("node is full "+key);
				
				if(node.parentNode==null){
					
					//System.out.println(key);
					//inserting into a full root
					//the new root is going to be a split of the full leaf
					BPTreeNode<TKey, TValue> newRoot = node.rootSplit();
					//this.print(newRoot);
					return newRoot;
				}
				else{
					//System.out.println(key);
					BPTreeNode<TKey, TValue> newRoot = node.splitIntoParent();
					//this.print(newRoot);
					return newRoot;
					
				}

				
			}
			else{
				//keep going up the parents
				BPTreeNode<TKey, TValue> highest = node;
				while(highest.parentNode!=null) highest=highest.parentNode;
				return highest;
			}
		}
		else{
			BPTreeInnerNode<TKey, TValue> node = (BPTreeInnerNode<TKey, TValue>) this;
			//navigate to the necessary position
			int index=0; while(index<this.getKeyCount() && this.getKey(index).compareTo(key)<0) index++;
			BPTreeNode<TKey, TValue> child = node.getChild(index);
			return child.insert(key,value);
			//return node;
		} 
		
	}



	/**
	 * Delete a key and its associated value from the B+ tree. The root node of the
	 * changed tree should be returned.
	 */
	public BPTreeNode<TKey, TValue> delete(TKey key) 
	{
		// Your code goes here
		if(this.search(key) == null) return this; //no deletion needed

		//navigate to affected node
		BPTreeNode<TKey, TValue> target = this;
		//target.printKeys(); System.out.println(" is a leaf? "+target.isLeaf());
		int parentIndex = 0;
		while(!target.isLeaf()){
			int i=0;
			while(i<target.getKeyCount() && target.getKey(i).compareTo(key) <= 0){ i++; /*parentIndex++;*/}
			
			parentIndex=i-1; //will wind up pointing to the left child index (which is also the parent key)
			target = ((BPTreeInnerNode<TKey, TValue> ) target).getChild(i);
		}
		//System.out.println("Deleting "+key+" parentIndex is "+parentIndex);
		//now at the target node
		//target.printKeys();
		int x = 0; while(x<target.getKeyCount() && target.getKey(x).compareTo(key) < 0) x++;
		
		//x points to the index - delete it and shift other elements
		target.setKey(x,null); ((BPTreeLeafNode<TKey, TValue>) target).setValue(x,null);
		for(int l=x; l<target.getKeyCount(); l++){
			//System.out.println("key/value pair "+)
			target.setKey(l,target.getKey(l+1)); 
			((BPTreeLeafNode<TKey, TValue>) target).setValue(l,((BPTreeLeafNode<TKey, TValue>) target).getValue(l+1));
			
		}
		target.keyTally--;

		//now do damage control

		//case 1 - if you deleted from the root, no sweat
		if(target.parentNode==null) return this;
		//case 2 - simple deletion - no shifting
		else if(target.getKeyCount()>=getMin()) return this;
		//case 3 - difficult 
		else if(target.getKeyCount()<getMin()){
			//left-sibling precedence
			BPTreeLeafNode<TKey, TValue> ls = (BPTreeLeafNode<TKey, TValue>) target.leftSibling;
			BPTreeLeafNode<TKey, TValue> rs = (BPTreeLeafNode<TKey, TValue>) target.rightSibling;
			if(ls!=null && validPair(target.getKeyCount(),ls.getKeyCount()) && areValidSiblings(target,ls)){
				//System.out.println("YAAAAAAS");
				//left sibling has enough to donate
				int size = getCount(target.getKeyCount(),ls.getKeyCount());
				//strategy - keep adding from one to the other until it is validPair
				int index = ls.getKeyCount()-1;
				while(target.getKeyCount()<getMin()){
					target.insert(ls.getKey(index),ls.getValue(index));
					ls.setKey(index,null); ls.setValue(index,null);
					ls.keyTally--;
					index--;
				}
				//now a valid amount of things in target
				//set the parent's key equal to the value of target's first key
				target.parentNode.setKey(parentIndex,target.getKey(0));

			}
			else if(rs!=null && validPair(target.getKeyCount(),rs.getKeyCount()) && areValidSiblings(target,rs)){
				//right sibling has enough to donate
				int size = getCount(target.getKeyCount(),rs.getKeyCount());
				//strategy - keep adding from one to the other until it is validPair
				int index = 0;
				while(target.getKeyCount()<getMin()){
					target.insert(rs.getKey(index),rs.getValue(index));
					//System.out.println("moving key/value "+rs.getKey(index)+"/"+rs.getValue(index));
					rs.setKey(index,null); rs.setValue(index,null);
					rs.keyTally--;
					//now move the things up in the right where its been adusted
					for(int y=0; y<rs.getKeyCount();y++){
						//System.out.println("new key/value "+rs.getKey(y+1)+"/"+rs.getValue(y+1));
						rs.setKey(y,rs.getKey(y+1)); 
						rs.setValue(y,rs.getValue(y+1));
					}
					index++;
				}
				//now a valid amount of things in target
				//set the parent's key equal to the value of target's first key
				target.parentNode.setKey(parentIndex+1,rs.getKey(0));
				//if(parentIndex>=0) target.parentNode.setKey(parentIndex,target.getKey(0));

			}
			else{
				//have to merge
				if(ls!=null && areValidSiblings(target,ls)){
					ls.rightSibling = target.rightSibling;
					
					//merge with left sibling
					//insert everything in the target into the left sibling - destory the reference in the parent, adjust parent
					int movers = target.getKeyCount();
					for(int u=0; u<movers; u++){ ls.insert(target.getKey(u),((BPTreeLeafNode<TKey, TValue>)target).getValue(u)); target.keyTally--; }

					//destory the right reference and the key in the parent
					target.parentNode.setKey(parentIndex,null); ((BPTreeInnerNode<TKey, TValue>) target.parentNode).setChild(parentIndex+1,null);

					//adjust the keys in the parent
					for(int v=parentIndex; v<target.parentNode.getKeyCount(); v++){
						target.parentNode.setKey(v,((BPTreeInnerNode<TKey, TValue>) target.parentNode).getKey(v+1));
						((BPTreeInnerNode<TKey, TValue>) target.parentNode).setChild(v+1,((BPTreeInnerNode<TKey, TValue>) target.parentNode).getChild(v+2));
						
					}
					//finally reflect change
					target.parentNode.keyTally--;

					//what happens if the parentnode is now empty?
					if(target.parentNode.getKeyCount()==0){
						return ls;
					}


				}
				else if(rs!=null && areValidSiblings(target,rs)){
					if(parentIndex==-1) parentIndex=0;
					rs.leftSibling = target.leftSibling;
					//merge with right sibling
					//insert everything in the target into the left right - destory the reference in the parent, adjust parent
					int movers = target.getKeyCount();
					for(int u=0; u<movers; u++){ rs.insert(target.getKey(u),((BPTreeLeafNode<TKey, TValue>)target).getValue(u)); target.keyTally--; }

					//destory the left reference and the key in the parent
					//System.out.println(parentIndex);
					target.parentNode.setKey(parentIndex,null); ((BPTreeInnerNode<TKey, TValue>) target.parentNode).setChild(parentIndex,null);

					//adjust the keys in the parent
					//first moved displaced right left
					((BPTreeInnerNode<TKey, TValue>) target.parentNode).setChild(parentIndex,((BPTreeInnerNode<TKey, TValue>) target.parentNode).getChild(parentIndex+1));
					for(int v=parentIndex; v<target.parentNode.getKeyCount(); v++){
						target.parentNode.setKey(v,((BPTreeInnerNode<TKey, TValue>) target.parentNode).getKey(v+1));
						((BPTreeInnerNode<TKey, TValue>) target.parentNode).setChild(v+1,((BPTreeInnerNode<TKey, TValue>) target.parentNode).getChild(v+2));
						
					}
					//finally reflect change
					target.parentNode.keyTally--;

					//what happens if the parentnode is now empty?
					if(target.parentNode.getKeyCount()==0){
						return rs;
						
					}


				}
			}

			return this;
		}
		else return this;
		
	}



	/**
	 * Return all associated key values on the B+ tree in ascending key order. An array
	 * of the key values should be returned.
	 */
	@SuppressWarnings("unchecked")
	public TValue[] values() 
	{
		// Your code goes here
		//if the start node is empty - no need to bother
		//if(this.getKeyCount()==0) return 
		
		//lets use a queue to make the process simpler
		Queue<TValue> q = new LinkedList<>();

		//find the first leaf key
		BPTreeNode<TKey, TValue> x = this;
		while(!x.isLeaf()) x= ((BPTreeInnerNode<TKey, TValue>) x).getChild(0);
		//now go at it breadthwise
		while(x!=null){
			for(int i=0; i<x.getKeyCount(); i++){
				q.add(  ((BPTreeLeafNode<TKey, TValue>) x).getValue(i));
			}
			x=x.rightSibling;
		}
		//now translate the lot into a return array
		
		TValue[] finalValues = (TValue[]) new Object[q.size()];
		int index=0; while(!q.isEmpty()){ finalValues[index]=q.poll(); index++;}

		return finalValues;
	}

	private int getMin(){
		return ((int) (Math.ceil(0.5*this.m)-1));
	}


	//helpers
	private boolean validPair(int x, int y){
		return (x+y>=2*getMin());

	}

	private int getCount(int x, int y){
		return x + y;
	}

	private boolean areValidSiblings(BPTreeNode<TKey, TValue> x, BPTreeNode<TKey, TValue> y){
		return (x.parentNode == y.parentNode);
	}
}