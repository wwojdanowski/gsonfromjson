package com.voovoo.antlr.entities;

import java.util.*;

public class ClassGenerator {

	private Printer printer = new Printer();
	private String packageName = null;
	
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

		String className = "";

		ArrayList <ClassDef> defsInThisBlock = new ArrayList<>();

		if (entity.getEntities().size() > 0) {

			for (EntityNode entityNode : entity.getEntities()) {

				switch (entityNode.getType()) {

					case STRING:
						return "String";
					case NUMBER:
						return "Integer";
					case OBJECT:

						entityNode.setName(entity.getName());
						ArrayList<ClassDef> localDefs = findClassDefinitions(entityNode);

						defsInThisBlock.addAll(localDefs);
						className = entityNode.getName();
						break;

					default:
						return "";
				}
			}


			if (defsInThisBlock.size() > 0) {

				definitions.addAll(mergeDefinitionsByName(defsInThisBlock));
			}
		} else {

			ClassDef classDef = new ClassDef();

			className = entity.getName();

			classDef.setName(className);
			definitions.add(classDef);
		}

		return className;
	}

	private ArrayList<ClassDef> mergeDefinitionsByName(ArrayList <ClassDef> defs) {

		HashMap <String, ClassDef> nameDefMapping = new HashMap<>();

		defs.forEach(def -> {
			ClassDef classDefMapping =
					Optional.ofNullable(nameDefMapping.get(def.getName()))
							.orElse(new ClassDef());

			classDefMapping.setName(def.getName());
			def.getFields().forEach(classDefMapping::addField);

			nameDefMapping.put(def.getName(), classDefMapping);

		});


		return new ArrayList<>(nameDefMapping.values());

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
				def.addField(name, "[" + className(outerMostType) + "]");
				break;

			case FALSE:
				break;
			case NULL:
				break;
			case NUMBER:
				def.addField(name, "Integer");
				break;
			case OBJECT:
				def.addField(name, name != null ? className(name) : null);
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
