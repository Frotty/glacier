package glacier.visitors;

import glacier.builder.cdefinitions.AttributeDef;
import glacier.builder.cdefinitions.MatrixDef;
import glacier.builder.cdefinitions.UniformDef;
import glacier.builder.cdefinitions.VariableDef;
import glacier.parser.VariableManager;
import glacier.parser.VariableManager.GlobalType;

import antlr4.GlacierBaseVisitor;
import antlr4.GlacierParser.ExprMemberVarContext;
import antlr4.GlacierParser.InBlockContext;
import antlr4.GlacierParser.OutBlockContext;
import antlr4.GlacierParser.ShaderProgContext;
import antlr4.GlacierParser.TransBlockContext;
import antlr4.GlacierParser.UniformsBlockContext;
import antlr4.GlacierParser.VarDefContext;

public class EvalVisitor extends GlacierBaseVisitor<String> {
	VariableManager varManager;
	private boolean vert = true;

	public EvalVisitor(VariableManager variableManager) {
		varManager = variableManager;
	}

	@Override
	public String visitShaderProg(ShaderProgContext ctx) {
		visit(ctx.vertexShader());
		vert = false;
		visit(ctx.fragmentShader());
		return null;
	}

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
			varManager.saveGlobal(GlobalType.IN, vert, vdef);
		}
		return "";
	}

	@Override
	public String visitOutBlock(OutBlockContext ctx) {
		System.out.println("out");
		for (VarDefContext outdef : ctx.outArgs.vardefs) {
			System.out.println("added: " + outdef.varName.getText());
			VariableDef vdef;
			if (outdef.varType != null) {
				vdef = new VariableDef(outdef.varName.getText(), outdef.varType.getText());
			} else {
				vdef = new VariableDef(outdef.varName.getText(), "vec4");
			}
			varManager.saveGlobal(GlobalType.OUT, vert, vdef);
		}
		return "";
	}

	@Override
	public String visitTransBlock(TransBlockContext ctx) {
		for (VarDefContext matdef : ctx.transArgs.vardefs) {
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
			varManager.saveGlobal(GlobalType.TRANS, vert, def);
		}
		return "matBlock";
	}

	@Override
	public String visitUniformsBlock(UniformsBlockContext ctx) {
		for (VarDefContext unidef : ctx.uniformArgs.vardefs) {
			UniformDef def = new UniformDef(unidef.varType.getText(), unidef.varName.getText());
			varManager.saveGlobal(GlobalType.UNI, vert, def);
		}
		return "uniBlock";
	}

	@Override
	public String visitExprMemberVar(ExprMemberVarContext ctx) {
		if (ctx.ieDirect != null) {
			// Is Implicit Access
			switch (ctx.ieDirect.getText()) {
			case "in":
				if (varManager.globalExists(GlobalType.IN, vert, ctx.varname.getText())) {
					return toOutInVar(ctx.varname.getText());
				} else {
					throw new UnsupportedOperationException("Undefine in variable " + ctx.varname.getText());
				}
			case "out":
				if (varManager.globalExists(GlobalType.OUT, vert, ctx.varname.getText())) {
					return toOutInVar(ctx.varname.getText());
				} else {
					throw new UnsupportedOperationException("Undefined out variable: " + ctx.varname.getText());
				}
			}
		}
		return "";
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

//	public String getVarType(String iED, String text) {
//		switch (iED) {
//		case "in":
//			if (inReg.containsKey(text)) {
//				return inReg.get(text).type;
//			}
//		case "out":
//			if (outReg.containsKey(text)) {
//				return outReg.get(text).type;
//			}
//		case "mats":
//			if (matReg.containsKey(text)) {
//				switch (text) {
//				case "mvp":
//					return MatrixDef.MVP.generateShaderUniDef().substring(8, 12);
//				case "view":
//					return MatrixDef.VIEW.generateShaderUniDef().substring(8, 12);
//				case "normal":
//					return MatrixDef.NORMAL.generateShaderUniDef().substring(8, 12);
//				case "world":
//					return MatrixDef.WORLD.generateShaderUniDef().substring(8, 12);
//				case "proj":
//					return MatrixDef.PROJ.generateShaderUniDef().substring(8, 12);
//				}
//			}
//		}
//		return "unknown";
//	}

}
