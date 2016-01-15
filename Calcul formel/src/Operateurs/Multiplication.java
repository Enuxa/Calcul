package Operateurs;

import base.*;

public class Multiplication extends Operateur{
	public Multiplication() {
		super("*", true, true, 1);
	}
	public float evaluer(float a) {
		throw new RuntimeException ("L'operateur " + this + " est binaire.");
	}

	@Override
	public float evaluer(float a, float b) {
		return a * b;
	}
	
}
