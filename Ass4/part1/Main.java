/*
Complete your details...
Name and Surname: ADRIAN RAE
Student/staff Number: 19004029
*/


public class Main
{
	/*
		This file will be overwritten by Fitchfork.
		Use it to test your code.
	*/
	public static void main(String[] args){
		
		//*/ constructor
		
		Graph G = new Graph("graph.txt");
		G.print();
		
		//*/

		/*//clone and reconstruct
			Graph H = G.clone();
			H.print();

			H.reconstructGraph("graph2.txt");
			H.print();

			G.print();

		//*/

		
		//System.out.println("Num edges between D and E: "+G.numEdges("D","E")); /* EDGES */
		
		//System.out.println("Degree of D: "+G.getDegree("D")); /* DEGREE */
		
		/* // Changing labels
		
		System.out.println("Changed value of a to P: "+G.changeLabel("a","P"));
		G.print(); 

		System.out.println("Changed value of F to Q: "+G.changeLabel("F","Q"));
		G.print(); 
		
		//*/
		
		String start = "a";
		System.out.println("DEPTH FIRST from "+start+": "+G.depthFirstTraversal(start));



	}
	
}
