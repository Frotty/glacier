package glacier.parser;

import glacier.builder.LibgdxBuilder;
import glacier.builder.cdefinitions.MatrixDef;
import glacier.util.ExtendedLexer;
import glacier.visitors.EvalVisitor;
import glacier.visitors.HeaderVisitor;
import glacier.visitors.TypeVisitor;
import glacier.visitors.PrintVisitor;

import java.io.IOException;
import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import antlr4.GlacierBaseListener;
import antlr4.GlacierListener;
import antlr4.GlacierParser;
import antlr4.GlacierParser.ShaderProgContext;

public class GlacierCompiler {
	public class CompilationResult {
		public String name;
		public String javaShaderSrc;
		public String vertShader;
		public String fragShader;
		public boolean fsShader;
		public int renderTargets;
	}
	
	String geometryTemplate;
	String fullscreenTemplate;
	
	public GlacierCompiler(String geoTemp, String fsTemp) {
		this.geometryTemplate = geoTemp;
		this.fullscreenTemplate = fsTemp;
	}
	

	// Returns:
	// - ShaderName
	// - Shader Class
	// - Vertex glsl
	// - Fragment glsl
	// - targets
	public CompilationResult compileShader(String grm) {
		// Create a lexer and parser for the input.
		ExtendedLexer lexer = new ExtendedLexer(new ANTLRInputStream(grm));
		GlacierParser parser = new GlacierParser(new CommonTokenStream(lexer));

		// Invoke the `select_stmt` production.
		ShaderProgContext tree = parser.shaderProg();
		ParseTreeWalker walker = new ParseTreeWalker();
		GlacierListener listener = new GlacierBaseListener();
		// The result
		CompilationResult result = new CompilationResult();
		// Start compilation
		VariableManager variableManager = new VariableManager();
		HeaderVisitor headerV = new HeaderVisitor();
		headerV.visit(tree);
		EvalVisitor evalVisitor = new EvalVisitor(variableManager);
		evalVisitor.visit(tree);
		TypeVisitor tV = new TypeVisitor(variableManager, false);
		PrintVisitor visitor = new PrintVisitor(null, tV);
		walker.walk(listener, tree);
		result.name = tree.shaderName.getText() + "Gen";
		// Evaluate
		System.out.println("\n\nEvaluating");

		// Fixing
		TypeVisitor fixv = new TypeVisitor(variableManager, true);
		fixv.visit(tree);
		// Print Shader Class
		LibgdxBuilder libgdxBuilder = new LibgdxBuilder();
		try {
			if(headerV.fullScreen) {
				result.javaShaderSrc = libgdxBuilder.build(result.name, variableManager, headerV, fullscreenTemplate);
			} else {
				result.javaShaderSrc = libgdxBuilder.build(result.name, variableManager, headerV, geometryTemplate);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Print GLSL
		System.out.println("Printing");
		visitor.visit(tree.vertexShader());
		result.vertShader = visitor.out();
		System.out.println("---------------\n");
		ArrayList<String> al = new ArrayList<>();
		al.addAll(visitor.outVars());

		visitor = new PrintVisitor(al, tV);
		visitor.visit(tree.fragmentShader());
		result.fragShader = visitor.out();
		result.renderTargets = variableManager.getRenderTargetCount();

		System.out.println(tree.toStringTree(parser));
		// Shader Class

		// show AST in GUI
		// JFrame frame = new JFrame("Antlr AST");
		// JPanel panel = new JPanel();
		// TreeViewer viewr = new TreeViewer(Arrays.asList(parser.getRuleNames()), tree);
		// viewr.setScale(0.75);// scale a little
		// panel.add(viewr);
		// frame.add(panel);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setSize(200, 200);
		// frame.setVisible(true);
		return result;
	}

}
