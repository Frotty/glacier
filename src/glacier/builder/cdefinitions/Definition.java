package glacier.builder.cdefinitions;

public interface Definition {
	public String getName();
	public String generateLocVarSet();
	public String generateLocVarDef();
	public String generateInit();
	public String generateBlock();
	public String generateInstance();
	public String generateShaderInDef();
	public String generateShaderOutDef();
	public String generateShaderUniDef();
}
