package glacier.builder;

import glacier.builder.cdefinitions.AttributeDef;
import glacier.builder.cdefinitions.Definition;
import glacier.builder.cdefinitions.Function;
import glacier.builder.cdefinitions.MatrixDef;
import glacier.builder.cdefinitions.UniformDef;
import glacier.builder.cdefinitions.AttributeDef.AttributeType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import antlr4.GlacierParser.ArgumentsContext;
import antlr4.GlacierParser.FragmentShaderContext;
import antlr4.GlacierParser.FunctionBlockContext;
import antlr4.GlacierParser.ShaderProgContext;
import antlr4.GlacierParser.StatementsBlockContext;
import antlr4.GlacierParser.UniformBlockContext;
import antlr4.GlacierParser.UniformDefContext;
import antlr4.GlacierParser.VarDefContext;
import antlr4.GlacierParser.VertexShaderContext;

public class Reader {
	ArrayList<Definition> vdefs;
	ArrayList<Definition> fdefs;
	ArrayList<Definition> adefs;
	ArrayList<Function> vfuncs;
	ArrayList<Function> ffuncs;
	private boolean vert = true;
	private String directory;
	
	public Reader(String workingDirectory) {
		vdefs = new ArrayList<>();
		fdefs = new ArrayList<>();
		adefs = new ArrayList<>();
		vfuncs = new ArrayList<>();
		ffuncs = new ArrayList<>();
		directory = workingDirectory;
	}

	public boolean read(ShaderProgContext context) {
		try {
			String name = context.shaderName.getText() + "Gen";
			VertexShaderContext vertShader = context.vertexShader();
			FragmentShaderContext fragShader = context.fragmentShader();
			System.out.println("Reading Shader " + name);
			readVertShader(vertShader);
			readFragShader(fragShader);
			GLSLBuilder glslbuilder = new GLSLBuilder();
			LibgdxBuilder libgdxbuilder = new LibgdxBuilder();
			adefs.addAll(vdefs);
			for (Definition f : fdefs){
			   if (!adefs.contains(f))
				   adefs.add(f);
			}
			glslbuilder.build(directory, name, vdefs, fdefs, vfuncs, ffuncs);
			libgdxbuilder.build(directory, name, adefs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void addDefinition(Definition c) {
		if(c!=null) {
			if (vert) {
				vdefs.add(c);
			} else {
				fdefs.add(c);
			}
		}
	}
	
	private void addFunction(Function f) {
		if(f!=null) {
			if (vert) {
				vfuncs.add(f);
			} else {
				ffuncs.add(f);
			}
		}
	}

	private void readFragShader(FragmentShaderContext fragShader) {
		vert = false;
//		parseIOArgs(fragShader.vshaderBlock().inoutArgs);
//		parseIArgs(fragShader.vshaderBlock().inArgs);
//		parseOArgs(fragShader.vshaderBlock().outArgs);
		parseMatrices(fragShader.vshaderBlock().matsArgs);
		parseUniforms(fragShader.vshaderBlock().uniformArgs);
	}

	private void readVertShader(VertexShaderContext vertShader) throws IOException {
		parseIOArgs(vertShader.vshaderBlock().inoutArgs);
		parseIArgs(vertShader.vshaderBlock().inArgs);
		parseOArgs(vertShader.vshaderBlock().outArgs);
		parseMatrices(vertShader.vshaderBlock().matsArgs);
		parseUniforms(vertShader.vshaderBlock().uniformArgs);
		readFunctions(vertShader.vshaderBlock().functionBlock());
		readMain(vertShader.vshaderBlock().mainFunc);
	}
	

	private void readMain(StatementsBlockContext mainFunc) {
		addFunction(new Function("main", new String[]{}, "void", mainFunc.getText(), true));
	}

	private void readFunctions(List<FunctionBlockContext> functionBlock) {
		for(FunctionBlockContext f : functionBlock) {
			String[] args = parseArgs(f.arguements);
			addFunction(new Function(f.funcName.getText(), args, f.returnType.getText(), f.body.getText(), false));
		}
	}

	private String[] parseArgs(ArgumentsContext arguements) {
		String[] args = new String[arguements.vardefs.size()];
		int i = 0;
		for(VarDefContext def : arguements.vardefs) {
			args[i] = def.getText();
			i++;
		}
		return args;
	}

	private void parseUniforms(UniformBlockContext uniformArgs) {
		if(uniformArgs != null) {
			for(UniformDefContext u : uniformArgs.uniforms) {
				addDefinition(new UniformDef(u.uniformType.getText(), u.uniformName.getText()));
			}
		}
	}

	private void parseMatrices(ArgumentsContext matsArgs) {
		if(matsArgs != null) {
			for(VarDefContext def : matsArgs.vardefs) {
				switch (def.varName.getText()) {
				case "mvp":
					addDefinition(MatrixDef.MVP);
					break;
				case "normal":
					addDefinition(MatrixDef.NORMAL);
					break;
				case "proj":
					addDefinition(MatrixDef.PROJ);
					break;
				case "view":
					addDefinition(MatrixDef.VIEW);
					break;
				case "world":
					addDefinition(MatrixDef.WORLD);
					break;
				default:
					break;
				}
			}
		}
	}

	private void parseIOArgs(ArgumentsContext inoutArgs) throws IOException {
		// Attributes that also get passed to the fragment shader
		if (inoutArgs != null) {
			for (VarDefContext vdef : inoutArgs.vardefs) {
				addDefinition(new AttributeDef(vdef.varName.getText(), AttributeType.INOUTVERT));
			}
		}
	}

	private void parseIArgs(ArgumentsContext inArgs) throws IOException {
		// Attributes that don't get passed to the fragment shader
		if (inArgs != null) {
			for (VarDefContext vdef : inArgs.vardefs) {
				addDefinition(new AttributeDef(vdef.varName.getText(), AttributeType.INVERT));
			}
		}
	}

	private void parseOArgs(ArgumentsContext outArgs) throws IOException {
		// Other non-attribute-variables that get passed to the fragment shader
		if (outArgs != null) {
			for (VarDefContext vdef : outArgs.vardefs) {
				addDefinition(new AttributeDef(vdef.varName.getText(), AttributeType.OUTVERT));
				vert = false;
				addDefinition(new AttributeDef(vdef.varName.getText(), AttributeType.OUTVERT));
				vert = true;
			}
		}
	}

}
