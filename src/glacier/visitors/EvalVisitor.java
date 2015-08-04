package glacier.visitors;

import glacier.builder.cdefinitions.AttributeDef;
import glacier.builder.cdefinitions.MatrixDef;
import glacier.builder.cdefinitions.VariableDef;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import antlr4.GlacierBaseVisitor;
import antlr4.GlacierParser.ExprMemberVarContext;
import antlr4.GlacierParser.FunctionBlockContext;
import antlr4.GlacierParser.InBlockContext;
import antlr4.GlacierParser.MatricesBlockContext;
import antlr4.GlacierParser.OutBlockContext;
import antlr4.GlacierParser.ShaderBlockContext;
import antlr4.GlacierParser.VarDefContext;

public class EvalVisitor extends GlacierBaseVisitor<String> {
	public HashMap<String, VariableDef> inReg = new HashMap<>();
	public Set<VarDefContext> inSet = new HashSet<>();
	public HashMap<String, VariableDef> outReg = new HashMap<>();
	public Set<VarDefContext> outSet = new HashSet<>();
	public HashMap<String, VariableDef> uniformReg = new HashMap<>();
	public Set<VarDefContext> uniformSet = new HashSet<>();
	public HashMap<String, VariableDef> matReg = new HashMap<>();
	public Set<VarDefContext> matSet = new HashSet<>();

	@Override
	public String visitInBlock(InBlockContext ctx) {
		System.out.println("in");
		for (VarDefContext indef : ctx.inArgs.vardefs) {
			System.out.println("added: " + indef.varName.getText());
			AttributeDef def;
			switch (indef.varName.getText()) {
			case "pos":
				def = AttributeDef.POS;
				break;
			case "normal":
				def = AttributeDef.NORMAL;
				break;
			case "texCoord":
				def = AttributeDef.TEXCOORD;
				break;
			default:
				throw new RuntimeException("not existing");
			}
			VariableDef vdef = new VariableDef(indef.varName.getText(), def.getType());
			inReg.put(vdef.name, vdef);
			inSet.add(indef);
		}
		return "";
	}

	@Override
	public String visitOutBlock(OutBlockContext ctx) {
		System.out.println("out");
		for (VarDefContext outdef : ctx.outArgs.vardefs) {
			System.out.println("added: " + outdef.varName.getText());
			VariableDef vdef;
			if(outdef.varType != null) {
				vdef = new VariableDef(outdef.varName.getText(), outdef.varType.getText());
			} else {
				vdef = new VariableDef(outdef.varName.getText(), "vec4");
			}
			outReg.put(outdef.varName.getText(), vdef);
			outSet.add(outdef);
		}
		return "";
	}

	@Override
	public String visitMatricesBlock(MatricesBlockContext ctx) {
		for (VarDefContext matdef : ctx.matsArgs.vardefs) {
			MatrixDef def;
			switch (matdef.varName.getText()) {
			case "mvp":
				def = MatrixDef.MVP;
				break;
			case "normal":
				def = MatrixDef.NORMAL;
				break;
			case "proj":
				def = MatrixDef.PROJ;
				break;
			case "view":
				def = MatrixDef.VIEW;
				break;
			case "world":
				def = MatrixDef.WORLD;
				break;
			default:
				throw new RuntimeException("not existing");
			}
			if(! matReg.containsKey(matdef.varName.getText())) {
				matReg.put(matdef.varName.getText(), new VariableDef(matdef.varName.getText(), def.getType()));
				matSet.add(matdef);
			}
		}
		return "matBlock";
	}

	@Override
	public String visitExprMemberVar(ExprMemberVarContext ctx) {
		if (ctx.ieDirect != null) {
			// Is Implicit Access
			switch (ctx.ieDirect.getText()) {
			case "in":
				if (inHas(ctx.varname.getText())) {
					return toOutInVar(ctx.varname.getText());
				} else {
					throw new RuntimeException("Undefine in variable");
				}
			case "out":
				if (outHas(ctx.varname.getText())) {
					return toOutInVar(ctx.varname.getText());
				} else {
					throw new RuntimeException("Undefined out variable: " + ctx.varname.getText());
				}
			}
		}
		return "";
	}

	private boolean outHas(String text) {
		return outReg.containsKey(text);
	}

	private boolean inHas(String text) {
		return inReg.containsKey(text);
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

	public String getVarType(String iED, String text) {
		switch (iED) {
		case "in":
			if (inReg.containsKey(text)) {
				return inReg.get(text).type;
			}
		case "out":
			if (outReg.containsKey(text)) {
				return outReg.get(text).type;
			}
		case "mats":
			if (matReg.containsKey(text)) {
				switch (text) {
				case "mvp":
					return MatrixDef.MVP.generateShaderUniDef().substring(8, 12);
				case "view":
					return MatrixDef.VIEW.generateShaderUniDef().substring(8, 12);
				case "normal":
					return MatrixDef.NORMAL.generateShaderUniDef().substring(8, 12);
				case "world":
					return MatrixDef.WORLD.generateShaderUniDef().substring(8, 12);
				case "proj":
					return MatrixDef.PROJ.generateShaderUniDef().substring(8, 12);
				}
			}
		}
		return "unknown";
	}

}
