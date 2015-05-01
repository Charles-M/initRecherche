package graph;

import java.util.ArrayList;
import java.io.*;

public class Graph {
	private ArrayList<Edge>[] adj;
	private final int V;
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

	public Iterable<Edge> adj(int v) {
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

}
