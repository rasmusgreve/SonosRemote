package commands;

public abstract class CommandResultListener<T> {
	public abstract void onResult(T result);
}
