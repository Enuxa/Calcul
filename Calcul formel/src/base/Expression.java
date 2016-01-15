package base;

public class Expression implements Operable<Expression> {
	private Expression gauche, droite;
	private Noeud valeur;
	
	public Expression (Expression g, Expression d, Noeud v){
		this.gauche = g;
		this.droite = d;

		this.valeur = v;
	}
	
	public Expression (Noeud v){
		this (null, null, v);
	}
	
	public Expression (int n){
		this (new Entier (n));
	}
	
	public Expression getGauche (){
		return this.gauche;
	}
	public Expression getDroite (){
		return this.droite;
	}
	public Noeud getValeur (){
		return this.valeur;
	}
	@Override
	public Expression oppose() {
		return new Expression (null, this.clone(), Operateur.getOperateur("-", false));
	}
	@Override
	public Expression additionner(Expression a) {
		return new Expression (this.clone(), a, Operateur.getOperateur("+", true));
	}
	
	@Override
	public Expression multiplier(Expression a) {
		return new Expression (this.clone(), a, Operateur.getOperateur("*", true));
	}
	
	public Expression clone (){
		if (this.gauche == null && this.droite == null && this.valeur instanceof Entier)
			return new Expression (((int)((Entier)this.valeur).getValeur()));
		else
			return new Expression (this.gauche.clone(), this.droite.clone(), this.valeur);
	}
	
	private String toString (int p){
		String g = "", d = "", v = this.valeur.toString(), r = "";
		int np = this.valeur instanceof Operateur ? ((Operateur)this.valeur).getPriorite() : p;

		if (this.gauche != null)
			g = this.gauche.toString(np);
		if (this.droite != null)
			d = this.droite.toString(np);
		r = g + v + d;
		if (this.valeur instanceof Operateur){
			Operateur o = (Operateur)this.valeur;
			if (p >= 0 && (np < p || !o.estAssociatif()))
				r = "(" + r + ")";
		}

		return r;
	}
	
	public String toString (){
		return this.toString(-1);
	}

	@Override
	public Expression diviser(Expression a) {
		return new Expression (this.clone(), a.clone(), Operateur.getOperateur("/", true));
	}
}
