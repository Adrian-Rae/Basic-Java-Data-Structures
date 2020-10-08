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
		
		Graph G = new Graph("graph2.txt");
		System.out.println("Graph G");
		String[] searchTerms = new String[]{"A","B","C","D"};
		for(int i=0; i<searchTerms.length; i++) System.out.println("The route: "+G.getChinesePostmanRoute(searchTerms[i]));
	}
	
}
