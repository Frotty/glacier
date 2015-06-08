package glacier.parser;


import glacier.util.ExtendedLexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.DOTTreeGenerator;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.gui.TreeViewer;

import antlr4.GlacierBaseListener;
import antlr4.GlacierLexer;
import antlr4.GlacierListener;
import antlr4.GlacierParser;
import antlr4.GlacierParser.ShaderProgContext;

import org.antlr.*;
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
        System.out.println(tree.toStringTree(parser));
      //show AST in GUI
        JFrame frame = new JFrame("Antlr AST");
        JPanel panel = new JPanel();
        TreeViewer viewr = new TreeViewer(Arrays.asList(
                parser.getRuleNames()),tree);
        viewr.setScale(0.75);//scale a little
        panel.add(viewr);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200,200);
        frame.setVisible(true);
        return shaders;
	}
	
}
