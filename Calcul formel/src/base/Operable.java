package base;

public interface Operable<T extends Operable<T>>{
	public T oppose ();
	public T additionner (T a);
	public T multiplier (T a);
	public T diviser (T a);
}
