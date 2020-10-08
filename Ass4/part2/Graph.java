/*
Complete your details...
Name and Surname: ADRIAN RAE
Student/staff Number: 19004029
*/

//INPUT AND OUTPUT
import java.io.File;
import java.io.FileNotFoundException;

//NO GRAPH DATA STRUCTURES - only linked lists, queues, Stacks and scanners
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList; 
import java.util.Queue;
import java.util.Stack;

/*
 You must complete this class to solve the Chinese Postman problem for any given graph.
 Additional instructions are provided in comments throughout the class.
 */

 //PART I
 
public class Graph
{
	/*
	1. You may not change the signatures of any of the given methods.  You may 
	however add any additional methods and/or fields which you may require to aid 
	you in the completion of this assignment.
	
	2. You will have to design and implement your own Graph class in terms of 
	graph representation.
	
	3. You may add any additional classes and/or methods to your assignment.
	*/


	//Primary Approach - vertex list
	private int numVertices;
	private List<Vertex> vertices;
	private List<String> vertexNames;

	//Dual approach - edge lists
	private int numEdges; //hold the number of undirected edges
	private List<Edge> edges; //holds unique edges - each vertex still has a copy which enforces logical consistency

	//Used for traversal
	private String visitString;
	
	//used for the postman problem
	private List<Vertex> oddVertices;
	private List<VertexPair> optimalPairing;

	public Graph(List<String> listEdges){ //the contents of a file, for example
		//get the number of nodes
			this.numVertices = 0;
			this.vertices = new ArrayList<>();
			this.vertexNames = new ArrayList<>();	
			
		//create a converter
			this.numEdges = 0;
			this.edges = new ArrayList<>(); 

		//generate the edges
			//add the edges and vertices
			for(int i=0; i<listEdges.size(); i++){
				String data = listEdges.get(i);
				//Add the edge
				String start = data.substring(0,data.indexOf(","));
				String end = data.substring(data.indexOf(",")+1,data.indexOf(",",data.indexOf(",")+1));
				int weight = Integer.parseInt(data.substring(data.indexOf(",",data.indexOf(",")+1)+1,data.length()));
				//System.out.println("ADDING EDGE FROM START:"+start+" END:"+end+" WEIGHT:"+weight);

				//if either vertex doesnt exist, add it
				int indexStart=this.vertexNames.indexOf(start);
				if(indexStart==-1){
					Vertex newVertex = new Vertex(start);
					this.vertices.add(newVertex);
					this.vertexNames.add(start);
					indexStart=this.vertexNames.indexOf(start);
				}

				int indexEnd=this.vertexNames.indexOf(end);
				if(indexEnd==-1){
					Vertex newVertex = new Vertex(end);
					this.vertices.add(newVertex);
					this.vertexNames.add(end);
					indexEnd=this.vertexNames.indexOf(end);
				}

				//both vertices exists, get them
				Vertex startVertex = this.vertices.get(indexStart);
				Vertex endVertex = this.vertices.get(indexEnd);

				//make the edge and add it to the vertices - structured so that the 'from element' of each edge matches the vertex in question
				Edge newEdge1 = new Edge(startVertex,endVertex,weight);
				Edge newEdge2 = new Edge(endVertex,startVertex,weight);

				//add the adges to the vertexes
				startVertex.addNeighbour(newEdge1);
				endVertex.addNeighbour(newEdge2);

				this.edges.add(newEdge1);
				//this.edges.add(newEdge2);
				this.numEdges++;
			}	
	}

	public Graph(String f)
	{
		/*
		The constructor receives the name of the file from which a graph
		is read and constructed.
		*/
		//=======================================================================================================================================
		// Underlying assumption: an undirected graph is equivalent to a digraph where each directed edge has an inverse edge of the same weight
		//=======================================================================================================================================

		//read from the file
		try {
			File fileObject = new File(f);
			Scanner line = new Scanner(fileObject);
			
			//get the number of nodes
			this.numVertices = Integer.parseInt(line.nextLine());
			this.vertices = new ArrayList<>();
			this.vertexNames = new ArrayList<>();
			

			//create a converter
			this.numEdges = 0;
			this.edges = new ArrayList<>(); 
	
			//add the edges and vertices
			while (line.hasNextLine()) {
				String data = line.nextLine();
				//break if empty line
				if(data.compareTo("")==0) break;
				//Add the edge
				String start = data.substring(0,data.indexOf(","));
				String end = data.substring(data.indexOf(",")+1,data.indexOf(",",data.indexOf(",")+1));
				int weight = Integer.parseInt(data.substring(data.indexOf(",",data.indexOf(",")+1)+1,data.length()));
				//System.out.println("ADDING EDGE FROM START:"+start+" END:"+end+" WEIGHT:"+weight);

				//if either vertex doesnt exist, add it
				int indexStart=this.vertexNames.indexOf(start);
				if(indexStart==-1){
					Vertex newVertex = new Vertex(start);
					this.vertices.add(newVertex);
					this.vertexNames.add(start);
					indexStart=this.vertexNames.indexOf(start);
				}

				int indexEnd=this.vertexNames.indexOf(end);
				if(indexEnd==-1){
					Vertex newVertex = new Vertex(end);
					this.vertices.add(newVertex);
					this.vertexNames.add(end);
					indexEnd=this.vertexNames.indexOf(end);
				}

				//both vertices exists, get them
				Vertex startVertex = this.vertices.get(indexStart);
				Vertex endVertex = this.vertices.get(indexEnd);

				//make the edge and add it to the vertices - structured so that the 'from element' of each edge matches the vertex in question
				Edge newEdge1 = new Edge(startVertex,endVertex,weight);
				Edge newEdge2 = new Edge(endVertex,startVertex,weight);

				//add the adges to the vertexes
				startVertex.addNeighbour(newEdge1);
				endVertex.addNeighbour(newEdge2);

				this.edges.add(newEdge1);
				//this.edges.add(newEdge2);
				this.numEdges++;
			}

			line.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("No such file exists.");
			return;
		}
	}
	
	public Graph clone()
	{
		/*
		The clone method should return a deep copy/clone
		of this graph.
		*/

		//make it as if youre reading file contents
		List<String> newEdges = new ArrayList<>();
		for(int i=0; i<this.numEdges; i++){
			Edge e = this.edges.get(i);
			String data = e.fromVertex+","+e.toVertex+","+e.weight;
			newEdges.add(data);
		}
		return new Graph(newEdges);
	}
		
	public boolean reconstructGraph(String fileName)
	{
		/*
		This method should discard the current graph and construct a new
		graph contained in the file named "fileName". Return true if reconstruction
		was successful.
		*/
		
		// Get the file 
        File f = new File(fileName); 
  
		//see whether file exists
		if (f.exists()){
			Graph h = new Graph(fileName);
			//make the aspects of this graph the ones of h
			this.numVertices = h.numVertices;
			this.vertices = h.vertices;
			this.vertexNames = h.vertexNames;
			this.numEdges = h.numEdges;
			this.edges = h.edges;
			return true;
		}     
        else return false;	
	}

	public int numEdges(String u, String v)
	{
		/*
		This method returns the number of direct edges between u and v vertices.
		If there are no direct edges, return 0.
		In there is 1 or more direct edges, return the number of edges.
		*/
		
		int numEdges = 0;
		//see if the start vertex and end vertex exist
		int indexStart = this.vertexNames.indexOf(u);
		int indexEnd = this.vertexNames.indexOf(v);
		if(indexStart==-1 || indexEnd==-1) return numEdges; //one vertex doesn't exist

		//get the start Vertex
		Vertex start = this.vertices.get(indexStart);
		//get the list of edges
		List<Edge> e = start.edges;
		//iterate through edges, if the 'to' vertex is the end vertex, add it
		for(int i=0; i<e.size(); i++){
			Vertex end = e.get(i).toVertex;
			//System.out.println("COMPARING: vertex value:"+end.value+" search value:"+v);
			if(end.value.compareTo(v)==0) numEdges++;
		}
		return numEdges;
	}
	
	public int getDegree(String u)
	{
		/*
		This method returns the degree of vertex u.
		*/
		int degree = 0;
		//see if the start vertex and end vertex exist
		int indexStart = this.vertexNames.indexOf(u);
		if(indexStart!=-1) degree = this.vertices.get(indexStart).degree;
		return degree;
	}
	
	
	public boolean changeLabel(String v, String newLabel)
	{
		/*
		Change the label of the vertex v to newLabel.  The method returns true
		if the label change was successful, and false otherwise.
		*/
		boolean changed = false;
		int indexStart = this.vertexNames.indexOf(v);
		if(indexStart!=-1){ this.vertices.get(indexStart).value=newLabel; changed=true;}
		return changed;
	}

	public String depthFirstTraversal(String v)
	{
		/*
		This method returns a depth first traversal of the graph,
		starting with node v. When there is choice between vertices,
		choose in alphabetical order.
		
		The list must be separated by commas containing no additional 
		white space.
		*/

		//All nodes are unvisited
		for(int i=0; i<this.vertices.size(); i++){this.vertices.get(i).visited=false;}
		//have a visiting order
		this.visitString = "";

		//get the vertex to start from
		int sIndex = this.vertexNames.indexOf(v);
		if(sIndex==-1) return "";
		Vertex START = this.vertices.get(sIndex);
		//go about recursively
		this.DFS(START);
		//trim the preceeding comma
		if(this.visitString.length()>0) this.visitString = this.visitString.substring(1,this.visitString.length());
		
		return this.visitString;
	}


	//PART II
	
	public String getOdd()	
	{
		/*
		This method returns a list of all vertices with odd degrees.
		The vertices should be sorted alphabetically. If there are no
		vertices with odd degrees, return an empty string.
		
		The list must be separated by commas containing no additional 
		white space.
		*/

		//consider being added to the list like 'visiting'
		//All nodes are unvisited
		for(int i=0; i<this.vertices.size(); i++){this.vertices.get(i).visited=false;}
		//have a visiting order
		this.visitString = "";
		//vist adjacent unvisited vertices in ALPHABETICAL orders
		List<Vertex> cases = this.vertices;
		this.oddVertices = new ArrayList<>();

		//while there is an unvisited vertex - go to the one with alphabetical precedence
		boolean canVisit = true;
		while(canVisit){
			//find the alphabetical successor that hasnt been visited
			String min = maxVInSet(cases); //predefined long value for which all strings are smaller
			int minIndex = -1;
			for(int i=0; i<cases.size(); i++){
				Vertex v = cases.get(i);
				boolean odd = (this.getDegree(v.value)%2 == 1);
				if(v.value.compareTo(min)<=0 && odd && !v.visited){
					minIndex = i;
					min = cases.get(i).value;
				}
			}
			if(minIndex!=-1){
				Vertex next = cases.get(minIndex);
				next.visited = true;
				this.oddVertices.add(next);
				this.visitString += ','+next.value;
			}
			else canVisit = false;
		}
		if(this.visitString.length()>0) this.visitString = this.visitString.substring(1,this.visitString.length());
		return this.visitString;
	}
	
	public String getPath(String u, String v)
	{
		/*
		This method should return the shortest path between two vertices.
		Inputs are the vertex labels, as read from the input file.
		
		The returned string should be the vertex labels, starting with u and
		ending with v. The list must be separated by commas containing no additional 
		white space.
		
		Assumption: both vertices are present in the graph, and a path between 
		them exists.
		*/

		//as both vertices are present and a path exists, get the vertices and generate the List
		Vertex start = this.vertices.get(this.vertexNames.indexOf(u));
		Vertex end = this.vertices.get(this.vertexNames.indexOf(v));

		//get the shortest path route
		List<Vertex> path = this.getShortestPath(start,end);

		//generate the string - always contains at least one element
		String list = path.get(0).value;
		for(int i=1; i<path.size(); i++) list+=","+path.get(i).value;
		return list;
	}
	
	public int getChinesePostmanCost()
	{
		/*
		This method should return the cost of the optimal Chinese Postman
		route determined by your algorithm.
		*/

		//get the length of all the edges 
		int edgeSum = this.sumEdges();
		//get the length of the added edges
		String p = this.getOdd();
		int cpCost = (int) this.minimalLength(this.oddVertices);

		return edgeSum+cpCost;
	}
		
	public Graph getChinesePostmanGraph()
	{
		/*
		This method should return your graph with the extra edges as constructed
		during the optimal Chinese postman route calculation.
		*/

		List<Edge> totalEdges = new ArrayList<>();
		
		//add the existing graph edges
		for(int e=0; e<this.edges.size(); e++) totalEdges.add(this.edges.get(e));

		//add the edges identified to be extensive
		List<VertexPair> pairs = this.getOptimalPairing();
		List<Edge> newEdges = new ArrayList<>();
		for(int p=0; p<pairs.size(); p++){
			VertexPair m = pairs.get(p);
			//this defines two vertices - add an edge for every vertex along with path between the two 
			List<Vertex> path = getShortestPath(m.u,m.v);
			//System.out.println("Path from A to H" + path);
			
			for(int j=path.size()-1; j>0; j--){
				Edge newEdge = new Edge(path.get(j), path.get(j-1), (int) this.getShortestPathDistance(path.get(j), path.get(j-1)));
				//System.out.println("New Edge" + newEdge);
				totalEdges.add(newEdge);
			}
		}
		for(int n=0; n<newEdges.size(); n++){
			totalEdges.add(newEdges.get(n));
		}

		//System.out.println("optimal pairing: "+pairs);

		//make a new GRAPH
		List<String> edgeValues = new ArrayList<>();
		for(int k=0; k<totalEdges.size();k++){
			Edge o = totalEdges.get(k);
			String edge = o.fromVertex.value+","+o.toVertex.value+","+o.weight;
			edgeValues.add(edge);
		}
		Graph h = new Graph(edgeValues);
		return h;
	}
		
		
	public String getChinesePostmanRoute(String v)
	{
		/*
		This method should return the circular optimal Chinese postman path from v 
		back to v. If there are vertices with odd degrees, return "not available".
		Otherwise, return a list of vertices starting and ending in v.
		When there are alternative paths, choose the next vertex in alphabetical order.

		The list must be separated by commas containing no additional 
		white space.
		*/
		//System.out.println(h.getOdd());
		Graph h = this.getChinesePostmanGraph();
		if(h.getOdd().compareTo("")!=0) return "not available";
		else return h.traverse(v);
		
	}



	//helpers
	public void print(){
		System.out.println("vertices: "+ this.vertices);
		for(int i=0; i<numEdges; i++){
			Edge e = this.edges.get(i);
			System.out.println("Edge "+i+":"+ e);
		}
	}

	private String maxInSet(List<Edge> e){
		//returns the Node value with least priority in the DFS
		String max = "";
		for(int i=0; i<e.size(); i++){
			if(e.get(i).toVertex.value.compareTo(max)>0) max=e.get(i).toVertex.value;
		}
		return max;
	}

	private String maxVInSet(List<Vertex> e){
		//returns the Node value with least priority in the DFS
		String max = "";
		for(int i=0; i<e.size(); i++){
			if(e.get(i).value.compareTo(max)>0) max=e.get(i).value;
		}
		return max;
	}

	private void DFS(Vertex v){
		
		//visit
		this.visitString+=","+v.value;
		v.visited = true;

		//vist adjacent unvisited vertices in ALPHABETICAL orders
		List<Edge> neighbours = v.edges;

		/*/DEBUG
		System.out.println("Presently at node "+v.value);
		for(int j=0; j<neighbours.size(); j++){
			System.out.print("[neighbour:"+neighbours.get(j).toVertex.value+"/visited:"+neighbours.get(j).toVertex.visited+"]");
		}
		System.out.println();
		//*/

		//while there is an unvisited edge - go to the one with alphabetical precedence
		boolean canVisit = true;
		while(canVisit){
			//find the alphabetical successor that hasnt been visited
			String min = maxInSet(neighbours); //predefined long value for which all strings are smaller
			int minIndex = -1;
			for(int i=0; i<neighbours.size(); i++){
				Vertex neighbour = neighbours.get(i).toVertex;
				//System.out.println("is "+neighbour.value+" smaller than "+min+":"+(neighbour.value.compareTo(min)<=0));
				//System.out.println("is "+neighbour.value+" visited:"+neighbour.visited);
				if(neighbour.value.compareTo(min)<=0 && !neighbour.visited){
					minIndex = i;
					min = neighbour.value;
				}
			}
			if(minIndex!=-1){
				Vertex next = neighbours.get(minIndex).toVertex;
				this.DFS(next);
			}
			else canVisit = false;
		}
	}

	public List<Vertex> getShortestPath(Vertex sourceVertex, Vertex targetVertex) {
		//A label correcting algorithm 
		
		//All nodes are unvisited
		for(int i=0; i<this.vertices.size(); i++){this.vertices.get(i).visited=false;}
		//have a visiting order
		this.visitString = "";
		if(this.vertices.contains(sourceVertex) && this.vertices.contains(targetVertex)){
			Queue<Vertex> toBeChecked = new LinkedList<>();
			//set all distances save the start, to infinity
			for(int i=0; i<this.vertices.size();i++){
				this.vertices.get(i).distance = Double.POSITIVE_INFINITY;
				this.vertices.get(i).predecessor = null;
			}
			sourceVertex.distance = 0;
			//add source to queue
			toBeChecked.add(sourceVertex);

			while(!toBeChecked.isEmpty()){
				Vertex curr = toBeChecked.poll();
				//get a list of all neighbouring nodes
				List<Edge> neighbourList = curr.edges;
				for(int j=0; j<neighbourList.size();j++){
					Vertex v = neighbourList.get(j).toVertex;
					double neighbourDistance = neighbourList.get(j).weight;
					double new_dist = curr.distance + neighbourDistance;
					if(new_dist<-1000000) return null;
					else if(new_dist < v.distance){
						v.distance = new_dist;
						//if(v.isVisited()) return null;
						v.predecessor = curr;
						if(!toBeChecked.contains(v)){
							//if(v.isVisited()) return null;
							v.visited = true;
							toBeChecked.add(v);
						}
					}
				}
			}

			//Distances and predeccessors have been updated, now generate the path
			Stack<Vertex> stackie = new Stack();
			Vertex e = targetVertex;
			while(e.predecessor!=null){
				stackie.push(e);
				e = e.predecessor;
			}
			//now points to the start variable
			if(e != sourceVertex) return new ArrayList<>();
			
			stackie.push(e);
			//new list
			List<Vertex> returnList = new ArrayList<>();
			while(!stackie.isEmpty()){
				returnList.add(stackie.pop());
			}

			return returnList;

		}
		else{
			return new ArrayList<>();
		}
	}

	public double getShortestPathDistance(Vertex sourceVertex, Vertex targetVertex) {
		List<Vertex> path = getShortestPath(sourceVertex,targetVertex);
		List<Vertex> empty = new ArrayList<>();
		if(path==null) return Double.NEGATIVE_INFINITY;
		else if(path.equals(empty)) return Double.POSITIVE_INFINITY;
		else return targetVertex.distance;
		// Your code here
	}

	public List<VertexPair> getOptimalPairing(){
		String oddString = this.getOdd(); //just a dummy variable, used more for the attribute it alters
		//generate a list of possible pairings  - garunteed to be even
		List<Vertex> odds = this.oddVertices;
		double minDis = this.minimalLength(odds); //sets the relevant details 
		//System.out.println("the minimum is "+minDis);
		return findSum(minDis,odds);
	} 

	//have a recursive function that pairs the list
	private double minimalLength(List<Vertex> p){
		List<String> choiceSpace = new ArrayList<>(); //generate a list of names of vertices
		for(int y=0; y<p.size(); y++) choiceSpace.add(p.get(y).value);
		return this.minimise(choiceSpace);
	}
	private double minimise(List<String> possibleSpace){ //level refers to how many pairs have already been chosen
		//possible space is a list of vertex names
		if(possibleSpace.size()==0) return 0; //if theres an empty list, no need to optimise.

		double minDistance = Double.POSITIVE_INFINITY; //start off with a high min, go down
		
		//for every N vertices, there are N-1 pairings possible 
		for(int i=0; i<possibleSpace.size()-1; i++){
			for(int j=i+1; j<possibleSpace.size(); j++){
				//get the vertices 
				Vertex u = this.vertices.get(this.vertexNames.indexOf(possibleSpace.get(i)));
				Vertex v = this.vertices.get(this.vertexNames.indexOf(possibleSpace.get(j)));
				//make a vertex pair 
				VertexPair casePair = new VertexPair(u,v,this);

				//remove the vertices from the search space
				List<String> newSearchSpace = new ArrayList<>();
				for(int k=0;k<possibleSpace.size(); k++)
				if(possibleSpace.get(k).compareTo(u.value)!=0 && possibleSpace.get(k).compareTo(v.value)!=0){ newSearchSpace.add(possibleSpace.get(k));}

				double revisedDistance = casePair.distance + this.minimise(newSearchSpace);
				if(revisedDistance < minDistance){
					//youve found a new optimal path
					minDistance = revisedDistance;
				}
			}
		}
		return minDistance;
	}


	public double getPathDistance(Vertex from, Vertex to){
		List<Vertex> path = getShortestPath(from,to);
		return path.get(path.size()-1).distance;
	}

	private double getChoiceDistance(List<VertexPair> l){
		//add the distances of each pair in the list
		double totalDist = 0;
		for(int i=0; i<l.size(); i++) totalDist+=l.get(i).distance;
		return totalDist;
	}

	private int sumEdges(){
		int sum = 0;
		for(int i=0; i<this.edges.size(); i++){
			sum+=this.edges.get(i).weight;
		}
		return sum;
	}

	private List<VertexPair> findSum(double inSum, List<Vertex> p){
		//find the first alphabetical pairing that is equivalent to this sum
		//honestly, randomly generate pairs until their sum is the required one
		List<VertexPair> results = null;
		double sum = -1;
		while(sum != inSum){
			//generate a random list of pairs
			results = new ArrayList<>();
			List<Integer> indexes = new ArrayList<>();

			int scale = p.size()/2;
			
			for(int i=0; i<scale; i++){
				int index1 = randBetween(0,2*scale-1);
				int index2 = randBetween(0,2*scale-1); 
				while(index1==index2 || indexes.indexOf(index1)>=0 || indexes.indexOf(index2)>=0 ){ index1=randBetween(0,2*scale-1); index2=randBetween(0,2*scale-1);}
				//System.out.println("Random Pairing: "+index1+","+index2);
				
				

				Vertex v = p.get(index1); Vertex u = p.get(index2);
				VertexPair casePair = new VertexPair(v, u, this);
				//System.out.println("pair in consideration: "+casePair);
				results.add(casePair);
				//remove from the list
				indexes.add(index1); indexes.add(index2);
				//System.out.println("Eliminating: "+index1+","+index2);
			}
			sum = getChoiceDistance(results);
			//System.out.println("Sum is: "+sum);

		}
		return results;
	}


	private int randBetween(int min, int max){
		return (int)(Math.random()*((max-min)+1))+min;
	}


	public int getChineseVertexCount(){ //returns the number of vertices that should be encountered in the optimal route
		int p = 0;
		for(int i=0; i<this.vertices.size(); i++){
			Vertex v = this.vertices.get(i);
			p+=(v.edges.size()/2);
		}
		return 1+p;
	}

	public boolean traversable(List<Edge> e){
		for(int i=0; i<e.size(); i++){
			if(!e.get(i).traversed) return true;
		}
		return false;
	}

	public String traverse(String input){
		String path = "";
		Stack<Vertex> s = new Stack();
		//get the vertex corresponding to the START
		boolean validCycle = false;
		int expectedNodes = this.getChineseVertexCount();
		int attempts = 0;
		String bestGuess = ".";
		//start at u - traverse edges until 17 nodes are found 
		while(!validCycle){
			path = "";
			int numNodes = 0;
			Vertex u = this.vertices.get(this.vertexNames.indexOf(input));
			//set ll vertices edges to unvisited
			for(int y=0; y<this.vertices.size(); y++){
				List<Edge> r = this.vertices.get(y).edges;
				for(int z=0; z<r.size(); z++)
					r.get(z).traversed = false;
			}
			for(int i=0; i<expectedNodes; i++){
				//System.out.println("Attempt: "+attempts+" at node: "+i+" value: "+u.value+" "+u.edges);
				int indexEdge=0;
				if(!traversable(u.edges)) break;
				boolean foundEdge = false;
				do{
					indexEdge=randBetween(0,u.edges.size()-1);
					if(u.edges.get(indexEdge).traversed==false) foundEdge=true;
				}
				while(!foundEdge);
				//visit the EDGE
				//have an index for the Edge
				Edge e = u.edges.get(indexEdge);
				e.traversed = true;
				Vertex v = e.toVertex;
				//make the converse true as well
				for(int j=0; j<v.edges.size(); j++){
					if(v.edges.get(j).toVertex.value.compareTo(u.value)==0){
						v.edges.get(j).traversed = true;
						break;
					}
				}
				path+=","+u.value;
				u = v; //go to the next value
				numNodes++;
				//System.out.println("going to: "+u.value);
			}
			path+=","+u.value;
			numNodes++;
			//System.out.println("Finished Trail at: "+u.value+" should be "+input+" with number "+numNodes);
			if(u.value.compareTo(input)==0 && numNodes==expectedNodes){
				if(bestGuess==".") bestGuess = path;
				else if(path.compareTo(bestGuess)<0) bestGuess = path;
			}
			
			if(attempts==(int) Math.pow(2,expectedNodes)) validCycle =true;
			attempts++;
		}
		return bestGuess.substring(1,bestGuess.length());


	}

	 
	
}

class Vertex implements Comparable<Vertex>{
	public String value;
	public List<Edge> edges;
	public double distance;
	public int degree;
	
	public boolean visited;
	public Vertex predecessor;


	public Vertex(String inValue){
		this.value = inValue;
		this.edges = new ArrayList<>();
		this.distance = Double.POSITIVE_INFINITY;
		this.degree = 0;
	}

	public void addNeighbour(Edge edge) {
		this.edges.add(edge);
		degree++;
	}

	@Override
	public String toString() {
		return this.value;
	}
 
	@Override
	public int compareTo(Vertex otherVertex) {
		return Double.compare(this.distance, otherVertex.distance);
	}




}

class Edge{
	public Vertex fromVertex;
	public Vertex toVertex;
	public int weight;
	public boolean traversed;
	public Edge(Vertex from, Vertex to, int mass){
		this.fromVertex = from;
		this.toVertex = to;
		this.weight = mass;
		this.traversed = false;
	}
	@Override
	public String toString() {
		return "["+fromVertex+","+toVertex+","+weight+","+traversed+"]";
	}
}

class VertexPair{
	public Vertex u;
	public Vertex v;
	public int distance;
	public VertexPair(Vertex p,Vertex q, Graph g){
		this.u = p;
		this.v = q;
		this.distance = (int) g.getPathDistance(u, q);
	}
	public boolean equalTo(VertexPair k){
		return (this.u == k.u && this.v==k.v) || (this.u == k.v && this.v == k.u);
	}

	@Override
	public String toString() {
		return "["+this.u.value+"|"+this.v.value+"|"+this.distance+"]";
	}
}


