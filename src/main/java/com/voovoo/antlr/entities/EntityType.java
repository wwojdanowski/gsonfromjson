package com.voovoo.antlr.entities;

import com.voovoo.antlr.json.JSONParser.ValueContext;

public enum EntityType {
	STRING(), NUMBER(), OBJECT(), ARRAY(), TRUE(), FALSE(), NULL();
	
	public static EntityType type(ValueContext ctx) {
		
		if (ctx.array() != null) {
			return ARRAY;
		}
		
		if (ctx.STRING() != null) {
			return STRING;
		}
		
		if (ctx.obj() != null) {
			return OBJECT;
		}
		
		if (ctx.NUMBER() != null) {
			return NUMBER;
		}
		
		return null;
	}

}
