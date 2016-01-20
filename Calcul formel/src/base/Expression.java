package base;

import java.util.*;

public class Expression {
	private Expression gauche, droite;
	private Noeud valeur;
	
	public Expression (Expression g, Expression d, Noeud v){
		this.gauche = g;
		this.droite = d;
		
		//	Si l'entier est négatif
		if (v instanceof Entier && ((Entier)v).compareTo(new Entier (0)) < 0){
			this.valeur = Operateur.getOperateur("-", false);
			this.droite = new Expression (-(int)((Entier)v).getValeur());
		}
		this.valeur = v;
	}
	
	public Expression (Noeud v){
		this (null, null, v);
	}
	
	public Expression (int n){
		this (new Entier (n));
	}
	
	public Expression (Fonction f, Expression[] args){
		this (new FonctionOccurrence (f, args));
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
	
	public Expression clone (){
		if (this.gauche == null && this.droite == null && this.valeur instanceof Entier)
			return new Expression (((int)((Entier)this.valeur).getValeur()));
		else
			return new Expression (this.gauche.clone(), this.droite.clone(), this.valeur);
	}
	
	private String toString (Operateur op0, int membre){
		String g = "", d = "", v = this.valeur.toString(), r = "";
		Operateur op1 = this.valeur instanceof Operateur ? (Operateur)this.valeur : op0;

		if (this.gauche != null)
			g = this.gauche.toString(op1, -1);
		if (this.droite != null)
			d = this.droite.toString(op1, 1);
		r = g + v + d;
		if (this.valeur instanceof Operateur){
			if (op0 != null && (op1.getPriorite() < op0.getPriorite() || (!op0.estAssociatif() && membre == 1)))
				r = "(" + r + ")";
		}

		return r;
	}
	
	public String toString (){
		return this.toString(null, 0);
	}

	public static Expression build (String e, Map<String, Variable> variablesLiees){
		TokenList list = new TokenList (e, variablesLiees.keySet());
		return list.build(variablesLiees);
	}
	
	public float evaluer (){
		return this.evaluer(null);
	}
	
	public float evaluer (Map<Variable, Float> valeurs){
		if (this.estFeuille()){
			if (this.valeur instanceof Constante)
				return ((Constante)this.valeur).getValeur();
			else if (this.valeur instanceof Variable)
				return valeurs.get(this.valeur);
			else
				return ((FonctionOccurrence)this.valeur).evaluer();
		}else{
			float d = this.droite.evaluer();
			Operateur o = (Operateur)this.valeur;
			return o.estBinaire() ? o.evaluer(this.gauche.evaluer(), d) : o.evaluer(d);
		}
	}
	
	private static class TokenList {
		private List<String> tokens;
		public TokenList (String e, Collection<String> variablesLiees){
			String s = "";
			this.tokens = new ArrayList<String> ();
			for (char c : e.toCharArray()){
				String cs = Character.toString(c);
				if (Operateur.hasOperateur(cs) || ConstanteFixe.hasConstante(cs)
						|| cs.equals("(") || cs.equals(")") || variablesLiees.contains(cs)){
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
			return this.build(null);
		}
		public Expression build (Map<String, Variable> variablesLiees){
			return this.build(this.vuePropre (this.tokens, 0, this.tokens.size() - 1), variablesLiees);
		}
		private Expression build (List<String> list, Map<String, Variable> variablesLiees){
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
					//	Si cet opérateur est de priorité supérieure ou égale au dernier rencontré
					if (dernierOperateur != null && op.getPriorite() >= dernierOperateur.getPriorite())
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
				//	Constante
				if (ConstanteFixe.hasConstante(token))
					return new Expression (ConstanteFixe.getConstante(token));
				//	Variable liée
				if (variablesLiees.containsKey(token))
					return new Expression (variablesLiees.get(token));
				//	Fonction
				else if (Fonction.hasFonction(token)){
					List<List<String>> args = separerArguments (list.subList(2, list.size() - 1));
					Expression[] argsex = new Expression [args.size()];
					for (int j = 0; j < args.size(); j++)
						argsex[j] = build (args.get(j), variablesLiees);
					return new Expression (Fonction.getFonction(token, argsex.length), argsex);
				}else
					return new Expression (Integer.valueOf(token));
			//	Si on a rencontré un opérateur
			}else{
				Expression gauche = null;
				if (dernierOperateurIndice != 0)
					gauche = this.build(this.vuePropre(list, 0, dernierOperateurIndice - 1), variablesLiees);
				Expression droite = this.build(this.vuePropre(list, dernierOperateurIndice + 1, list.size() - 1), variablesLiees);
				
				Operateur operateur = Operateur.getOperateur(dernierOperateur.getSymbole(), gauche != null);
				
				return new Expression (gauche, droite, operateur);
			}
		}
		private List<String> vuePropre (List<String> liste, int min, int max){
			liste = liste.subList(min, max + 1);
			boolean continuer = true;
			while (continuer && liste.get(0).equals("(") && liste.get(liste.size() - 1).equals(")")){
				int n = 0;
				for (int i = 1; i < liste.size() - 1; i++){
					String t = liste.get(i);
					if (t.equals("("))
						n++;
					else if (t.equals(")"))
						n--;
					//	Si le parenthésage est incorrect en ne considérant pas la première parenthèse ouvrante
					if (n < 0)
						continuer = false;
				}
				if (n == 0 && continuer)
					liste = liste.subList(1, liste.size() - 1);
				else
					continuer = false;
			}
			return liste;
		}
		private List<List<String>> separerArguments (List<String> liste){
			List<List<String>> args = new ArrayList<List<String>>();
			int n = 0, debut = 0;
			
			for (int i = 0; i < liste.size(); i++){
				String token = liste.get(i);
				if (token.equals("("))
					n++;
				else if (token.equals(")"))
					n--;
				else if (token.equals(",") && n == 0){
					args.add(liste.subList(debut, i));
					debut = i + 1;
				}
			}
			
			if (debut < liste.size())
				args.add(liste.subList(debut, liste.size()));
			
			return args;
		}
	}
}
