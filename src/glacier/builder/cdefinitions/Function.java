package glacier.builder.cdefinitions;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import antlr4.GlacierParser.ExprMemberVarContext;
import antlr4.GlacierParser.StatementContext;
import antlr4.GlacierParser.StatementsBlockContext;
import antlr4.GlacierParser.StmtSetContext;

public class Function {
	String name;
	String[] args;
	String returnType;
	String body;
	static Function mainFunc;
	
	public Function(String name, String[] args, String returnType, StatementsBlockContext body2, boolean main) {
		this.name = name;
		this.args = args;
		this.returnType = returnType;
		this.body = parseBody(body2);
		if(main) {
			mainFunc = this;
		}
		
	}
	
	private String parseBody(StatementsBlockContext body2) {
		StringBuilder sb = new StringBuilder();
		body2.accept(new ParseTreeVisitor<StatementsBlockContext>() {

			@Override
			public StatementsBlockContext visit(ParseTree arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public StatementsBlockContext visitChildren(RuleNode arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public StatementsBlockContext visitErrorNode(ErrorNode arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public StatementsBlockContext visitTerminal(TerminalNode arg0) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
		return sb.toString();
	}



	public String print() {
		String s = Arrays.toString(args);
		return returnType + " " + name + "(" + s.substring(1, s.length()-1) + ") {\n" + body + "\n}";
	}
}
