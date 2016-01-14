public interface Operable<T extends Operable<T>>{
	public T oppose ();
	public T additionner (T a);
	public T inverse ();
	public T multiplier (T a);
}
