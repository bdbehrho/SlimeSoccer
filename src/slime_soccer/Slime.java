package slime_soccer;

import java.awt.Color;
import java.awt.Graphics;

public class Slime extends BoardItem{
	public static final int HEIGHT = 160;
	public static final int WIDTH = 200;
	public static final int EYE_SIZE = WIDTH/10;
	public static final int SPEED = 10;
	private final Color EYE_COLOR = Color.white;
	private Color color;
	private int slimeNumber;
	
	public Slime(float x, float y, Color color, int slimeNumber) {
		super(x,y);
		this.color = color;
		this.slimeNumber = slimeNumber;
	}
	
	public void paintItem(Graphics g) {
		g.setColor(color);
		g.fillArc((int) getX(), (int) getY(), WIDTH, HEIGHT, 0, 180);
		g.setColor(EYE_COLOR);
		if (slimeNumber == 1) {
			g.fillOval((int) getX() + WIDTH - 2 * EYE_SIZE, (int) getY() + (int) (1.5 * EYE_SIZE), EYE_SIZE, EYE_SIZE );
			g.setColor(Color.BLACK);
			g.fillOval((int) getX() + WIDTH - 2 * EYE_SIZE + EYE_SIZE/2, (int) getY() + (int) (1.7 * EYE_SIZE), EYE_SIZE/2, EYE_SIZE/2 );
		} else {
			g.fillOval((int) getX() + 2 * EYE_SIZE, (int) getY() + (int) (1.5 * EYE_SIZE), EYE_SIZE, EYE_SIZE );
			g.setColor(Color.BLACK);
			g.fillOval((int) getX() + 2 * EYE_SIZE, (int) getY() + (int) (1.7 * EYE_SIZE), EYE_SIZE/2, EYE_SIZE/2 );
		}
		
	}
	
	public void translate() {
		if (slimeNumber == 1) {
			if (Board.keysPressed[Board.A_INDEX] && Board.keysPressed[Board.D_INDEX]) {
				setDx(0);
			} else if (Board.keysPressed[Board.A_INDEX]) {
				setDx(-SPEED);
			} else if (Board.keysPressed[Board.D_INDEX]) {
				setDx(SPEED);
			} else {
				setDx(0);
			}
			
			if (Board.keysPressed[Board.W_INDEX] && this.getDy() == 0 && this.getY() >= Board.BOARD_HEIGHT - HEIGHT/2) {
				setDy(-Board.JUMP_SPEED);
			}
			
			this.setX(this.getX() + this.getDx());
			this.setY(this.getY() + this.getDy());
			
			if (this.getY() <= Board.BOARD_HEIGHT - HEIGHT / 2) {
				this.setDy(this.getDy() + this.GRAVITY);
			} else {
				this.setDy(0);
				this.setY(Board.BOARD_HEIGHT - HEIGHT / 2);
			}
		} else if (slimeNumber == 2) {
			if (Board.keysPressed[Board.LEFT_INDEX] && Board.keysPressed[Board.RIGHT_INDEX]) {
				setDx(0);
			} else if (Board.keysPressed[Board.LEFT_INDEX]) {
				setDx(-SPEED);
			} else if (Board.keysPressed[Board.RIGHT_INDEX]) {
				setDx(SPEED);
			} else {
				setDx(0);
			}
			
			if (Board.keysPressed[Board.UP_INDEX] && this.getDy() == 0 && this.getY() >= Board.BOARD_HEIGHT - HEIGHT/2) {
				setDy(-Board.JUMP_SPEED);
			}
			
			this.setX(this.getX() + this.getDx());
			this.setY(this.getY() + this.getDy());
			
			if (this.getY() <= Board.BOARD_HEIGHT - HEIGHT / 2) {
				this.setDy(this.getDy() + this.GRAVITY);
			} else {
				this.setDy(0);
				this.setY(Board.BOARD_HEIGHT - HEIGHT / 2);
			}
		} else if (slimeNumber == 3) {
			Ball ball = (Ball) Board.getItems().get(2);
			if (ball.getX() > Board.BOARD_WIDTH/2 - WIDTH/2) {
				if (getX() - ball.getX() < 0) {
					setDx(SPEED);
				} else if (getX() - ball.getX() > 0) {
					setDx(-SPEED);
				}
			} else {
				if (getX() < Board.BOARD_WIDTH - Board.SLIME_START_POSITION - HEIGHT) {
					setDx(SPEED);
				} else if (getX() < Board.BOARD_WIDTH - Board.SLIME_START_POSITION - HEIGHT) {
					setDx(-SPEED);
				} else {
					setDx(0);
				}
			}
			if (this.getY() <= Board.BOARD_HEIGHT - HEIGHT / 2) {
				this.setDy(this.getDy() + this.GRAVITY);
			} else {
				this.setDy(0);
				this.setY(Board.BOARD_HEIGHT - HEIGHT / 2);
			}
			if(ball.getX() > getX() && ball.getX() < getX() + WIDTH &&
			   getY() > Board.BOARD_HEIGHT - HEIGHT/2 - 4 && ball.getY() > Board.BOARD_HEIGHT - HEIGHT) {
				setDy(-Board.JUMP_SPEED);
			}
			this.setX(this.getX() + this.getDx());
			this.setY(this.getY() + this.getDy());
		}
		if (this.getX() < -WIDTH/2) {
			this.setX(-WIDTH/2);
			this.setDx(0);
		}
		
		if (this.getX() > Board.BOARD_WIDTH - WIDTH/2) {
			this.setX(Board.BOARD_WIDTH - WIDTH/2);
			this.setDx(0);
		}
	}
	
	public float[] checkCollision(Ball ball) {
		float[] velocity = new float[3];
		float[] ballCenter = ball.getCenter();
		
		velocity[0] = ballCenter[0] - getX() - WIDTH/2;
		velocity[1] = getY() + HEIGHT/2 - ballCenter[1];
		
		double magnitude = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));
		magnitude -= Ball.DIAMETER;
		double angle = Math.atan(velocity[1] / velocity[0]);
		if( angle < 0 ) {
			angle = Math.PI + angle;
		}
		
		//System.out.println("Magnitude: " +magnitude +" Angle: " +angle);
		//System.out.println("YposSlime: " +getX() +" Ypos: " +ballCenter[1]);

		
		velocity[0] /= magnitude;
		velocity[1] /= magnitude;
		
		double[] ovalBound = new double[2];
		ovalBound[0] = Math.abs(WIDTH/2 * Math.cos(angle));
		ovalBound[1] = Math.abs(HEIGHT/2 * Math.sin(angle));
		
		if (Math.abs(magnitude * Math.cos(angle)) < ovalBound[0] && Math.abs(magnitude * Math.sin(angle)) < ovalBound[1]) {
			if (slimeNumber == 1) {
				if (!Board.keysPressed[Board.S_INDEX] || Board.keysPressed[Board.W_INDEX]) {
					velocity[2] = (float) (SPEED + Math.abs(getDy()/2));
					//ball.setX( getX() + WIDTH/2 + WIDTH/2 * velocity[0]);
					//ball.setY( getY() + HEIGHT - HEIGHT * velocity[1]);
				} else {
					ball.setY( (float) -ovalBound[1] - Ball.DIAMETER + getY() + HEIGHT/2);
					ball.setDx(0);
					ball.setDy(0);
				}
			} else {
				if (!Board.keysPressed[Board.DOWN_INDEX] || Board.keysPressed[Board.UP_INDEX]) {
					velocity[2] = (float) (SPEED + Math.abs(getDy()/2));
					//ball.setX( getX() + WIDTH/2 + WIDTH/2 * velocity[0]);
					//ball.setY( getY() + HEIGHT - HEIGHT * velocity[1]);
				} else {
					ball.setY( (float) -ovalBound[1] -Ball.DIAMETER + getY() + HEIGHT/2 );
					ball.setDx(0);
					ball.setDy(0);
				}
			}
		} else {
			velocity[2] = 0;
		}
		
		return velocity;
	}
}
