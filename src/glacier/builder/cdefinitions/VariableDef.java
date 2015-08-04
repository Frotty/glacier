package glacier.builder.cdefinitions;

public class VariableDef {
	public String name;
	public String type;
	public int uses = 0;
	
	public VariableDef(String name,	String type) {
		this.name = name;
		this.type = type;
	}

}
