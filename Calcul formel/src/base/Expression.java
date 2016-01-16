package base;

import java.util.*;

public class Expression implements Operable<Expression> {
	private Expression gauche, droite;
	private Noeud valeur;
	
	public Expression (Expression g, Expression d, Noeud v){
		this.gauche = g;
		this.droite = d;

		this.valeur = v;
	}
	
	public Expression (Noeud v){
		this (null, null, v);
	}
	
	public Expression (int n){
		this (new Entier (n));
	}
	
	public boolean estFeuille (){
		return this.gauche == null && this.droite == null;
	}
	
	public Expression getGauche (){
		return this.gauche;
	}
	public Expression getDroite (){
		return this.droite;
	}
	public Noeud getValeur (){
		return this.valeur;
	}
	@Override
	public Expression oppose() {
		return new Expression (null, this.clone(), Operateur.getOperateur("-", false));
	}
	@Override
	public Expression additionner(Expression a) {
		return new Expression (this.clone(), a, Operateur.getOperateur("+", true));
	}
	
	@Override
	public Expression multiplier(Expression a) {
		return new Expression (this.clone(), a, Operateur.getOperateur("*", true));
	}
	
	public Expression clone (){
		if (this.gauche == null && this.droite == null && this.valeur instanceof Entier)
			return new Expression (((int)((Entier)this.valeur).getValeur()));
		else
			return new Expression (this.gauche.clone(), this.droite.clone(), this.valeur);
	}
	
	private String toString (int p){
		String g = "", d = "", v = this.valeur.toString(), r = "";
		int np = this.valeur instanceof Operateur ? ((Operateur)this.valeur).getPriorite() : p;

		if (this.gauche != null)
			g = this.gauche.toString(np);
		if (this.droite != null)
			d = this.droite.toString(np);
		r = g + v + d;
		if (this.valeur instanceof Operateur){
			Operateur o = (Operateur)this.valeur;
			if (p >= 0 && (np < p || !o.estAssociatif()))
				r = "(" + r + ")";
		}

		return r;
	}
	
	public String toString (){
		return this.toString(-1);
	}

	@Override
	public Expression diviser(Expression a) {
		return new Expression (this.clone(), a.clone(), Operateur.getOperateur("/", true));
	}
	
	public static Expression build (String e){
		TokenList list = new TokenList (e);
		return list.build();
	}
	
	public float evaluer (){
		if (this.estFeuille())
			return ((Constante)this.valeur).getValeur();
		else{
			float d = this.droite.evaluer();
			Operateur o = (Operateur)this.valeur;
			return o.estBinaire() ? o.evaluer(this.gauche.evaluer(), d) : o.evaluer(d);
		}
	}
	
	private static class TokenList {
		private List<String> tokens;
		public TokenList (String e){
			String s = "";
			this.tokens = new ArrayList<String> ();
			for (char c : e.toCharArray()){
				String cs = Character.toString(c);
				if (Operateur.hasOperateur(cs) || ConstanteFixe.hasConstante(cs)
						|| cs.equals("(") || cs.equals(")")){
					this.add(s);
					this.add(cs);
					s = "";
				}else
					s += cs;
			}
			this.add(s);
		}
		private void add (String t){
			if (!t.equals(""))
				this.tokens.add(t.trim());
		}
		public String toString (){
			return this.tokens.toString();
		}
		public Expression build (){
			return this.build(this.tokens);
		}
		private Expression build (List<String> list){
			int nbParentheses = 0;
			Operateur dernierOperateur = null;
			boolean continuer = true;
			int i = list.size() - 1, dernierOperateurIndice = list.size();
			while (i >= 0 && continuer){
				String t = list.get(i);
				if (t.equals(")"))
					nbParentheses++;
				else if (t.equals("("))
					nbParentheses--;
				//	Si on rencontre un opérateur hors des parenthèses
				else if (nbParentheses == 0 && Operateur.hasOperateur(t)){
					Operateur op = Operateur.getOperateur(t);
					//	Si cet opérateur est de priorité supérieure au dernier rencontré
					if (dernierOperateur != null && op.getPriorite() > dernierOperateur.getPriorite())
						continuer = false;
					else{
						dernierOperateur = op;
						dernierOperateurIndice = i;
					}
				}
				i--;
			}
			
			//	Si on n'a rencontré aucun opérateur, on suppose qu'il n'y a qu'un seul token
			if (i < 0 && dernierOperateur == null){
				String token = list.get(0);
				if (ConstanteFixe.hasConstante(token))
					return new Expression (ConstanteFixe.getConstante(token));
				else
					return new Expression (Integer.valueOf(token));
				//TODO : Traiter le cas de la fonction
			//	Si on a rencontré un opérateur (à la position i)
			}else{
				Expression gauche = this.build(this.vuePropre(list, 0, dernierOperateurIndice - 1));
				Expression droite = this.build(this.vuePropre(list, dernierOperateurIndice + 1, list.size() - 1));
				
				return new Expression (gauche, droite, dernierOperateur);
			}
		}
		private List<String> vuePropre (List<String> liste, int min, int max){
			List<String> l = liste.subList(min, max + 1);
			int n = 0;
			while (l.size() >= 2 * n && l.get(n).equals("(") && l.get(l.size() - 1 - n).equals(")"))
				n++;
			return l.subList(n, l.size () - n);
		}
	}
}
