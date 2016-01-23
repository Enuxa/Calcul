package base;

import java.util.*;

public class Fonction {
	private static Map<String, List<Fonction>> fonctions = new HashMap<String, List<Fonction>> ();
	private String symbole;
	private int argumentNb;
	private Expression expression;
	private List<Variable> arguments;
	
	public String getSymbole (){
		return this.symbole;
	}
	
	public int getArgumentNb (){
		return this.argumentNb;
	}
	
	public Fonction (String symbole, int argumentNb){
		this.symbole = symbole;
		this.argumentNb = argumentNb;
	}
	
	public Fonction (String symbole, Expression expression, List<Variable> arguments){
		this.symbole = symbole;
		this.expression = expression;
		this.arguments = arguments;
		if (arguments != null)
			this.argumentNb = arguments.size ();
		else
			this.argumentNb = 0;
	}
	
	public float evaluer (float[] args){
		if (args == null || args.length < this.argumentNb)
			throw new RuntimeException ("Il n'y a pas suffisament d'arguments pour la fonction " + this);
		for (int i = 0; i < this.argumentNb; i++)
			this.arguments.get(i).setValeur(new ConstanteFixe ("", args[i]));
		return this.expression.evaluer();
	}
	
	public static void putFonction (Fonction f){
		String s = f.getSymbole();
		List<Fonction> l;
		if (!fonctions.keySet().contains(s)){
			l = new ArrayList<Fonction> ();
			fonctions.put(s, l);
		}else
			l = fonctions.get(s);
		for (Fonction item : l)
			if (f.getArgumentNb() == item.getArgumentNb())
				throw new RuntimeException ("Il existe déjà une fonction " + f + " avec " + f.getArgumentNb() + " arguments.");
		l.add(f);
	}
	
	public static Fonction getFonction(String s, int argsNb){
		List<Fonction> l = fonctions.get(s);
		for (Fonction f : l)
			if (f.getArgumentNb() == argsNb)
				return f;
		return null;
	}
	
	public static boolean hasFonction (String s){
		return fonctions.containsKey(s);
	}
	
	public Expression calculer (List<Expression> expressions) {
		Expression expression = this.expression.clone();
		for (int i = 0; i < this.argumentNb; i++){
			Variable v = this.arguments.get(i);
			Expression e = expressions.get(i);
			expression.remplacer(v, e);
		}
		
		return expression;
	}
	
	public static void chargerFonctions (){
		//	Fonction sinus
		putFonction (new Fonction("sin", 1) {
			public float evaluer (float[] args){
				return (float)Math.sin(args[0]);
			}
			public Expression calculer (List<Expression> expressions){
				//	S'il n'y a pas assez d'arguments
				if (expressions.size() < 1)
					throw new RuntimeException ("Pas assez d'aguments pour la fonction " + this);
				
				Expression e = expressions.get(0);
				ConstanteFixe pi = ConstanteFixe.getConstante("pi");
				//	Si l'expression contient pi ou vaut 0
				if (e.contient(pi) || (e.estFeuille() && e.getValeur() instanceof Entier && ((Entier)e.getValeur()).equals(new Entier (0)))){
					int n = separerProduitTrigonometrique  (e);
					
					int s = 1;

					if (n >= 6){
						n %= 6;
						s = -1;
					}
					
					//	x * pi
					if (n % 6 == 0)
						return new Expression (0);
					//	x * pi / 2
					else if (n % 3 == 0)
						return new Expression (s);
					//	x * pi / 3
					else if (n % 2 == 0)
						return Expression.build(Integer.toString(s) + "sqrt(3)/2", e.getContexte());
					//	x * pi / 6
					else
						return Expression.build(Integer.toString(s) + "sqrt(2)/2", e.getContexte());
				}
				
				return Expression.build("sin(" + e + ")", e.getContexte());
			}
		});
		//	Fonction cosinus
		putFonction (new Fonction("cos", 1) {
			public float evaluer (float[] args){
				return (float)Math.cos(args[0]);
			}
		});
		//	Fonction tan
		putFonction (new Fonction("tan", 1) {
			public float evaluer (float[] args){
				return (float)Math.tan(args[0]);
			}
		});
		//	Fonction exp
		putFonction (new Fonction("exp", 1) {
			public float evaluer (float[] args){
				return (float)Math.exp(args[0]);
			}
		});
		//	Fonction ln
		putFonction (new Fonction("ln", 1) {
			public float evaluer (float[] args){
				return (float)Math.log(args[0]);
			}
		});
		//	Fonction racine carrée
		putFonction (new Fonction("sqrt", 1) {
			public float evaluer (float[] args){
				return (float)Math.sqrt(args[0]);
			}
		});
		//	Fonction racine n-ième
		putFonction (new Fonction("root", 2) {
			public float evaluer (float[] args){
				return (float)Math.pow(args[1], 1f/args[0]);
			}
		});
	}
	
	private static int separerProduitTrigonometrique (Expression e){
		ConstanteFixe pi = ConstanteFixe.getConstante("pi");
		if (e.contient(pi) || (e.estFeuille() && e.getValeur() instanceof Entier && ((Entier)e.getValeur()).equals(new Entier (0)))){
			Expression e2 = Util.separerProduit (e, pi);
			int n = 0;
			if (e2.estFeuille())
				n = (int)((Entier)e.getValeur()).getValeur() * 6;
			else if (e2.getValeur() == Operateur.getOperateur("/")){
				n = (int)((Entier)e.getGauche().getValeur()).getValeur() * 6;
				n /= (int)((Entier)e.getDroite().getValeur()).getValeur();
			}
			n %= 12;
			
			return n;
		}
		
		throw new RuntimeException ("Impossible de séparer le produit trigonométrique.");
	}
	
	public String toString (){
		return this.symbole;
	}
}
