package graph;

public class Sommet {

	public Integer num;
	// true = AND false = OR
	public boolean operateur;

	public Sommet(int num, boolean operateur) {
		super();
		this.num = num;
		this.operateur = operateur;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((num == null) ? 0 : num.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sommet other = (Sommet) obj;
		if (num == null) {
			if (other.num != null)
				return false;
		} else if (!num.equals(other.num))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String op = (operateur) ? "AND" : "OR";
		return num+"" ;
		//return "\"" + num + " (" + op + ")\"";
	}
}
