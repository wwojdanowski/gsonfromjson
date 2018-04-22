package com.voovoo.antlr.entities;

import java.util.HashMap;

public class ClassDef {

	private HashMap <String, String> fields = new HashMap <String, String>();
	private String name = "<unknown>";
	private String packageName = "<unknown>";
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void addField(String name, String type) {
		fields.put(name, type);
	}
	
	public String toString() {
		StringBuilder out = new StringBuilder();
		
		out.append("[" + name + "]");
		
		for (String key : fields.keySet()) {
			out.append("\n " + key + ": " + fields.get(key));
		}
		
		return out.toString();
	}
	
}
