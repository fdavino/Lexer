import java.io.File;


public class Main {


	public static void main(String[] args){

		File f = new File(args[0]);
		Lexer lex = new Lexer(f);
		while(lex.hasNext()){
			Token t = lex.next_token();
			if(t.equals(Lexer.ERROR)){
				System.err.println("Syntax error");
				System.exit(-1);
			}
			System.out.println(t);
		}
	}


}
