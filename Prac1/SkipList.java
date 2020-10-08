// Name: Adrian Rae
// Student number: 19004029

import java.util.Random;

@SuppressWarnings("unchecked")
public class SkipList<T extends Comparable<? super T>> {

    public int maxLevel;
    public SkipListNode<T>[] root;
    private int[] powers;
    private Random rd = new Random();

    SkipList(int i) {
        maxLevel = i;
        root = new SkipListNode[maxLevel];
        powers = new int[maxLevel];
        for (int j = 0; j < i; j++) {
            root[j] = null;
        }
        choosePowers();
        rd.setSeed(1230456789);
    }

    SkipList() {
        this(4);
    }

    public void choosePowers() {
        powers[maxLevel - 1] = (2 << (maxLevel - 1)) - 1;
        for (int i = maxLevel - 2, j = 0; i >= 0; i--, j++) {
            powers[i] = powers[i + 1] - (2 << j);
        }
    }

    public int chooseLevel() {
        int i, r = Math.abs(rd.nextInt()) % powers[maxLevel - 1] + 1;
        for (i = 1; i < maxLevel; i++) {
            if (r < powers[i]) {
                return i - 1;
            }
        }
        return i - 1;
    }

    ////// You may not change any code above this line //////
    ////// Implement the functions below this line //////
    public boolean isEmpty() {
        //Your code goes here
        //Check to see whether something follows the root at the lowest level
        return !root[0];
    }

    public void insert(T key) {
        //Your code goes here
        
        SkipListNode<T> update[maxLevel];
        
        SkipListNode<T> Cur = root[maxLevel-1];
        
        //Search for larger element
        for (Integer lvl=maxLevel-1;lvl>=0;lvl--){
            while(Cur.next[lvl] && Cur.next[lvl].key<key){
                Cur = Cur.next[lvl];
                update[lvl] = Cur;
            }
        }
        //reached bottom
        Cur=Cur.next[0]
        if(Cur || Cur.key != key){
            Integer randLvl = chooseLevel();
            
            SkipListNode<T> NewNode = new SkipListNode(key,randLvl);
            for( Integer i=0; i<= randLvl; i++){
                newNode.next[i] = update[i].next[i];
                update[i].next[i] = newNode;
            }
        }
    }

    public boolean delete(T key) {
        //Your code goes here
    }

    public T first() {
        //Your code goes here
        if (!isEmpty()) {
            return root[0].key;
        } else {
            return null;
        }
    }

    public T last() {
        //Your code goes here
        SkipListNode<T> Current = root[maxLevel-1];
        //start at highest level, keep traversing, when next is null, go down a level and repeat
        for (Integer i = maxLevel-1; i >= 0; i--) {
            while (Current.next[i]) {
                Current = Current.next[i]; //While there's a next to go to, go to it
            }
        }
        if Current 
            return Current.key;
        else {
            return null; //NOthing was in the list
        }
    }

    public T search(T key) {
        //Your code goes here
        //start at head
        SkipListNode<T> Current = root[maxLevel-1];

        //Progress Through Smaller Values at higher levels
        for (Integer i = maxLevel-1; i >= 0; i--) {
            while (Current.next[i] && Current.next[i] < key) {
                Current = Current.next[i];
            }
        }

        //next is either the right one or not - At lowest level
        Current = Current.next[0];
        if (Current && Current.key == key) {
            return key;
        } else {
            return null;
        }

    }
}
