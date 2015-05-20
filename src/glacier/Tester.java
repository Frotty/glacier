package glacier;


import glacier.builder.Reader;
import glacier.parser.Parser;
import glacier.util.ExtendedLexer;

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
	
	public static void main(String[] args) {
		try {
			String shaderFile = new String(Files.readAllBytes(Paths.get("./src/test/test.gl")));
			Parser parser = new Parser();
			System.out.println("Parsing Shader");
			ShaderProgContext context = parser.parseShader(shaderFile);
			System.out.println("Shader parsed.");
			Reader r = new Reader("C:/Users/Frotty/Documents/GitHub/Metagine/");
			r.read(context);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
