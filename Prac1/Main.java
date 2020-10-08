public class Main
{

	public static void firstKey(SkipList<Integer> skiplist)
	{
		if (skiplist.isEmpty())
			System.out.println("List is empty");
		else
			System.out.println("First key : " + skiplist.first());
	}

	public static void lastKey(SkipList<Integer> skiplist)
	{
		if (skiplist.isEmpty())
			System.out.println("List is empty");
		else
			System.out.println("Last key : " + skiplist.last());
	}

	public static void deleteKey(SkipList<Integer> skiplist, Integer key)
	{
		if (skiplist.isEmpty())
			System.out.println("List is empty");
		else
		{
			boolean result = skiplist.delete(key);
			if (result)
				System.out.println("Deleted key " + key);
			else
				System.out.println("Key " + key + " not found for deletion");	
		}
	}

	public static void searchKey(SkipList<Integer> skiplist, Integer key)
	{
		if (skiplist.isEmpty())
			System.out.println("List is empty");
		else
		{
			Integer result = skiplist.search(key);
			if (result != null)
				System.out.println("Found key " + result);
			else
				System.out.println("Key " + key + " not found");	
		}
	}

	public static void printList(SkipList<Integer> skiplist) 
	{ 
    		System.out.println();
		System.out.println("Skip List Content:"); 
    		for (int i = 0; i < skiplist.maxLevel; i++) 
    		{ 
        		SkipListNode<Integer> node = skiplist.root[i]; 
        		System.out.print("Level " + i + ": "); 
        		while (node != null) 
        		{ 
            			System.out.print(node.key + " "); 
            			node = node.next[i]; 
        		} 
        		System.out.println(); 
    		}
		System.out.println();
	} 

	public static void main(String[] args)
	{
		//Practical 1

		SkipList<Integer> skiplist = new SkipList<Integer>();
		
		skiplist.insert(8);

		skiplist.insert(15);

		skiplist.insert(9);

		skiplist.insert(18);

		firstKey(skiplist);
		lastKey(skiplist);

		printList(skiplist);
		
		searchKey(skiplist, 10);

		skiplist.insert(10);

		deleteKey(skiplist, 18);

		deleteKey(skiplist, 9);

		deleteKey(skiplist, 3);

		printList(skiplist);

		firstKey(skiplist);
		lastKey(skiplist);

		searchKey(skiplist, 10);
           
		/* Expected Output:
		First key : 8
		Last key : 18

		Skip List Content:
		Level 0: 8 9 15 18
		Level 1: 9 15
		Level 2: 15
		Level 3:

		Key 10 not found
		Deleted key 18
		Deleted key 9
		Key 3 not found for deletion

		Skip List Content:
		Level 0: 8 10 15
		Level 1: 15
		Level 2: 15
		Level 3:

		First key : 8
		Last key : 15
		Found key 10
		*/


	}
}
