import base.*;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		Operateur.chargerOperateurs();
		ConstanteFixe.chargerConstantes();
				
		Scanner sc = new Scanner (System.in);
		
		while (true){
			Expression exp = Expression.build(sc.nextLine());
			System.out.println(exp + "=" + exp.evaluer());
		}
	}

}
