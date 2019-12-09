import java.lang.String;
public class Token {
	public int type;
	public String strval;
	public int line;
	public String errorInfo;
	public Token(int type,String strval,int line) {
		this.type = type;
		this.strval = strval;
		this.line = line;
	}
	public Token(int type,String strval,int line,String errorInfo) {
		this.type = type;
		this.strval = strval;
		this.line = line;
		this.errorInfo = errorInfo;
	}
	public void tostring() {
		if(type == TokenType.ERROR){
			System.out.println("error"+"    "+strval+ " is " + errorInfo + "line" + line);
		}else{
		System.out.println('<'+""+type+"    "+strval+"    "+line+'>');
		}
	}
}
