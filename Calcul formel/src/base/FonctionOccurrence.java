package base;

public class FonctionOccurrence implements Noeud {
	private Fonction fonction;
	private Expression[] args;
	public FonctionOccurrence (Fonction f, Expression[] args){
		this.fonction = f;
		this.args = args;
	}
	public float evaluer (){
		float[] argsf = new float [args.length];
		for (int i = 0; i < args.length; i++)
			argsf[i] = args[i].evaluer();
		return this.fonction.evaluer(argsf);
	}
	
	public String toString (){
		String s = "";
		for (int i = 0; i < this.args.length; i++){
			s += this.args[i];
			if (i < this.args.length - 1)
				s += ",";
		}
		return this.fonction.toString() + "(" + s + ")";
	}
}
