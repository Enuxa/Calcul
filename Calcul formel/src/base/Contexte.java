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
		this (c.get());
	}
	
	/**
	 * Crée un contexte à partir de variables.
	 * @param v Les variables à mettre dans la collection.
	 */
	public Contexte (Collection<Variable> v){
		this();
		for (Variable t : v)
			this.add(t);
	}
	
	/**
	 * @param contexte Le contexte à partir duquel copier les variables.
	 * @param args Liste des variables.
	 */
	public Contexte (Contexte contexte, Collection<Variable> variables){
		this(contexte);
		for (Variable v : variables)
			this.add(v);
	}
	
	/**
	 * @param s Chaîne de la forme <i>"x<sub>1</sub>, x<sub>2</sub>, x<sub>3</sub>, ..."</i> où les <i>x<sub>i</sub></i> sont des noms de variables.
	 */
	public Contexte (String s){
		this (new Contexte (), Util.separerVariables(s));
	}
	
	/**
	 * Réunie deux contexte
	 * @param contexte Le contexte à joindre.
	 * @return Le noueau contexte contenant les variables des deux contextes.
	 * @throws RuntimeException s'il existe une variable dans chaque contexte porant le même nom.
	 */
	public Contexte joindre (Contexte contexte){
		return new Contexte (contexte, this.get());
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
	 * Ajoute une variable au contexte s'il n'en contient aucune portant le même nom.
	 * @param v La variable à ajouter.
	 * @throws RuntimeException si le contexte possède déjà une variable portant ce nom.
	 */
	public void add (Variable v){
		if (!this.variables.containsKey(v.toString()))
			this.variables.put(v.toString(), v);
		else
			throw new RuntimeException ("La variable " + v + " existe déjà dans ce contexte.");
	}
	
	/**
	 * Indique s'il existe une variable dans ce contexte portant ce nom.
	 * @param symbole Le nom de la variable.
	 * @return <code>true</code> si cette variable existe.
	 */
	public boolean has (String symbole){
		return this.variables.containsKey(symbole);
	}
	
	/**
	 * Récupère les variables.
	 * @return Les variables du contexte.
	 */
	public Collection<Variable> get (){
		return this.variables.values();
	}
}
