// Name: Adrian Rae
// Student number: 19004029
import java.util.Arrays;

public class Sort
{

	////// Implement the functions below this line //////	

	/********** HEAP **********/
	public static <T extends Comparable<? super T>> void heapsort(T[] data, boolean debug)
	{
		// Your code here
		for(int i = data.length/2 -1; i>=0; --i) movedown(data,i,data.length-1,debug);
		for(int j = data.length-1; j>=1; --j){
			//swap;
				T temp = data[0];
				data[0] = data[j];
				data[j] = temp;
			//
			movedown(data,0,j-1,debug);
		}
	}

	private static <T extends Comparable<? super T>> void movedown(T[] data, int first, int last, boolean debug)
	{
		// Your code here
		int largest = 2*first+1;
		while(largest<=last){
			if((largest<last) && (data[largest].compareTo(data[largest+1])<0)){
				largest++;
			}
			if(data[first].compareTo(data[largest])<0){
				//swap===========
					T temp = data[first];
					data[first] = data[largest];
					data[largest] = temp;
				//==============
				first = largest;
				largest = 2*first+1;
			}
			else largest = last+1;
		}

		//DO NOT MOVE OR REMOVE!
		if (debug)
			System.out.println(Arrays.toString(data));
	}


	/********** MERGE **********/
	public static <T extends Comparable<? super T>> void mergesort(T[] data, int first, int last, boolean debug)
	{
		// Your code here
		if (first<last){
			int mid = (first+last)/2;
			mergesort(data,first,mid,debug);
			mergesort(data,mid+1,last,debug);
			merge(data,first,last,debug);
		}
	}
     
	private static <T extends Comparable<? super T>> void merge(T[] data, int first, int last, boolean debug)
	{
		//Your code here
		int middle = (first+last)/2;
		// Find sizes of two subarrays to be merged 
        int nLeft = middle - first + 1; 
        int nRight = last - middle; 
  
        // temporary storage
        @SuppressWarnings("unchecked")
		T tempLeft[] = (T[]) java.lang.reflect.Array.newInstance(data[0].getClass(),nLeft); for (int i=0; i<nLeft; ++i) tempLeft[i] = data[first + i];
        T tempRight[] = (T[]) java.lang.reflect.Array.newInstance(data[0].getClass(),nRight); for (int j=0; j<nRight; ++j) tempRight[j] = data[middle + 1+ j]; 
        
        //Merging	
        int i = 0, j = 0;  //existing subarrays
        int k = first; //new superarray
        while (i < nLeft && j < nRight){ 
            if (tempLeft[i].compareTo(tempRight[j]) <=0) 
            { 
                data[k] = tempLeft[i]; 
                i++; 
            } 
            else
            { 
                data[k] = tempRight[j]; 
                j++; 
            } 
            k++; 
        } 
		//remenents
        while (i < nLeft){ 
            data[k] = tempLeft[i]; 
            i++; 
            k++; 
        } 
        while (j < nRight){ 
            data[k] = tempRight[j]; 
            j++; 
            k++; 
        }
        
		//DO NOT MOVE OR REMOVE!
		if (debug)
			System.out.println(Arrays.toString(data));
	}

	//helper functions 
	/*
	private void makeHeap(T[] array, int n, int i ){
		//heapify a subtree rooted at node i, for heap of size n
		int maxima = i;
		int leftChild = 2*i+1;
		int rightChild = 2*i+2;
		
		//if either children are larger, adjust
		if (leftChild < n && array[leftChild].compareTo(array[maxima]) > 0 ) maxima = leftChild;
		if (rightChild < n && array[rightChild].compareTo(array[maxima]) > 0 ) maxima = rightChild;
		//if neither is not the root, swap
        if (maxima != i) 
        { 
            int temp = array[i]; 
            array[i] = array[maxima]; 
            array[maxima] = temp; 
  
            // Recursively heapify the affected sub-tree 
            makeHeap(array, n, maxima); 
        }
		else return;

	}
	*/

}