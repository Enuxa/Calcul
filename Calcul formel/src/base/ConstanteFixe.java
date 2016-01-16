package base;

import java.util.*;

public class ConstanteFixe extends Constante {
	private final float valeur;
	private String symbole;
	private static Map<String, ConstanteFixe> constantes = new HashMap<String, ConstanteFixe> ();
	public ConstanteFixe (String symbole, float valeur){
		this.symbole = symbole;
		this.valeur = valeur;
	}
	public float getValeur() {
		return this.valeur;
	}
	public String getSymbole (){
		return this.symbole;
	}
	public String toString (){
		return this.symbole;
	}
	public static ConstanteFixe getConstante (String symbole){
		return constantes.get(symbole);
	}
	public static boolean hasConstante (String symbole){
		return constantes.containsKey(symbole);
	}
	public static void putConstante (ConstanteFixe constante){
		constantes.put(constante.getSymbole(), constante);
	}
	public static void chargerConstantes (){
		putConstante(new ConstanteFixe ("e", (float)Math.E));
		putConstante(new ConstanteFixe ("pi", (float)Math.PI));
	}
}
