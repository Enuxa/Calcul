package base;

public class Entier extends Constante implements Operable<Entier>{
	private int n;

	public Entier (int n){
		this.n = n;
	}
	
	@Override
	public Entier oppose () {
		return new Entier (-this.n);
	}

	@Override
	public Entier additionner(Entier a) {
		return new Entier (this.n + a.n);
	}

	@Override
	public Entier multiplier(Entier a) {
		
		return new Entier (this.n * a.n);
	}
	
	public float getValeur (){
		return this.n;
	}
	
	public String toString (){
		return Integer.toString(this.n);
	}

	public int compareTo(Entier a) {
		return this.n - a.n;
	}

	@Override
	public Entier diviser(Entier a) {
		if (this.n % a.n != 0)
			throw new RuntimeException (this.toString() + " n'est pas divisible par " + a);
		return new Entier (this.n / a.n);
	}

}
