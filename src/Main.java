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
	private static final String ID = "id";
	private static final String KEYWORD = "keyword";
	private static final String CONST = "const";
	private static final String SEPARATOR = "separator";
	private static final String BLANCK = "blanck";
	private static final String BRACKET = "braket";

	private static final String LE = "LE";
	private static final String NE = "NE";
	private static final String LT = "LT";
	private static final String EQ = "EQ";
	private static final String GE = "GE";
	private static final String GT = "GT";
	private static final String ASS = "ASS";

	private static final String IF = "IF";
	private static final String THEN = "THEN";
	private static final String ELSE = "ELSE";
	private static final String INT = "INT";

	private static final String ROUNDOP = "ROUNDOP";
	private static final String ROUNDCL = "ROUNDCL";
	private static final String SQUAREOP = "SQUAREOP";
	private static final String SQUARECL = "SQUARECL";
	private static final String CURLYOP = "CURLYOP";
	private static final String CURLYCL = "CURLYCL";
	
	private static final Token ERROR = new Token("Err","Err");
	private static final Token APPEND = new Token("Append", "Append");
	private static final char NULL = '#';

	private static final int A = 65;
	private static final int Z = 90;
	private static final int a = 97;
	private static final int z = 122;

	private static HashMap<String, Token> keywordMap;
	private static ArrayList<String> symbolMap;
	private static int symbolCounter = 0;

	private static BufferedReader sc;
	private static boolean star;
	private static boolean stop;
	private static boolean last;
	private static char c;
	private static String s;
	private static int state;

	public static void main(String[] args){

		File f = new File("src\\input.txt");
		try {
			sc = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		symbolMap = new ArrayList<>();
		keywordMap = new HashMap<>();
		star = false;
		stop = false;
		last = false;

		keywordMap.put(IF,new Token(KEYWORD,IF));
		keywordMap.put(THEN,new Token(KEYWORD,THEN));
		keywordMap.put(ELSE,new Token(KEYWORD,ELSE));
		keywordMap.put(INT,new Token(KEYWORD,INT));

		getTokens();

	}

	private static void getTokens(){

		while(!stop){
			//for(int i = 0; i < 70; i++){
			Token toPrint = relop(); 
			if (toPrint != APPEND /*&& toPrint.getClasse()!="blanck"*/)
				System.out.println(toPrint.toString());
			if (toPrint.getClasse() == ID ||  
					toPrint.getClasse() == CONST){
				System.out.println("___"+symbolMap.get(Integer.parseInt(toPrint.getOp())));
			}
		}
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
			else{
				star = true;
				state = 9;
			}
			break;

		case 1:

			c = readNext();
			if(c == NULL)
				toReturn = ERROR;

			if(c == '=')
				state = 2;
			else if(c == '>')
				state = 3;
			else if(c == '-')
				state = 30;
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
			
		case 30:
			state = 0;
			toReturn = new Token(RELOP, ASS);
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

		case 9:
			c = readNext();
			if(c == NULL)
				toReturn = ERROR;

			s="";

			if(isLetter(c)){
				state = 10;
				s+=""+c;
			}
			else{
				star = true;
				state = 12; // sarà state = inizio dfa per numeri
			}

			break;

		case 10:
			c = readNext();
			if(c == NULL)
				toReturn = ERROR;

			while(isLetter(c) || isDigit(c)){
				s+=""+c;

				c = readNext();
				if(c == NULL)
					toReturn = ERROR;
			}

			star = true;
			state = 11;

			break;

		case 11:
			state = 0;
			star = true;
			if(keywordMap.containsKey(s.toUpperCase()))
				toReturn = keywordMap.get(s.toUpperCase());
			else{
				if(symbolMap.contains(s))
					toReturn = new Token(ID, symbolMap.indexOf(s)+"");
				else{
					symbolMap.add(symbolCounter,s);
					toReturn = new Token(ID, symbolCounter+"");
					symbolCounter ++;
				}
			}
			break;

		case 12:
			c = readNext();
			if(c == NULL)
				toReturn = ERROR;

			s="";

			if(isDigit(c)){
				state = 13;
				s+=""+c;
			}
			else{
				star = true;
				state = 22; // sarà state = pozzo
			}
			break;

		case 13:
			c = readNext();
			if(c == NULL)
				toReturn = ERROR;

			while(isDigit(c)){
				s += ""+c;

				c = readNext();
				if(c == NULL)
					toReturn = ERROR;
			}

			if(c == '.'){
				s += ""+c;
				state = 14;
			}else if(c == 'E'){
				s += ""+c;
				state = 16;
			}else
				state = 20;

			break;

		case 14:
			c = readNext();
			if(c == NULL)
				toReturn = ERROR;

			if(isDigit(c)){
				state = 15;
				s+=""+c;
			}
			else{
				star = true;
				state = 22; // sarà state = pozzo
			}	

			break;


		case 15:
			c = readNext();
			if(c == NULL)
				toReturn = ERROR;

			while(isDigit(c)){
				s += ""+c;

				c = readNext();
				if(c == NULL)
					toReturn = ERROR;
			}

			if(c == 'E'){
				s += ""+c;
				state = 16;
			}else{
				state = 21;
			}

			break;


		case 16:
			c = readNext();
			if(c == NULL)
				toReturn = ERROR;

			if(c =='+' || c == '-'){
				s += ""+c;
				state = 17;
			}else if(isDigit(c)){
				s += ""+c;
				state = 18;
			}else{
				star = true;
				state = 22;
			}

			break;


		case 17:
			c = readNext();
			if(c == NULL)
				toReturn = ERROR;

			if(isDigit(c)){
				s += ""+c;
				state = 18;
			}else{
				star = true;
				state = 22; // state = pozzo
			}

			break;


		case 18:
			c = readNext();
			if(c == NULL)
				toReturn = ERROR;

			while(isDigit(c)){
				s += ""+c;

				c = readNext();
				if(c == NULL)
					toReturn = ERROR;
			}

			state = 19;
			break;


		case 19:
			state = 0;
			star = true;

			if(symbolMap.contains(s)){
				toReturn = new Token(CONST, symbolMap.indexOf(s)+"");
			}
			else{
				symbolMap.add(symbolCounter,s);
				toReturn = new Token(CONST, symbolCounter+"");
				symbolCounter ++;
			}
			break;

		case 20:
			state = 0;
			star = true;

			if(symbolMap.contains(s)){
				toReturn = new Token(CONST, symbolMap.indexOf(s)+"");
			}
			else{	
				symbolMap.add(symbolCounter,s);
				toReturn = new Token(CONST, symbolCounter+"");
				symbolCounter ++;
			}
			break;

		case 21:
			state = 0;
			star = true;
			
			if(symbolMap.contains(s)){
				toReturn = new Token(CONST, symbolMap.indexOf(s)+"");
			}
			else{
				symbolMap.add(symbolCounter,s);
				toReturn = new Token(CONST, symbolCounter+"");
				symbolCounter ++;
			}
			break;
			
		case 22:
			c = readNext();
			if(c == NULL)
				toReturn = ERROR;

			if(c == '(')
				state = 23;
			else if(c == ')')
				state = 24;
			else if(c == '[')
				state = 25;
			else if(c == ']')
				state = 26;
			else if(c == '{')
				state = 27;
			else if(c == '}')
				state = 28;
			else
				state = 29;

			break;
			
		case 23:
			state = 0;
			toReturn = new Token(BRACKET,ROUNDOP);
			break;
		case 24:
			state = 0;
			toReturn = new Token(BRACKET,ROUNDCL);
			break;
		case 25:
			state = 0;
			toReturn = new Token(BRACKET,SQUAREOP);
			break;
		case 26:
			state = 0;
			toReturn = new Token(BRACKET,SQUARECL);
			break;
		case 27:
			state = 0;
			toReturn = new Token(BRACKET,CURLYOP);
			break;
		case 28:
			state = 0;
			toReturn = new Token(BRACKET,CURLYCL);
			break;
		case 29:
			if(isSeparator(c)){
				star = false;
				state = 0;
				toReturn = new Token(SEPARATOR , ""+c);
			}
			else if(isBlanck(c)){
				star = false;
				state = 0;
				
				switch(c){
					case ' ' : toReturn = new Token(BLANCK, "_"); break;
					case '\n': toReturn = new Token(BLANCK, "\\n"); break;
					case '\t': toReturn = new Token(BLANCK, "\\t"); break;
					case '\r': toReturn = new Token(BLANCK, "\\r"); break;
				}			
				

				if(last) stop = true;
			}
			else if(isBracket(c)){
				star = true;
				state = 23;
			}
			else{
				stop = true;
				toReturn = ERROR;	
			}

			break;
			
		}
		return toReturn;

	}


	private static boolean isLetter(char c){
		int value = c;
		return((value >= A && value <= Z) ||(value >= a && value <= z));
	}

	private static boolean isDigit(char c){
		String numbers = "0123456789";
		return numbers.contains(""+c);
	}

	private static boolean isSeparator(char c){
		String separator = ".,:;";
		return separator.contains(""+c);
	}
	
	private static boolean isBracket(char c){
		String brackets = "()[]{}";
		return brackets.contains(""+c);
	}

	private static boolean isBlanck(char c){
		return c == ' ' || c == '\n' || c == '\t' || c == '\r';
	}

	private static char readNext(){
		if(star){
			star = false;
			return c;
		}
		else{
			try {
				if((c=(char) sc.read())!=-1){
					//System.out.println(c+" "+(int)c);
					if((int)c > 256){
						last = true;
						star = true;
						return ' ';
					}

					return (char) c;
				}
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
