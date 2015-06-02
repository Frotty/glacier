package glacier.parser;


import glacier.util.ExtendedLexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import antlr4.GlacierBaseListener;
import antlr4.GlacierLexer;
import antlr4.GlacierListener;
import antlr4.GlacierParser;
import antlr4.GlacierParser.ShaderProgContext;

public class Parser {
	
	public String[] parseShader(String grm) {
		String[] shaders = new String[2];
		 // Create a lexer and parser for the input.
        ExtendedLexer lexer = new ExtendedLexer(new ANTLRInputStream(grm));
        GlacierParser parser = new GlacierParser(new CommonTokenStream(lexer));

        // Invoke the `select_stmt` production.
        ShaderProgContext tree = parser.shaderProg();
        ParseTreeWalker walker = new ParseTreeWalker();
        GlacierListener listener = new GlacierBaseListener();
        PrintVisitor visitor = new PrintVisitor(null);
        walker.walk(listener, tree);
//        System.out.println("Ye: " + visitor.visit(tree));
        visitor.visit(tree.vertexShader());
        shaders[0] = visitor.out();
        System.out.println("---------------\n");
        ArrayList<String> al = new ArrayList<>();
        al.addAll(visitor.outVars());
        visitor = new PrintVisitor(al);
        visitor.visit(tree.fragmentShader());
        shaders[1] = visitor.out();
        return shaders;
	}
	
}
