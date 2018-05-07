package com.voovoo.antlr.entities;

import java.util.HashMap;

public class ClassDef {

	private HashMap <String, String> fields = new HashMap <String, String>();
	private String name = "<unknown>";
	private String packageName = "<unknown>";
	private boolean packageNameWasSet = false;
	private boolean classNameWasSet = false;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		classNameWasSet = true;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
		packageNameWasSet = true;
	}

	public void addField(String name, String type) {
		fields.put(name, type);
	}

	public boolean hasValidPackageName() {
		return packageNameWasSet;
	}

	public boolean hasValidClassName() {
		return classNameWasSet;
	}

	public String getField(String name) {
		return fields.get(name);
	}

	public HashMap<String, String> getFields() {
		return fields;
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
