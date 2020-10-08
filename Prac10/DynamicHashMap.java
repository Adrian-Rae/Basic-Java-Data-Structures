// Name: ADRIAN RAE
// Student number: 19004029
import java.util.ArrayList;

public class DynamicHashMap {

    /**
     * This interface is partly based on Java's HashMap:
     * https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
     */
    private int capacity;
    private double loadFactor;
    private ArrayList<ArrayList<Integer>> table;
    private ArrayList<ArrayList<String>> keys;


    /**
     * Create a new empty hash map
     * @param tSize - the number of cells in the table
     *      or the maximum number of chain that can be in the table
     * @param loadFactor - The loadFactor is defined as the average chain length.
     *      If the average chain length exceeds the loadFactor
     *      the table size should be doubled, and rehashing done.
     *      The loadFactor given here stays constant.
     */
    public DynamicHashMap(int tSize, double loadFactor) {
        this.capacity = tSize;
        this.loadFactor = loadFactor;

        keys = new ArrayList<ArrayList<String>>();
        table = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < tSize; i++) {
            keys.add(new ArrayList<String>());
            table.add(new ArrayList<Integer>());
        }

    }


    /**
     * Returns the current highest number of cells in the table.
     * This is also equal to the maximum amount of chains that can be in the table.
     */
    public int getTableSize() {
        return capacity;
    }


    /**
     * Returns the set load factor of the table.
     * This value determines when to double the table size and rehash all entries.
     */
    public double getLoadFactor() {
        return loadFactor;
    }


    private Integer[] chain(int index) {
        return table.get(index).toArray(new Integer[table.get(index).size()]);
    }


    private String chainToString(Integer[] chain) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < chain.length; i++) {
            sb.append(chain[i]);
            if (i + 1 != chain.length) {
                sb.append(",");
            }
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * String representation of all table chains
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getTableSize(); i++) {
            sb.append(chainToString(chain(i)));  
        }

        return sb.toString();
    }


    ////// You may not change any code above this line //////

    ////// Implement the methods below this line //////

    /**
     * Calculate and return the hash of the given key.
     * The input key should be interpreted as an ASCII string.
     * 
     * The hash should be calculated by XORing all the characters of the string
     * Example: h(abcd) = (a XOR b XOR c XOR d) mod TSize
     *          h(a)    = (a) mod TSize
     * 
     * NOTE: This hash function limits the maximum size of the table to 128.
     * See section 10.1.2 in the textbook.
     * 
     * For information on the XOR operator:
     * https://en.wikipedia.org/wiki/Exclusive_or
     * 
     * For information on ASCII:
     * https://en.wikipedia.org/wiki/ASCII
     */
    public int hash(String key) {
        // Your code here
        int length = key.length();
        int result = (int) key.charAt(0); //this is the ASCII value of the first characters - this represents the case of a 0 length XOR
        for(int i=1; i<length; i++){
            int newChar = (int) key.charAt(i);
            result = result ^ newChar;  
		}
        //XOR now stores the XOR of the whole string 
        //normalise the number
        result = result % this.capacity;
        return result;
        
    }

    
    /**
     * Return the value associated with the key. If no value is associated, return null.
     */
    public Integer get(String key) {
        // Your code here
        int index = this.hash(key); //get index;
        //find the row containing the value
        
        //see if the key already exists in the key table
        ArrayList<String> keyRow = this.keys.get(index);
        int keyIndex = keyRow.indexOf(key);
        if(keyIndex==-1) return null; //key-value pair does not already exist
        
        //else get the value from the relevant entry in the table
        ArrayList<Integer> tableRow = this.table.get(index);
        return tableRow.get(keyIndex);
    }


    /**
     * Set the value asociated with the key.
     * If after the update, the internal loadFactor is greater than the set loadFactor,
     * the table size should be doubled and all key-values pairs should be re-inserted.
     * 
     * Return the previous value or null if no previous value was associated with the key.
     */
    public Integer put(String key, Integer value) {
         // Your code here
        Integer returnVal = get(key); //get prev val
        int index = this.hash(key); //get index;
        ArrayList<Integer> tableRow = this.table.get(index);
        ArrayList<String> keyRow = this.keys.get(index);
        if(returnVal==null){
            //if not already contained - add to the keys and values 
            tableRow.add(value);
            keyRow.add(key);
		}
        else{
            //simply get the index of it and adjust the table value
            int keyIndex = keyRow.indexOf(key);
            tableRow.set(keyIndex,value);
		}
        //rehash the table if the need be
        //System.out.println("inserting: "+key+"/"+value+" current load factor: "+this.getNewLoadFactor()+" maxLoad: "+this.loadFactor);
        if(this.loadFactor < this.getNewLoadFactor() ) this.rehash();
        return returnVal;
    }


    /**
     * Remove the value associated with the given key.
     * 
     * The table size should never decrease.
     * Return the associated value or null if no value was associated
     */
    public Integer remove(String key) {
        // Your code here
        Integer returnValue = this.get(key);
        //see if it exists to begin with
        if(returnValue!=null){
            //find the hashed value
            int index = this.hash(key);
            //remove the key, then the index
            ArrayList<Integer> tableRow = this.table.get(index);
            ArrayList<String> keyRow = this.keys.get(index);
            //get the position of the key and value
            int pos = keyRow.indexOf(key);
            //remove
            keyRow.remove(pos);
            tableRow.remove(pos);     
		}
        return returnValue;
    }


    //Helper methods
    private double getNewLoadFactor(){
        //in this, total the chains lengths, divy by the num of chains
        double numChains = this.capacity;
        double chainTotal = 0.0;
        for(int p=0; p<this.capacity; p++){
            ArrayList<Integer> caseEntry = this.table.get(p);
            chainTotal += caseEntry.size();
		}
        return (chainTotal/numChains);
	}

    private void rehash(){
        //get all key value pairs
        ArrayList<Integer> saveValues = new ArrayList<Integer>();
        ArrayList<String> saveKeys = new ArrayList<String>();
        for(int i=0; i<this.capacity; i++){
            ArrayList<String> keyRow = this.keys.get(i);
            ArrayList<Integer> valueRow = this.table.get(i);
            for(int j=0; j<keyRow.size(); j++){ //or the table size/ they are merely reflections
                saveKeys.add(keyRow.get(j));
                saveValues.add(valueRow.get(j));
			}
		}
        //the load factor has been exceeded, get all the key value pairs, insert them into a new table double the size
        this.capacity = 2*this.capacity;
        //load factor remains the same
        //create new arrays
        ArrayList<ArrayList<Integer>> newTable = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<String>> newKeys = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < this.capacity; i++) {
            newKeys.add(new ArrayList<String>());
            newTable.add(new ArrayList<Integer>());
        }
        this.keys = newKeys;
        this.table = newTable;
        //add all the key value pairs again
        for(int p=0; p<saveKeys.size(); p++){
            this.put(saveKeys.get(p), saveValues.get(p));  
		}

	}


}