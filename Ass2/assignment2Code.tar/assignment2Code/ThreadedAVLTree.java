import java.lang.*;
/*
Name and Surname: Adrian Rae
Student Number: 19004029
*/

public class ThreadedAVLTree<T extends Comparable<? super T>>
{
   /*
   TODO: You must complete each of the methods in this class to create your own threaded AVL tree.
   Note that the AVL tree is single-threaded, as described in the textbook and slides.
   
   You may add any additional methods or data fields that you might need to accomplish your task.
   You may NOT alter the given class name, data fields, or method signatures.
   */
   
   private ThreadedAVLNode<T> root; // the root node of the tree
   private T dataStartBalance;
   
   public ThreadedAVLTree()
   {
      //The default constructor - does nothing of interest
      this.root = null;
   }
   
   public ThreadedAVLTree(ThreadedAVLTree<T> other)
   {
      /*
      The copy constructor
      ~ take advantage of java's pass by value
      */
      this.root = AddLevelWise(other.root);
   }
   
   public ThreadedAVLTree<T> aclone()
   {
      /*
      The clone method should return a copy/clone of this tree.
      */
      return new ThreadedAVLTree(this);
   }
   
   public ThreadedAVLNode<T> getRoot()
   {
      /*
      Return the root of the tree.
      */
      
      return this.root;
   }
   
   public int getNumberOfNodes()
   {
      /*
      This method should count and return the number of nodes currently in the tree.
      */
      return countNodeRec(this.root);
   }
   
   public int getHeight()
   {
      /*
      This method should return the height of the tree. The height of an empty tree is 0; the
      height of a tree with nothing but the root is 1.
      */
      return getHeight(this.root);
   }
   
   public boolean insert(T element)
   {

      if(this.root == null) {this.root = new ThreadedAVLNode<T>(element); return true;}
      else if(!this.contains(element)){
       
      /*
      The element passed in as a parameter should be inserted into the tree. Duplicate values are
      not allowed in the tree. Threads must be updated accordingly, as necessary. If the insertion
      operation is successful, the method should return true. If the insertion operation is
      unsuccessful for any reason, the method should return false.
      
      NB: Do not throw any exceptions.
      */

      ThreadedAVLNode<T> Curr = this.root, Prev = null;
      while(Curr != null){ //traverse to appropriate spot
        if(Curr.data.compareTo(element)>0){
            Prev = Curr;
            Curr = Curr.left;
		}
        else{
            Prev = Curr;
            if (!Curr.hasThread) Curr = Curr.right; else Curr=null;
		}
	  }
      //Cur is now null - Prev points to parent node
      //add the new node
      ThreadedAVLNode<T> NewNode = new ThreadedAVLNode<T>(element);


      NewNode.parent = Prev;
      if (Prev.data.compareTo(element)<0){
      //heres the kicker - the new element will now have a thread pointing to the parent's successor
      // two cases - parent is/is not threaded
            if (Prev.hasThread){
                //NewNode.parent = Prev;
                NewNode.hasThread = true;
                NewNode.right = Prev.right; // set new thread to parent's successor
                Prev.right = NewNode;
                Prev.hasThread =  false;
			}
            else{ //simpler case - just add in order
                //NewNode.parent = Prev;
                Prev.right = NewNode;
			}
      }
      else{
      //simple case - new element just has a thread to parent, parent element remains the same
      Prev.left = NewNode;
      //NewNode.parent = Prev;
      NewNode.hasThread = true;
      NewNode.right = Prev;
	  }

      //now adjust the balance factor of all nodes
      setBalanceFactors();

      /*see whether adjustment needs to happen
      int bf = this.root.balanceFactor;
      if( !(bf>=-1 && bf<=1) ){
            System.out.println("Requires balancing");
            balanceTreeInsert();
	  }
      //*/
      balanceTreeInsert(NewNode);
      

      return true;

      }
      else return false;
   }
   
   public boolean delete(T element)
   {

      if(this.root == null) return false; //empty tree
      else if(this.contains(element)){
      
      /*
      The element passed in as a parameter should be deleted from this tree. Threads must be
      updated accordingly, as necessary. If the deletion operation was successful, the method
      should return true. If the deletion operation is unsuccessful for any reason (e.g. the
      requested element is not found in the tree), the method should return false.
      
      NB: Do not throw any exceptions.
      */

      dethreadTree(); //make the tree simpler to search
   

      deleteNode(element);
      setBalanceFactors();

      //*/
      if(dataStartBalance!=null){
          //navigate to node to start update from - garunteed to find it
          ThreadedAVLNode<T> Curr = root;
          while(Curr!=null){
            if(Curr.data==dataStartBalance) break;
            else if(Curr.data.compareTo(dataStartBalance)<0){Curr=Curr.right;}
            else {Curr=Curr.left;}
	      }
          //Curr points to target
          balanceTreeDelete(Curr);
      }
      //if its null then start from the root
      else{balanceTreeDelete(root);}
      
      //*/
      
      threadTree();

      return true;
      }
      else return false; //doesnt contain element
   }


   // Function to delete node from a BST
    private void deleteNode(T element){ this.root=deleteNode(this.root,element); }
	private ThreadedAVLNode<T> deleteNode(ThreadedAVLNode<T> START, T key)
	{
		if (START == null) {} //not in tree
		else if (key.compareTo(START.data)<0) { //in left subtree
            START.left = deleteNode(START.left, key);
            if(START.left!=null) START.left.parent = START;
		}
		else if (key.compareTo(START.data)>0) { //in right subtree
           if(!START.hasThread) {START.right = deleteNode(START.right, key); if(START.left!=null) START.left.parent = START; }
            
		}
		else //in this subtree
		{
                        
            // Case 1: is just a leaf
			if (START.left == null && START.hasThread)
			{
				// update START to null
                if(START.parent!=null) dataStartBalance = START.parent.data; else dataStartBalance = null;
                return null;
			}
			// Case 2: node to be deleted has two children
			else if (START.left != null && START.right != null && !START.hasThread)
			{
				// find its in-order predecessor node
				ThreadedAVLNode<T> Pred = START.left;
                while(Pred.right!=null && !Pred.hasThread) Pred = Pred.right;
                dataStartBalance = Pred.data;

				// Copy the value of predecessor to current node
				START.data = Pred.data;

				// recursively delete the predecessor. Note that the
				// predecessor will have at-most one child (left child)
				START.left = deleteNode(START.left, Pred.data);
			}
			// Case 3: node to be deleted has only one child
			else
			{
				if(START.parent!=null) dataStartBalance = START.parent.data; else dataStartBalance = null;
                // find child node
				ThreadedAVLNode<T> child = (START.left != null)? START.left: START.right;
				START = child;
			}


            //node has been deleted
            setBalanceFactors();

		}
		return START;
	}



   
   public boolean contains(T element)
   {
      /*
      This method searches for the element passed in as a parameter. If the element is found, true
      should be returned. If the element is not in the tree, the method should return false.
      */
      return this.contains(element,this.root);
   }
   
   public String inorder()
   {
      /*
      This method must return a string representation of the elements in the tree visited during an
      inorder traversal. This method must not be recursive. Instead, the threads must be utilised
      to perform a depth-first inorder traversal.
      
      Note that there are no spaces in the string, and the elements are comma-separated. Note that
      no comma appears at the end of the string.
      
      If the tree looks as follows:
      
          C
         / \
        B   E
       /   / \
      A   D   F
      
      The following string must be returned:
      
      A,B,C,D,E,F
      */

      String output = "";

      ThreadedAVLNode<T> Prev = null, Curr=this.root;
      if(Curr!=null){
        while(Curr.left != null){Curr=Curr.left;}
        while(Curr != null){
            //visit
                if(Curr.right == null && !Curr.hasThread) output+=Curr.data;
                else output+=Curr.data+",";
            //end visit
            Prev = Curr;
            Curr=Curr.right;
            if(Curr != null && !Prev.hasThread)
                while(Curr.left != null) Curr=Curr.left;
		}
	  }

      return output;
      
   }
   
   public String inorderDetailed()
   {
      /*
      This method must return a string representation of the elements in the tree visited during an
      inorder traversal. This method must not be recursive. Instead, the threads must be utilised
      to perform a depth-first inorder traversal.
      
      Note that there are no spaces in the string, and the elements are comma-separated.
      Additionally, whenever a thread is followed during the traversal, a pipe symbol should be
      printed instead of a comma. Note that no comma or pipe symbol appears at the end of the
      string. Also note that if multiple threads are followed directly after one another, multiple
      pipe symbols will be printed next to each other.
      
      If the tree looks as follows:
      
          C
         / \
        B   E
       /   / \
      A   D   F
      
      In this tree, there is a thread linking the right branch of node A to node B, a thread
      linking the right branch of node B to node C, and a thread linking the right branch of node D
      to node E. The following string must therefore be returned:
      
      A|B|C,D|E,F
      */
      
      String output = "";

      ThreadedAVLNode<T> Prev = null, Curr=this.root;
      if(Curr!=null){
        while(Curr.left != null){Curr=Curr.left;}
        while(Curr != null){
            //visit
                if(Curr.right == null && !Curr.hasThread) output+=Curr.data;
                else if(!Curr.hasThread) output+=Curr.data+",";
                else output+=Curr.data+"|";
            //end visit
            Prev = Curr;
            Curr=Curr.right;
            if(Curr != null && !Prev.hasThread)
                while(Curr.left != null) Curr=Curr.left;
		}
	  }

      return output;
   }
   
   public String preorder()
   {
      /*
      This method must return a string representation of the elements in the tree visited during a
      preorder traversal. This method must not be recursive. Instead, the threads must be utilised
      to perform a depth-first preorder traversal. See the last paragraph on page 240 of the
      textbook for more detail on this procedure.
      
      Note that there are no spaces in the string, and the elements are comma-separated. Note that
      no comma appears at the end of the string.
      
      If the tree looks as follows:
      
          C
         / \
        B   E
       /   / \
      A   D   F
      
      The following string must be returned:
      
      C,B,A,E,D,F
      */
      
      String output = "";
      ThreadedAVLNode<T> Curr=this.root;
      if(Curr!=null){
            while(Curr!=null){
                //visit
                    output+=Curr.data+",";
                //end visit
                if(Curr.left!=null) Curr=Curr.left; //go left
                else if(!Curr.hasThread) Curr=Curr.right; //else go right
                else{
                    while(Curr.hasThread) Curr=Curr.right;
                    Curr=Curr.right;
                } //else follow threads as high as possible
			}     
	  }
      return output.substring(0,output.length()-1);
   }
   
   public String preorderDetailed()
   {
      /*
      This method must return a string representation of the elements in the tree visited during a
      preorder traversal. This method must not be recursive. Instead, the threads must be utilised
      to perform a depth-first preorder traversal. See the last paragraph on page 240 of the
      textbook for more detail on this procedure.
      
      Note that there are no spaces in the string, and the elements are comma-separated.
      Additionally, whenever a thread is followed during the traversal, a pipe symbol should be
      printed instead of a comma. Note that no comma or pipe symbol appears at the end of the
      string. Also note that if multiple threads are followed directly after one another, multiple
      pipe symbols will be printed next to each other.
      
      If the tree looks as follows:
      
          C
         / \
        B   E
       /   / \
      A   D   F
      
      In this tree, there is a thread linking the right branch of node A to node B, a thread
      linking the right branch of node B to node C, and a thread linking the right branch of node D
      to node E. The following string must therefore be returned:
      
      C,B,A||E,D|F
      
      Note that two pipe symbols are printed between A and E, because the thread linking the right
      child of node A to node B is followed, B is not printed because it has already been visited,
      and the thread linking the right child of node B to node C is followed.
      */
      
      String output = "";
      ThreadedAVLNode<T> Curr=this.root;
      if(Curr!=null){
            while(Curr!=null){
                //visit
                    output+=Curr.data;
                //end visit
                if(Curr.left!=null) {Curr=Curr.left; output+=",";} //go left
                else if(!Curr.hasThread) {Curr=Curr.right; output+=","; if(Curr == null) output = output.substring(0,output.length()-1);} //else go right
                else{
                    while(Curr.hasThread) {Curr=Curr.right; output+="|";}
                    if(Curr.right == null) output = output.substring(0,output.length()-1);
                    Curr=Curr.right;
                    
                } //else follow threads as high as possible
			}     
	  }
      return output;
   }







   /*===========================
   Helper declerations
   ============================*/
   private int getHeight(ThreadedAVLNode<T> START){
      
       /*Recursive Height method - must also account for nodes being threaded
       
       //case 1 - no node - has 0 height
       if(START==null) return 0;

       //case 2 - is threaded, so only left subtree to consider
       else if(START.hasThread) return 1 + getHeight(START.left);
       
       //case 3 - is not threaded, the left subtree is larger
       else if(getHeight(START.left) > getHeight(START.right)) return 1 + getHeight( START.left);
       
       //case 4 - is not threaded, right subtree larger or the same size
       else return 1 + getHeight(START.right);
       //*/

       //case 1 - no node
       if(START==null) return 0;

       //case 2 - is threaded, so only left subtree to consider
       else if(START.hasThread) return 1 + getHeight(START.left);
       
       //case 3 - is not threaded, the left subtree is larger
       else if(START.balanceFactor < 0) return 1 + getHeight( START.left);
       
       //case 4 - is not threaded, right subtree larger or the same size
       else return 1 + getHeight(START.right);


   }
   
   
   
   
   private int countNodeRec(ThreadedAVLNode<T> START){
        if(START==null) return 0;
        else if(!START.hasThread) return 1+countNodeRec(START.left)+countNodeRec(START.right);
        else return 1 + countNodeRec(START.left);
   }

   //see if an element is already in the tree recursicvely
   private boolean contains(T data, ThreadedAVLNode<T> START){
        if(START == null) return false;
        else if(START.data == data) return true;
        else if(!START.hasThread) return (false || contains(data,START.left) || contains(data,START.right));
        else return (false || contains(data,START.left));

   }

   //adjust the balance factors for all nodes
   private void setBalanceFactors(){setBalanceFactors(this.root);}
   private void setBalanceFactors(ThreadedAVLNode<T> START){
       if(START == null) return; //no need to do anything
       else{
            if(!START.hasThread) START.balanceFactor = (getHeight(START.right)-getHeight(START.left));
            else START.balanceFactor = -1*getHeight(START.left);
            setBalanceFactors(START.left);
            if(!START.hasThread) setBalanceFactors(START.right);
	   }
   }

   //rotations
   private ThreadedAVLNode<T> rotateRight(ThreadedAVLNode<T> y) { 
		if(y==null) return null;
		ThreadedAVLNode<T> x = y.left;
        ThreadedAVLNode<T> W = x.right; 
        if(!x.hasThread) {y.left = W; if(W!=null) W.parent = y;}
        else{x.dethread(); y.left=null;}
        x.parent = y.parent;
        y.parent = x;
        x.right = y; 
		    
		return x; 
	} 
	
	private ThreadedAVLNode<T> rotateLeft(ThreadedAVLNode<T> y) { 
		if(y==null) return null;
		ThreadedAVLNode<T> x = y.right;
        ThreadedAVLNode<T> W = x.left;         
        x.left = y; 
		if(W!=null) {y.right = W; W.parent = y;}
        else {y.right = x; y.thread();}
		
        x.parent = y.parent;
        y.parent = x;
        
        return x; 
		
	} 

  
    private void balanceTreeInsert(ThreadedAVLNode<T> START){
        
        if(START == null) return;
        else if(START.parent == null){
            //=========== BASE CASE ===================
            //case 1 - left subtree of left child
            if (START.balanceFactor < -1 && START.left.balanceFactor<0){
                this.root = rotateRight(START);  
		    }
            //case 2 - right subtree of left child
            else if (START.balanceFactor < -1 && START.left.balanceFactor>0){
                START.left = rotateLeft(START.left);
                this.root = rotateRight(START);
    
		    }
            //case 3 - right subtree of right child
            else if (START.balanceFactor > 1 && START.right.balanceFactor>0){
                this.root = rotateLeft(START);  
		    }
            //case 2 - left subtree of right child
            else if (START.balanceFactor > 1 && START.right.balanceFactor<0){
                START.right = rotateRight(START.right);
                this.root = rotateLeft(START);
		    }
            setBalanceFactors();
        
		}
        else{
            //===================== RECURSIVE CASE ==============//
            //case 1 - left subtree of left child
            if (START.balanceFactor < -1 && START.left.balanceFactor<0){
                START = rotateRight(START); 
                if(START.data.compareTo(START.parent.data)<0) START.parent.left = START; else START.parent.right = START;
		    }
            //case 2 - right subtree of left child
            else if (START.balanceFactor < -1 && START.left.balanceFactor>0){
                START.left = rotateLeft(START.left);
                START = rotateRight(START);
                if(START.data.compareTo(START.parent.data)<0) START.parent.left = START; else START.parent.right = START;
    
		    }
            //case 3 - right subtree of right child
            else if (START.balanceFactor > 1 && START.right.balanceFactor>0){
                START = rotateLeft(START);  
                if(START.data.compareTo(START.parent.data)<0) START.parent.left = START; else START.parent.right = START;
		    }
            //case 2 - left subtree of right child
            else if (START.balanceFactor > 1 && START.right.balanceFactor<0){
                START.right = rotateRight(START.right);
                START = rotateLeft(START);
                if(START.data.compareTo(START.parent.data)<0) START.parent.left = START; else START.parent.right = START;
		    }

            setBalanceFactors();
            balanceTreeInsert(START.parent);
        }
       

	}

    public void printTree(){printTree(this.root); }
    private void printTree(ThreadedAVLNode<T> START){
        if (START==null) return;
        else{
        printTree(START.left);
        System.out.println(START.data+" [BF:"+START.balanceFactor+" HS:"+START.hasThread+" L:"+(START.left!=null ? START.left.data : "NULL")+" R:"+(START.right!=null ? START.right.data : "NULL")+" P:"+(START.parent!=null ? START.parent.data : "NULL")+"]");
        if (!START.hasThread) printTree(START.right); 
        }
    }

    public ThreadedAVLNode<T> AddLevelWise(ThreadedAVLNode<T> CopyRoot){
        ThreadedAVLTree<T> nT = new ThreadedAVLTree();
        int h=getHeight(CopyRoot);
        for(int i=0; i<=h; i++){
            nT.AddGivenLevel(CopyRoot,i);
		}
        return nT.root;
	}
    private void AddGivenLevel(ThreadedAVLNode<T> START, int level){
        
        if(level==1) {
        if(START!=null) this.insert(START.data);

        }
        else if(level>1){
            AddGivenLevel(START.left,level-1);
            if(!START.hasThread) AddGivenLevel(START.right,level-1);
		}
	}
    
    private void setParents(){this.root.parent=null; this.root = setParents(this.root);}
    private ThreadedAVLNode<T> setParents(ThreadedAVLNode<T> START){
        if(START==null) return null;
        else{
            if(START.left!=null){
                START.left.parent = START;
                START.left=setParents(START.left);
			}
            if(START.right!=null && !START.hasThread){
                START.right.parent = START;
                START.right=setParents(START.right);
			}
        }
        return START;
     }

    public void dethreadTree(){ this.root=dethreadTree(this.root);}
    private ThreadedAVLNode<T> dethreadTree(ThreadedAVLNode<T> START){
        if(START==null) return null;
        if(START.hasThread){
            START.dethread(); 
            START.right = null;
            START.left = dethreadTree(START.left);
        }
        else{
            START.left = dethreadTree(START.left);
            START.right = dethreadTree(START.right);
		}
        return START;

	} 

    public void threadTree(){ this.root=threadTree(this.root);}
    private ThreadedAVLNode<T> threadTree(ThreadedAVLNode<T> START){
        //for every node, find its predecessor, if it has no right child, thread it
        if(START==null) return null;
        ThreadedAVLNode<T> Pred = START.left;
        if(Pred!=null){ //there exists a predecessor
            while(Pred.right!=null && !Pred.hasThread) Pred = Pred.right;
            Pred.thread();
            Pred.right = START;
        }
        START.left = threadTree(START.left);
        if(!START.hasThread) START.right = threadTree(START.right);
        return START;
    }

    private void balanceTreeDelete(ThreadedAVLNode<T> START){
        
        if(START == null) return;
        else if(START.parent == null){
            if (START.balanceFactor > 1 && START.right.balanceFactor>=0){
                this.root = rotateLeft(START);  
		    }
            else if (START.balanceFactor > 1 && START.right.balanceFactor<0){
                START.right = rotateRight(START.right); 
                this.root = rotateLeft(START); 
		    }
            else if (START.balanceFactor < -1 && START.left.balanceFactor<0){
                this.root = rotateRight(START);  
		    }
            else if (START.balanceFactor < -1 && START.left.balanceFactor>=0){
                START.left = rotateLeft(START.left); 
                this.root = rotateRight(START); 
		    }
            setBalanceFactors();
		}
        else{
            if (START.balanceFactor > 1 && START.right.balanceFactor>=0){
                START = rotateLeft(START);  
		    }
            else if (START.balanceFactor > 1 && START.right.balanceFactor<0){
                START.right = rotateRight(START.right); 
                START = rotateLeft(START); 
		    }
            else if (START.balanceFactor < -1 && START.left.balanceFactor<0){
                START = rotateRight(START);  
		    }
            else if (START.balanceFactor < -1 && START.left.balanceFactor>=0){
                START.left = rotateLeft(START.left); 
                START = rotateRight(START); 
		    }
            setBalanceFactors();
        }
       

	}




}
