package graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class Main {

	private int nb_noeud;
	public static String contenu_fichier;
	private Graph graph;

	public Main() throws IOException {
		String nom = "papillon";
		lireFichier(nom);
		construireGraph();
		graph.writeDot("dot1.txt", false, null);
		Runtime.getRuntime().exec("circo -Tjpg -o images/" + nom + ".jpg dot1.txt");

		/********** TRANSITION **********/
		/*
		 * Graph trans = graph.transition(); trans.writeDot("dotTrans.txt",
		 * false, null); Runtime.getRuntime().exec("dot -Tjpg -o images/"+nom+
		 * "Transition.jpg dotTrans.txt");
		 */

		/******************* COUPE ******************/

		GrapheOuvert[] go = graph.getGrapheOuvert();
		go[0].writeDot("dotOuv1.txt", false);
		Runtime.getRuntime().exec("dot -Tjpg -o images/" + nom + "Ouvert1.jpg dotOuv1.txt");
		go[1].writeDot("dotOuv2.txt", false);
		Runtime.getRuntime().exec("dot -Tjpg -o images/" + nom + "Ouvert2.jpg dotOuv2.txt");

		GraphTransOuvert transAlpes = go[0].transitionOuvert();
		GraphTransOuvert transAtlantique = go[1].transitionOuvert();

		transAlpes.writeDot("dotTransOuv1.txt", false);
		Runtime.getRuntime().exec("dot -Tjpg -o images/" + nom + "TransOuvert1.jpg dotTransOuv1.txt");
		transAtlantique.writeDot("dotTransOuv2.txt", false);
		Runtime.getRuntime().exec("dot -Tjpg -o images/" + nom + "TransOuvert2.jpg dotTransOuv2.txt");

		transAlpes.simplification();
		transAtlantique.simplification();

		transAlpes.writeDot("dotTransOuv1Simpl.txt", false);
		Runtime.getRuntime().exec("dot -Tjpg -o images/" + nom + "TransOuvert1Simpl.jpg dotTransOuv1Simpl.txt");

		transAtlantique.writeDot("dotTransOuv2Simpl.txt", false);
		Runtime.getRuntime().exec("dot -Tjpg -o images/" + nom + "TransOuvert2Simpl.jpg dotTransOuv2Simpl.txt");

		Graph RESULTAT_FINAL = transAlpes.papillon(transAtlantique);

		/**************** SCC ***************/

		/*ArrayList<Integer> todo = new ArrayList<Integer>();
		for (int i = 1; i < nb_noeud + 1; i++)
			todo.add(new Integer(i));
		graph.writeDot("dot3.txt", false, graph.SCC());
		Runtime.getRuntime().exec("dot -Tjpg -o images/" + nom + "SCC.jpg dot3.txt");*/

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
		graph = new Graph(nb_noeud);
		int ligne = 0, colonne;
		String splited[] = contenu_fichier.split(" ");
		StringReader s2;
		Sommet source;
		boolean operande1, operande2;
		for (String string : splited) {
			operande1 = (string.charAt(ligne) == '1') ? true : false;
			source = new Sommet(ligne, operande1);
			s2 = new StringReader(string);
			char c;
			colonne = 0;
			while ((c = (char) s2.read()) != 65535) {
				operande2 = (splited[colonne].charAt(colonne) == '1') ? true : false;
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
