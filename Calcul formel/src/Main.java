import base.*;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		Operateur.chargerOperateurs();
		ConstanteFixe.chargerConstantes();
		Fonction.chargerFonctions();
				
		Scanner sc = new Scanner (System.in);

		Map<String, Variable> vl = new HashMap<String, Variable> ();
		vl.put("x", new Variable ("x"));

		while (true){
			String line = sc.nextLine();
			Expression exp = Expression.build(line, vl);
			Fonction f = new Fonction (f, exp, vl.);
			System.out.println(exp + "=" + exp.evaluer());
		}
	}

}
