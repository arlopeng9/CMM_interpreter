import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class WordAnalyze {
	// �洢�ʷ��������token
	public ArrayList<Token> TokenList = new ArrayList<>();

	// �жϵ�ǰ�ַ��Ƿ�����ĸ
	private boolean isLetter(char curcharacter) {
		if ((curcharacter >= 'a' && curcharacter <= 'z') || (curcharacter >= 'A' && curcharacter <= 'Z')) {
			return true;
		} else
			return false;
	}

	// �жϵ�ǰ�ַ��Ƿ�������
	private boolean isDigit(char curcharacter) {
		if (curcharacter >= '0' && curcharacter <= '9') {
			return true;
		} else
			return false;
	}

	// �жϵ�ǰ�����Ƿ�Ϊ�ؼ��֣��������tokenlist
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
		case "return":
			TokenList.add(new Token(TokenType.RETURN, word, line));
			return true;
		case "main":
			TokenList.add(new Token(TokenType.MAIN, word, line));
			return true;
		default:
			return false;
		}
	}

	private void wordAnalyze(char[] words) {
		int line = 1;
		for (int i = 0; i < words.length; i++) {
			String word = "";
			if (isLetter(words[i])) {
				while (isLetter(words[i]) || isDigit(words[i])) {
					word += words[i];
					i++;
					if (i >= words.length)
						break; // �ж��Ƿ�����Խ��
				}
				i--;
				if (!isKeyword(word, line)) { // �ж��Ƿ�Ϊ�ؼ���
					TokenList.add(new Token(TokenType.IDENTIFIER, word, line));
				}
			} else if (isDigit(words[i])) { // ����
				boolean point = false; // ��ǰ�ַ��Ƿ�ΪС����
				int pointflag = 0; // С�������?
				while (isDigit(words[i]) || point) {
					if (pointflag >= 2)
						break; // ͨ���жϸ�token����С��������ж��Ƿ��?�Ϸ�С��
					word += words[i];
					i++;
					if (i >= words.length)
						break; // �ж��Ƿ�����Խ��
					if (words[i] == '.') // �����ж��Ƿ�ΪС��
					{
						point = true;
						pointflag++;
					} else {
						point = false;
					}
				}
				if (isLetter(words[i]) || point) { // �����ǰ�ַ�����ĸ��С����?,��ó������?Ϊ����
					word += words[i];
					TokenList.add(new Token(TokenType.ERROR, word, line));
				} else {
					i--;
					TokenList.add(new Token(TokenType.CONSTANT, word, line));
				}
			} else {
				switch (words[i]) {
				// �����?
				case '+':
					word += words[i];
					TokenList.add(new Token(TokenType.ADD, word, line));
					break;
				case '-':
					word += words[i];
					TokenList.add(new Token(TokenType.SUBSTRACT, word, line));
					break;
				case '*':
					word += words[i];
					TokenList.add(new Token(TokenType.MULTIPLY, word, line));
					break;
				case '/':
					word += words[i];
					if (i < words.length - 1) { // �жϵ�ǰ�ַ��Ƿ��Ѿ�λ������ĩβ
						i++;
						if (words[i] == 10)
							line++; // �ж��Ƿ���
						if (words[i] == '=') { // ��������= ��ʶ��Ϊ<=
							word += words[i];
							TokenList.add(new Token(TokenType.MAEQ, word, line));
						} else if (words[i] == '/') { // ��������/���򽫻��з�ǰ�������ַ�ʶ��Ϊע��,�޻��з���ע����������ַ�?
							while ((words[i] != 10) && (i != words.length - 1)) {
								i++;
								if (words[i] == 10)
									line++; // �ж��Ƿ���
							}
						} else { // �������ʶ���?/
							i--;
							TokenList.add(new Token(TokenType.MAIOR, word, line));
						}
					} else
						TokenList.add(new Token(TokenType.MAIOR, word, line));
					break;
				case '>':
					word += words[i];
					if (i < words.length - 1) { // �жϵ�ǰ�ַ��Ƿ��Ѿ�λ������ĩβ
						i++;
						if (words[i] == '=') { // ��������= ��ʶ��Ϊ<=
							word += words[i];
							TokenList.add(new Token(TokenType.MAEQ, word, line));
						} else { // �������ʶ���?>
							i--;
							TokenList.add(new Token(TokenType.MAIOR, word, line));
						}
					} else
						TokenList.add(new Token(TokenType.MAIOR, word, line));
					break;
				case '<':
					word += words[i];
					if (i < words.length - 1) { // �жϵ�ǰ�ַ��Ƿ��Ѿ�λ������ĩβ
						i++;
						if (words[i] == '=') { // ��������= ��ʶ��Ϊ>=
							word += words[i];
							TokenList.add(new Token(TokenType.MEEQ, word, line));
						} else if (words[i] == '>') { // ��������> ��ʶ��Ϊ<>
							word += words[i];
							TokenList.add(new Token(TokenType.UNEQ, word, line));
						} else { // �������ʶ���?<
							i--;
							TokenList.add(new Token(TokenType.MAIOR, word, line));
						}
					} else
						TokenList.add(new Token(TokenType.MAIOR, word, line));
					break;
				case '=':
					word += words[i];
					if (i < words.length - 1) { // �жϵ�ǰ�ַ��Ƿ��Ѿ�λ������ĩβ
						i++;
						if (words[i] == '=') { // ��������= ��ʶ��Ϊ==
							word += words[i];
							TokenList.add(new Token(TokenType.EQEQ, word, line));
						} else { // �������ʶ���?=
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
					if (i < words.length - 1) { // �жϵ�ǰ�ַ��Ƿ��Ѿ�λ������ĩβ
						i++;
						if (words[i] == '&') { // ��������& ��ʶ��Ϊ&&
							word += words[i];
							TokenList.add(new Token(TokenType.AND, word, line));
						} else { // �������ʶ���?&
							i--;
							TokenList.add(new Token(TokenType.L_AND, word, line));
						}
					} else
						TokenList.add(new Token(TokenType.L_AND, word, line));
					break;
				case '|':
					word += words[i];
					if (i < words.length - 1) { // �жϵ�ǰ�ַ��Ƿ��Ѿ�λ������ĩβ
						i++;
						if (words[i] == '|') { // ��������| ��ʶ��Ϊ||
							word += words[i];
							TokenList.add(new Token(TokenType.AND, word, line));
						} else { // �������ʶ��Ϊ|
							i--;
							TokenList.add(new Token(TokenType.L_AND, word, line));
						}
					} else
						TokenList.add(new Token(TokenType.L_AND, word, line));
					break;
				// �ֽ��?
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
				// ��������
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
					for (Token token : TokenList) { // ��ʶ����ֹ��ʱ���ʷ�������������ӡ�������Ľ��?
						token.tostring();
					}
					return;
				// �����ո��?
				case 32:
					break;
				// �������з�
				case 10:
					line++;
					break;
				// �����س��з�
				case 13:
					break;
				default:
					System.out.println("����δ�������?" + words[i] + "    " + line);
					break;
				}
			}

		}
		for (Token token : TokenList) { // ������ɨ�����ʱ���ʷ�������������ӡ�������Ľ��
			token.tostring();
		}
		return;
	}

	public static void main(String args[]) throws Exception {
		File file = new File("D:\\data.txt");// ����һ��file����������ʼ��FileReader
		// File fileout = new
		// File("D:\\dataout.txt");//����һ��fileout���������ض��������?
		// PrintStream ps = new PrintStream(fileout);
		// System.setOut(ps);
		FileReader reader = new FileReader(file);// ����һ��fileReader����������ʼ��BufferedReader
		int length = (int) file.length();
		// ���ﶨ���ַ������ʱ�����?�ඨ��һ��,��Ϊ�ʷ���������������ǰ��ȡһ���ַ���ʱ����������һ��
		// �ַ�����ȡ������ڶ�ȡ��һ���ַ��ͻ����Խ�����?
		char buf[] = new char[length];
		reader.read(buf);
		reader.close();
		WordAnalyze analyze = new WordAnalyze();
		analyze.wordAnalyze(buf);
	}
}
