package graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	private int nb_noeud;
	private String contenu_fichier;
	private Graph graph;

	public Main() throws IOException {
		
		lireFichier("graph1");
		construireGraph();
		graph.writeDot("dot.txt", true);
		Runtime.getRuntime().exec("circo -Tjpg -o img.jpg dot.txt");

		/*HashMap<Integer, ArrayList<Integer>> coupe = new HashMap<Integer, ArrayList<Integer>>();
		for (int i = 1; i <= nb_noeud; i++) {
			ArrayList<Integer> l = new ArrayList<Integer>();
			l.add(i);
			coupe.put(i, l);
		}
		
		graph.contraction(new Edge(new Sommet(10, false), new Sommet(8, false), '+'), coupe) ;
		
		graph.writeDot("dot2.txt", true);
		Runtime.getRuntime().exec("circo -Tjpg -o img1.jpg dot2.txt");
		
		graph.contraction(new Edge(new Sommet(10, false), new Sommet(9, false), '+'), coupe) ;
		
		graph.writeDot("dot3.txt", true);
		Runtime.getRuntime().exec("circo -Tjpg -o img2.jpg dot3.txt");
		
		graph.contraction(new Edge(new Sommet(6, false), new Sommet(10, false), '+'), coupe) ;
		
		graph.writeDot("dot4.txt", true);
		Runtime.getRuntime().exec("circo -Tjpg -o img3.jpg dot4.txt");
		
		graph.contraction(new Edge(new Sommet(10, false), new Sommet(6, false), '+'), coupe) ;
		
		graph.writeDot("dot5.txt", true);
		Runtime.getRuntime().exec("circo -Tjpg -o img4.jpg dot5.txt");
		
		graph.contraction(new Edge(new Sommet(9, false), new Sommet(10, false), '+'), coupe) ;
		
		graph.writeDot("dot6.txt", true);
		Runtime.getRuntime().exec("circo -Tjpg -o img5.jpg dot6.txt");
		
		graph.contraction(new Edge(new Sommet(9, false), new Sommet(10, false), '+'), coupe) ;
		
		graph.writeDot("dot7.txt", true);
		Runtime.getRuntime().exec("circo -Tjpg -o img6.jpg dot7.txt");*/
		
		try {
			int i = 0;
			while(i<100){
				construireGraph();
				System.out.println(""+(i++)+" "+graph.minCut());
			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void lireFichier(String path) throws IOException {
		BufferedReader f = new BufferedReader(new FileReader(new File(path)));
		String retour = "", line = "";
		while ((line = f.readLine()) != null) {
			nb_noeud = line.length() ;
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
				if (c != '0')
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
