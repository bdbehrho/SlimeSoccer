package slime_soccer;

import java.awt.Graphics;

public abstract class BoardItem {

	private float x;
	private float y;
	private float startX;
	private float startY;
	private float dx;
	private float dy;
	public static final float GRAVITY = 1.5f;

	public BoardItem(float x, float y) {
		this.x = x;
		this.y = y;
		startX = x;
		startY = y;
		this.dx = 0;
		this.dy = 0;
	}
	
	public BoardItem() {
		this(0, 0);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getDx() {
		return dx;
	}
	
	public float getDy() {
		return dy;
	}
	
	public abstract void translate();
	
	public void setDx(float dx) {
		this.dx = dx;
	}
	
	public void setDy(float dy) {
		this.dy = dy;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void reset() {
		x = startX;
		y = startY;
		dx = 0;
		dy = 0;
	}
	
	
	public abstract void paintItem(Graphics g);
}
