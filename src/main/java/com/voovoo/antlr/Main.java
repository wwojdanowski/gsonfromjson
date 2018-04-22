package com.voovoo.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.voovoo.antlr.simpleParser.ExprContext;

public class Main extends simpleBaseListener {

	public static void main(String[] args) {
	
		CodePointCharStream input = CharStreams.fromString("Hello World!");
		
		simpleLexer lexer = new simpleLexer(input);
		simpleParser parser = new simpleParser(new CommonTokenStream(lexer));
		
		parser.addParseListener(new Main());
		
		parser.expr();
		
	}
	
	@Override
	public void enterExpr(ExprContext ctx) {
		super.enterExpr(ctx);
		
		System.out.println("Expression discovered");
	}
	
	@Override
	public void visitTerminal(TerminalNode node) {
		System.out.println("Terminal node: " + node.getText());
	}
}
