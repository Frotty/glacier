package glacier.builder.cdefinitions;


public class UniformDef implements Definition{
	private String type;
	private String nameL;
	private String nameU;
	private boolean isSampler = false;
	
	public UniformDef(String type, String name) {
		this.type = type;
		if(type.equals("sampler") || type.equals("sampler2D")) {
			isSampler = true;
			this.nameL = "s_" + name.substring(0, 1).toLowerCase() + name.substring(1);
			this.nameU = "s_" + name.substring(0, 1).toUpperCase() + name.substring(1);
		} else {
			this.nameL = "u_" + name.substring(0, 1).toLowerCase() + name.substring(1);
			this.nameU = "u_" + name.substring(0, 1).toUpperCase() + name.substring(1);
		}
	}
	
	public String generateLocVarDef() {
		return "private int " + nameU + ";\n";
	}
	
	public String generateLocVarSet() {
		return nameU + " = program.getUniformLocation(\"" + nameL + "\");\n";
	}

	@Override
	public String generateInit() {
		return "";
	}

	@Override
	public String generateBlock() {
		return "";
	}

	@Override
	public String generateInstance() {
		return "";
	}

	@Override
	public String generateShaderInDef() {
		return "";
	}

	@Override
	public String generateShaderOutDef() {
		return "";
	}

	@Override
	public String generateShaderUniDef() {
		return "uniform " + type + " " + nameL + ";\n";
	}

	@Override
	public String getName() {
		return nameL;
	}
}
