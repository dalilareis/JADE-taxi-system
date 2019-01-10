package aiAula3;

import java.util.Random;

public class Position {
	private static Random rand = new Random();
	private int x;
	private int y;
	
	public Position() {
		x = rand.nextInt(101);
		y = rand.nextInt(101);
	}
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Position(Position p) {
		this.x = p.getX();
		this.y = p.getY();
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(x + " " + y);
		return sb.toString();
	}
	
	public double calcDist(Position p) {
		int x = p.getX();
		int y = p.getY();
		int diffX = x - this.x;
		int diffY = y - this.y;
		return Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
	}
}
