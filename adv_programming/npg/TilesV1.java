import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
/**
 * Creates the Tiles that are placed on the frame for the game, basically almost all game controls are
 * carried out here. 
 * @authors Eduardo && CŽsar
 *
 */
public class TilesV1 extends JComponent implements KeyListener, ActionListener  {

	private int tiles[][];
	private int winningSet[][];
	private int n;
	private String playerName;
	private ArrayList<Integer> numbers;
	private Timer time;
	private int zeroIndex1;
	private int zeroIndex2;
	private int slide1 = 0, slide2 = 0;
	private int xMovement = 0, yMovement = 0;
	private boolean state = false;
	

	private int width;
	private int height;
	private static int fontSize;
	private static final int PAD = 25;
	
	/**
	 * Construct random tiles with the size of the matrix entered
	 * @param matrixSize the size of matrix
	 * @param name the name of the player
	 */
	public TilesV1 (int matrixSize, String name){
		n = matrixSize;
		playerName = name;
		
		int delay = 2;
		time = new Timer(delay, this);
		
		numbers = new ArrayList<Integer>();
		for(int i = 0; i <= Math.pow(n, 2) - 1; i++) {
			numbers.add(i);
		}
		Collections.shuffle(numbers);

		tiles = new int[n][n];
		winningSet = new int[n][n];

		int number = 1;
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				tiles[i][j] = numbers.get(number - 1);
				winningSet[i][j] = number;
				number++;
			}
		}

		winningSet[n - 1][n - 1] = 0;
		addKeyListener(this);
		setFocusable(true);
	}
	/**
	 * Construct tiles with a given matrix with a given size
	 * @param matrixSize the given size of the matrix
	 * @param matrixArray the given matrix
	 * @param name the name of the player
	 */
	public TilesV1 (int matrixSize, int matrixArray[], String name) {
		n = matrixSize;
		playerName = name;
		
		int delay = 1;
		time = new Timer(delay, this);
		
		tiles = new int[n][n];
		winningSet = new int[n][n];
		
		int number = 1;
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				tiles[i][j] = matrixArray[number - 1];
				winningSet[i][j] = number;
				number++;
			}
		}
		winningSet[n - 1][n - 1] = 0;
		addKeyListener(this);
		setFocusable(true);
	}
	
	/**
	 * This method paint a grid of white rectangles and numbers above each rectangle
	 */
	public void paintComponent(Graphics g) {
	//	super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		width = (int)(getWidth() - 2*PAD)/n;
		height = (int)(getHeight() - 2*PAD)/n;
		fontSize = (height)/2;

		g2.setColor	(Color.RED); 
		
		// Paint a rectangle as a PAD
		g2.fillRect(PAD, PAD, getWidth() - 2*PAD, getHeight() - 2*PAD); 
		
		//Displays the name typed by the player.
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);  // Make numbers smooth
		g2.setFont(new Font("", Font.BOLD, fontSize/3)); 
		g2.setColor(Color.WHITE);
		//Draw the player name at the top of the frame.
		g2.drawString(playerName, 3*getWidth()/7, getHeight()/25);

		for(int i = 0; i < n; i++) {
			int y = PAD + i * height;
			int tempY = y;
			for(int j = 0; j < n; j++) {
				 int x = PAD + j * width;
				 
				// Paint all tiles and number except zero
				if (tiles[i][j] != 0) { 

					g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);  // Make numbers smooth
					g2.setFont(new Font("", Font.BOLD, fontSize));  // Font of the numbers
					
					/**
					 * This function takes care of the movement of the squares individually, 
					 * when a tile is selected for movement, 
					 * this will ensure its movement and nothing else.
					 */
					if (i == zeroIndex1 && j == zeroIndex2) {
						x = x + slide1;
						y = y + slide2;
					}
					
					g2.setColor(Color.WHITE);  
					// Make rectangles (tiles)
					g2.fill3DRect(x, y, width, height, true);  


					if (tiles[i][j] % 2 == 0) {
						g2.setColor(Color.BLUE);
					}
					else {
						g2.setColor(Color.RED); 
					}
					/**
					 * If the number has two digits, set this location
					 * Draw a number on top each rectangle
					 * Else, the number has one digit, set this location
					 */
					if (tiles[i][j]/10 > 0) { //  
						g2.drawString("" + tiles[i][j], x + 2*width/9, y + 2*height/3);  
					}
					else {  
						g2.drawString("" + tiles[i][j], x + 3*width/8, y + 2*height/3);  
					}	
				}
				y = tempY;
		     }
		}
	}
	
	
	/**
	 * Get the zero location, 
	 * a key part of the game, the location of the zero 
	 * in the matrix must always be known.
	 */
	public  void zeroLocation() {
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				if(tiles[i][j] == 0){
					zeroIndex1 = i;
					zeroIndex2 = j;
				}
			}
		}
	}
	/**
	 * The available tile that can move to the right (next to zero) moves to the right.
	 */
	public void right() {
		zeroLocation();
		if (zeroIndex2 > 0) {
			zeroIndex2 = zeroIndex2 - 1;
			xMovement = 3;
			yMovement = 0;
		}
	}
	/**
	 * The available tile that can move to the left (next to the zero) moves to the left.
	 */
	public void left() {
		zeroLocation();
		if (zeroIndex2 < n - 1) {
			zeroIndex2 = zeroIndex2 + 1;
			xMovement = -3;
			yMovement = 0;
		}
	}
	/**
	 * The available tile that can move up (next to the zero) moves up.
	 */
	public void up() {
		zeroLocation();
		if (zeroIndex1 < n - 1) {
			zeroIndex1 = zeroIndex1 + 1;
			xMovement = 0;
			yMovement = -3;
		}
	}
	
	/**
	 * The available tile that can move down (next to the zero) moves down.
	 */
	public void down() {
		zeroLocation();
		if (zeroIndex1 > 0) {
			zeroIndex1 = zeroIndex1 - 1;
			xMovement = 0;
			yMovement = 3;
		}
	}
	
	/**
	 * Determines if the game has been won or not.
	 * Compare if the tiles array is equals to winningSet array
	 */
	public void theWin() {
		System.out.println("It Works");
		if(Arrays.deepEquals(tiles, winningSet)) {
			JOptionPane.showMessageDialog(null, playerName + " has win the game");
			System.exit(0);
		}
	}
	
	public void checkState() {
		if(zeroIndex1 == n - 1 && zeroIndex2 == n - 1)
			//System.out.println("it works");
			theWin();
	}
	/**
	 * This method perform each movement of the tiles and repaint each one.
	 * The timer method continuously executes this method until the timer is stopped.
	 */
	public void actionPerformed (ActionEvent e) {
			slide1 = slide1 + xMovement;
			slide2 = slide2 + yMovement;
			repaint();
			if(slide1%(width/3) == 0 & slide2%(height/3) == 0) {
				time.stop();
				slide1 = 0;
				slide2 = 0;
				state = false;
		
				if(xMovement == 3) {
					zeroLocation();
					if( zeroIndex2 > 0) {
						int temp = tiles[zeroIndex1][zeroIndex2 - 1];
						tiles[zeroIndex1][zeroIndex2 - 1] = 0;
						tiles[zeroIndex1][zeroIndex2] = temp;
						//theWin();
						xMovement = 0;
						yMovement = 0;
					}
				}
				
				else if(xMovement == -3) {
					zeroLocation();
					if(zeroIndex2 < n - 1) {
						int temp = tiles[zeroIndex1][zeroIndex2 + 1];
						tiles[zeroIndex1][zeroIndex2 + 1] = 0;
						tiles[zeroIndex1][zeroIndex2] = temp;
						xMovement = 0;
						yMovement = 0;
						//theWin();
					}
				}
				
				else if(yMovement == -3){
					zeroLocation();
					if(zeroIndex1 < n - 1){
						int temp = tiles[zeroIndex1 + 1][zeroIndex2];
						tiles[zeroIndex1 + 1][zeroIndex2] = 0;
						tiles[zeroIndex1][zeroIndex2] = temp;
						xMovement = 0;
						yMovement = 0;
						//theWin();
					}
				}
				
				else if(yMovement == 3){
					zeroLocation();
					if(zeroIndex1 > 0){
						int temp = tiles[zeroIndex1 - 1][zeroIndex2];
						tiles[zeroIndex1 - 1][zeroIndex2] = 0;
						tiles[zeroIndex1][zeroIndex2] = temp;
						xMovement = 0;
						yMovement = 0;
						//theWin();
					}
				}
				
			}
			checkState();
	}

	@Override
	/**
	 * This method implements the key listener, if you press any of the arrow keys the tiles moves,
	 * also if you press Alt and F4 the game will be closed
	 */
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(!state) {
			int keyCode = e.getKeyCode();
			int keyCodeF4 = e.getKeyCode();
		
			if (keyCode == KeyEvent.VK_UP) {
				up();
				time.start();   // Start the time
				state = true;
			}
			else if (keyCode == KeyEvent.VK_DOWN) {
				down();
				time.start();   // Start the time
				state = true;
			}
			else if (keyCode == KeyEvent.VK_RIGHT) {
				right();
				time.start();   // Start the time
				state = true;
			}
			else if (keyCode == KeyEvent.VK_LEFT) {
				left();
				time.start();   // Start the time
				state = true;
			}
			if ((keyCode | keyCodeF4) == (KeyEvent.VK_ALT | KeyEvent.VK_F4)) {
				JOptionPane.showMessageDialog(null, "Shame on you!");
				System.exit(0);
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
