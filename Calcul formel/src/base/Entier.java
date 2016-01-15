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
	public Rationnel inverse() {
		return new Rationnel (1, this.n);
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

}
