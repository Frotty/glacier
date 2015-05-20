package glacier.builder;

import glacier.builder.cdefinitions.Definition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class LibgdxBuilder {
	
	private ArrayList<Definition> components;
	private String shaderName;
	private String directory;
	public LibgdxBuilder() {
	}
	
	public void build(String directory, String name, ArrayList<Definition> components) throws IOException {
		this.components = components;
		this.directory = directory;
		shaderName = name;
		File f = new File(directory + "/core/src-gen/glacier/shaders/" + shaderName + "Shader.java");
		if(f.exists()) {
			f.delete();
		}
		File parentDir = f.getParentFile();
		if(! parentDir.exists()) 
		      parentDir.mkdirs();
		f.createNewFile();
		FileWriter classFile = new FileWriter(f);
		String tmp = fillTemplate(new String(Files.readAllBytes(Paths.get("./src/glacier/builder/templates/Shader.tmp"))));
		classFile.write(tmp);
		classFile.close();
		System.out.println("created");
	}
	
	public String fillTemplate(String template) {
		// Shadername
		template = template.replaceAll("<shadername>", shaderName);
		// Mandatory Members
		template = template.replaceAll("\t<otherMembersBlock>;", locVarDefFromList());
		template = template.replaceAll("\t\t<getLocationsBlock>;", locVarSetFromList());
		template = template.replaceAll("\t\t<initBlock>;", initFromList());
		template = template.replaceAll("\t\t<setBlockUniforms>;", beginBlock());
		template = template.replaceAll("\t\t<instanceBlock>;", genInstance());
		template = template.replaceAll("\t\t<contextOptions>;", genContextOptions());
		return template.replaceAll("((?m)^[ \t]*\r?\n){2,}", "");
	}
	
	public String locVarDefFromList() {
		StringBuilder sb = new StringBuilder();
		for(Definition item : components) {
			String s = item.generateLocVarDef();
			if(s != null && s.length() > 0) {
				sb.append("\t" + s);
			}
		}
		return sb.toString();
	}
	
	public String locVarSetFromList() {
		StringBuilder sb = new StringBuilder();
		for(Definition item : components) {
			String s = item.generateLocVarSet();
			if(s != null && s.length() > 0) {
				sb.append("\t\t" + s);
			}
		}
		return sb.toString();
	}
	
	public String initFromList() {
		StringBuilder sb = new StringBuilder();
		for(Definition item : components) {
			String s = item.generateInit();
			if(s != null && s.length() > 0) {
				sb.append("\t" + s);
			}
		}
		return sb.toString();
	}
	
	public String beginBlock() {
		StringBuilder sb = new StringBuilder();
		for(Definition item : components) {
			String s = item.generateBlock();
			if(s != null && s.length() > 0) {
				sb.append("\t\t" + s);
			}
		}
		return sb.toString();
	}
	
	public String genInstance() {
		StringBuilder sb = new StringBuilder();
		for(Definition item : components) {
			String s = item.generateInstance();
			if(s != null && s.length() > 0) {
				sb.append("\t\t" + s);
			}
		}
		return sb.toString();
	}
	
	public String genContextOptions() {
		StringBuilder sb = new StringBuilder();
		//TODO
		return sb.toString();
	}

}
