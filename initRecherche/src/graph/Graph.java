package graph;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Graph implements Cloneable {
	private ArrayList<Edge>[] adj;
	int V;
	int E;

	@SuppressWarnings("unchecked")
	public Graph(int N) {
		this.V = N;
		this.E = 0;
		adj = new ArrayList[N];
		for (int v = 0; v < N; v++)
			adj[v] = new ArrayList<Edge>();

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

	public void removeEdge(Edge e) {
		int v = e.from.num;
		int w = e.to.num;
		while (adj[v].remove(e)) {
		}
		while (adj[w].remove(e)) {
		}
	}

	public ArrayList<Edge> adj(int v) {
		return adj[v];
	}

	public Iterable<Edge> next(int v) {
		ArrayList<Edge> n = new ArrayList<Edge>();
		for (Edge e : adj(v))
			if (e.to.num != v)
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

	public void writeDot(String s, boolean horizontal) {
		try {
			PrintWriter writer = new PrintWriter(s, "UTF-8");
			writer.println("digraph G{");
			if (horizontal)
				writer.println("rankdir=\"LR\"");
			for (Edge e : edges())
				writer.println(e.from + " -> " + e.to + "[label=\"" + e.sign + "\"];");
			writer.println("}");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dfs(Integer v, ArrayList<Integer> mark, ArrayList<Integer> todo) {
		if (!todo.remove(v))
			return;
		mark.add(v);
		for (Edge s : next(v))
			if (!mark.contains(s.to.num))
				dfs(s.to.num, mark, todo);
	}

	public ArrayList<Integer> dfs(ArrayList<Integer> todo) {
		ArrayList<Integer> mark = new ArrayList<Integer>();
		while (!todo.isEmpty())
			dfs(todo.get(0), mark, todo);
		return mark;
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
		ArrayList<Integer> mark = new ArrayList<Integer>();
		for (int i = 1; i < V; i++)
			todo.add(new Integer(i));
		ArrayList<Integer> s = dfs(todo);

		Graph inv = inverserGraph();
		// Collections.reverse(s);

		while (!s.isEmpty()) {
			mark = new ArrayList<Integer>();
			inv.dfs(s.get(0), mark, s);
			listeComposantesConnexes.add(mark);
		}
		return listeComposantesConnexes;
	}

	public HashMap<Integer, ArrayList<Integer>> minCut() throws CloneNotSupportedException {
		HashMap<Integer, ArrayList<Integer>> coupe = new HashMap<Integer, ArrayList<Integer>>();
		for (int i = 1; i < V; i++) {
			ArrayList<Integer> l = new ArrayList<Integer>();
			l.add(i);
			coupe.put(i, l);
		}

		Graph copy = (Graph) clone();

		int cnt = copy.V - 1;
		while (cnt > 2) {
			cnt--;
			ArrayList<Edge> arete = (ArrayList<Edge>) copy.edges();
			int rand = (int) (Math.random() * arete.size());
			Edge tmp = arete.get(rand);
			System.out.println("tmp = " + tmp);
			copy.contraction(tmp, coupe);
		}
		return coupe;
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
			if (e.to.num != edge.to.num || e.from.num != edge.from.num) {
				if (e.to.num == suppr) {
					if (e.from.num != edge.from.num){
						addEdge(new Edge(e.from, edge.from, edge.sign));
					}
				} else {
					if (edge.from.num != e.to.num){
						addEdge(new Edge(edge.from, e.to, edge.sign));
					}
				}
				removeEdge(e);
			}
		}
		removeEdge(edge);
	}
}
