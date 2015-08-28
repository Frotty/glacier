package glacier.visitors;

import antlr4.GlacierBaseVisitor;
import antlr4.GlacierParser.ContextOptContext;
import antlr4.GlacierParser.GlacierHeaderContext;
import antlr4.GlacierParser.VarDefContext;

public class HeaderVisitor extends GlacierBaseVisitor<String> {
	enum DepthTest {
		LESS() {
			@Override
			public String toString() {
				return "GL20.GL_LESS";
			}
		}
	}

	enum CullFace {
		BACK() {
			@Override
			public String toString() {
				return "GL20.GL_BACK";
			}
		},
		FRONT() {
			@Override
			public String toString() {
				return "GL20.GL_FRONT";
			}
		},
		NONE() {
			@Override
			public String toString() {
				return "GL20.GL_NONE";
			}
		}
	}

	public boolean fullScreen;
	private boolean depthMask;
	private DepthTest depthTest;
	private CullFace cullFace;

	@Override
	public String visitGlacierHeader(GlacierHeaderContext ctx) {
		if(ctx.contextOptions() == null)
			return "";
		for (ContextOptContext co : ctx.contextOptions().options) {
			String varname = co.optionName.getText();
			switch (varname) {
			case "depthmask":
				depthMask = true;
				break;
			case "depthtest":
				depthTest = DepthTest.valueOf(co.optionValue.getText().toUpperCase());
				break;
			case "cullFace":
				cullFace = CullFace.valueOf(co.optionValue.getText().toUpperCase());
				break;
			default:
				break;
			}
		}
		return "";
	}

	public String getContextOptions() {
		String s = "";
		if (depthMask) {
			s += "\tcontext.setDepthMask(true);\n";
		}
		if (depthTest != null) {
			s += "\tcontext.setDepthTest(" + depthTest.toString() + ");\n";
		}
		if (cullFace != null) {
			s += "\tcontext.setCullFace(" + cullFace.toString() + ");\n";
		}
		return s;
	}

}
