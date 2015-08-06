package glacier.builder;

import glacier.builder.cdefinitions.Definition;
import glacier.builder.cdefinitions.MatrixDef;
import glacier.visitors.EvalVisitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;

import antlr4.GlacierParser.VarDefContext;

public class LibgdxBuilder {

	private EvalVisitor evalV;
	private String shaderName;

	public String build(String name, EvalVisitor evalV, String template) throws IOException {
		this.evalV = evalV;
		shaderName = name;
		String tmp = fillTemplate(template);
		System.out.println("created");
		return tmp;
	}

	public String fillTemplate(String template) {
		// Shadername
		template = template.replaceAll("<shadername>", shaderName);
		// Mandatory Members
		template = template.replaceAll("\t<otherMembersBlock>;", otherMembersBlock());
		template = template.replaceAll("\t\t<getLocationsBlock>;", getLocationsBlock());
		template = template.replaceAll("\t\t<initBlock>;", initFromList());
		template = template.replaceAll("\t\t<setBlockUniforms>;", beginBlock());
		template = template.replaceAll("\t\t<instanceBlock>;", genInstance());
		template = template.replaceAll("\t\t<contextOptions>;", genContextOptions());
		return template.replaceAll("((?m)^[ \t]*\r?\n){2,}", "");
	}

	public String otherMembersBlock() {
		StringBuilder sb = new StringBuilder();
		// Add Mats
		sb.append("\t// Matrices\n");
		for (VarDefContext vdc : evalV.matSet) {
			appendMatrix(vdc, sb);
		}
		sb.append("\n\t// Uniforms\n");
		// Add Uniforms
		for (VarDefContext vdc : evalV.uniformSet) {
			sb.append("private int u_" + vdc.varName.getText() + ";\n");
		}
		return sb.toString();
	}

	public String getLocationsBlock() {
		StringBuilder sb = new StringBuilder();
		// Add Mats
		sb.append("\t\t// Matrices\n");
		for (VarDefContext vdc : evalV.matSet) {
			appendMatrixLoc(vdc, sb);
		}
		sb.append("\n\t\t// Uniforms\n");
		// Add Uniforms
		for (VarDefContext vdc : evalV.uniformSet) {
			sb.append("u_" + vdc.varName.getText() + " = program.getUniformLocation(\"u_" + vdc.varName.getText() + "\");\n");
		}
		return sb.toString();
	}

	public String initFromList() {
		StringBuilder sb = new StringBuilder();
		// TODO
		return sb.toString();
	}

	public String beginBlock() {
		StringBuilder sb = new StringBuilder();
		// Add Mats
		sb.append("\t\t// Matrices\n");
		for (VarDefContext vdc : evalV.matSet) {
			appendMatrixBegin(vdc, sb);
		}
		return sb.toString();
	}

	public String genInstance() {
		StringBuilder sb = new StringBuilder();
		// Add Mats
		sb.append("\t\t// Matrices\n");
		for (VarDefContext vdc : evalV.matSet) {
			appendMatrixRender(vdc, sb);
		}
		return sb.toString();
	}

	public String genContextOptions() {
		StringBuilder sb = new StringBuilder();
		// TODO
		return sb.toString();
	}

	private void appendMatrix(VarDefContext vdc, StringBuilder sb) {
		switch (vdc.varName.getText()) {
		case "mvp":
			sb.append(MatrixDef.MVP.generateLocVarDef());
			break;
		case "view":
			sb.append(MatrixDef.VIEW.generateLocVarDef());
			break;
		case "normal":
			sb.append(MatrixDef.NORMAL.generateLocVarDef());
			break;
		case "world":
			sb.append(MatrixDef.WORLD.generateLocVarDef());
			break;
		case "proj":
			sb.append(MatrixDef.PROJ.generateLocVarDef());
			break;
		}
	}

	private void appendMatrixLoc(VarDefContext vdc, StringBuilder sb) {
		switch (vdc.varName.getText()) {
		case "mvp":
			sb.append(MatrixDef.MVP.generateLocVarSet());
			break;
		case "view":
			sb.append(MatrixDef.VIEW.generateLocVarSet());
			break;
		case "normal":
			sb.append(MatrixDef.NORMAL.generateLocVarSet());
			break;
		case "world":
			sb.append(MatrixDef.WORLD.generateLocVarSet());
			break;
		case "proj":
			sb.append(MatrixDef.PROJ.generateLocVarSet());
			break;
		}
	}

	private void appendMatrixBegin(VarDefContext vdc, StringBuilder sb) {
		switch (vdc.varName.getText()) {
		case "mvp":
			sb.append(MatrixDef.MVP.generateBlock());
			break;
		case "view":
			sb.append(MatrixDef.VIEW.generateBlock());
			break;
		case "normal":
			sb.append(MatrixDef.NORMAL.generateBlock());
			break;
		case "world":
			sb.append(MatrixDef.WORLD.generateBlock());
			break;
		case "proj":
			sb.append(MatrixDef.PROJ.generateBlock());
			break;
		}
	}

	private void appendMatrixRender(VarDefContext vdc, StringBuilder sb) {
		switch (vdc.varName.getText()) {
		case "mvp":
			sb.append(MatrixDef.MVP.generateInstance());
			break;
		case "view":
			sb.append(MatrixDef.VIEW.generateInstance());
			break;
		case "normal":
			sb.append(MatrixDef.NORMAL.generateInstance());
			break;
		case "world":
			sb.append(MatrixDef.WORLD.generateInstance());
			break;
		case "proj":
			sb.append(MatrixDef.PROJ.generateInstance());
			break;
		}
	}
}
