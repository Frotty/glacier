package glacier.parser;

import glacier.builder.LibgdxBuilder;
import glacier.util.ExtendedLexer;
import glacier.visitors.EvalVisitor;
import glacier.visitors.FixVisitor;
import glacier.visitors.PrintVisitor;

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
import antlr4.GlacierParser.VarDefContext;

import org.antlr.*;

public class GlacierCompiler {

	// Returns:
	// - ShaderName
	// - Shader Class
	// - Vertex glsl
	// - Fragment glsl
	public String[] compileShader(String grm, String template) {
		String[] output = new String[4];
		// Create a lexer and parser for the input.
		ExtendedLexer lexer = new ExtendedLexer(new ANTLRInputStream(grm));
		GlacierParser parser = new GlacierParser(new CommonTokenStream(lexer));

		// Invoke the `select_stmt` production.
		ShaderProgContext tree = parser.shaderProg();
		ParseTreeWalker walker = new ParseTreeWalker();
		GlacierListener listener = new GlacierBaseListener();
		PrintVisitor visitor = new PrintVisitor(null);
		walker.walk(listener, tree);
		output[0] = tree.shaderName.getText();
		// Evaluate
		System.out.println("\n\nEvaluating");
		EvalVisitor evalVisitor = new EvalVisitor();
		evalVisitor.visit(tree);
		for(VarDefContext vdc : evalVisitor.matSet) {
			System.out.println("mat " + vdc.varName.getText());
		}
		// Fixing
		FixVisitor fixv = new FixVisitor(evalVisitor);
		fixv.visit(tree);
		// Print Shader Class
		LibgdxBuilder libgdxBuilder = new LibgdxBuilder();
		try {
			output[1] = libgdxBuilder.build(output[0], evalVisitor, template);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Print GLSL
		System.out.println("Printing");
		visitor.visit(tree.vertexShader());
		output[2] = visitor.out();
		System.out.println("---------------\n");
		ArrayList<String> al = new ArrayList<>();
		al.addAll(visitor.outVars());

		visitor = new PrintVisitor(al);
		visitor.visit(tree.fragmentShader());
		output[3] = visitor.out();
		System.out.println(tree.toStringTree(parser));
		// Shader Class
		
		// show AST in GUI
//		JFrame frame = new JFrame("Antlr AST");
//		JPanel panel = new JPanel();
//		TreeViewer viewr = new TreeViewer(Arrays.asList(parser.getRuleNames()), tree);
//		viewr.setScale(0.75);// scale a little
//		panel.add(viewr);
//		frame.add(panel);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(200, 200);
//		frame.setVisible(true);
		return output;
	}

}
