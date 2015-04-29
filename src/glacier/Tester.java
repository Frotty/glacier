package glacier;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import antlr4.GlacierBaseListener;
import antlr4.GlacierLexer;
import antlr4.GlacierListener;
import antlr4.GlacierParser;
import antlr4.GlacierParser.ShaderProgContext;

public class Tester {
	public static ShaderProgContext parseConfig(String grm) {
		 // Create a lexer and parser for the input.
        ExtendedLexer lexer = new ExtendedLexer(new ANTLRInputStream(grm));
        GlacierParser parser = new GlacierParser(new CommonTokenStream(lexer));

        // Invoke the `select_stmt` production.
        ShaderProgContext tree = parser.shaderProg();
        ParseTreeWalker walker = new ParseTreeWalker();
        GlacierListener listener = new GlacierBaseListener();
        walker.walk(listener, tree);
        
        return tree;
	}
	
	public static void main(String[] args) {
		try {
			parseConfig(new String(Files.readAllBytes(Paths.get("./src/test/test.gl"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
