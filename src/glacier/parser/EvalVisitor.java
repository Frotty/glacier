package glacier.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import antlr4.GlacierBaseVisitor;
import antlr4.GlacierParser.ExprMemberVarContext;
import antlr4.GlacierParser.InBlockContext;
import antlr4.GlacierParser.MatricesBlockContext;
import antlr4.GlacierParser.OutBlockContext;
import antlr4.GlacierParser.VarDefContext;

public class EvalVisitor extends GlacierBaseVisitor<String> {
	HashMap<VarDefContext, Integer> inMap = new HashMap<>();
	HashMap<String, VarDefContext> inReg = new HashMap<>();
	Set<VarDefContext> inSet = new HashSet<>();
	HashMap<VarDefContext, Integer> outMap = new HashMap<>();
	HashMap<String, VarDefContext> outReg = new HashMap<>();
	Set<VarDefContext> outSet = new HashSet<>();
	HashMap<VarDefContext, Integer> uniformMap = new HashMap<>();
	HashMap<String, VarDefContext> uniformReg = new HashMap<>();
	Set<VarDefContext> uniformSet = new HashSet<>();
	HashMap<VarDefContext, Integer> matMap = new HashMap<>();
	HashMap<String, VarDefContext> matReg = new HashMap<>();
	Set<VarDefContext> matSet = new HashSet<>();
	
	@Override
	public String visitInBlock(InBlockContext ctx) {
		System.out.println("in");
		for(VarDefContext indef : ctx.inArgs.vardefs) {
			inMap.put(indef, 0);
			inReg.put(indef.varName.getText(), indef);
			inSet.add(indef);
		}
		return "";
	}
	
	@Override
	public String visitOutBlock(OutBlockContext ctx) {
		System.out.println("out");
		for(VarDefContext indef : ctx.outArgs.vardefs) {
			System.out.println("added: ");
			outMap.put(indef, 0);
			outReg.put(indef.varName.getText(), indef);
			outSet.add(indef);
		}
		return "";
	}
	
	@Override
	public String visitMatricesBlock(MatricesBlockContext ctx) {
		for(VarDefContext indef : ctx.matsArgs.vardefs) {
			matMap.put(indef, 0);
			matReg.put(indef.varName.getText(), indef);
			matSet.add(indef);
		}
		return "";
	}
	
	@Override
	public String visitExprMemberVar(ExprMemberVarContext ctx) {
		if(ctx.ieDirect != null) {
			// Is Implicit Access
			switch(ctx.ieDirect.getText()) {
			case "in" : 
				if(inHas(ctx.varname.getText())) {
					return toOutInVar(ctx.varname.getText());
				} else {
					throw new RuntimeException("Undefine in variable");
				}
			case "out" : 
				if(outHas(ctx.varname.getText())) {
					return toOutInVar(ctx.varname.getText());
				} else {
					throw new RuntimeException("Undefine out variable: " + ctx.varname.getText());
				}
			}
		}
		return "";
	}

	private boolean outHas(String text) {
		return outReg.containsKey(text);
	}
	
	private boolean inHas(String text) {
		return outReg.containsKey(text);
	}
	
	private String toOutInVar(String text) {
		switch (text) {
		case "pos":
			return "v_position";
		case "normal":
			return "v_normal";
		case "texCoord":
			return "v_texCoord0";
		default:
			return "v_" + text;
		}
	}

	public void printShader() {
		StringBuilder sb = new StringBuilder();
		
	}



}
