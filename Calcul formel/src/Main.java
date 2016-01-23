import base.*;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		Operateur.chargerOperateurs();
		ConstanteFixe.chargerConstantes();
		Fonction.chargerFonctions();
		
		Contexte c = new Contexte ();
		
		Scanner sc = new Scanner (System.in);

		while (true){
			String line = sc.nextLine();
			String[] t = line.split("=");
			if (t.length >= 2){
				String nom = t[0].split("\\(")[0];
				List<Variable> argts = Util.separerVariables(t[0].substring(nom.length() + 1, t[0].length() - 1));
				
				Contexte contexte = new Contexte (c, argts);
				
				Expression exp = Expression.build(t[1], contexte);
				Fonction f = new Fonction (nom, exp, argts);
				
				try{
					Fonction.putFonction(f);
				}catch (Exception e){
					System.out.println("Erreur : " + e);
				}
			}else{
				Expression exp = Expression.build(line, c);
				System.out.println(exp + "=" + exp.evaluer());
			}
			
			sc.close();
		}
	}

}
