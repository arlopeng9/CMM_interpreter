
import java.util.ArrayList;

public class WordAnalyze {
	// 锟芥储锟绞凤拷锟斤拷锟斤拷锟斤拷锟絫oken
	public ArrayList<Token> TokenList = new ArrayList<>();

	// 锟叫断碉拷前锟街凤拷锟角凤拷锟斤拷锟斤拷母
	private boolean isLetter(char curcharacter) {
		if ((curcharacter >= 'a' && curcharacter <= 'z') || (curcharacter >= 'A' && curcharacter <= 'Z')) {
			return true;
		} else
			return false;
	}

	// 锟叫断碉拷前锟街凤拷锟角凤拷锟斤拷锟斤拷锟斤拷
	private boolean isDigit(char curcharacter) {
		if (curcharacter >= '0' && curcharacter <= '9') {
			return true;
		} else
			return false;
	}

	// 锟叫断碉拷前锟斤拷锟斤拷锟角凤拷为锟截硷拷锟街ｏ拷锟斤拷锟斤拷锟斤拷锟絫okenlist
	private boolean isKeyword(String word, int line) {
		switch (word) {
		case "break":
			TokenList.add(new Token(TokenType.BREAK, word, line));
			return true;
		case "if":
			TokenList.add(new Token(TokenType.IF, word, line));
			return true;
		case "else":
			TokenList.add(new Token(TokenType.ELSE, word, line));
			return true;
		case "while":
			TokenList.add(new Token(TokenType.WHILE, word, line));
			return true;
		case "for":
			TokenList.add(new Token(TokenType.FOR, word, line));
			return true;
		case "int":
			TokenList.add(new Token(TokenType.INT, word, line));
			return true;
		case "float":
			TokenList.add(new Token(TokenType.FLOAT, word, line));
			return true;
		case "bool":
			TokenList.add(new Token(TokenType.BOOL, word, line));
			return true;
		case "true":
		case "True":
			TokenList.add(new Token(TokenType.TRUE, word, line));
			return true;
		case "false":
		case "False":
			TokenList.add(new Token(TokenType.FALSE, word, line));
			return true;
		case "return":
			TokenList.add(new Token(TokenType.RETURN, word, line));
			return true;
		case "continue":
			TokenList.add(new Token(TokenType.CONTINUE, word, line));
			return true;
		case "scanf":
			TokenList.add(new Token(TokenType.SCANF, word, line));
			return true;
		case "scan":
			TokenList.add(new Token(TokenType.SCAN, word, line));
			return true;
		case "print":
			TokenList.add(new Token(TokenType.PRINT, word, line));
			return true;
		case "printf":
			TokenList.add(new Token(TokenType.PRINTF, word, line));
			return true;
		//case "main":
		//	TokenList.add(new Token(TokenType.MAIN, word, line));
		//	return true;
		default:
			return false;
		}
	}

	void wordAnalyze(char[] words) {
		int line = 1;
		for (int i = 0; i < words.length; i++) {
			String word = "";
			if (isLetter(words[i])) {
				while (isLetter(words[i]) || isDigit(words[i])) {
					word += words[i];
					i++;
					if (i >= words.length)
						break; // 锟叫讹拷锟角凤拷锟斤拷锟斤拷越锟斤拷
				}
				i--;
				if (!isKeyword(word, line)) { // 锟叫讹拷锟角凤拷为锟截硷拷锟斤拷
					TokenList.add(new Token(TokenType.IDENTIFIER, word, line));
				}
			} else if(isDigit(words[i])) { // 锟斤拷锟斤拷
				boolean point = false; // 锟斤拷前锟街凤拷锟角凤拷为小锟斤拷锟斤拷
				int pointflag = 0; // 小锟斤拷锟斤拷锟斤拷锟?
				while (isDigit(words[i]) || point) {
					if (pointflag >= 2)
						break; // 通锟斤拷锟叫断革拷token锟斤拷锟斤拷小锟斤拷锟斤拷锟斤拷锟斤拷卸锟斤拷欠锟轿?锟较凤拷小锟斤拷
					word += words[i];
					i++;
					if (i >= words.length)
						break; // 锟叫讹拷锟角凤拷锟斤拷锟斤拷越锟斤
					if (words[i] == '.') // 锟斤拷锟斤拷锟叫讹拷锟角凤拷为小锟斤拷
					{
						point = true;
						pointflag++;
					} else {
						point = false;
					}
				}
				if (!isDigit(words[i]) && point) { // 锟斤拷锟斤拷锟角帮拷址锟斤拷锟斤拷锟侥革拷锟叫★拷锟斤拷锟?,锟斤拷贸锟斤拷锟斤拷锟皆?为锟斤拷锟斤拷
					word += words[i];
					TokenList.add(new Token(TokenType.ERROR, word, line,"非法常数"));
				} else {
					i--;
					if(pointflag > 0)
					TokenList.add(new Token(TokenType.FLOATVAL, word, line));
					else 
					TokenList.add(new Token(TokenType.INTVAL, word, line));
				}
			} else {
				switch (words[i]) {
				// 锟斤拷锟斤拷锟?
				case '+':
					word += words[i];
					if (i < words.length - 1) { 
						i++;
						if (words[i] == '=') { 
							word += words[i];
							TokenList.add(new Token(TokenType.ADD_EQUAL, word, line));
						}else if (words[i] == '+') { 
							word += words[i];
							TokenList.add(new Token(TokenType.ADD_ADD, word, line));
						} else { 
							i--;
							TokenList.add(new Token(TokenType.ADD, word, line));
						}
					} 
					break;
				case '-':
					word += words[i];
					if (i < words.length - 1) { 
						i++;
						if (words[i] == '=') { 
							word += words[i];
							TokenList.add(new Token(TokenType.SUBSTRACT_EQUAL, word, line));
						} else if (words[i] == '-') { 
							word += words[i];
							TokenList.add(new Token(TokenType.SUBSTRACT_SUBSTRACT, word, line));
						} else { 
							i--;
							TokenList.add(new Token(TokenType.SUBSTRACT, word, line));
						}
					} 
					break;
				case '*':
					word += words[i];
					if (i < words.length - 1) { 
						i++;
						if (words[i] == '=') { 
							word += words[i];
							TokenList.add(new Token(TokenType.MULTIPLY_EQUAL, word, line));
						} else { 
							i--;
							TokenList.add(new Token(TokenType.MULTIPLY, word, line));
						}
					} 
					break;
				case '/':
					word += words[i];
					if (i < words.length - 1) { // 锟叫断碉拷前锟街凤拷锟角凤拷锟窖撅拷位锟斤拷锟斤拷锟斤拷末尾
						i++;
						if (words[i] == '=') { // 锟斤拷锟斤拷锟斤拷锟斤拷= 锟斤拷识锟斤拷为<=
							word += words[i];
							TokenList.add(new Token(TokenType.DIVIDE_EQUAL, word, line));
						} else if ((words[i] == '/')) { // 锟斤拷锟斤拷锟斤拷锟斤拷/锟斤拷锟津将伙拷锟叫凤拷前锟斤拷锟斤拷锟斤拷锟街凤拷识锟斤拷为注锟斤拷,锟睫伙拷锟叫凤拷锟斤拷注锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷址锟?
							while ((words[i] != 10) && (i != words.length - 1)) {
								i++;
								if (words[i] == 10)
									line++; // 锟叫讹拷锟角凤拷锟斤拷
							}
						}else if ((words[i] == '*')) { // 锟斤拷锟斤拷锟斤拷锟斤拷/锟斤拷锟津将伙拷锟叫凤拷前锟斤拷锟斤拷锟斤拷锟街凤拷识锟斤拷为注锟斤拷,锟睫伙拷锟叫凤拷锟斤拷注锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷址锟?
								i++;
							while ((i != words.length - 1)) {
								i++;
								if (words[i] == 10)
									line++; 
								if((words[i-1] == '*')&&(words[i]=='/'))
								break;
							}
						} else { 
							i--;
							TokenList.add(new Token(TokenType.DIVIDE, word, line));
						}
					} 
					break;
				case '>':
					word += words[i];
					if (i < words.length - 1) { 
						i++;
						if (words[i] == '=') { 
							word += words[i];
							TokenList.add(new Token(TokenType.MAEQ, word, line));
						} else { 
							i--;
							TokenList.add(new Token(TokenType.MAIOR, word, line));
						}
					} 
					break;
				case '<':
					word += words[i];
					if (i < words.length - 1) { // 锟叫断碉拷前锟街凤拷锟角凤拷锟窖撅拷位锟斤拷锟斤拷锟斤拷末尾
						i++;
						if (words[i] == '=') { // 锟斤拷锟斤拷锟斤拷锟斤拷= 锟斤拷识锟斤拷为>=
							word += words[i];
							TokenList.add(new Token(TokenType.MEEQ, word, line));
						} else if (words[i] == '>') { // 锟斤拷锟斤拷锟斤拷锟斤拷> 锟斤拷识锟斤拷为<>
							word += words[i];
							TokenList.add(new Token(TokenType.UNEQ, word, line));
						} else { // 锟斤拷锟斤拷锟斤拷锟绞讹拷锟轿?<
							i--;
							TokenList.add(new Token(TokenType.MENOR, word, line));
						}
					} else
						TokenList.add(new Token(TokenType.MENOR, word, line));
					break;
				case '=':
					word += words[i];
					if (i < words.length - 1) { // 锟叫断碉拷前锟街凤拷锟角凤拷锟窖撅拷位锟斤拷锟斤拷锟斤拷末尾
						i++;
						if (words[i] == '=') { // 锟斤拷锟斤拷锟斤拷锟斤拷= 锟斤拷识锟斤拷为==
							word += words[i];
							TokenList.add(new Token(TokenType.EQEQ, word, line));
						} else { // 锟斤拷锟斤拷锟斤拷锟绞讹拷锟轿?=
							i--;
							TokenList.add(new Token(TokenType.EQUAL, word, line));
						}
					} else
						TokenList.add(new Token(TokenType.EQUAL, word, line));
					break;
				case '!':
					word += words[i];
					TokenList.add(new Token(TokenType.NOT, word, line));
					break;
				case '&':
					word += words[i];
					if (i < words.length - 1) { // 锟叫断碉拷前锟街凤拷锟角凤拷锟窖撅拷位锟斤拷锟斤拷锟斤拷末尾
						i++;
						if (words[i] == '&') { // 锟斤拷锟斤拷锟斤拷锟斤拷& 锟斤拷识锟斤拷为&&
							word += words[i];
							TokenList.add(new Token(TokenType.AND, word, line));
						} else { // 锟斤拷锟斤拷锟斤拷锟绞讹拷锟轿?&
							i--;
							TokenList.add(new Token(TokenType.L_AND, word, line));
						}
					} else
						TokenList.add(new Token(TokenType.L_AND, word, line));
					break;
				case '|':
					word += words[i];
					if (i < words.length - 1) { // 锟叫断碉拷前锟街凤拷锟角凤拷锟窖撅拷位锟斤拷锟斤拷锟斤拷末尾
						i++;
						if (words[i] == '|') { // 锟斤拷锟斤拷锟斤拷锟斤拷| 锟斤拷识锟斤拷为||
							word += words[i];
							TokenList.add(new Token(TokenType.OR, word, line));
						} else { // 锟斤拷锟斤拷锟斤拷锟绞讹拷锟轿獆
							i--;
							TokenList.add(new Token(TokenType.L_OR, word, line));
						}
					} else
						TokenList.add(new Token(TokenType.L_OR, word, line));
					break;
				// 锟街斤拷锟?
				case '(':
					word += words[i];
					TokenList.add(new Token(TokenType.LEFT_PARENTHESIS, word, line));
					break;
				case ')':
					word += words[i];
					TokenList.add(new Token(TokenType.RIGHT_PARENTHESIS, word, line));
					break;
				case '[':
					word += words[i];
					TokenList.add(new Token(TokenType.L_SQUARE_BRACKET, word, line));
					break;
				case ']':
					word += words[i];
					TokenList.add(new Token(TokenType.R_SQUARE_BRACKET, word, line));
					break;
				case '{':
					word += words[i];
					TokenList.add(new Token(TokenType.L_BRACKET, word, line));
					break;
				case '}':
					word += words[i];
					TokenList.add(new Token(TokenType.R_BRACKET, word, line));
					break;
				// 锟斤拷锟斤拷锟斤拷锟斤拷
				case ';':
					word += words[i];
					TokenList.add(new Token(TokenType.COLON, word, line));
					break;
				case ',':
					word += words[i];
					TokenList.add(new Token(TokenType.COMMA, word, line));
					break;
				case '#':
					word += words[i];
					TokenList.add(new Token(TokenType.STOP_SIGN, word, line));
					for (Token token : TokenList) { // 锟斤拷识锟斤拷锟斤拷止锟斤拷时锟斤拷锟绞凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷印锟斤拷锟斤拷锟斤拷锟侥斤拷锟?
						token.tostring();
					}
					return;
				case 32:
					break;
				case 10:
					line++;
					break;
				case 13:
					break;
				default:
					System.out.println("未定义符号" + words[i] + "    " + line);
					break;
				}
			}

		}
		for (Token token : TokenList) { // 锟斤拷锟斤拷锟斤拷扫锟斤拷锟斤拷锟绞憋拷锟斤拷史锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟接★拷锟斤拷锟斤拷锟斤拷慕锟斤拷
			token.tostring();
		}
		return;
	}

}
