public class Main {
 
	public static void main(String[] args) {
		
		//*
		// Case 1
		Vertex vertexA = new Vertex("A");
		Vertex vertexB = new Vertex("B");
		Vertex vertexC = new Vertex("C");
		Vertex vertexD = new Vertex("D");
		Vertex vertexE = new Vertex("E");
		
		vertexA.addNeighbour(new Edge(vertexA, vertexC, 10));
		vertexA.addNeighbour(new Edge(vertexA, vertexB, 17));
		vertexC.addNeighbour(new Edge(vertexC, vertexB, 5));
		vertexC.addNeighbour(new Edge(vertexC, vertexD, 9));
		vertexC.addNeighbour(new Edge(vertexC, vertexE, 11));
		vertexB.addNeighbour(new Edge(vertexB, vertexD, 1));
		vertexD.addNeighbour(new Edge(vertexD, vertexE, 6));
		
		Graph graph = new Graph();
		graph.addVertex(vertexA);
		graph.addVertex(vertexB);
		graph.addVertex(vertexC);
		graph.addVertex(vertexD);
		graph.addVertex(vertexE);

		Vertex startVertex = vertexA;
		Vertex endVertex = vertexE;
		
		System.out.println("Minimum distance from " + startVertex.getName() + " to " + endVertex.getName() + " : " + graph.getShortestPathDistance(startVertex, endVertex));
		System.out.println("Shortest Path from " + startVertex.getName() + " to " + endVertex.getName() + " : " + graph.getShortestPath(startVertex, endVertex));

		// Expected output
		//Minimum distance from A to E : 21.0
		//Shortest Path from A to E : [A, C, E]
		
		//*/
		/*
		
		Vertex vertexA = new Vertex("A");
		Vertex vertexB = new Vertex("B");
		Vertex vertexC = new Vertex("C");
		Vertex vertexD = new Vertex("D");
		Vertex vertexE = new Vertex("E");
		
		vertexA.addNeighbour(new Edge(vertexA, vertexB, 1));
		vertexB.addNeighbour(new Edge(vertexB, vertexC, -1));
		vertexC.addNeighbour(new Edge(vertexC, vertexD, -1));
		vertexD.addNeighbour(new Edge(vertexC, vertexB, -1));
		vertexB.addNeighbour(new Edge(vertexB, vertexE, 1));
		
		Graph graph = new Graph();
		graph.addVertex(vertexA);
		graph.addVertex(vertexB);
		graph.addVertex(vertexC);
		graph.addVertex(vertexD);
		graph.addVertex(vertexE);

		Vertex startVertex = vertexA;
		Vertex endVertex = vertexE;
		
		System.out.println("Minimum distance from " + startVertex.getName() + " to " + endVertex.getName() + " : " + graph.getShortestPathDistance(startVertex, endVertex));
		System.out.println("Shortest Path from " + startVertex.getName() + " to " + endVertex.getName() + " : " + graph.getShortestPath(startVertex, endVertex));
		// Expected output
		//Minimum distance from A to E : -infinity
		//Shortest Path from A to E : null
		//*/

	}
}