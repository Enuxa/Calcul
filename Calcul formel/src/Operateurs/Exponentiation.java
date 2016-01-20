package Operateurs;

import base.*;

public class Exponentiation extends Operateur{
	public Exponentiation() {
		super("^", true, false, 2);
	}
	public float evaluer(float a) {
		throw new RuntimeException ("L'operateur " + this + " est binaire.");
	}

	@Override
	public float evaluer(float a, float b) {
		return (float)Math.pow(a, b);
	}
	
}
