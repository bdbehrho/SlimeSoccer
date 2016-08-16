package slime_soccer;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Ball extends BoardItem {
	
	public static final int DIAMETER = 40;
	public static final int SPEED = 10;
	public static final float BOUNCE_DECELERATION = .7f;
	private float startX;
	private float startY;
	private Color color;
	
	public Ball(float x, float y, Color color) {
		super(x, y);
		startX = x;
		startY = y;
		this.color = color;
	}
	
	public void paintItem(Graphics g) {
		g.setColor(color);
		g.fillOval((int) getX(), (int) getY(), DIAMETER, DIAMETER);
		//g.fillOval(0, 0, DIAMETER, DIAMETER);
		
	}

	@Override
	public void translate() {
		if (getY() <= Board.BOARD_HEIGHT - DIAMETER) {
			setDy(getDy() + GRAVITY / 1.5f);
		} else {
			if( getDy() > 2 || getDy() < -2 ) {
				setDy(-getDy() * BOUNCE_DECELERATION);
				setY(getDy() + getY());
			} else {
				setY(Board.BOARD_HEIGHT - DIAMETER);
				setDy(0);
			}
		}
		float[] collisionChange;
		collisionChange = checkCollisions();
		if (collisionChange[2] != 0) {
			setDy((float) (-collisionChange[1] * collisionChange[2]));
			setDx(collisionChange[0] * collisionChange[2]);
		}
		
		if (getX() > Board.BOARD_WIDTH - DIAMETER) {
			if (getY() > Board.BOARD_HEIGHT - Board.GOAL_HEIGHT) {
				Board.score(1);
			} else {
				setDx(-BOUNCE_DECELERATION * getDx());
				setX(Board.BOARD_WIDTH - DIAMETER);
			}
		}
		
		if (getX() < 0) {
			if (getY() > Board.BOARD_HEIGHT - Board.GOAL_HEIGHT) {
				Board.score(2);
			} else {
				setDx(-BOUNCE_DECELERATION * getDx());
				setX(0);
			}
		}
		
		if (getY() < 0) {
			setDy(-getDy());
			setY(1);
		}
		setY(getY() + getDy());
		setX(getX() + getDx());
		
	}
	
	public float[] checkCollisions() {
		float[] speedChanges = {Board.BOARD_HEIGHT, Board.BOARD_HEIGHT, 0f};
		ArrayList<BoardItem> boardItems = Board.getItems();
		for (int i = 0; i < 2; i++) {
			float[] speedChangeTest = ((Slime) boardItems.get(i)).checkCollision(this);
			if (speedChangeTest[2] > 0) {
				speedChanges = speedChangeTest;
			}
		}
		
		return speedChanges;
	}
	
	public float [] getCenter() {
		float[] center = new float[2];
		center[0] = getX() + DIAMETER/2;
		center[1] = getY() + DIAMETER/2;
		return center;
	}
	

}
