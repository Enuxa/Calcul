package base;

import java.util.*;

public abstract class Operateur implements Noeud {
	private String symbole;
	private boolean binaire, associatif;
	private int priorite;
	private static Map<String, List<Operateur>> operateurs = new HashMap <String, List<Operateur>> ();
	public Operateur (String s, boolean b, boolean a, int p){
		this.symbole = s;
		this.binaire = b;
		this.associatif = a;
		this.priorite = p;
	}
	public int getPriorite (){
		return this.priorite;
	}
	public boolean estAssociatif (){
		return this.associatif;
	}
	public String getSymbole (){
		return this.symbole;
	}
	public boolean estBinaire (){
		return this.binaire;
	}
	public boolean estUnaire (){
		return !this.binaire;
	}
	public abstract float evaluer (float a);
	public abstract float evaluer (float a, float b);

	public Expression simplifier (Expression e){
		if (e.getValeur() != this)
			throw new RuntimeException ("L'operateur utilis� pour simplifier l'expression n'est pas la valeur du noeud " + e);
		return null;
	}
	
	public static void putOperateur (Operateur o){
		String s = o.getSymbole();
		List<Operateur> l;
		if (!operateurs.keySet().contains(s)){
			l = new ArrayList<Operateur> ();
			operateurs.put(s, l);
		}else
			l = operateurs.get(s);
		for (Operateur item : l)
			if (o.estBinaire() == item.estBinaire())
				throw new RuntimeException ("Il existe d�j� un op�rateur " + (o.estBinaire() ? "binaire" : "unaire") + " dont le symbole est " + o.getSymbole());
		l.add(o);
	}
	
	public static Operateur getOperateur (String s, boolean b){
		List<Operateur> l = operateurs.get(s);
		for (Operateur o : l)
			if (o.estBinaire() == b)
				return o;
		return null;
	}
	
	public String toString (){
		return this.symbole;
	}
}
