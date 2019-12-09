import java.io.File;
import java.io.FileReader;

public class Interpreter {
public static void main(String args[]) throws Exception {
		File file = new File("D:\\data.txt");
		// File fileout = new File("D:\\dataout.txt");
		// PrintStream ps = new PrintStream(fileout);
		// System.setOut(ps);
		FileReader reader = new FileReader(file);
		int length = (int) file.length();
		char buf[] = new char[length];
		reader.read(buf);
		reader.close();
		WordAnalyze wordanalyze = new WordAnalyze();
        wordanalyze.wordAnalyze(buf);
        SyntaxAnalyze  syntax = new SyntaxAnalyze(wordanalyze.TokenList);
        String m = syntax.parse();
        System.out.print(m);
        syntax.printTree(syntax.getHeadNode(),"");
}
}