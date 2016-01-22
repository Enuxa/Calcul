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
	
	public static void chargerFonctions (){
		//	Fonction sinus
		putFonction (new Fonction("sin", 1) {
			public float evaluer (float[] args){
				return (float)Math.sin(args[0]);
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
	
	public String toString (){
		return this.symbole;
	}
}
