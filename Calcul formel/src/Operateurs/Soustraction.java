package Operateurs;

import base.*;

public class Soustraction extends Operateur{
	public Soustraction() {
		super("-", true, false, 0);
	}
	public float evaluer(float a) {
		throw new RuntimeException ("L'operateur " + this + " est binaire.");
	}

	@Override
	public float evaluer(float a, float b) {
		return a - b;
	}
	
}
