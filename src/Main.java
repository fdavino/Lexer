import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;


public class Main {

	private static final String RELOP = "relop";
	private static final String ID = "char";
	private static final String KEYWORD = "keyword";
	private static final String CONST = "const";

	private static final String LE = "LE";
	private static final String NE = "NE";
	private static final String LT = "LT";
	private static final String EQ = "EQ";
	private static final String GE = "GE";
	private static final String GT = "GT";

	private static final String IF = "IF";
	private static final String THEN = "THEN";
	private static final String ELSE = "ELSE";
	private static final String INT = "INT";


	private static final Token ERROR = new Token("Err","Err");
	private static final Token APPEND = new Token("Append", "Append");
	
	private static final char NULL = '#';

	private static ArrayList<String> keywordMap;
	private static ArrayList<String> symbolMap;
	private static int symbolCounter = 0;

	private static BufferedReader sc;
	private static boolean star;
	private static boolean stop;
	private static char c;
	private static String s;
	private static int state;


	public static void main(String[] args){

		File f = new File("src\\input.txt");
		try {
			sc = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		symbolMap = new ArrayList<>();
		keywordMap = new ArrayList<>();
		star = false;
		stop = false;

		keywordMap.add(IF);
		keywordMap.add(THEN);
		keywordMap.add(ELSE);
		keywordMap.add(INT);
		
		getTokens();

	}

	private static void getTokens(){

		while(!stop){
			Token toPrint = relop(); 
			if (toPrint != APPEND)
				System.out.println(toPrint.toString());
		}
	}

	private static boolean digitLex(char input){
		String numbers = "0123456789";
		return numbers.contains(input+"");
	}

	private static boolean letterLex(char input){
		int c = input;
		return ((c >= 65 && c <= 90) || (c >= 97 && c <= 122));
	}

	private static Token relop(){
		
		Token toReturn = APPEND;
		
		switch(state){
		case 0:
			
			c = readNext();
			if(c == NULL)
				toReturn = ERROR;
			
			if(c == '<')
				state = 1;
			else if(c == '=')
				state = 5;
			else if(c == '>')
				state = 6;
			else 
				break;
			//else
				// state = #inizio nuovo DFA
			break;
			
		case 1:
			
			c = readNext();
			if(c == NULL)
				toReturn = ERROR;
			
			if(c == '=')
				state = 2;
			else if(c == '>')
				state = 3;
			else 
				state = 4;
			
			break;
			
		case 2:
			state = 0;
			toReturn = new Token(RELOP,LE);
			break;
			
		case 3:
			state = 0;
			toReturn = new Token(RELOP, NE);
			break;
			
		case 4:
			state = 0;
			star = true;
			toReturn = new Token(RELOP,LT);
			break;
			
		case 5:
			state = 0;
			toReturn = new Token(RELOP,EQ);
			break;
			
		case 6:
			c = readNext();
			if(c == NULL)
				toReturn = ERROR;
			
			if(c == '=')
				state = 7;
			else
				state = 8;
			break;
			
		case 7:
			state = 0;
			toReturn = new Token(RELOP, GE);
			break;
			
		case 8:
			state = 0;
			star = true;
			toReturn = new Token(RELOP, GT);
			break;
		}
		
		return toReturn;
		
	}





	private static char readNext(){
		if(star){
			star = false;
			return c;
		}
		else{
			try {
				if((c=(char) sc.read())!=-1)
					return (char) c;
				else{
					stop = true;
					return NULL;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return NULL;
		}
	}



}
