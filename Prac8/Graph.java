// Name:
// Student number:
import java.util.ArrayList;
import java.util.List;
import java.util.*; //for the queue
 
public class Graph {
 
	private List<Vertex> verticesList;

	public Graph() {
		this.verticesList = new ArrayList<>();
	}

	public void addVertex(Vertex vertex) {
		this.verticesList.add(vertex);
	}

	public void reset() {
		for(Vertex vertex : verticesList) {
			vertex.setVisited(false);
			vertex.setPredecessor(null);
			vertex.setDistance(Double.POSITIVE_INFINITY);
		}
	}

	////// Implement the methods below this line //////

	public List<Vertex> getShortestPath(Vertex sourceVertex, Vertex targetVertex) {
		if(verticesList.contains(sourceVertex) && verticesList.contains(targetVertex)){
			Queue<Vertex> toBeChecked = new LinkedList<>();
			//set all distances save the start, to infinity
			for(int i=0; i<verticesList.size();i++){
				verticesList.get(i).setDistance(Double.POSITIVE_INFINITY);
				verticesList.get(i).setPredecessor(null);
			}
			sourceVertex.setDistance(0);
			//add source to queue
			toBeChecked.add(sourceVertex);

			while(!toBeChecked.isEmpty()){
				Vertex curr = toBeChecked.poll();
				//get a list of all neighbouring nodes
				List<Edge> neighbourList = curr.getAdjacenciesList();
				for(int j=0; j<neighbourList.size();j++){
					Vertex v = neighbourList.get(j).getEndVertex();
					double neighbourDistance = neighbourList.get(j).getWeight();
					double new_dist = curr.getDistance() + neighbourDistance;
					if(new_dist<-1000000) return null;
					else if(new_dist < v.getDistance()){
						v.setDistance(new_dist);
						//if(v.isVisited()) return null;
						v.setPredecessor(curr);
						if(!toBeChecked.contains(v)){
							//if(v.isVisited()) return null;
							v.setVisited(true);
							toBeChecked.add(v);
						}
					}
				}
			}

			//Distances and predeccessors have been updated, now generate the path
			Stack<Vertex> stackie = new Stack();
			Vertex e = targetVertex;
			while(e.getPredecessor()!=null){
				stackie.push(e);
				e = e.getPredecessor();
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
		// Your code here
	}

	public double getShortestPathDistance(Vertex sourceVertex, Vertex targetVertex) {
		List<Vertex> path = getShortestPath(sourceVertex,targetVertex);
		List<Vertex> empty = new ArrayList<>();
		if(path==null) return Double.NEGATIVE_INFINITY;
		else if(path.equals(empty)) return Double.POSITIVE_INFINITY;
		else return targetVertex.getDistance();
		// Your code here
	}


	//HELPER FUNCTIONS




}