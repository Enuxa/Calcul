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
	public Expression inverse() {
		return new Expression (new Expression (new Entier (1)), this.clone(), Operateur.getOperateur("/", true));
	}
	@Override
	public Expression multiplier(Expression a) {
		return new Expression (this.clone(), a, Operateur.getOperateur("*", true));
	}
	
	public Expression clone (){
		if (this.gauche == null && this.droite == null && (this.valeur instanceof Rationnel || this.valeur instanceof Entier)){
			if (this.valeur instanceof Entier){
				Entier e = (Entier)this.valeur;
				return new Expression (e.multiplier(new Entier (1)));
			}else{
				Rationnel r = (Rationnel)this.valeur;
				return new Expression (r.multiplier(new Rationnel (1, 1)));
			}
		}else
			return new Expression (this.gauche.clone(), this.droite.clone(), this.valeur);
	}
	
	private String toString (int p){
		String g = "", d = "", v = this.valeur.toString(), r = "";
		if (this.gauche != null)
			g = this.gauche.toString(p);
		if (this.droite != null)
			d = this.droite.toString(p);
		r = g + v + d;
		if (this.valeur instanceof Operateur){
			Operateur o = (Operateur)this.valeur;
			if ((o.getPriorite() < p && p >= 0)
					|| !o.estAssociatif())
				r = "(" + r + ")";
		}
		if (this.valeur instanceof Rationnel && Operateur.getOperateur("/", true).getPriorite() <= p)
			r = "(" + r + ")";
		return r;
	}
	
	public String toString (){
		return this.toString(-1);
	}
}
