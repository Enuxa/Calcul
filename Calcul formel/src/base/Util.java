package base;

import java.util.ArrayList;
import java.util.*;

public abstract class Util {
	/**
	 * Sépare une chaîne de la forme <i>"x<sub>1</sub>, x<sub>2</sub>, x<sub>3</sub>, ..."</i> où les <i>x<sub>i</sub></i> sont des noms d'éléments, et construit la liste de ces noms.
	 * @param s Chaîne 
	 * @return Les noms des éléments.
	 */	
	public static List<String> separerElements (String s){
		String[] t = s.split(",");
		for (int i = 0; i < t.length; i++)
			t[i] = t[i].trim();
		return Arrays.asList(t);
	}
	
	/**
	 * Sépare une chaîne de la forme <i>"x<sub>1</sub>, x<sub>2</sub>, x<sub>3</sub>, ..."</i> où les <i>x<sub>i</sub></i> sont des noms de variables, et construit la liste de ces variables libres.
	 * @param s Chaîne 
	 * @return Les variables.
	 */	
	public static List<Variable> separerVariables (String s){
		List<String> liste = separerElements (s);
		List<Variable> v = new ArrayList<Variable> ();
		for (String n : liste){
			for (int i = 0; i < n.length(); i++){
				if (!Character.isAlphabetic(n.charAt(i)))
						throw new RuntimeException (n + " n'est pas un nom de variable autorisé.");
			}
			v.add(new Variable (n));
		}
		return v;
	}

	public static Expression separerProduit(Expression e, ConstanteFixe pi) {
	}
}
