package graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class Main {
	
	private int nb_noeud ;
	private String contenu_fichier;
	private Graph graph;
	
	public static void main(String[] args) {
		try {
			new  Main() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Main() throws IOException {
		lireFichier("graph1_8") ;
		construireGraph() ;
		graph.writeDot("dot.txt", true) ;
		Runtime.getRuntime().exec("dot -Tjpg -o img.jpg dot.txt") ;
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
        int ligne = 1, colonne = 1;
        String splited[] =contenu_fichier.split(" ") ;
        for (String string : splited){
        	string = string.substring(0, ligne - 1);
            StringReader s2 = new StringReader(string);
            int c;
            while ((c = s2.read()) != -1) {
                if ((char)c == '+') {
                	graph.addEdge(new Edge(new Sommet(ligne, true), new Sommet(colonne, true), '+'));
                } else if((char)c == '-'){
                	graph.addEdge(new Edge(new Sommet(ligne, true), new Sommet(colonne, true), '-'));
                }
                colonne++;
            }
            colonne = 1;
            ligne++;
            s2.close();
        }
    }
}
