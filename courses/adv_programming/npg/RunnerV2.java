import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.util.Scanner;
/**
 * The RunnerV2 class input the name of a player, the size of the matrix and runs the TilesV2 class
 * @author Cesar && Eduardo
 */
public class RunnerV2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner input = new Scanner(System.in);

		String gameNumber;
		String playerName1;
		String playerName2;
		int option;
		int size;
		String tempSize;
		String sizeIfNo;
		int[] array = null;
		TilesV2 tiles1 = null;
		int amountOfPlayers;

		/**
		 * Prompts the end user for the details of the game: Name of the player, size of matrix
		 * and whether or not the matrix is self, randomly, generated or created by the end user. 
		 */
		amountOfPlayers = JOptionPane.showConfirmDialog(null, "Two player mode?", "Game Type", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if(amountOfPlayers == JOptionPane.YES_OPTION){
			playerName1 = JOptionPane.showInputDialog(null, "Enter player 1 name");
			playerName2 = JOptionPane.showInputDialog(null, "Enter player 2 name");
			option = JOptionPane.showConfirmDialog(null, "Randomly generated game?", "Welcome", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(option == JOptionPane.YES_OPTION){
				do {
					gameNumber = JOptionPane.showInputDialog(null, "Enter the size between 3 and 6 for the NPG");
					size = Integer.parseInt(gameNumber);
				}
				while (size<3 || 6<size);
				tiles1 = new TilesV2(size, playerName1, playerName2);		

			}
			/**
			 * Takes the input as a string and arranges each part (according to instructions)
			 * in an array used to construct an object of the class TilesV1
			 */
			else {
				System.out.println("Enter the size between 3 and 6 for the NPG and then enter the numbers of the matrix");
				sizeIfNo = input.nextLine();
				tempSize = sizeIfNo.substring(7,8);
				size = Integer.parseInt(tempSize);

				sizeIfNo = sizeIfNo.substring(10);
				sizeIfNo = sizeIfNo.replaceAll(",", "");
				sizeIfNo = sizeIfNo.trim();
				String[] numbers = sizeIfNo.split(" ");

				array = new int[(int) Math.pow(size, 2)];

				for (int i = 0; i < numbers.length; i++) {
					array[i] = Integer.parseInt(numbers[i]);
				}
				tiles1 = new TilesV2(size, array, playerName1, playerName2);			

			}

			/**
			 * Creates the frame where the objects are placed
			 */
			JFrame frame1 = new JFrame();

			frame1.setSize(1000,500);
			frame1.setResizable(false);
			//frame1.setUndecorated(true);
			frame1.setLocation(100,100);
			frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame1.getContentPane().setBackground(Color.RED.darker());

			frame1.add(tiles1);

			frame1.setVisible(true);
		}

	else {
		

		String name;
		TilesV1 tiles = null;
		/**
		 * Prompts the end-user for the details of the game: Name of the player, size of the matrix
		 * and whether or not the matrix is self, randomly, generated or created by the end-user.
		 */
		name = JOptionPane.showInputDialog(null, "Enter player name");
		option = JOptionPane.showConfirmDialog(null, "Randomly generated game?", "Welcome", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(option == JOptionPane.YES_OPTION){
			do {
				gameNumber = JOptionPane.showInputDialog(null, "Enter the size between 3 and 6, for the NPG");
				size = Integer.parseInt(gameNumber);
			}
			while (size<3 || 6<size);
			tiles = new TilesV1(size, name);
		}
		/**
		 * Takes the input as a string and arranges each part (according to instructions)
		 * in an array used to construct an object of the class TilesV1.
		 */
		else {

			System.out.println("Enter the size between 3 and 6 for the cubic matrix and then enter the numbers of the matrix");
			sizeIfNo = input.nextLine();
			tempSize = sizeIfNo.substring(7,8);
			size = Integer.parseInt(tempSize);

			sizeIfNo = sizeIfNo.substring(10);
			sizeIfNo = sizeIfNo.replaceAll(",", "");
			sizeIfNo = sizeIfNo.trim();
			String[] numbers = sizeIfNo.split(" ");

			array = new int[(int) Math.pow(size, 2)];

			for (int i = 0; i < numbers.length; i++) {
				array[i] = Integer.parseInt(numbers[i]);
			}
			tiles = new TilesV1(size, array, name);
		}
		/**
		 * Creates the frame where the objects are placed. 
		 */
		JFrame frame = new JFrame();
		frame.setSize(500,500);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.setLocation(300,100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.RED.darker());
		frame.add(tiles);
		frame.setVisible(true);
	}
	}
}



