package base;

import java.util.*;

public class Expression {
	private Expression gauche, droite;
	private Noeud valeur;
	private Contexte contexte;
	
	/**
	 * @param g L'expression de gauche.
	 * @param d L'expression de droite.
	 * @param v La valeur de ce noeud
	 * @param contexte Le contexte dans lequel existe cette expression.
	 */
	public Expression (Expression g, Expression d, Noeud v, Contexte contexte){
		this.gauche = g;
		this.droite = d;
		this.contexte = contexte;
		
		//	Si l'entier est négatif
		if (v instanceof Entier && ((Entier)v).compareTo(new Entier (0)) < 0){
			this.valeur = Operateur.getOperateur("-", false);
			this.droite = new Expression (-(int)((Entier)v).getValeur());
		}
		this.valeur = v;
	}
	
	/**
	 * @param v La valeur de noeud.
	 */
	public Expression (Noeud v){
		this (null, null, v, null);
	}
	
	/**
	 * La valeur de l'entier representé par ce noeud.
	 * @param n
	 */
	public Expression (int n){
		this (new Entier (n));
	}
	
	/**
	 * @param f La fonction associée à ce noeud.
	 * @param args Les arguments de ce noeud.
	 */
	public Expression (Fonction f, Expression[] args){
		this (new FonctionOccurrence (f, args));
	}
	
	/**
	 * Indique si cette expression est une feuille.
	 * @return <code>true</code> si c'est une feuille.
	 */
	public boolean estFeuille (){
		return this.gauche == null && this.droite == null;
	}
	
	/**
	 * Récupère l'expression de droite.
	 * @return L'expression de droite.
	 */
	public Expression getGauche (){
		return this.gauche;
	}
	/**
	 * Récupère l'expression de gauche.
	 * @return L'expression de gauche.
	 */
	public Expression getDroite (){
		return this.droite;
	}
	/**
	 * Récupère l'objet stocké par ce noeud.
	 * @return Lavlauer du noeud
	 */
	public Noeud getValeur (){
		return this.valeur;
	}
	
	/**
	 * Récupère le contexte dans lequel existe cette expression.
	 * @return
	 */
	public Contexte getContexte (){
		return this.contexte;
	}
	
	/**
	 * Clone l'epression et son contexte.
	 * @return La copie de cette expression.
	 */
	private Expression clone_rec (){
		if (this.gauche == null && this.droite == null && this.valeur instanceof Entier)
			return new Expression (((int)((Entier)this.valeur).getValeur()));
		else
			return new Expression (this.gauche.clone_rec(), this.droite.clone_rec(), this.valeur, new Contexte (this.contexte));
	}
	/**
	 * Clone l'expression
	 * @return La copie de cette expression
	 */
	public Expression clone (){
		return this.clone_rec();
	}
	/**
	 * Traduit cette expression en texte.
	 * @param op0 L'opérateur porté par le noeud parent.
	 * @param membre <code>-1</code> si cette expression est le membre de gauche, <code>1</code> si elle est celui de droite.
	 * @return L'équivalent textuelle de cette expression.
	 */
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

	/**
	 * Contruit une expression à partir d'ue chaîne et d'un contexte.
	 * @param e La chaîne à traduire en expression.
	 * @param contexte Le contexte dans lequel existe l'expression.
	 * @return L'expression
	 */
	public static Expression build (String e, Contexte contexte){
		TokenList list = new TokenList (e, contexte);
		return list.build();
	}
	
	/**
	 * Evalue la valeur en nombre à virgule flottante de cette expression.
	 * @return La valeur de cette expression
	 */
	public float evaluer (){
		if (this.estFeuille()){
			if (this.valeur instanceof Constante)
				return ((Constante)this.valeur).getValeur();
			else if (this.valeur instanceof Variable)
				return ((Variable)this.valeur).getValeur().getValeur();
			else
				return ((FonctionOccurrence)this.valeur).evaluer();
		}else{
			float d = this.droite.evaluer();
			Operateur o = (Operateur)this.valeur;
			return o.estBinaire() ? o.evaluer(this.gauche.evaluer(), d) : o.evaluer(d);
		}
	}
	
	/**
	 * Remplace dans une expression une certaine variable.
	 * @param variable La variable à remplacer.
	 * @param expression L'expression par laquelle remplacer la variable.
	 * @return La nouvelle expression.
	 */
	public Expression remplacer (Variable variable, Expression expression){
		Contexte c = new Contexte (this.contexte);
		c.retirer(variable);
		return this.remplacer_rec(variable, expression, c);
	}
	
	/**
	 * Remplace récursivement une variable par une expression.
	 * @param variable La variable à remplacer.
	 * @param expression L'expression par laquelle remplacer la variable.
	 * @param contexte Le contexte actuel.
	 * @return La nouevelle expression.
	 */
	private Expression remplacer_rec (Variable variable, Expression expression, Contexte contexte){
		Expression g = null, d = null;
		if (this.gauche != null)
			g = this.gauche.remplacer_rec(variable, expression, contexte);
		if (this.droite != null)
			g = this.droite.remplacer_rec(variable, expression, contexte);
		if (this.estFeuille()){
			if (this.valeur == variable)
				return expression.clone();
			else
				return this.clone();
		}else{
			Noeud n = this.valeur;
			if (this.valeur instanceof Entier)
				n = new Entier ((int)((Entier)this.valeur).getValeur());
			return new Expression (g, d, n, contexte);
		}
	}
	
	/**
	 * Indique si cette expression contient une certaine constante ou variable.
	 * @param x La constante ou variable considérée.
	 * @return <code>true</code> si cette expression contient ce nombre.
	 */
	public boolean contient (Nombre x){
		boolean g = false, d = false;
		if (x instanceof ConstanteFixe || x instanceof Variable){
			if (this.gauche != null)
				g = this.gauche.contient(x);
			if (this.droite != null)
				d = this.droite.contient(x);
			if (this.estFeuille())
				return this.valeur == x;
		}
		return g || d;
	}
	
	/**
	 * Classe correspondant à la décomposition en tokens d'une chaîne de caractères représentant une expression.
	 * @author Pierre
	 */
	private static class TokenList {
		private List<String> tokens;
		private Contexte contexte;
		/**
		 * @param e L'expression à construire.
		 * @param contexte Le contexte dans laquelle l'expresion existe.
		 */
		public TokenList (String e, Contexte contexte){
			this.contexte = contexte;
			String s = "";
			this.tokens = new ArrayList<String> ();
			for (char c : e.toCharArray()){
				String cs = Character.toString(c);
				if (Operateur.hasOperateur(cs) || ConstanteFixe.hasConstante(cs)
						|| cs.equals("(") || cs.equals(")") || this.contexte.has(cs) || cs.equals(",")){
					this.add(s);
					this.add(cs);
					s = "";
				}else
					s += cs;
			}
			this.add(s);
		}
		/**
		 * Ajoute un token à la fin de la liste.
		 * @param t Le token à ajouter
		 */
		private void add (String t){
			if (!t.equals(""))
				this.tokens.add(t.trim());
		}
		public String toString (){
			return this.tokens.toString();
		}
		/**
		 * Construit l'expression associée à cette liste de tokens.
		 * @return L'expression.
		 */
		public Expression build (){
			return this.build(vuePropre (this.tokens, 0, this.tokens.size() - 1));
		}
		/**
		 * Construit l'expression associée à cette liste de tokens.
		 * @param list La liste de tokens.
		 * @return L'expression.
		 */
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
				//	Variable
				if (this.contexte.has(token))
					return new Expression (this.contexte.get(token));
				//	Fonction
				else if (Fonction.hasFonction(token)){
					List<List<String>> args = separerArguments (list.subList(2, list.size() - 1));
					Expression[] argsex = new Expression [args.size()];
					for (int j = 0; j < args.size(); j++)
						argsex[j] = build (args.get(j));
					return new Expression (Fonction.getFonction(token, argsex.length), argsex);
				}else
					return new Expression (Integer.valueOf(token));
			//	Si on a rencontré un opérateur
			}else{
				Expression gauche = null;
				if (dernierOperateurIndice != 0)
					gauche = this.build(vuePropre(list, 0, dernierOperateurIndice - 1));
				Expression droite = this.build(vuePropre(list, dernierOperateurIndice + 1, list.size() - 1));
				
				Operateur operateur = Operateur.getOperateur(dernierOperateur.getSymbole(), gauche != null);
				
				return new Expression (gauche, droite, operateur, this.contexte);
			}
		}
		/**
		 * "Nettoie" la liste de tokens en supprimant les parenthèses exterieures inutiles
		 * @param liste La liste de tokens.
		 * @param min L'indice du premier token à considérer.
		 * @param max L'indice du dernier token  considérer.
		 * @return Une vue propre de cette liste.
		 */
		private static List<String> vuePropre (List<String> liste, int min, int max){
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
		/**
		 * Sépare une liste de tokens en fonction des virgules, nécessaire pour la séparation d'arguments.
		 * @param liste La liste des tokens.
		 * @return Une liste des listes de tokens.
		 */
		public static List<List<String>> separerArguments (List<String> liste){
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
