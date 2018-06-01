package lcpp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
public class LCPP {

	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		
		if (args.length == 0) {
			throw new ArrayIndexOutOfBoundsException("No args found!");
		}
		
		for (String arg : args) {
			File inputFile = new File(arg);
			File outputFile = new File(inputFile.getParent(), inputFile.getName().replace(".lts", ".cpp"));
			File outputHeaderFile = new File(inputFile.getParent(), inputFile.getName().replace(".lts", ".h"));
			
			boolean isNative = false;
	
			List<String> lines = FileUtils.readLines(inputFile);
			List<String> funcs = new ArrayList<String>();
			List<String> result = new ArrayList<String>();
			
			result.add("/** LCPP Compiler - (c) Litikz 2018");
			result.add("LCPP is an open-source compiler check at : https://www.github.com/Rappaaa");
			result.add("*/\n");
			
			funcs.add("#include <iostream>\n\nusing namespace std;\nclass " + inputFile.getName().split("\\.")[0] + "{\n\tpublic:");
			
			if (!isNative) {
				result.add("#include <iostream>\n\nusing namespace std;");
			}
			
			result.add("#include \"" + outputHeaderFile.getName() + "\"");
			
			List<String> varname = new ArrayList<String>();
			
			for (int i = 0; i < lines.size(); i++) {
				
				String ss = lines.get(i);
				
				String s = ss.replace(";", "//");
				
				String wouttab = s.replace("\t", "");
				
				String wouttabcpp = ss.replace("\t", "");
				
				String lastClassName = "";
				
				for (String zee : varname) {
					if (wouttab.contains(zee)) {
						wouttab = wouttab.replace(zee, lastClassName + "_" + zee);
					}
				}
				
				if (wouttab.startsWith("var")) {
					
					String type = wouttab.split("\\:")[1].replaceFirst(" ", "");
					String name = wouttab.split("\\:")[0].replaceFirst("var", "").replace(" ", "");
					
					result.add(tabs(numberOfTabs(s)) + type + " " + name + ";");
					funcs.add("\t\t" + type + " " + name + ";");
				}
				
				if (wouttab.startsWith("use")) {
					String path = wouttab.split("from")[1].replaceFirst(" ", "");
					funcs.add("#include \"" + path.replace(".lts", ".h") + "\"");
					result.add(tabs(numberOfTabs(s)) + "#include \"" + path.replace(".lts", ".h") + "\"");
				}
				
				if (wouttab.startsWith("def")) {
					result.add(tabs(numberOfTabs(s)) + wouttab.replace("%p", "\\.").replaceFirst("def ", "") + ";");
					
				}
				
				if (wouttab.startsWith("cpp")) {
					result.add(tabs(numberOfTabs(s)) + wouttabcpp.replaceFirst("cpp ", ""));
					
				}
				
				if (wouttab.startsWith("func")) {
					result.add(tabs(numberOfTabs(s)) + wouttab.replace("%p", "\\.").replaceFirst("func", "void") + "() {");
					funcs.add("\t\t" + wouttab.replace("%p", "\\.").replaceFirst("func", "void") + "();");
				}
				
				if (wouttab.startsWith("if")) {
					result.add(tabs(numberOfTabs(s)) + wouttab.replace("%p", "\\.").replace("if", "if (").replace("!=", "%IE%").replace("=", "==").replace("%IE%", "!=").replace("and", "\\&\\&").replace("or", "\\|\\|").replace("then", ") {"));
					
				}
				
				if (wouttab.startsWith("while")) {
					result.add(tabs(numberOfTabs(s)) + wouttab.replace("while", "while (").replace("%p", "\\.").replace("!=", "%IE%").replace("=", "==").replace("%IE%", "!=").replace("and", "\\&\\&").replace("or", "\\|\\|").replace("then", ") {"));
					
				}
				
				if (wouttab.startsWith("type")) {
					result.add(tabs(numberOfTabs(s)) + wouttab.replace("%p", "\\.").replaceFirst("type", "typedef") + " {");
				}
				
				if (wouttab.startsWith("call")) {
					result.add(tabs(numberOfTabs(s)) + wouttab.replaceFirst("call ", "").replace("%p", "\\.") + "();");
				}
				
				if (wouttab.startsWith("break")) {
					result.add(tabs(numberOfTabs(s)) + "return;");
				}
				
				if (wouttab.startsWith("end")) {
					result.add(tabs(numberOfTabs(s)) + wouttab.replace(" ", "").replaceFirst("end", "}"));
				}
				
			}
			
			funcs.add("}");
			
			outputFile.createNewFile();
			outputHeaderFile.createNewFile();
			
			FileUtils.writeLines(outputFile, result);
			FileUtils.writeLines(outputHeaderFile, funcs);
		}
	}
	
	public static int numberOfTabs(String s) {
		String str = s;
		int i = 0;
		
		if (str.contains("\t")) i++;
		
		while ((str = str.replaceFirst("\t", "")).contains("\t")) {
			i++;
		}
		
		return i;
	}
	
	public static String tabs(int number) {
		String str = "";
		
		for (int i = 0; i < number; i++)
			str += "\t";
		
		return str;
		
	}
	
}
