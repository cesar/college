import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;


public class LexAnalyzer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		long start = System.currentTimeMillis();
		//Variables and RegEx patterns

		FileReader file = null;
		String word = "";
		String currentLetter;
		String currentState = "start";
		int spaces = 0;
		BufferedWriter output = null;

		//Creating the RegEx to match strings later
		// An identifier can either being with a letter or an underscore, after that it can also contain digits. 
		String letterBegin = "[a-zA-Z|\\_]";
		String letter = "[a-zA-Z0-9|\\_]";
		//White spaces for INDENTS AND DEDENTS
		String whiteSpace = " ";
		//String Literals
		String stringLiterals = "\"|\'|\'\'\'|\"\"\"";
		//Numerical String Literal
		String numericalLiteral = "[0-9a-zA-Z|\\.]";

		//Keep track of INDENTS and DEDENTS
		Stack<Integer> indentation = new Stack<Integer>();
		indentation.push(0);
		//Initiate the Delimiters
		Map<String, String> delimiter = new HashMap<String, String>();
		try{
			Scanner theMeat = new Scanner(new FileReader("delimiter.txt"));
			while(theMeat.hasNextLine()){
				delimiter.put(theMeat.nextLine(), "DELIMITER");
			}
		}

		catch(FileNotFoundException e){
			e.printStackTrace();
		}

		//Initiate the Operations
		Map<String, String> operators = new HashMap<String, String>();
		try{
			Scanner theMeat = new Scanner(new FileReader("operator.txt"));
			while(theMeat.hasNextLine()){
				operators.put(theMeat.nextLine(), "OPERATOR");
			}
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}

		//Initiate the Keywords
		Map<String, String> keywords = new HashMap<String, String>();
		try{
			Scanner theMeat = new Scanner(new FileReader("keywords.txt"));
			while(theMeat.hasNextLine()){
				keywords.put(theMeat.nextLine(), "KEYWORD");
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}

		//Get the source code
		try {
			file = new FileReader("input.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner input = new Scanner(file);
		//Setup an Output file.
		try{
			output = new BufferedWriter(new FileWriter("outputJava.txt"));
			//Analyze each line of the source code.
			while(input.hasNextLine()){
				String currentLine = input.nextLine() + " ";
				for(int i = 0; i < currentLine.length(); i++){
					char current = currentLine.charAt(i);
					currentLetter = ""+current;
					//Comments
					if(currentLetter.matches("#")){
						currentState = "start";
						break;
					}
					//INDENTIFIER && KEYWORDS
					else if(currentState.equals("start") && currentLetter.matches(letterBegin)){
						word+= currentLetter;
						currentState = "A";
						continue;					
					}
					else if(currentState.equals("start") && i == currentLine.length() - 1){
						continue;
					}
					else if(currentState.equals("A") && currentLetter.matches(letter)){
						word += currentLetter;
						continue;
					}
					else if(currentState.equals("A") && currentLetter.matches(stringLiterals)){
						word += currentLetter;
						currentState = "F";
						continue;
					}
					else if(currentState.equals("A") && (currentLetter.matches(whiteSpace) || !currentLetter.matches(letter))){
						if(keywords.containsKey(word)){
							output.write(keywords.get(word) + " "+word + "\n");
							currentState = "start";
							if(!currentLetter.matches(whiteSpace)){
								i--;
							}
							word = "";
							continue;
						}
						else if(!keywords.containsKey(word)){
							output.write("IDENTIFIER " + word + "\n");
							currentState = "start";
							if(!currentLetter.matches(whiteSpace)){
								i--;
							}
							word = "";
							continue;
						}
					}
					//OPERATORS && DELIMITERS
					else if(currentState.equals("start") && (!currentLetter.matches(letter) && !currentLetter.matches(whiteSpace) && !currentLetter.matches(stringLiterals))){
						word += currentLetter;
						currentState = "B";
						continue;
					}
					else if(currentState.equals("B") && (!currentLetter.matches(letter) && !currentLetter.matches(whiteSpace) && !currentLetter.matches(stringLiterals))){
						word+= currentLetter;
						if(delimiter.containsKey(word + "" +currentLine.charAt(i + 1))){
							output.write(delimiter.get(word + "" +currentLine.charAt(i + 1)) + " " + word + "" +currentLine.charAt(i + 1) + "\n");
							word = "";
							i++;
							currentState = "start";
							continue;
						}
						else if(operators.containsKey(word + "" +currentLine.charAt(i + 1))){
							output.write(operators.get(word + "" +currentLine.charAt(i + 1)) + " " + word + "" +currentLine.charAt(i + 1) + "\n");
							word = "";
							i++;
							currentState = "start";
							continue;
						}
						else if(delimiter.containsKey(word)){
							output.write(delimiter.get(word) + " " + word + "\n");
							word = "";
							currentState = "start";
							continue;
						}
						else if(operators.containsKey(word)){
							output.write(operators.get(word) + " " + word + "\n");
							word = "";
							currentState = "start";
							continue;
						}
						else if(delimiter.containsKey(word.substring(0, 1))){
							output.write(delimiter.get(word.subSequence(0, 1)) + " " +word.subSequence(0, 1) + "\n");
							word = "";
							currentState = "start";
							i--;
							continue;
						}
						else if(operators.containsKey(word.substring(0, 1))){
							output.write(operators.get(word.subSequence(0, 1)) + " " + word.subSequence(0, 1) + "\n");
							word = "";
							currentState = "start";
							i--;
							continue;
						}
					}
					else if(currentState.equals("B") && (currentLetter.matches(letter) || currentLetter.matches(whiteSpace) || currentLetter.matches(stringLiterals))){
						if(delimiter.containsKey(word)){
							output.write(delimiter.get(word) + " " + word + "\n");
							word = "";
							i--;
							currentState = "start";
							continue;
						}
						else if(operators.containsKey(word)){
							output.write(operators.get(word) + " " + word + "\n");
							word = "";
							i--;
							currentState = "start";
							continue;
						}
					}
					//LITERALS
					else if(currentState.equals("start") && currentLetter.matches(stringLiterals)){
						word += currentLetter;
						currentState = "F";
						continue;
					}
					else if(currentState.equals("F") && currentLetter.matches("\\\\")){
						i++;
						continue;
					}
					else if(currentState.equals("F") && (currentLetter.matches(letter) || (!currentLetter.matches(letter) && !currentLetter.matches(stringLiterals)))){
						word += currentLetter;
						continue;
					}
					else if(currentState.equals("F") && (currentLetter.matches(stringLiterals) && currentLetter.matches(""+word.charAt(0)))){
						word+=currentLetter;
						output.write("LITERAL " + word + "\n");
						currentState = "start";
						word = "";
						continue;
					}
					else if(currentState.equals("start") && currentLetter.matches(numericalLiteral)){
						word += currentLetter;
						currentState = "E";
						continue;
					}
					else if(currentState.equals("E") && (currentLetter.matches(numericalLiteral) || currentLetter.matches(letter))){
						word += currentLetter;
						continue;
					}
					else if(currentState.equals("E") && currentLetter.matches("\\+|\\-")){
						if(currentLine.charAt(i - 1) == 'e'){
							word += currentLetter;
							continue;
						}
						else{
							output.write("LITERAL " + word + "\n");
							word = "";
							i--;
							currentState = "start";
							continue;
						}
					}
					else if(currentState.equals("E") && (!currentLetter.matches(numericalLiteral))){
						output.write("LITERAL " + word + "\n");
						i--;
						word = "";
						currentState = "start";
						continue;
					}

					//INDENTS && DEDENT
					else if(currentState.equals("start") && (currentLetter.matches(whiteSpace) && i == 0)){
						currentState = "D";
						spaces++;
						continue;
					}
					else if(currentState.equals("D") && currentLetter.matches(whiteSpace)){
						spaces++;
						continue;
					}
					else if(currentState.equals("D") && (!currentLetter.matches(whiteSpace))){
						if(indentation.peek() <= spaces){
							indentation.push(spaces);
							output.write("INDENT \n");
							currentState = "start";
							spaces = 0;
							i--;
							continue;
						}
						else if(indentation.peek() > spaces){
							indentation.pop();
							output.write("DEDENT \n");
							currentState = "start";
							spaces = 0;
							i--;
							continue;
						}
					}

				}
				output.write("NEWLINE \n");
			}
			output.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		long finish = System.currentTimeMillis();
		System.out.println((finish - start)/1000.0);
	}
}
