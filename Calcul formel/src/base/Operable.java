package base;

public interface Operable<T extends Operable<T>>{
	public T oppose ();
	public T additionner (T a);
	@SuppressWarnings("rawtypes")
	public Operable inverse ();
	public T multiplier (T a);
}
