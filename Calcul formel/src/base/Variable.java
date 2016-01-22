
package base;

public class Variable extends Nombre {
	private Constante valeur;
	private String symbole;
	public Variable (String symbole, Constante c){
		this.symbole = symbole;
		this.valeur = c;
	}
	public Variable (String symbole){
		this (symbole, null);
	}
	public void setValeur (Constante c){
		this.valeur = c;
	}
	public Constante getValeur (){
		return this.valeur;
	}
}
