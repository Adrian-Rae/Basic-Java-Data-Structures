/**
 * A simplistic database table class. Uses the record class to store row data and the index class to 
 * maintain indexes for specific columns.
 * Class also implements basic SQL methods. Uses the error class for common error messages.
 */
public class Table {

	private String name;
	private String[] columns;
	private Record[] records;
	private Index[] indexes;
	private int rowId;
	private int recordCount;
	private int indexCount;
	private int m;

	public Table(String name, String[] columns) {
		this.rowId = 1; //start index in records array
		this.recordCount = 0;
		this.indexCount = 0;
		this.name = name;
		this.columns = columns; 
		this.records = new Record[1000]; //initial size of table
		this.indexes = new Index[10]; //initial number of indexes
		this.m = 4;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRecordCount() {
		return this.recordCount;
	}

	public int getIndexCount() {
		return this.indexCount;
	}

	public String debug() {
		String result = "";
		if (indexCount > 0) {
			for (int i = 0; i < indexCount; i++) {
				Index idx = indexes[i];
				result = result + idx.getIndex().debug();
				if ((i+1) < indexCount)
					result = result + " ";
			}
		} else {
			result = "No Indexes!";
		}
		return result;
	}

	////// You may not change any code above this line //////

	////// Implement the functions below this line //////



	/**
	 * Insert the given "rec" in this table. Every record needs to have a unique row id, which is never reused. 
	 * Should indexes be present, they need to be updated. 
	 */
	//SQL: insert into table values (rec)
	@SuppressWarnings("unchecked")
	public void insert(Record rec) {
		// Your code goes here
		records[rowId-1] = rec;
		recordCount++;
		

		//update all available indeces
		for(int j=0; j<columns.length; j++){ //for all columns
			//if an index exists for this coloumn, update the index
			int index = getIndexOfCol(columns[j]);
			if(index != -1){
				indexes[index].getIndex().insert((Comparable) rec.getColumn(j),rowId);
			}
		}
		rowId++;
		
	}



	/**
	 * Represents the full delete SQL query. Depending on given parameters, different subqueries are called.
	 */
	//SQL: delete from table where column equals value
	public void delete(String column, Object value) {
		if (column == null && value == null) {
			this.delete();
		} else {
			this.deletep(column, value);
		}
	}



	/**
	 * Delete all the records in this table where the given "column" matches "value". Needs to use the index 
	 * for "column" and call the search method of the used B+ tree. If no index exists for "column", conventional
	 * record iteration and search should be used. Deleted rows remain empty and the records array should not be 
	 * compacted. recordCount however should be updated. Should indexes be present, they need to be updated.
	 */
	//SQL: delete from table where column equals value
	@SuppressWarnings("unchecked")
	private void deletep(String column, Object value) {

		// Your code goes here
		Error errors = new Error();
		//empty table
		//if(this.recordCount==0){ System.out.println(errors.Err1); return;}

		//column index
		int colIndex = getPlaceOfCol(column);
		//System.out.println("Column index for "+column+" is "+colIndex);


		//look for appropriate Index
		int aptIndex = getIndexOfCol(column);
		if(aptIndex != -1){
			//System.out.println("found an appropriate index while deleting "+value);
			//there exists an index on the search field
			//get the value of the rowId it stores
			Object rowIdFound = indexes[aptIndex].getIndex().search((Comparable) value);
			if(rowIdFound == null){ return; }
			
			//============== otherwise, delete the record from all corresponding indeces =====================
				Record toBeDeleted = this.records[ (int) rowIdFound]; 
				for(int i=0; i<columns.length; i++){
					//get the index of the index corresponding to the coloumn
					int o = getIndexOfCol(columns[i]);
					Object crit = toBeDeleted.getColumn(i+1);
					if(o!=-1) indexes[o].getIndex().delete((Comparable) crit);
				}
				
			//================================================================================================
			toBeDeleted = null;
			this.records[ (int) rowIdFound] = toBeDeleted;
			this.recordCount--;
		}
		else{
			//no index exists - linear search
			for(int p=0; p<rowId; p++){
				Record rCase = records[p];
				if( rCase != null && ( ((Comparable) rCase.getColumn(colIndex)).compareTo((Comparable) value) == 0)){
					//found it
					//============== otherwise, delete the record from all corresponding indeces =====================
						for(int i=0; i<columns.length; i++){
							//get the index of the index corresponding to the coloumn
							int o = getIndexOfCol(columns[i]);
							Object crit = rCase.getColumn(i+1);
							if(o!=-1) indexes[o].getIndex().delete((Comparable) crit);
						}
					//================================================================================================
					rCase = null;
					this.recordCount--;
				}
				records[p] = rCase;
			}
		}
		//makes it this far, nothing found

	}



	/**
	 * Delete all the records in this table. recordCount and row id should be updated. Should also 
	 * reset all indexes if present. 
	 */
	//SQL: delete from table
	private void delete() {

		// Your code goes here
		
		//delete all records
		this.rowId = 1; //start index in records array
		this.recordCount = 0;
		this.records = new Record[1000];

		//reset all indexes
		for(int i=0; i<indexCount; i++){
			//somehow declare the type of the new tree
			BPTree newTreeI = new BPTree(this.m);
			indexes[i] = new Index(indexes[i].getName(),indexes[i].getColumnName(),newTreeI);
		}

	}



	/**
	 * Represents full select SQL query. Depending on given parameters, different subqueries are called.
	 */ 
	//SQL: select * from table where column equals value order by ocolumn
	public void select(String column, Object value, String ocolumn) {
		if (column == null && value == null) {
			if (ocolumn != null)
				this.select(ocolumn);
			else
				this.select();
		} else {
			this.select(column, value);
		}
		System.out.println();		
	}



	/**
	 * Print all the records in this table where the given "column" matches "value". Should call the getValues 
	 * method of the record class. Needs to use the index for "column" and call the search method of the used 
	 * B+ tree. If no index exists for "column", conventional record iteration and search should be used. 
	 * If the table is empty, print error message 1. If no record matches, print error message 4.
	 */
	//SQL: select * from table where column equals value
	@SuppressWarnings("unchecked")
	private void select(String column, Object value) {
		// Your code goes here
		boolean result = false;
		Error errors = new Error();
		//empty table
		if(this.recordCount==0){ System.out.println(errors.Err1); return;}

		//column index
		int colIndex = getPlaceOfCol(column);
		//System.out.println("Column index for "+column+" is "+colIndex);


		//look for appropriate Index
		int aptIndex = getIndexOfCol(column);
		if(aptIndex != -1){
			//there exists an index on the search field
			//get the value of the rowId it stores
			Object rowIdFound = indexes[aptIndex].getIndex().search((Comparable) value);
			if(rowIdFound == null){ System.out.println(errors.Err4); return; }
			
			//otherwise, show
			System.out.println(this.records[ (int) rowIdFound].getValues());
			result = true;
		}
		else{
			//no index exists - linear search
			for(int p=0; p<rowId; p++){
				Record rCase = records[p];
				if( rCase != null && ( ((Comparable) rCase.getColumn(colIndex)).compareTo((Comparable) value) == 0)){
					//found it
					System.out.println(rCase.getValues());
					result = true;
				}
			}
		}
		//makes it this far, nothing found
		if(!result) System.out.println(errors.Err4); return;



		
	}



	/**
	 * Print all the records in this table ordered by the given "ocolumn" in ascending order. Should call 
	 * the getValues method of the record class. Needs to use the index for ocolumn and call the values 
	 * method of the used B+ tree. If the table is empty, print error message 1. If no indexes are 
	 * present, print error message 2. If there is no index available for "ocolumn", print error message 3.
	 */
	//SQL: select * from table order by ocolumn
	private void select(String ocolumn) {
		// Your code goes here
		//System.out.println(ocolumn);
		Error errors = new Error();
		//empty table
		if(this.recordCount==0){ System.out.println(errors.Err1); return;}
		if(this.indexCount==0){ System.out.println(errors.Err2); return;}
		if(this.getIndexOfCol(ocolumn)==-1){ System.out.println(errors.Err3); return;}
		
		
		//no errors, a sortable coloumn exists
		BPTree subject = this.indexes[getIndexOfCol(ocolumn)].getIndex();
		Object[] locations = subject.values();

		for(int u=0; u<locations.length; u++){
			String outString = this.records[(int)locations[u]].getValues();
			System.out.println(outString);
		}
		
		
	}



	/**
	 * Print all the records in this table. Should call the getValues method of the record class. If 
	 * the table is empty print error message 1.
	 */
	//SQL: select * from table
	private void select() {
		// Your code goes here
		Error errors = new Error();
		if(this.recordCount==0){ System.out.println(errors.Err1); return;}
		for(int i=0; i<rowId-1; i++){
			if(records[i]!=null){
				System.out.println(records[i].getValues());
			}
		}
		//System.out.println("Should work");
		
	}



	/**
	 * Create an index called "name" using the record values from "column" as keys and the row id as value. 
	 * The created B+ tree must match the data type of "column". Return true if successful and false if 
	 * column does not exist.
	 */
	//@SuppressWarnings("unchecked")
	public boolean createIndex(String name, String column) {
		if (!validColumn(column)) return false;
		if (this.recordCount == 0) return false;
		
		//get the placement of a coloumn in a record
		int colIndex = getPlaceOfCol(column);

		//the coloumn exists
		BPTree subjectTree = new BPTree(this.m);

		//now add all existing table values into the index
		for(int o=0; o<rowId-1; o++){
			if(records[o]!=null){
				subjectTree.insert((Comparable) records[o].getColumn(colIndex),o);
			}
		}

		Index newIndex = new Index(name,column,subjectTree);
		this.indexes[indexCount]=newIndex;
		this.indexCount++;
		// Your code goes here
		return true;
	}



	/**
	 * Print all the keys in the index "name". Should call the print method of the used B+ tree.
	 */
	public void printIndex(String name) {
		// Your code goes here
		int index = indexOfIndex(name);
		if(index == -1) return;
		
		BPTree crit = indexes[index].getIndex();
		crit.print();

	}



	//Helper methods
	private boolean validColumn(String col){
		for(int i=0; i<this.columns.length; i++)
			if(col == columns[i]) return true;
		return false;
	}

	private int getIndexOfCol(String col){
		int index = -1;
		for(int i=0; i<indexCount; i++){
			if(this.indexes[i].getColumnName()==col){
				index = i;
				break;
			}
		}
		return index;
	}

	private int getPlaceOfCol(String col){
		for(int i=0; i<this.columns.length; i++)
			if(col == columns[i]) return i+1;
		return -1;
	}

	private Record getFirstRecord(){
		for(int i=0; i<rowId-1; i++){
			if(this.records[i]!=null) return this.records[i];
		}
		return null;
	}

	private int indexOfIndex(String name){
		for(int p=0; p<this.indexCount; p++){
			if(indexes[p].getName()==name) return p;
		}
		return -1;
	}


}