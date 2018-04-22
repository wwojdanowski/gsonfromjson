package com.voovoo.antlr.entities;

public class ClassGenerator {

	private Printer printer = new Printer();
	private String packageName = "";
	
	public void setPackage(String pn) {
		packageName = pn;
	}
	
	public String generateFromObject(EntityNode entity) throws Exception {
		
		if (entity.getType() != EntityType.OBJECT) {
			throw new Exception("EntityNode is not of type OBJECT");
		}
		
		StringBuilder output = new StringBuilder();
		
		output.append(packageName);
		
		output.append(printer.printClassHeader(entity));
		

		output.append("\n");
		
		for (EntityNode e : entity.getEntities()) {
			output.append("\n");	
			output.append(printer.print(e));
		}
		
		
		output.append("\n");
		output.append(printer.printClassTail());
		
		return output.toString();
	}
}
