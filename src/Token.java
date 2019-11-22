import java.lang.String;
public class Token {
	public int code;
	public String strval;
	public int line;
	public Token(int code,String strval,int line) {
		this.code = code;
		this.strval = strval;
		this.line = line;
	}
	public void tostring() {
		System.out.println('<'+""+code+"    "+strval+"    "+line+'>');
	}
}
