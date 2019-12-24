import java.io.File;
import java.io.FileReader;

public class Interpreter {
public static void main(String args[]) throws Exception {
	//File file = new File("D:\\01bag.c");
	File file = new File("D:\\manacher.c");
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
        String parseErrorInfo = syntax.parse();
        System.out.print(parseErrorInfo);
		syntax.printTree(syntax.getHeadNode(),"");
		SemanticAnalyze semantic = new SemanticAnalyze(syntax.getHeadNode());
		String semanticErrorInfo = semantic.semantic();
		System.out.print(semanticErrorInfo);
		//semantic.printSymbalTable();
}
}