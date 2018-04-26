package com.voovoo.antlr.entities;

import java.util.ArrayList;

public class ClassGenerator {

	private Printer printer = new Printer();
	private String packageName = "";
	
	public void setPackage(String pn) {
		packageName = pn;
	}
	
	public ArrayList <ClassDef> findClassDefinitions(EntityNode entity) {
		ArrayList <ClassDef> definitions = new ArrayList<ClassDef>();
		
		if (entity.getType() == EntityType.OBJECT) {
			
			findClassDefinitionsInternal(entity, definitions);
			
		} else if (entity.getType() == EntityType.ARRAY) {
			processArray(entity, definitions);
		}
		
		return definitions;
	}
	
	/**
	 * @param entity this is assumed to be a json array
	 */
	private String processArray(EntityNode entity, ArrayList <ClassDef> definitions) {
		
		ArrayList <ClassDef> localDefs = new ArrayList<ClassDef>();
		
		for (EntityNode e : entity.getEntities()) {
			localDefs = findClassDefinitions(e);
		}
		
		if (localDefs.size() > 0) {
			definitions.addAll(localDefs);
			return localDefs.get(0).getName();
		} else {
			if (entity.getEntities().size() > 0) {
				EntityType type = entity.getEntities().get(0).getType();
				
				switch (type) {
				case NUMBER:
					return "Integer";
				case STRING:
					return "String";
				default:
					return "Other";
				
				}
			} else {
				return "Other";
			}
		}
		
	}
	
	private void findClassDefinitionsInternal(EntityNode entity, ArrayList <ClassDef> definitions) {
		
		ClassDef def = new ClassDef();
		
		def.setName(entity.getName());

		if (packageName != null) {
			def.setPackageName(packageName);
		}
		
		for (EntityNode e : entity.getEntities()) {
			
			EntityType type = e.getType();
			String name = e.getName();
			
			switch (type) {
			case ARRAY:
				
				String outerMostType = processArray(e, definitions);
				def.addField(name, outerMostType);
				
				break;
			case FALSE:
				break;
			case NULL:
				break;
			case NUMBER:
				def.addField(name, "Integer");
				break;
			case OBJECT:
				def.addField(name, className(name));
				findClassDefinitionsInternal(e, definitions);
				break;
			case STRING:
				def.addField(name, "String");
				break;
			case TRUE:
				break;
			default:
				break;
			
			}	
		}
		
		definitions.add(def);
	}
	
	private String className(String name) {
		StringBuilder newName = new StringBuilder(name);
		newName.setCharAt(0, name.substring(0, 1).toUpperCase().charAt(0));
		
		return newName.toString();
	}
	
}
