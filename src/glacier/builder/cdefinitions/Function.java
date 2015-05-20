package glacier.builder.cdefinitions;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Function {
	String name;
	String[] args;
	String returnType;
	String body;
	static Function mainFunc;
	
	public Function(String name, String[] args, String returnType, String body, boolean main) {
		this.name = name;
		this.args = args;
		this.returnType = returnType;
		this.body = parseBody(body);
		if(main) {
			mainFunc = this;
		}
		
	}
	
	private String parseBody(String body2) {
		Scanner sc = new Scanner(body2);
		sc.useDelimiter(Pattern.compile("[\\r\\n]+"));
		StringBuilder sb = new StringBuilder();
		String indentLvl = "";
		while(sc.hasNext()) {
			String token = sc.next();
			if(token.startsWith("$begin")) {
				indentLvl += "\t";
				token = token.substring(6);
			}
			if(token.endsWith("$end")) {
				indentLvl = indentLvl.substring(0, indentLvl.length()-1);
				token = token.substring(4);
			}
			sb.append(indentLvl + token + "\n");
		}
		System.out.println(sb);
		return sb.toString();
	}



	public String print() {
		String s = Arrays.toString(args);
		return returnType + " " + name + "(" + s.substring(1, s.length()-1) + ") {\n" + body + "\n}";
	}
}
