package base;

public class Rationnel extends Constante implements Operable<Rationnel>{
	private Entier n, d;

	public Rationnel (Entier n, Entier d){
		this.n = n;
		this.d = d;
		if (d.getValeur() == 0)
			throw new RuntimeException ("Le rationnel " + this + " n'existe pas.");
	}
	
	public Rationnel (int n, int d){
		this (new Entier (n), new Entier (d));
	}
	
	public Entier getNumerateur (){
		return this.n;
	}
	public Entier getDenominateur (){
		return this.d;
	}
	
	@Override
	public Rationnel oppose() {
		return new Rationnel (this.n.oppose(), this.d);
	}

	@Override
	public Rationnel additionner(Rationnel r) {
		Rationnel x = new Rationnel (this.n.multiplier(r.d).additionner(r.n.multiplier(this.d)), this.d.multiplier(r.d));
		x.simplifier();
		return x;
	}

	@Override
	public Rationnel inverse() {
		if (this.n.equals(0))
			throw new RuntimeException ("Le rationnel " + this + "n'admet pas d'inverse.");
		return new Rationnel (this.d, this.n);
	}

	@Override
	public Rationnel multiplier(Rationnel r) {
		Rationnel a = new Rationnel (this.n.multiplier(r.n), this.d.multiplier(r.d));
		a.simplifier();
		return a;
	}

	public String toString (){
		return this.n.toString() + (this.d.compareTo(new Entier (1)) == 0 ? "" : "/" + this.d.toString());
	}

	public int compareTo(Rationnel r) {
		return this.additionner(r.oppose()).n.compareTo(new Entier (0));
	}
	
	private void simplifier (){
		int x = (int)PGCD(this.n, this.d).getValeur();
		this.n = new Entier ((int)this.n.getValeur() / x);
		this.d = new Entier ((int)this.d.getValeur() / x);
	}
	
	public static Entier PGCD (Entier a, Entier b){
	   if (b.getValeur() == 0)
		   return a;
	   return PGCD (b, new Entier ((int)(a.getValeur() % b.getValeur())));
	}
	
	public static Entier PPCM (Entier a, Entier b){
		return new Rationnel (a.multiplier(b), new Entier (1)).multiplier(PGCD (a, b).inverse()).n;
	}

	@Override
	public float getValeur() {
		return this.n.getValeur() / this.d.getValeur();
	}
}
