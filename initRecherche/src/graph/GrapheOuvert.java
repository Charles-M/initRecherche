package graph;

import java.util.ArrayList;


public class GrapheOuvert extends Graph{
	
	private ArrayList<Integer> frontiere ;
	private int emetteur ;
	
	public GrapheOuvert(int N) {
		super(N);
		frontiere = new ArrayList<Integer>() ;
	}

	public void addSommetFrontiere(int i){
		frontiere.add(i) ;
	}
	
	public void setEmetteur(int emetteur) {
		this.emetteur = emetteur;
	}
	
	/**     A REMPLIR SUR LE MODELE DE TRANSITION  !! **/
	public GraphTransOuvert transitionOuverte() {
		try {
			throw new Exception("mange ma") ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}

	public Graph papillon(GrapheOuvert ouvert2) {
		// TODO Auto-generated method stub
		return null;
	}
}
