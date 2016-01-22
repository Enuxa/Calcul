package base;

import java.util.*;

public class Contexte {
	private Map<String, Variable> variables;
	
	public Contexte (){
		this.variables = new HashMap<String, Variable> ();
	}
	/**
	 * @param c Le contexte à copier
	 */
	public Contexte (Contexte c){
		this.variables = new HashMap<String, Variable> ();
		for (String s : c.variables.keySet())
			this.add(c.variables.get(s));
	}
	
	/**
	 * Récupère une variable.
	 * @param symbole Le symbole représentant la variable.
	 * @return La variable demandée, <code>null</code> sinon.
	 */
	public Variable get (String symbole){
		return this.variables.get(symbole);
	}
	
	/**
	 * Ajoute une variable au contexte s'il n'en contient pas portant le même nom.
	 * @param v La variable à ajouter.
	 * @throws RuntimeException si le contexte possède déjà une variable portant ce nom.
	 */
	public void add (Variable v){
		if (!this.variables.containsKey(v.toString()))
			this.variables.put(v.toString(), v);
		else
			throw new RuntimeException ("La variable " + v + " existe déjà dans ce contexte.");
	}
}
