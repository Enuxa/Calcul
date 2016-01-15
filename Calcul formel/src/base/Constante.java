package base;

public abstract class Constante extends Nombre implements Comparable<Constante>{
	public abstract float getValeur ();
	public int compareTo (Constante c){
		return (int)Math.signum(this.getValeur() - c.getValeur ());
	}
}
