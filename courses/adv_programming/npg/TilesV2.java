import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
/**
 * The TilesV2 class creates tiles that are placed on the frame for the game, basically almost all game controls are
 * carried out here
 * @author Eduardo && Cesar
 */
public class TilesV2 extends JComponent implements KeyListener, ActionListener  {

	private int tilesA[][];
	private int tilesB[][];
	private int winningSet[][];
	private int n;
	private String playerName1;
	private String playerName2;
	private ArrayList<Integer> numbers;
	private Timer time1;
	private Timer time2;
	private int zeroIndex1A, zeroIndex2A, zeroIndex1B, zeroIndex2B;
	private int slide1A = 0, slide2A = 0, slide1B = 0, slide2B = 0;
	private int xMovementA = 0, yMovementA = 0, xMovementB = 0, yMovementB = 0;
	private boolean stateA = false, stateB = false;
	
	private int width;
	private int height;
	private static int fontSize;
	private static final int PAD = 25;

	/**
	 * Construct random tiles with the size of the entered matrix
	 * @param matrixSize the size of matrix
	 * @param name the name of the player
	 */
	public TilesV2 (int matrixSize, String name1, String name2){
		n = matrixSize;
		playerName1 = name1;
		playerName2 = name2;

		int delay = 2;
		time1 = new Timer(delay, this);
		time2 = new Timer(delay,this);
		numbers = new ArrayList<Integer>();
		for(int i = 0; i <= Math.pow(n, 2) - 1; i++) {
			numbers.add(i);
		}
		
		Collections.shuffle(numbers);

		tilesA = new int[n][n];
		tilesB = new int[n][n];
		winningSet = new int[n][n];

		int number = 1;
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				tilesA[i][j] = numbers.get(number - 1);
				tilesB[i][j] = numbers.get(number - 1);
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
	public TilesV2 (int matrixSize, int matrixArray[], String name1, String name2) {
		n = matrixSize;
		playerName1 = name1;
		playerName2 = name2;
		

		int delay = 2;
		time1 = new Timer(delay, this);
		time2 = new Timer(delay, this);

		tilesA = new int[n][n];
		tilesB = new int[n][n];
		winningSet = new int[n][n];

		int number = 1;
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				tilesA[i][j] = matrixArray[number - 1];
				tilesB[i][j] = matrixArray[number - 1];
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
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		width = (int) (((int)(getWidth() - 2*PAD)/n)/2.1);
		height = (int)(getHeight() - 2*PAD)/n;
		fontSize = (height)/2;

		g2.setColor	(Color.RED); 
		// Paint a rectangle as a PAD
		g2.fillRect(PAD, PAD, getWidth() - 2*PAD, getHeight() - 2*PAD - 1); 

		// Make numbers smooth
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// Set the size of the font for the name of the player
		g2.setFont(new Font("", Font.BOLD, fontSize/3));   
		g2.setColor(Color.WHITE);
	
		// Draw the player 1 name at the top of the frame
		g2.drawString(playerName1, 22*getWidth()/100, getHeight()/25);  
		// Draw the player 2 name at the top of the frame
		g2.drawString(playerName2, 72*getWidth()/100, getHeight()/25);  
		
		/**
		 * Paint the tiles for the first player
		 */
		
		for(int i = 0; i < n; i++) {
			int y1 = PAD + i * height;
			int tempY = y1;
			for(int j = 0; j < n; j++) {
				int x1 = PAD + j * width;


				// Paint all tiles and number except zero
				if (tilesA[i][j] != 0) { 

					// Make numbers smooth
					g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);  
					// Font of numbers
					g2.setFont(new Font("", Font.BOLD, fontSize));  

					/**
					 * This function takes care of the movement of the square individually, when a tile is selected for movement, 
					 * this will ensure its movement and nothing else. 
					 */
					if (i == zeroIndex1A && j == zeroIndex2A) { 
						x1 = x1 + slide1A;
						y1 = y1 + slide2A;
					}

					g2.setColor(Color.WHITE);  
					// Make rectangles (tiles)
					g2.fill3DRect(x1, y1, width, height, true);  


					if (tilesA[i][j] % 2 == 0) {
						g2.setColor(Color.BLUE);
					}
					else {
						g2.setColor(Color.RED); 
					}

					/**
					 * If the number has two digits, set this location
					 * Draw a number on the top each rectangle
					 * Else, the number has one digit, set this location
					 */
					if (tilesA[i][j]/10 > 0) {   
						g2.drawString("" + tilesA[i][j], x1 + 2*width/9, y1 + 2*height/3);  
					}
					else { 
						g2.drawString("" + tilesA[i][j], x1 + 3*width/8, y1 + 2*height/3);   
					}	
				}

				y1 = tempY;
			}
		}

		//Paint the PAD between two tiles group
		g2.setColor(Color.RED.darker());
		g2.fillRect(9*getWidth()/19, 0, 2*PAD + 1, getHeight());	
		
		/**
		 * Paint the tiles for the second player
		 */
		for(int i = 0; i < n; i++) {
			int y2 = PAD + i * height;
			int tempY = y2;
			for(int j = 0; j < n; j++) {
				int x2 = PAD + j * width + (getWidth())/2;

				// Paint all tiles and number except zero
				if (tilesB[i][j] != 0) { 

					// Make numbers smooth
					g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);  
					// Font of numbers
					g2.setFont(new Font("", Font.BOLD, fontSize));  

					/**
					 * This function takes care of the movement of the square individually, when a tile is selected for movement, 
					 * this will ensure its movement and nothing else. 
					 */
					if (i == zeroIndex1B && j == zeroIndex2B) { 
						x2 = x2 + slide1B;
						y2 = y2 + slide2B;
					}

					g2.setColor(Color.WHITE);  
					// Make rectangles (tiles)
					g2.fill3DRect(x2, y2, width, height, true);  


					if (tilesB[i][j] % 2 == 0) {
						g2.setColor(Color.BLUE);
					}
					else {
						g2.setColor(Color.RED); 
					}

					/**
					 * If the number has two digits, set this location
					 * Draw a number on the top each rectangle
					 * Else, the number has one digit, set this location
					 */
					if (tilesB[i][j]/10 > 0) {   
						g2.drawString("" + tilesB[i][j], x2 + 2*width/9, y2 + 2*height/3);  
					}
					else { 
						g2.drawString("" + tilesB[i][j], x2 + 3*width/8, y2 + 2*height/3);   
					}	
				}

				y2 = tempY;
			}
		}
	}

	/**
	 * Get the zero location of tilesA of,
	 * a key part of the game, the location of the zero,
	 * in the matrix must always be known.
	 */
	public  void zeroLocationA() {
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				if(tilesA[i][j] == 0){
					zeroIndex1A = i;
					zeroIndex2A = j;
				}
			}
		}
	}
	
	/**
	 * Get the zero location of tilesB of
	 * a key part of the game, the location of the zero,
	 * in the matrix must always be known.
	 */
	public  void zeroLocationB() {
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				if(tilesB[i][j] == 0){
					zeroIndex1B = i;
					zeroIndex2B = j;
				}
			}
		}
	}
	
	/**
	 * The available tile in tilesA that can move to the right (next to zero) moves to the right.
	 */
	public void rightA() {
		zeroLocationA();
		if (zeroIndex2A > 0) {
			zeroIndex2A = zeroIndex2A - 1;
			xMovementA = 3;
			yMovementA = 0;
		}
	}
	
	/**
	 * The available tile in tilesA that can move to the left (next to the zero) moves to the left.
	 */
	public void leftA() {
		zeroLocationA();
		if (zeroIndex2A < n - 1) {
			zeroIndex2A = zeroIndex2A + 1;
			xMovementA = -3;
			yMovementA = 0;
		}
	}
	
	/**
	 * The available tile in tilesA that can move up (next to the zero) moves up.
	 */
	public void upA() {
		zeroLocationA();
		if (zeroIndex1A < n - 1) {
			zeroIndex1A = zeroIndex1A + 1;
			xMovementA = 0;
			yMovementA = -3;
		}
	}
	
	/**
	 * The available tile in tilesA that can move down (next to the zero) moves down.
	 */
	public void downA() {
		zeroLocationA();
		if (zeroIndex1A > 0) {
			zeroIndex1A = zeroIndex1A - 1;
			xMovementA = 0;
			yMovementA = 3;
		}
	}
	
	
	/**
	 * The available tile in tilesB that can move to the right (next to zero) moves to the right.
	 */	
	public void rightB() {
		zeroLocationB();
		if (zeroIndex2B > 0) {
			zeroIndex2B = zeroIndex2B - 1;
			xMovementB = 3;
			yMovementB = 0;
		}
	}
	
	/**
	 * The available tile in tilesB that can move to the left (next to the zero) moves to the left.
	 */
	public void leftB() {
		zeroLocationB();
		if (zeroIndex2B < n - 1) {
			zeroIndex2B = zeroIndex2B + 1;
			xMovementB = -3;
			yMovementB = 0;
		}
	}
	
	
	/**
	 * The available tile in tilesB that can move up (next to the zero) moves up.
	 */
	public void upB() {
		zeroLocationB();
		if (zeroIndex1B < n - 1) {
			zeroIndex1B = zeroIndex1B + 1;
			xMovementB = 0;
			yMovementB = -3;
		}
	}
	/**
	 * The available tile in tilesB that can move down (next to the zero) moves down.
	 */
	public void downB() {
		zeroLocationB();
		if (zeroIndex1B > 0) {
			zeroIndex1B = zeroIndex1B - 1;
			xMovementB = 0;
			yMovementB = 3;
		}
	}

	/**
	 * Determines if player 1 has been won or not.
	 * Compare if the tiles array is equals to winningSet array
	 */
	public void theWinA() {
		if(Arrays.deepEquals(tilesA, winningSet)) {   
			JOptionPane.showMessageDialog(null, playerName1 + " has won the game");
			System.exit(0);
		}		
	}
	
	/**
	 * Determines if player 2 has been won or not.
	 * Compare if the tiles array is equals to winningSet array
	 */
	public void theWinB() {
		if(Arrays.deepEquals(tilesB, winningSet)) {   
			JOptionPane.showMessageDialog(null, playerName2 + " has won the game");
			System.exit(0);
		}
	}
	
	/**
	 * This method perform each movement of the tiles and repaint each one.
	 * The timer method continuously executes this method until the timer is stopped.
	 */
	public void actionPerformed (ActionEvent e) {
		
		repaint();

		if (stateA) {
			slide1A = slide1A + xMovementA;
			slide2A = slide2A + yMovementA;
			
			if(slide1A%(width/3) == 0 & slide2A%(height/3) == 0) {
				time2.stop();
				slide1A = 0;
				slide2A = 0;
				stateA = false;

				if(xMovementA == 3) {
					zeroLocationA();
					if( zeroIndex2A > 0) {
						int temp = tilesA[zeroIndex1A][zeroIndex2A - 1];
						tilesA[zeroIndex1A][zeroIndex2A - 1] = 0;
						tilesA[zeroIndex1A][zeroIndex2A] = temp;
						xMovementA = 0;
						yMovementA = 0;
						theWinA();
					}
				}

				else if(xMovementA == -3) {
					zeroLocationA();
					if(zeroIndex2A < n - 1) {
						int temp = tilesA[zeroIndex1A][zeroIndex2A + 1];
						tilesA[zeroIndex1A][zeroIndex2A + 1] = 0;
						tilesA[zeroIndex1A][zeroIndex2A] = temp;
						xMovementA = 0;
						yMovementA = 0;
						theWinA();
					}
				}

				else if(yMovementA == -3){
					zeroLocationA();
					if(zeroIndex1A < n - 1){
						int temp = tilesA[zeroIndex1A + 1][zeroIndex2A];
						tilesA[zeroIndex1A + 1][zeroIndex2A] = 0;
						tilesA[zeroIndex1A][zeroIndex2A] = temp;
						xMovementA = 0;
						yMovementA = 0;
						theWinA();
					}
				}

				else if(yMovementA == 3){
					zeroLocationA();
					if(zeroIndex1A > 0){
						int temp = tilesA[zeroIndex1A - 1][zeroIndex2A];
						tilesA[zeroIndex1A - 1][zeroIndex2A] = 0;
						tilesA[zeroIndex1A][zeroIndex2A] = temp;
						xMovementA = 0;
						yMovementA = 0;
						theWinA();
					}
				}
			}
		}
	
	//=======================================================================
		
		if (stateB) {
			slide1B = slide1B + xMovementB;
			slide2B = slide2B + yMovementB;

			if(slide1B%(width/3) == 0 & slide2B%(height/3) == 0) {
				time1.stop();
				slide1B = 0;
				slide2B = 0;
				stateB = false;

				if(xMovementB == 3) {
					zeroLocationB();
					if( zeroIndex2B > 0) {
						int temp = tilesB[zeroIndex1B][zeroIndex2B - 1];
						tilesB[zeroIndex1B][zeroIndex2B - 1] = 0;
						tilesB[zeroIndex1B][zeroIndex2B] = temp;
						xMovementB = 0;
						yMovementB = 0;
						theWinB();
					}
				}

				else if(xMovementB == -3) {
					zeroLocationB();
					if(zeroIndex2B < n - 1) {
						int temp = tilesB[zeroIndex1B][zeroIndex2B + 1];
						tilesB[zeroIndex1B][zeroIndex2B + 1] = 0;
						tilesB[zeroIndex1B][zeroIndex2B] = temp;
						xMovementB = 0;
						yMovementB = 0;
						theWinB();
					}
				}

				else if(yMovementB == -3){
					zeroLocationB();
					if(zeroIndex1B < n - 1){
						int temp = tilesB[zeroIndex1B + 1][zeroIndex2B];
						tilesB[zeroIndex1B + 1][zeroIndex2B] = 0;
						tilesB[zeroIndex1B][zeroIndex2B] = temp;
						xMovementB = 0;
						yMovementB = 0;
						theWinB();
					}
				}

				else if(yMovementB == 3){
					zeroLocationB();
					if(zeroIndex1B > 0){
						int temp = tilesB[zeroIndex1B - 1][zeroIndex2B];
						tilesB[zeroIndex1B - 1][zeroIndex2B] = 0;
						tilesB[zeroIndex1B][zeroIndex2B] = temp;
						xMovementB = 0;
						yMovementB = 0;
						theWinB();
					}
				}
			}
		}
	}

	@Override
	/**
	 * This method implements the key listener, if you press any of the arrow keys the tiles moves,
	 * also if you press Alt and F4 the game will be closed
	 */
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		int keyCode1 = e.getKeyCode();
		int keyCodeF4 = e.getKeyCode();
		
		if(!stateA) {

			if (keyCode1 == KeyEvent.VK_W) {
				upA();
				time2.start();   // Start the time
				stateA = true;
			}
			else if (keyCode1 == KeyEvent.VK_S) {
				downA();
				time2.start();   // Start the time
				stateA = true;
			}
			else if (keyCode1 == KeyEvent.VK_D) {
				rightA();
				time2.start();   // Start the time
				stateA = true;
			}
			else if (keyCode1 == KeyEvent.VK_A) {
				leftA();
				time2.start();   // Start the time
				stateA = true;
			}
			else if ((keyCode1 | keyCodeF4) == (KeyEvent.VK_ALT | KeyEvent.VK_F4)) {
				JOptionPane.showMessageDialog(null, "Shame on you!");
				System.exit(0);
			}
		}
		
		if(!stateB) {

			if (keyCode1 == KeyEvent.VK_UP) {
				upB();
				time1.start();   // Start the time
				stateB = true;
			}
			else if (keyCode1 == KeyEvent.VK_DOWN) {
				downB();
				time1.start();   // Start the time
				stateB = true;
			}
			else if (keyCode1 == KeyEvent.VK_RIGHT) {
				rightB();
				time1.start();   // Start the time
				stateB = true;
			}
			else if (keyCode1 == KeyEvent.VK_LEFT) {
				leftB();
				time1.start();   // Start the time
				stateB = true;
			}
			else if ((keyCode1 | keyCodeF4) == (KeyEvent.VK_ALT | KeyEvent.VK_F4)) {
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


