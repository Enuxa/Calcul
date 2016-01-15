package Operateurs;

import base.*;

public class Addition extends Operateur{
	public Addition() {
		super("+", true, true, 0);
	}
	public float evaluer(float a) {
		throw new RuntimeException ("L'operateur " + this + " est binaire.");
	}

	@Override
	public float evaluer(float a, float b) {
		return a + b;
	}
	
}
