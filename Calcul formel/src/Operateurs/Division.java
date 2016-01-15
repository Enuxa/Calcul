package Operateurs;

import base.*;

public class Division extends Operateur{
	public Division() {
		super("/", true, false, 1);
	}
	public float evaluer(float a) {
		throw new RuntimeException ("L'operateur " + this + " est binaire.");
	}

	@Override
	public float evaluer(float a, float b) {
		return a / b;
	}
	
}
