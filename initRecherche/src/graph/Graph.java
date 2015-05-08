package graph;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class Graph {
	private ArrayList<Edge>[] adj;
	int V;
	int E;

	public Graph(int N) {
		this.V = N;
		this.E = 0;
		adj = new ArrayList[N];
		for (int v = 0; v < N; v++)
			adj[v] = new ArrayList<Edge>();

	}

	public Graph(ArrayList<Edge>[] adj, int v, int e) {
		super();
		this.adj = new ArrayList[v];
		for (int i = 0; i < adj.length; i++) {
			this.adj[i] = new ArrayList<Edge>(adj[i]);
		}
		V = v;
		E = e;
	}

	public int vertices() {
		return V;
	}

	public void addEdge(Edge e) {
		int v = e.from.num;
		int w = e.to.num;
		adj[v].add(e);
		adj[w].add(e);
	}

	public boolean removeEdge(Edge e) {
		int v = e.from.num;
		int w = e.to.num;
		return (adj[v].remove(e)) || (adj[w].remove(e));
	}

	public ArrayList<Edge> adj(int v) {
		return adj[v];
	}

	public ArrayList<Edge> next(int v) {
		ArrayList<Edge> n = new ArrayList<Edge>();
		for (Edge e : adj(v))
			if (e.to.num != v)
				n.add(e);
		return n;
	}

	public ArrayList<Edge> prev(int v) {
		ArrayList<Edge> n = new ArrayList<Edge>();
		for (Edge e : adj(v))
			if (e.from.num != v)
				n.add(e);
		return n;
	}

	public Iterable<Edge> edges() {
		ArrayList<Edge> list = new ArrayList<Edge>();
		for (int v = 0; v < V; v++)
			for (Edge e : adj(v)) {
				if (e.to.num != v)
					list.add(e);
			}
		return list;
	}

	public void writeDot(String s, boolean horizontal, ArrayList<ArrayList<Integer>> coupe) {
		try {
			PrintWriter writer = new PrintWriter(s, "UTF-8");
			writer.println("digraph G{");
			//writer.println("splines=true;");
			//writer.println("outputorder=breadthfirst;");
			// writer.println("truecolor=true;");
			if (horizontal)
				writer.println("rankdir=\"LR\"");
			if (coupe != null) {
				for (ArrayList<Integer> L : coupe) {
					writer.print("subgraph cluster" + Math.abs(L.hashCode()) + "{");
					for (Integer i : L)
						writer.print(i + ";");
					writer.println("}");
				}
			}
			for (Edge e : edges())
				writer.println(e.from + " -> " + e.to + "[color=\"" + ((e.sign == '+') ? "darkgreen" : "red") + "\"];");
			writer.println("}");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dfs(Integer v, ArrayList<Integer> gray, ArrayList<Integer> black, ArrayList<Integer> todo) {
		if (!todo.remove(v))
			return;
		gray.add(v);
		for (Edge s : next(v))
			if (!gray.contains(s.to.num))
				dfs(s.to.num, gray, black, todo);
		black.add(v);
	}

	public ArrayList<Integer> dfs(ArrayList<Integer> todo) {
		ArrayList<Integer> gray = new ArrayList<Integer>();
		ArrayList<Integer> black = new ArrayList<Integer>();
		while (!todo.isEmpty())
			dfs(todo.get(0), gray, black, todo);
		return black;
	}

	public Graph inverserGraph() {
		Graph res = new Graph(V);
		for (Edge e : edges()) {
			res.addEdge(new Edge(e.to, e.from, e.sign));
		}
		return res;
	}

	public ArrayList<ArrayList<Integer>> SCC() {
		ArrayList<ArrayList<Integer>> listeComposantesConnexes = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> todo = new ArrayList<Integer>();
		for (int i = 1; i < V; i++)
			todo.add(new Integer(i));
		ArrayList<Integer> gray = new ArrayList<Integer>();
		ArrayList<Integer> black = new ArrayList<Integer>();
		ArrayList<Integer> s = dfs(todo);

		Graph inv = inverserGraph();
		Collections.reverse(s);

		while (!s.isEmpty()) {
			gray = new ArrayList<Integer>();
			black = new ArrayList<Integer>();
			inv.dfs(s.get(0), gray, black, s);
			listeComposantesConnexes.add(black);
		}
		return listeComposantesConnexes;
	}

	public ArrayList<ArrayList<Integer>> minCut() {
		HashMap<Integer, ArrayList<Integer>> coupe = new HashMap<Integer, ArrayList<Integer>>();
		for (int i = 1; i < V; i++) {
			ArrayList<Integer> l = new ArrayList<Integer>();
			l.add(i);
			coupe.put(i, l);
		}

		Graph copy = new Graph(adj, V, E);

		int cnt = copy.V - 1;
		while (cnt > 2) {
			cnt--;
			ArrayList<Edge> arete = (ArrayList<Edge>) copy.edges();
			int rand = (int) (Math.random() * arete.size());
			Edge tmp = arete.get(rand);
			// System.out.println("tmp = " + tmp);
			copy.contraction(tmp, coupe);
		}
		return new ArrayList<ArrayList<Integer>>(coupe.values());
	}

	// Algo de Karger
	public void contraction(Edge edge, HashMap<Integer, ArrayList<Integer>> coupe) {

		for (Integer i : coupe.get(edge.to.num)) {
			coupe.get(edge.from.num).add(i);
		}
		coupe.remove(edge.to.num);
		Integer suppr = edge.to.num;
		ArrayList<Edge> ad = new ArrayList<Edge>(adj(suppr));
		for (Edge e : ad) {
			// if (e.to.num != edge.to.num || e.from.num != edge.from.num) {
			if (!e.equals(edge)) {
				if (e.to.num == suppr) {
					if (e.from.num != edge.from.num) {
						addEdge(new Edge(e.from, edge.from, edge.sign));
					}
				} else {
					if (edge.from.num != e.to.num) {
						addEdge(new Edge(edge.from, e.to, edge.sign));
					}
				}
				while (removeEdge(e))
					;
			}
		}
		while (removeEdge(edge))
			;
	}

	public boolean coupeValide(ArrayList<ArrayList<Integer>> coupe) {
		ArrayList<Integer> liste1 = coupe.get(0);
		ArrayList<Integer> liste2 = coupe.get(1);

		int somme = liste1.size() + liste2.size();

		if (liste1.size() < somme / 3 || liste2.size() < somme / 3)
			return false;
		boolean cpt[] = new boolean[somme];
		for (Integer i : liste1) {
			for (Edge e : next(i)) {
				if (liste2.contains(e.to.num)) {
					cpt[e.from.num - 1] = true;
				}
			}

		}
		int cpt1 = 0;
		for (int i = 0; i < cpt.length; i++) {
			if (cpt[i])
				cpt1++;
		}
		if (cpt1 != 1)
			return false;

		cpt = new boolean[somme];
		for (Integer i : liste2) {
			for (Edge e : next(i)) {
				if (liste1.contains(e.to.num)) {
					cpt[e.from.num - 1] = true;
				}
			}

		}
		cpt1 = 0;
		for (int i = 0; i < cpt.length; i++) {
			if (cpt[i])
				cpt1++;
		}
		if (cpt1 != 1)
			return false;

		return true;
	}

	public Graph transition() {
		int nb_sommets = (int) Math.pow(2, V);
		Graph g = new Graph(nb_sommets);
		for (int i = 0; i < nb_sommets; i++) {
			//System.out.println("i = "+i);
			boolean[] bits = new boolean[V];
			for (int h =0; h < V; h++) 
				bits[h] = (i & (1 << h)) != 0;
			for (int j = 0; j < V; j++) {
				//System.out.println("j = "+j);
				boolean op = next(j).get(0).from.operateur;
				boolean resultat;
				resultat = op;
				//String symb = (op)? "/\\" : "\\/" ;
				for (Edge e : prev(j)) {
					boolean p = (e.sign == '+')?bits[e.from.num]:!bits[e.from.num] ;
					resultat = (op) ? resultat && p : resultat || p;

					//System.out.print("   "+p+" "+symb+" ");
				}
				//System.out.println("");
				if (resultat ^ bits[j]) {
					//System.out.println("resultat = "+resultat);
					int to = (int) ((resultat) ? i + Math.pow(2, j) : i - Math.pow(2, j));
					//System.out.println("to ="+to);
					g.addEdge(new Edge(new Sommet(i, true), new Sommet(to, true), '*'));
				}
			}
		}

		return g;
	}
}
