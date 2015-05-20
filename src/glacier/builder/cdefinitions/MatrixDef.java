package glacier.builder.cdefinitions;


public enum MatrixDef implements Definition {
	WORLD, NORMAL, PROJ, VIEW, MVP;

	@Override
	public String generateLocVarSet() {
		switch (this) {
		case MVP:
			return "u_MvpTrans = program.getUniformLocation(\"u_mvpTrans\");\n";
		case NORMAL:
			return "u_NormalTrans = program.getUniformLocation(\"u_normalTrans\");\n";
		case PROJ:
			return "u_ProjTrans = program.getUniformLocation(\"u_projTrans\");\n";
		case VIEW:
			return "u_ViewTrans = program.getUniformLocation(\"u_viewTrans\");\n";
		case WORLD:
			return "u_WorldTrans = program.getUniformLocation(\"u_worldTrans\");\n";
		default:
			break;
		}
		return null;
	}

	@Override
	public String generateLocVarDef() {
		switch (this) {
		case MVP:
			return "private int u_MvpTrans;\n\tprivate final Matrix4 tempMat4 = new Matrix4();\n";
		case NORMAL:
			return "private int u_NormalTrans;\n";
		case PROJ:
			return "private int u_ProjTrans;\n";
		case VIEW:
			return "private int u_ViewTrans;\n";
		case WORLD:
			return "private int u_WorldTrans;\n\tprivate final Matrix3 tempMat3 = new Matrix3();\n";
		default:
			break;
		}
		return "";
	}

	@Override
	public String generateInit() {
		return "";
	}

	@Override
	public String generateBlock() {
		switch (this) {
		case MVP:
			return "";
		case NORMAL:
			return "";
		case PROJ:
			return "program.setUniformMatrix(u_ProjTrans, camera.projection);\n";
		case VIEW:
			return "program.setUniformMatrix(u_ViewTrans, camera.view);\n";
		case WORLD:
			return "";
		default:
			break;
		}
		return "";
	}

	@Override
	public String generateInstance() {
		switch (this) {
		case MVP:
			return "tempMat4.set(camera.combined).mul(renderable.worldTransform);\n\t\tprogram.setUniformMatrix(u_MvpTrans, tempMat4);\n";
		case NORMAL:
			return "tempMat3.set(renderable.worldTransform).inv().transpose();\n\t\tprogram.setUniformMatrix(u_NormalTrans, tempMat3);\n";
		case PROJ:
			return "";
		case VIEW:
			return "";
		case WORLD:
			return "program.setUniformMatrix(u_WorldTrans, renderable.worldTransform);\n";
		default:
			break;
		
		}
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
		switch (this) {
		case MVP:
			return "uniform mat4 u_mvpTrans;\n";
		case NORMAL:
			return "uniform mat3 u_normalTrans;\n";
		case PROJ:
			return "uniform mat4 u_projTrans;\n";
		case VIEW:
			return "uniform mat4 u_viewTrans;\n";
		case WORLD:
			return "uniform mat4 u_worldTrans;\n";
		default:
			break;
		
		}
		return null;
	}

	@Override
	public String getName() {
		switch (this) {
		case MVP:
			return "u_mvpTrans";
		case NORMAL:
			return "u_normalTrans";
		case PROJ:
			return "u_projTrans";
		case VIEW:
			return "u_viewTrans";
		case WORLD:
			return "u_worldTrans";
		default:
			break;
		
		}
		return null;
	}
	
}
