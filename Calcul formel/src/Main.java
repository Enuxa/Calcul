import base.*;
import Operateurs.*;

public class Main {

	public static void main(String[] args) {
		Operateur.putOperateur(new Addition ());
		Operateur.putOperateur(new Soustraction ());
		Operateur.putOperateur(new Multiplication ());
		Operateur.putOperateur(new Division ());
		Operateur.putOperateur(new Oppose ());
		
		Expression e = new Expression (new Entier (5));
		e = e.additionner(new Expression (7));
		e = e.multiplier(new Expression (9));
		e = e.diviser(new Expression (8));
		
		System.out.println(e);
	}

}
