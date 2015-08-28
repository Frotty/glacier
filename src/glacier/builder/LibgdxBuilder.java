package glacier.builder;

import glacier.builder.cdefinitions.Definition;
import glacier.builder.cdefinitions.MatrixDef;
import glacier.builder.cdefinitions.UniformDef;
import glacier.parser.VariableManager;
import glacier.parser.VariableManager.GlobalType;
import glacier.visitors.EvalVisitor;
import glacier.visitors.HeaderVisitor;

import java.io.IOException;

public class LibgdxBuilder {

	private VariableManager vm;
	private String shaderName;
	private HeaderVisitor hV;
	public String build(String name, VariableManager vm, HeaderVisitor hV, String template) throws IOException {
		this.vm = vm;
		this.hV = hV;
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
		for (Definition vdc : vm.getGlobalSet(GlobalType.MATS)) {
			sb.append(vdc.generateLocVarDef());
		}
		sb.append("\n\t// Uniforms\n");
		// Add Uniforms
		for (Definition udc : vm.getGlobalSet(GlobalType.UNI)) {
			sb.append(udc.generateLocVarDef());
		}
		return sb.toString();
	}

	public String getLocationsBlock() {
		StringBuilder sb = new StringBuilder();
		// Add Mats
		sb.append("\t\t// Matrices\n");
		for (Definition vdc : vm.getGlobalSet(GlobalType.MATS)) {
			sb.append(vdc.generateLocVarSet());
		}
		sb.append("\n\t\t// Uniforms\n");
		// Add Uniforms
		for (Definition vdc : vm.getGlobalSet(GlobalType.UNI)) {
			sb.append(vdc.generateLocVarSet());
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
		for (Definition vdc : vm.getGlobalSet(GlobalType.MATS)) {
			sb.append((vdc).generateBlock());
		}
		return sb.toString();
	}

	public String genInstance() {
		StringBuilder sb = new StringBuilder();
		// Add Mats
		sb.append("\t\t// Matrices\n");
		for (Definition vdc : vm.getGlobalSet(GlobalType.MATS)) {
			sb.append(vdc.generateInstance());
		}
		return sb.toString();
	}

	public String genContextOptions() {
		StringBuilder sb = new StringBuilder();
		sb.append(hV.getContextOptions());
		return sb.toString();
	}

}
