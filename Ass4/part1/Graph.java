/*
Complete your details...
Name and Surname: ADRIAN RAE
Student/staff Number: 19004029
*/

//INPUT AND OUTPUT
import java.io.File;
import java.io.FileNotFoundException;

//NO GRAPH DATA STRUCTURES - only linked lists and scanners
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

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
		this.visitString = this.visitString.substring(1,this.visitString.length());
		
		return this.visitString;
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

	public void print(){
		System.out.println("vertices: "+ this.vertices);
		for(int i=0; i<numEdges; i++){
			Edge e = this.edges.get(i);
			System.out.println("Edge: "+ e);
		}
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
		return "";
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
		
		return "";
	}
	
	public int getChinesePostmanCost()
	{
		/*
		This method should return the cost of the optimal Chinese Postman
		route determined by your algorithm.
		*/
		
		return 0;
	}
		
	public Graph getChinesePostmanGraph()
	{
		/*
		This method should return your graph with the extra edges as constructed
		during the optimal Chinese postman route calculation.
		*/
		
		return null;
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
		
		return "not available";
	}



	//helpers
	private String maxInSet(List<Edge> e){
		//returns the Node value with least priority in the DFS
		String max = "";
		for(int i=0; i<e.size(); i++){
			if(e.get(i).toVertex.value.compareTo(max)>0) max=e.get(i).toVertex.value;
		}
		return max;
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
	public Edge(Vertex from, Vertex to, int mass){
		this.fromVertex = from;
		this.toVertex = to;
		this.weight = mass;
	}
	@Override
	public String toString() {
		return "FROM:"+fromVertex+" TO:"+toVertex+" Weight:"+weight;
	}
}
