package glacier.visitors;

import antlr4.GlacierBaseVisitor;
import antlr4.GlacierParser.ExprContext;
import antlr4.GlacierParser.ExprPrimaryContext;

public class FixVisitor extends GlacierBaseVisitor<String> {
	private EvalVisitor evalV;

	public FixVisitor(EvalVisitor evalV) {
		this.evalV = evalV;
	}
	
	@Override
	public String visitExpr(ExprContext ctx) {
		if(ctx.op != null && ctx.left != null && ctx.right != null) {
			// Expression has an operator and 2 variables
			if(ctx.op.getText().equals("*")) {
				// Is a multiplication
				System.out.println(visit(ctx.left));
				System.out.println(visit(ctx.right));
				// Fix "mul(mat4, vec3)"
			}
		}
		if(ctx.ieD != null) {
			return evalV.getVarType(ctx.ieD.getText(), ctx.varName.getText());
		}
		return "";
	}
	
	@Override
	public String visitExprPrimary(ExprPrimaryContext ctx) {
		if(ctx.varname != null) {
			// Local variable ?
		}
		return "primaryUnknown";
	}
}
