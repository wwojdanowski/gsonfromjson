package com.voovoo.antlr.entities;

public class Printer {

	public String print(EntityNode node) {
		
		String output = null;
		EntityType type = node.getType();
		String name = node.getName();
		
		
		switch (type) {
		
		case ARRAY:
			return "\tprivate List<" + "> " + name + ";";
		case FALSE:
			break;
		case NULL:
			break;
		case NUMBER:
			return "\tprivate int " + name + ";";
		case OBJECT:
			return "\tprivate " + className(name) + " " + name + ";";
		case STRING:
			return "\tprivate String " + name + ";";
		case TRUE:
			break;
		default:
			return "undefined";
		}
		
		return output;
	}
	
	public String printClassHeader(EntityNode node) {
		return "class " + className(node.getName()) + " {";
	}
	
	public String printClassTail() {
		return "}";
	}
	
	private String className(String name) {
		StringBuilder newName = new StringBuilder(name);
		newName.setCharAt(0, name.substring(0, 1).toUpperCase().charAt(0));
		
		return newName.toString();
	}
}
