package glacier.builder.cdefinitions;

public class AttributeDef implements Definition {
	public enum AttributeType {
		INVERT, OUTVERT, INOUTVERT, INFRAG, OUTFRAG;
	}
	private String name;
	private AttributeType attrType;
	
	public AttributeDef(String attrName, AttributeType attrType) {
		switch (attrName) {
		case "pos":
			name = "vec3 a_position;\n";
			break;
		case "normal":
			name = "vec3 a_normal;\n";
			break;
		case "texCoord":
			name = "vec2 a_texCoord0;\n";
			break;
		default:
			name = attrName;
			break;
//			throw new RuntimeException("not implemented");
		}
		this.attrType = attrType;
	}

	@Override
	public String generateLocVarSet() {
		return "";
	}

	@Override
	public String generateLocVarDef() {
		return "";
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
		switch (attrType) {
		case INVERT:
			return "in " + name;
		case INOUTVERT:
			return "in " + name;
		case INFRAG:
			return "in " + name.replace("a_", "v_");
		case OUTFRAG:
			break;
		case OUTVERT:
			break;
		default:
			break;
		}
		return "";
	}

	@Override
	public String generateShaderOutDef() {
		switch (attrType) {
		case INOUTVERT:
			return "out " + name.replace("a_", "v_");
		case OUTVERT:
			return "out " + name.replace("a_", "v_");
		case INFRAG:
			break;
		case INVERT:
			break;
		case OUTFRAG:
			break;
		default:
			break;
		}
		return "";
	}

	@Override
	public String generateShaderUniDef() {
		return "";
	}

	@Override
	public String getName() {
		return name;
	}


}
