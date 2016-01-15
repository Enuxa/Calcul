package Operateurs;

import base.*;

public class Oppose extends Operateur{
	public Oppose() {
		super("-", false, false, 0);
	}
	public float evaluer(float a) {
		return -a;
	}

	@Override
	public float evaluer(float a, float b) {
		throw new RuntimeException ("L'operateur " + this + " est unaire.");
	}
	
}
