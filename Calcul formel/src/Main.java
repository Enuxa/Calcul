import base.*;

public class Main {

	public static void main(String[] args) {
		Operateur.chargerOperateurs();
		ConstanteFixe.chargerConstantes();
		
		Expression exp = Expression.build("(1-1-1)^2");
		System.out.println(exp + "=" + exp.evaluer());
	}

}
