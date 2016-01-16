import base.*;

public class Main {

	public static void main(String[] args) {
		Operateur.chargerOperateurs();
		ConstanteFixe.chargerConstantes();
		
		Expression exp = Expression.build("e");
		System.out.println(exp + "=" + exp.evaluer());
	}

}
