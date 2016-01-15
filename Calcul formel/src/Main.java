import base.*;
import Operateurs.*;

public class Main {

	public static void main(String[] args) {
		Operateur.putOperateur(new Addition ());
		Operateur.putOperateur(new Soustraction ());
		Operateur.putOperateur(new Multiplication ());
		Operateur.putOperateur(new Division ());
		Operateur.putOperateur(new Oppose ());
		
		Expression e = new Expression (new Entier (7)).inverse().additionner(new Expression (new Rationnel (2, 5))).additionner(new Expression (new Entier (5)));
		System.out.println(e);
	}

}
