/*
Name and Surname: Adrian Rae
Student Number: u19004029
*/
import java.util.*;

public class Tester 
{
   public static void main(String[] args) throws Exception
   {
      /*
      TODO: Write code to thoroughly test your implementation here.
      
      Note that this file will be overwritten for marking purposes.
      */
     
    System.out.println("=================START==================");
      
    //*/===============MAIN PROGAM=========================================== 
    System.out.println('\n'+"========================================"
                      +'\n'+"---TREE 1 : Insert/Basic Functions---"
                      +'\n'+"========================================"+'\n'
    );
    ThreadedAVLTree<Integer> A = new ThreadedAVLTree();
    System.out.println("Insertion Order:"+'\t'+"[72,4,38,10,3,15,48,1,7,12]");
    try{ A.insert(72); }catch(Exception e){System.out.println("<ERROR> insert()");}
    try{ System.out.println("Root Node: "+'\t'+'\t'+((A.getRoot()!=null && A.getRoot().data==72) ? "CORRECT" : "INCORRECT")); }catch(Exception e){System.out.println("<ERROR> getRoot()");}
    try{ System.out.println("Contains function: "+'\t'+(A.contains(72)&&!A.contains(100)?"WORKS":"DOES NOT WORK")); }catch(Exception e){System.out.println("<ERROR> contains()");}
    try{
    A.insert(4);
    A.insert(38);
    A.insert(10);
    A.insert(3);
    A.insert(15);
    A.insert(48);
    A.insert(1);
    A.insert(7);
    A.insert(12);
    }catch(Exception e){System.out.println("<ERROR> insert()");}
    try{ System.out.println("Nodes: "+'\t'+'\t'+'\t'+A.getNumberOfNodes()+" ["+(A.getNumberOfNodes()==10 ? "CORRECT]": "INCORRECT]")); }catch(Exception e){System.out.println("<ERROR> getNumberOfNodes()");}
    try{ System.out.println("Height: "+'\t'+'\t'+A.getHeight()+" ["+(A.getHeight()==4 ? "CORRECT]": "INCORRECT]")); }catch(Exception e){System.out.println("<ERROR> getHeight()");}
    try{ System.out.println("inorder: "+'\t'+'\t'+A.inorder()); }catch(Exception e){System.out.println("<ERROR> inorder()");}
    try{ System.out.println("inorderDetailed: "+'\t'+A.inorderDetailed()); }catch(Exception e){System.out.println("<ERROR> inorderDetailed()");}
    try{ System.out.println("preorder: "+'\t'+'\t'+A.preorder()); }catch(Exception e){System.out.println("<ERROR> preorder()");}
    try{ System.out.println("preorderDetailed: "+'\t'+A.preorderDetailed()); }catch(Exception e){System.out.println("<ERROR> preorderDetailed()");}
    
    System.out.println('\n'+"========================================"
                      +'\n'+"---TREE 2 : Copy Contructor---"
                      +'\n'+"========================================"+'\n'
    );
    
    System.out.println();
    ThreadedAVLTree<Integer> B = new ThreadedAVLTree(A);
    System.out.println("Insertion Order:"+'\t'+"TREE 1 + [2]");

    try{ B.insert(2); }catch(Exception e){System.out.println("<ERROR> insert()");}
    try{ System.out.println("Affects Original?: "+'\t'+(A.contains(2)?"YES":"NO")); }catch(Exception e){System.out.println("<ERROR> contains()");}
    try{ System.out.println("Nodes: "+'\t'+'\t'+'\t'+B.getNumberOfNodes()+" ["+(B.getNumberOfNodes()==11 ? "CORRECT]": "INCORRECT]")); }catch(Exception e){System.out.println("<ERROR> getNumberOfNodes()");}
    try{ System.out.println("Height: "+'\t'+'\t'+B.getHeight()+" ["+(B.getHeight()==4 ? "CORRECT]": "INCORRECT]")); }catch(Exception e){System.out.println("<ERROR> getHeight()");}
    try{ System.out.println("inorder: "+'\t'+'\t'+B.inorder()); }catch(Exception e){System.out.println("<ERROR> inorder()");}
    try{ System.out.println("inorderDetailed: "+'\t'+B.inorderDetailed()); }catch(Exception e){System.out.println("<ERROR> inorderDetailed()");}
    try{ System.out.println("preorder: "+'\t'+'\t'+B.preorder()); }catch(Exception e){System.out.println("<ERROR> preorder()");}
    try{ System.out.println("preorderDetailed: "+'\t'+B.preorderDetailed()); }catch(Exception e){System.out.println("<ERROR> preorderDetailed()");}
    
   
    System.out.println('\n'+"========================================"
                      +'\n'+"---TREE 3 : Clone---"
                      +'\n'+"========================================"+'\n'
    );
    ThreadedAVLTree<Integer> TEMP = new ThreadedAVLTree(); TEMP.insert(3); TEMP.insert(4); TEMP.insert(5);
    ThreadedAVLTree<Integer> C = TEMP.aclone();
    System.out.println("Insertion Order:"+'\t'+"[3,4,5] + [2]");
    try{ C.insert(2); }catch(Exception e){System.out.println("<ERROR> insert()");}
    try{ System.out.println("Affects Original?: "+'\t'+(TEMP.contains(2)?"YES":"NO")); }catch(Exception e){System.out.println("<ERROR> contains()");}
    try{ System.out.println("Nodes: "+'\t'+'\t'+'\t'+C.getNumberOfNodes()+" ["+(C.getNumberOfNodes()==4 ? "CORRECT]": "INCORRECT]")); }catch(Exception e){System.out.println("<ERROR> getNumberOfNodes()");}
    try{ System.out.println("Height: "+'\t'+'\t'+C.getHeight()+" ["+(C.getHeight()==3 ? "CORRECT]": "INCORRECT]")); }catch(Exception e){System.out.println("<ERROR> getHeight()");}
    try{ System.out.println("inorder: "+'\t'+'\t'+C.inorder()); }catch(Exception e){System.out.println("<ERROR> inorder()");}
    try{ System.out.println("inorderDetailed: "+'\t'+C.inorderDetailed()); }catch(Exception e){System.out.println("<ERROR> inorderDetailed()");}
    try{ System.out.println("preorder: "+'\t'+'\t'+C.preorder()); }catch(Exception e){System.out.println("<ERROR> preorder()");}
    try{ System.out.println("preorderDetailed: "+'\t'+C.preorderDetailed()); }catch(Exception e){System.out.println("<ERROR> preorderDetailed()");}
    
    System.out.println('\n'+"========================================"
                      +'\n'+"---TREE 4 : Delete function---"
                      +'\n'+"========================================"+'\n'
    );
    ThreadedAVLTree<Integer> D = new ThreadedAVLTree();
    System.out.println("Insertion Order:"+'\t'+"[72,4,38,10,3,15,48,1,7,12] - [3,72]");
    try{
    D.insert(72);
    D.insert(4);
    D.insert(38);
    D.insert(10);
    D.insert(3);
    D.insert(15);
    D.insert(48);
    D.insert(1);
    D.insert(7);
    D.insert(12);
    }catch(Exception e){System.out.println("<ERROR> insert()");}

    try{ 
    D.delete(3);
    D.delete(72);
    }catch(Exception e){System.out.println("<ERROR> delete()");}

    try{ System.out.println("Nodes: "+'\t'+'\t'+'\t'+D.getNumberOfNodes()+" ["+(D.getNumberOfNodes()==8 ? "CORRECT]": "INCORRECT]")); }catch(Exception e){System.out.println("<ERROR> getNumberOfNodes()");}
    try{ System.out.println("Height: "+'\t'+'\t'+D.getHeight()+" ["+(D.getHeight()==4 ? "CORRECT]": "INCORRECT]")); }catch(Exception e){System.out.println("<ERROR> getHeight()");}
    try{ System.out.println("inorder: "+'\t'+'\t'+D.inorder()); }catch(Exception e){System.out.println("<ERROR> inorder()");}
    try{ System.out.println("inorderDetailed: "+'\t'+D.inorderDetailed()); }catch(Exception e){System.out.println("<ERROR> inorderDetailed()");}
    try{ System.out.println("preorder: "+'\t'+'\t'+D.preorder()); }catch(Exception e){System.out.println("<ERROR> preorder()");}
    try{ System.out.println("preorderDetailed: "+'\t'+D.preorderDetailed()); }catch(Exception e){System.out.println("<ERROR> preorderDetailed()");}
    
    
    //===============MAIN PROGAM===========================================//*/ 


    
    
    System.out.println("=================END==================");

	}
}
