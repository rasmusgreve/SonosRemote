package model;

public class PlayPosition {

	public final int h, m, s;
	public PlayPosition(int h, int m, int s) {
		this.h = h;
		this.m = m;
		this.s = s;
	}
	
	public PlayPosition(int m, int s){
		this(0,m,s);
	}
	
	@Override
	public String toString(){
		return String.format("%02d:%02d:%02d", h, m, s);
	}

}
