
public class Token {
	private final String NO_CLASS = "!<>!";
	private String classe;
	private String op;
	
	public Token(String classe, String op){
		this.classe = classe;
		this.op = op;
	}
	
	public Token(String op){
		this.classe = NO_CLASS;
		this.op = op;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	@Override
	public String toString() {
		return String.format("<%s--%s>", this.getClasse(), this.getOp());
	}
	
	
}
