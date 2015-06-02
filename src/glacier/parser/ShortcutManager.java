package glacier.parser;

import java.util.HashMap;

public class ShortcutManager {
	HashMap<String, Shortcut> iedshortcuts = new HashMap<>();
	HashMap<String, Shortcut> matshortcuts = new HashMap<>();
	HashMap<String, Shortcut> funcshortcuts = new HashMap<>();
	
	public class Shortcut {
		String name;
		String type;
		String scname;
		public Shortcut(String scname, String type, String name) {
			this.name = name;
			this.type = type;
			this.scname = scname;
		}
	}
	
	public void addIEDShortcut(Shortcut sc) {
		iedshortcuts.put(sc.scname, sc);
	}
	
	public void addMatShortcut(Shortcut sc) {
		matshortcuts.put(sc.scname, sc);
	}
	
	public void addFuncShortcut(Shortcut sc) {
		funcshortcuts.put(sc.scname, sc);
	}
	
	public Shortcut getIEDShortcut(String scname) {
		if(iedshortcuts.containsKey(scname)) {
			return iedshortcuts.get(scname);
		}
		return null;
	}
	
	public Shortcut getMatShortcut(String scname) {
		if(matshortcuts.containsKey(scname)) {
			return matshortcuts.get(scname);
		}
		return null;
	}

	public void loadDefaultShortcuts() {
		// Geometry Data
		addIEDShortcut(new Shortcut("pos", "vec3", "position"));
		addIEDShortcut(new Shortcut("normal", "vec3", "normal"));
		addIEDShortcut(new Shortcut("texCoord", "vec2", "texCoord0"));
		// Camera-Matrices
		addMatShortcut(new Shortcut("world", "mat4", "worldTrans"));
		addMatShortcut(new Shortcut("view", "mat4", "viewTrans"));
		addMatShortcut(new Shortcut("proj", "mat4", "projTrans"));
		addMatShortcut(new Shortcut("normal", "mat3", "normalTrans"));
		addMatShortcut(new Shortcut("mvp", "mat4", "mvpTrans"));
		// Functions
		addFuncShortcut(new Shortcut("norm", "vec4", "normalize"));
	}

	public Shortcut getFuncShortcut(String text) {
		if(funcshortcuts.containsKey(text)) {
			return funcshortcuts.get(text);
		}
		return null;
	}

}
