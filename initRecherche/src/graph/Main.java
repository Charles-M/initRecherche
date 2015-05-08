package graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class Main {

	private int nb_noeud;
	private String contenu_fichier;
	private Graph graph;

	public Main() throws IOException {
		String nom = "graph1" ;
		lireFichier(nom);
		construireGraph();
		
		graph.writeDot("dot1.txt", false, null);
		Runtime.getRuntime().exec("circo -Tjpg -o "+nom+".jpg dot1.txt");
		
		ArrayList<ArrayList<Integer>> coupe = graph.minCut();
		while(!graph.coupeValide(coupe)) coupe = graph.minCut() ;
		System.out.println(coupe);
		graph.writeDot("dot2.txt", false, coupe);
		Runtime.getRuntime().exec("dot -Tjpg -o "+nom+"Cut.jpg dot2.txt");

		ArrayList<Integer> todo = new ArrayList<Integer>();
		for (int i = 1; i < nb_noeud+1; i++)
			todo.add(new Integer(i));
		
		graph.writeDot("dot3.txt", false, graph.SCC());
		Runtime.getRuntime().exec("dot -Tjpg -o "+nom+"SCC.jpg dot3.txt");

	}

	private void lireFichier(String path) throws IOException {
		BufferedReader f = new BufferedReader(new FileReader(new File(path)));
		String retour = "", line = "";
		while ((line = f.readLine()) != null) {
			nb_noeud = line.length();
			retour = retour + line + " ";
		}
		f.close();
		contenu_fichier = retour;
	}

	private void construireGraph() throws IOException {
		graph = new Graph(nb_noeud + 1);
		int ligne = 1, colonne;
		String splited[] = contenu_fichier.split(" ");
		StringReader s2;
		Sommet source;
		boolean operande1, operande2;
		for (String string : splited) {
			operande1 = (string.charAt(ligne - 1) == '1') ? true : false;
			source = new Sommet(ligne, operande1);
			s2 = new StringReader(string);
			char c;
			colonne = 1;
			while ((c = (char) s2.read()) != 65535) {
				operande2 = (splited[colonne - 1].charAt(colonne - 1) == '1') ? true : false;
				if (c != '0' && ligne != colonne)
					graph.addEdge(new Edge(source, new Sommet(colonne, operande2), c));
				colonne++;
			}
			ligne++;
			s2.close();
		}
	}

	public static void main(String[] args) {
		try {
			new Main();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
