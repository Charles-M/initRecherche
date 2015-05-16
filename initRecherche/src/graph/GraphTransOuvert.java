package graph;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GraphTransOuvert extends Graph {

	private int emetteur = -1;

	public int getEmetteur() {
		return emetteur;
	}

	public void setEmetteur(int emetteur) {
		this.emetteur = emetteur;
	}

	public GraphTransOuvert(int N, int emetteur) {
		super(N);
		this.emetteur = emetteur;
	}

	public void simplification() {
		ArrayList<ArrayList<Integer>> scc = SCC();
		System.out.println("SCC = " + scc);
		for (int i = 0; i < V; i++) {
			int[] val = new int[V];
			for (Edge e : next(i)) {
				val[e.to.num]++;
			}
			boolean suppr = true;
			for (int j = 0; j < val.length; j++) {
				if (val[j] == 1)
					suppr = false;
			}
			/*
			 * for(int t = 0; t<val.length; t++)
			 * System.out.print(" t = "+val[t]); System.out.println("");
			 */
			if (suppr) {
				// System.out.println("aaa");
				for (ArrayList<Integer> l : scc)
					if (l.size() == 1 && l.contains(i))
						removeSommet(i);
			}
		}
	}

	public void writeDot(String s, boolean horizontal) {
		try {
			PrintWriter writer = new PrintWriter(s, "UTF-8");
			writer.println("digraph G{");
			if (horizontal)
				writer.println("rankdir=\"LR\"");
			for (int i = 0; i < V; i++) {
				for (Edge e : next(i)) {
					writer.println(e.from + " -> " + e.to + "[label=" + e.label + " color=\"" + ((e.sign == '+') ? "darkgreen" : "red") + "\"];");
				}
				Edge e = boucle(i);
				if (e != null)
					writer.println(e.from + " -> " + e.to + "[label=" + e.label + " color=\"" + ((e.sign == '+') ? "darkgreen" : "red") + "\"];");
			}
			writer.println("}");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Edge boucle(int v) {
		for (Edge e : adj(v))
			if (e.to.num == e.from.num)
				return e;
		return null;
	}

	public Graph papillon(GraphTransOuvert transAtlantique) {
		Graph g = new Graph(V * transAtlantique.V);
		for (int i = 0; i < V; i++) {
			for (Edge e : next(i)) {
				for (int j = 0; j < transAtlantique.V; j++) {
					Edge e2 = transAtlantique.boucle(j);
					if (e.getLabel().equals(e2.getLabel())) {
						g.addEdge(new Edge(new Sommet(i + V * j, true), new Sommet(e.to.num + V * e2.to.num, true), '*'));
					}
				}
			}
			Edge e = boucle(i);
			for (int j = 0; j < transAtlantique.V; j++) {
				for (Edge e2 : transAtlantique.next(j)) {
					if (e.getLabel().equals(e2.getLabel())) {
						g.addEdge(new Edge(new Sommet(i + V * j, true), new Sommet(e.to.num + V * e2.to.num, true), '*'));
					}
				}
			}
		}
		return g;
	}

}
