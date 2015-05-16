package graph;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GrapheOuvert extends Graph {

	private ArrayList<Edge> frontiere;
	private int emetteur = -1;
	private int NEffectif;

	public GrapheOuvert(int N, int NEffectif) {
		super(N);
		frontiere = new ArrayList<Edge>();
		this.NEffectif = NEffectif;
	}

	public void addSommetFrontiere(Edge e) {
		frontiere.add(e);
	}

	public ArrayList<Edge> getFrontiere() {
		return frontiere;
	}

	public int getEmetteur() {
		return emetteur;
	}

	public void setEmetteur(int emetteur) {
		this.emetteur = emetteur;
	}

	public GraphTransOuvert transitionOuvert() {
		System.out.println("v = " + V);
		int nb_sommets = (int) Math.pow(2, NEffectif);
		GraphTransOuvert g = new GraphTransOuvert(nb_sommets, emetteur);
		for (int i = 0; i < nb_sommets; i++) {
			int bitemetteur = 0;
			boolean[] bits = null;
			for (int parcours = 0; parcours <= 1; parcours++) {
				System.out.println("i = " + i);
				bits = new boolean[V];
				for (int h = 0; h < V; h++)
					bits[h] = (i & (1 << h)) != 0;
				int cnt = 0;
				for (int j = 0; j < V; j++) {
					if (j == emetteur)
						bitemetteur = cnt;
					boolean onestpasseparla = false;
					// if (!adj(j).isEmpty() || NEffectif == 1) {
					System.out.println("j = " + j);
					boolean op = false;
					op = Main.contenu_fichier.charAt(j + j * (V + 1)) != '0';
					System.out.println("char = " + Main.contenu_fichier.charAt(j + j * (V + 1)));
					System.out.println(j + " = " + op);
					boolean resultat;
					resultat = op;
					// String symb = (op)? "/\\" : "\\/" ;
					for (Edge e : prev(j)) {
						boolean p = (e.sign == '+') ? bits[e.from.num] : !bits[e.from.num];
						resultat = (op) ? resultat && p : resultat || p;
					}
					for (Edge e : frontiere) {
						if (e.to.num == j) {
							onestpasseparla = true;
							System.out.println("aaa");
							boolean p = (e.sign == '+') ? parcours == 1 : parcours == 0;
							resultat = (op) ? resultat && p : resultat || p;
						}
					}
					if (!adj(j).isEmpty() || onestpasseparla) {
						if (resultat ^ bits[cnt]) {
							System.out.println("cnt = " + cnt);
							System.out.println("resultat = " + resultat);
							int to = (int) ((resultat) ? i + Math.pow(2, cnt) : i - Math.pow(2, cnt));
							System.out.println("to =" + to);
							Edge e = new Edge(new Sommet(i, true), new Sommet(to, true), '-');
							e.setLabel(parcours + "");
							g.addEdge(e);
						}
						cnt++;
					}
					// }
					System.out.println("");
				}
			}
			if (!g.adj(i).isEmpty()) {
				Edge e = new Edge(new Sommet(i, true), new Sommet(i, true), '+');
				e.setLabel(((bits[bitemetteur]) ? 1 : 0) + "");
				g.addEdge(e);
			}
		}
		return g;
	}
	
	public void writeDot(String s, boolean horizontal) {
		try {
			PrintWriter writer = new PrintWriter(s, "UTF-8");
			writer.println("digraph G{");
			if (horizontal)
				writer.println("rankdir=\"LR\"");
			writer.println(emetteur + "[color=cyan]");
			for (Edge integ : frontiere) {
				writer.println(integ.to.num + "[style=filled, fillcolor=pink]");
			}
			for (Edge e : edges()) {
				writer.println(e.from + " -> " + e.to + "[color=\"" + ((e.sign == '+') ? "darkgreen" : "red") + "\"];");
			}
			writer.println("}");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
