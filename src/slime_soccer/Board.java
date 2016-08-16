package slime_soccer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener {

	private static Board board;
	private static ArrayList<BoardItem> boardItems;
	public static final int BOARD_HEIGHT = 600;
	public static final int BOARD_WIDTH = 1500;
	public static final int GOAL_WIDTH = 15;
	public static final int GOAL_HEIGHT = 180;
	public static final int SLIME_START_POSITION = 50;
	public static final int BALL_START_HEIGHT = 100;
	
	public static final float JUMP_SPEED = 20f;
	public static boolean [] keysPressed = new boolean[10];
	public static final int A_INDEX = 0;
	public static final int S_INDEX = 1;
	public static final int D_INDEX = 2;
	public static final int W_INDEX = 3;
	public static final int LEFT_INDEX = 4;
	public static final int UP_INDEX = 5;
	public static final int RIGHT_INDEX = 6;
	public static final int DOWN_INDEX = 7;

	public static final int TIMER_SPEED = 12;
	private double DEFAULT_ROUND_TIME = 60;
	private boolean twoPlayers = true;
	private double roundTime = DEFAULT_ROUND_TIME;
	private static int player1Score = 0;
	private static int player2Score = 0;
	private Timer timer;
	
	public Board() {
		boardItems = new ArrayList<BoardItem>();
		
		setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
		setBackground(Color.blue);
		
		boardItems.add(new Slime(50, BOARD_HEIGHT - Slime.HEIGHT / 2, Color.cyan, 1));
		if (twoPlayers) {
			boardItems.add(new Slime(BOARD_WIDTH - Slime.WIDTH - SLIME_START_POSITION, BOARD_HEIGHT - Slime.HEIGHT / 2, Color.RED, 2));
		} else {
			boardItems.add(new Slime(BOARD_WIDTH - Slime.WIDTH - SLIME_START_POSITION, BOARD_HEIGHT - Slime.HEIGHT / 2, Color.RED, 3));
		}
		boardItems.add(new Ball(BOARD_WIDTH/2 - Ball.DIAMETER/2, BOARD_HEIGHT - Ball.DIAMETER - BALL_START_HEIGHT, Color.YELLOW));
				
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "Move Right");
		getActionMap().put("Move Right", new Move(D_INDEX, true));
		
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "Move Left");
		getActionMap().put("Move Left", new Move(A_INDEX, true));
		
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released D"), "Stop Right");
		getActionMap().put("Stop Right", new Move(D_INDEX, false));
		
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released A"), "Stop Left");
		getActionMap().put("Stop Left", new Move(A_INDEX, false));
		
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "Slime Jump");
		getActionMap().put("Slime Jump", new Move(W_INDEX, true));
		
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released W"), "Stop Slime Jump");
		getActionMap().put("Stop Slime Jump", new Move(W_INDEX, false));
		
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "Slime Hold");
		getActionMap().put("Slime Hold", new Move(S_INDEX, true));
		
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released S"), "Stop Slime Hold");
		getActionMap().put("Stop Slime Hold", new Move(S_INDEX, false));
		
		//Slime 2 key mappings
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "Move Right 2");
		getActionMap().put("Move Right 2", new Move(RIGHT_INDEX, true));
		
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "Move Left 2");
		getActionMap().put("Move Left 2", new Move(LEFT_INDEX, true));
		
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released RIGHT"), "Stop Right 2");
		getActionMap().put("Stop Right 2", new Move(RIGHT_INDEX, false));
		
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released LEFT"), "Stop Left 2");
		getActionMap().put("Stop Left 2", new Move(LEFT_INDEX, false));
		
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "Slime Jump 2");
		getActionMap().put("Slime Jump 2", new Move(UP_INDEX, true));
		
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released UP"), "Stop Slime Jump 2");
		getActionMap().put("Stop Slime Jump 2", new Move(UP_INDEX, false));
		
		
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "Slime Hold 2");
		getActionMap().put("Slime Hold 2", new Move(DOWN_INDEX, true));
		
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released DOWN"), "Stop Slime Hold 2");
		getActionMap().put("Stop Slime Hold 2", new Move(DOWN_INDEX, false));
		
		timer = new Timer(12, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int size = boardItems.size();
		for ( int i = 0; i < size; i++ )
			boardItems.get(i).paintItem(g);
		g.setColor(Color.GRAY);
		g.fillRect(0, BOARD_HEIGHT - GOAL_HEIGHT, GOAL_WIDTH, GOAL_HEIGHT);
		g.fillRect(BOARD_WIDTH - GOAL_WIDTH, BOARD_HEIGHT - GOAL_HEIGHT, GOAL_WIDTH, GOAL_HEIGHT);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Helvetica",Font.BOLD,50));
		g.drawString("" + player1Score, 50, 50);
		g.drawString("" + player2Score, BOARD_WIDTH - 80, 50);
		
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Helvetica",Font.BOLD,80));
		g.drawString("" + Math.round(roundTime), BOARD_WIDTH/2 - 20, 80);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		board = new Board();
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.GREEN);
		
		frame.add(board, BorderLayout.NORTH);
		frame.add(bottomPanel, BorderLayout.CENTER);
		frame.setTitle("Slime Soccer");
		frame.setSize(new Dimension(1500, 700));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if ( roundTime > 0 ) {
			int size = boardItems.size();
			for (int i = 0; i < size; i++) {
				boardItems.get(i).translate();
			}
			roundTime -= TIMER_SPEED * .001;
			repaint();
		} else {
			timer.stop();
			int winner = 0;
			if (player1Score > player2Score) {
				winner = 1;
			} else if (player2Score > player1Score){
				winner = 2;
			}
			String notification;
			if (winner == 0) {
				notification = "Tie! Play again?";
			} else {
				notification = "Player " + winner + " won. Play again?";
			}
			int input = JOptionPane.showOptionDialog( new JFrame(), notification, "Game Over", JOptionPane.YES_NO_OPTION,
					                      JOptionPane.PLAIN_MESSAGE, null, null, null);
			if (input == JOptionPane.YES_OPTION){
				roundTime = DEFAULT_ROUND_TIME;
				for (int i = 0; i < boardItems.size(); i++) {
					boardItems.get(i).reset();
				}
				for (int i = 0; i < keysPressed.length; i++) {
					keysPressed[i] = false;
				}
				player1Score = 0;
				player2Score = 0;
				timer.start();
			} else {
				System.exit(0);
			}
		}
		
	}
	
	public static ArrayList<BoardItem> getItems() {
		return boardItems;
	}
	
	class Move extends AbstractAction {
		
		private int key;
		private boolean pressed;
		
		public Move(int key, boolean pressed) {
			this.key = key;
			this.pressed = pressed;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			keysPressed[key] = pressed;
		}
		
	}

	public static void score(int player) {
		if (player == 1) {
			player1Score++;
		} else {
			player2Score++;
		}
		for (int i = 0; i < boardItems.size(); i++) {
			boardItems.get(i).reset();
		}
		
	}

}
