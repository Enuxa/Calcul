package base;

import java.util.*;

public class Fonction {
	private static Map<String, List<Fonction>> fonctions = new HashMap<String, List<Fonction>> ();
	private String symbole;
	private Expression expression;
	private List<Variable> variables;
	private int argumentNb;
	public Fonction (String symbole, Expression e, List<Variable> variables){
		this.expression = e;
		this.variables = variables;
		this.symbole = symbole;
		this.argumentNb = variables.size();
	}
	public Fonction (String symbole, String expression, List<Variable> variables){
		this (symbole, Expression.build(expression, listToMap (variables)), variables);
	}
	public Fonction (String symbole, String expression, Map<String, Variable> variables){
		this (symbole, Expression.build(expression, variables), variables);
	}
	private static Map<String, Variable> listToMap (List<Variable> l){
		Map<String, Variable> m = new HashMap<String, Variable> ();
		for (Variable v : l)
			m.put(v.toString(), v);
		return m;
	}
	public Fonction (String symbole, int argumentNb){
		this.symbole = symbole;
		this.argumentNb = argumentNb;
		this.expression = null;
		this.variables = null;
	}
	
	public String getSymbole (){
		return this.symbole;
	}
	
	public int getArgumentNb (){
		return this.argumentNb;
	}
	
	public float evaluer (float[] args){
		Map<Variable, Float> valeurs = new HashMap <Variable, Float> ();
		for (int i = 0; i < this.argumentNb; i++)
			valeurs.put(this.variables.get(i), args[i]);
		return this.expression.evaluer(valeurs);
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
				throw new RuntimeException ("Il existe déjà  une fonction " + f + " avec " + f.getArgumentNb() + " arguments.");
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
	}
	
	public String toString (){
		return this.symbole;
	}
}
