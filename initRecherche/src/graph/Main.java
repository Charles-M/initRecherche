package graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Stack;

public class Main {
	
	private int nb_noeud ;
	private String contenu_fichier;
	private Graph graph;
	
	public Main() throws IOException {
		lireFichier("graph1_8") ;
		construireGraph() ;
		/*graph.writeDot("dot.txt", true) ;
		Runtime.getRuntime().exec("circo -Tjpg -o img.jpg dot.txt") ;*/
		Stack<Integer> s = new Stack<Integer>() ;
		graph.dfs(1, s) ;
		for(Integer i : s)
			System.out.println(i);
	}
	
	private void lireFichier(String path) throws IOException {
        nb_noeud = Integer.parseInt(path.split("_")[1]);
        BufferedReader f = new BufferedReader(new FileReader(new File(path)));
        String retour = "", line = "";
        while ((line = f.readLine()) != null) {
            retour = retour + line + " ";
        }
        f.close();
        contenu_fichier = retour;
    }
	
	private void construireGraph() throws IOException {
        graph = new Graph(nb_noeud + 1);
        int ligne = 1, colonne ;
        String splited[] =contenu_fichier.split(" ") ;
        StringReader s2 ;
        Sommet source ;
        boolean operande1, operande2 ;
        for (String string : splited){
        	operande1 = (string.charAt(ligne-1) == '1' )? true : false  ;
        	source = new Sommet(ligne, operande1) ;
            s2 = new StringReader(string);
            char c;
            colonne = 1;
            while ((c = (char)s2.read()) != 65535) {
            	operande2 = (splited[colonne-1].charAt(colonne-1) == '1') ? true : false ;
            	if(c != '0')
                	graph.addEdge(new Edge(source, new Sommet(colonne, operande2), c));
                colonne++;
            }
            ligne++;
            s2.close();
        }
    }
	
	public static void main(String[] args) {
		try {
			new  Main() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
