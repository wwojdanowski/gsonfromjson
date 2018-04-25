package com.voovoo.antlr;

import static java.lang.System.out;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.voovoo.antlr.entities.ClassDef;
import com.voovoo.antlr.entities.ClassGenerator;
import com.voovoo.antlr.entities.EntityNode;
import com.voovoo.antlr.entities.EntityType;
import com.voovoo.antlr.json.JSONBaseListener;
import com.voovoo.antlr.json.JSONLexer;
import com.voovoo.antlr.json.JSONParser;
import com.voovoo.antlr.json.JSONParser.ArrayContext;
import com.voovoo.antlr.json.JSONParser.JsonContext;
import com.voovoo.antlr.json.JSONParser.ObjContext;
import com.voovoo.antlr.json.JSONParser.PairContext;
import com.voovoo.antlr.json.JSONParser.ValueContext;

public class GsonGenerator extends JSONBaseListener {

	public static void main(String[] args) throws IOException {

		GsonGenerator generator = new GsonGenerator();
		
		generator.run();
	}
	

	public void generateClasses(String jsonFilePath, String rootClassName, String packageName) throws IOException {
		CharStream stream = CharStreams.fromFileName(jsonFilePath);

		JSONLexer lexer = new JSONLexer(stream);
		JSONParser parser = new JSONParser(new CommonTokenStream(lexer));

		parser.addParseListener(new GsonGenerator());

		parser.json();
	}

	private void run() throws IOException {

		ClassLoader loader = GsonGenerator.class.getClassLoader();

		CharStream stream = CharStreams.fromStream(loader.getResourceAsStream("sample.json"));

		JSONLexer lexer = new JSONLexer(stream);
		JSONParser parser = new JSONParser(new CommonTokenStream(lexer));

		parser.addParseListener(new GsonGenerator());

		parser.json();
	}

	private ArrayDeque<EntityNode> nodes = new ArrayDeque<EntityNode>();
	private ArrayDeque<EntityNode> containers = new ArrayDeque<EntityNode>();
	
	EntityNode lastValue = null;
	EntityNode lastContainer = null;


	private void prettyPrint(EntityNode root, int level) {
		
		ClassGenerator generator = new ClassGenerator();
		
		for (EntityNode node : root.getEntities()) {
			
			for (int ii = 0; ii < level; ++ ii) {
				out.print(" ");
			}
			
			out.println("" + node.getName() + ": [" + node.getType() + "] -> " + root.toString());
			
			if (node.getType() == EntityType.OBJECT || node.getType() == EntityType.ARRAY) {
				prettyPrint(node, level + 1);
				
				if (node.getType() == EntityType.OBJECT) {
					try {
						
						ArrayList<ClassDef> defs = generator.findClassDefinitions(node);
						
						for (ClassDef cdef : defs) {
							System.out.println(cdef);
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	
	@Override
	public void enterObj(ObjContext ctx) {
		
		EntityNode node = nodes.peek();
		containers.push(node);
		
	}

	@Override
	public void exitObj(ObjContext ctx) {
		lastContainer = containers.pop();
		
		if (lastContainer.getName() == null) {
			lastContainer.setName("unknown");
		}

	}

	@Override
	public void enterJson(JsonContext ctx) {
		EntityNode node = new EntityNode();
		
		nodes.push(node);
		containers.push(node);
	}

	
	@Override
	public void exitJson(JsonContext ctx) {
		//prettyPrint(lastContainer, 0);
		
		
		ClassGenerator generator = new ClassGenerator();
		ArrayList<ClassDef> defs = generator.findClassDefinitions(lastContainer);
		
		for (ClassDef cdef : defs) {
			System.out.println(cdef);
		}
	}

	@Override
	public void enterPair(PairContext ctx) {

	}
	
	@Override
	public void exitPair(PairContext ctx) {
		
		String name = ctx.STRING().getText();
		lastValue.setName(name.replaceAll("\"", ""));
	}

	@Override
	public void enterArray(ArrayContext ctx) {
		EntityNode node = nodes.peek();
		containers.push(node);
		
	}

	@Override
	public void exitArray(ArrayContext ctx) {
		lastContainer = containers.pop();
	}

	@Override
	public void enterValue(ValueContext ctx) {
		EntityNode node = new EntityNode();
		
		nodes.push(node);
		containers.peek().addEntity(node);
		
	}

	@Override
	public void exitValue(ValueContext ctx) {

		EntityNode node = nodes.pop();
		lastValue = node;
		node.setType(EntityType.type(ctx));
	}

	@Override
	public void enterEveryRule(ParserRuleContext ctx) {

	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {

	}

	@Override
	public void visitTerminal(TerminalNode node) {
		
	}

	@Override
	public void visitErrorNode(ErrorNode node) {

	}

}
