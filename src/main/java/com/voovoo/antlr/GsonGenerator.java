package com.voovoo.antlr;

import com.voovoo.antlr.entities.ClassDef;
import com.voovoo.antlr.entities.ClassGenerator;
import com.voovoo.antlr.entities.EntityNode;
import com.voovoo.antlr.entities.EntityType;
import com.voovoo.antlr.json.JSONBaseListener;
import com.voovoo.antlr.json.JSONLexer;
import com.voovoo.antlr.json.JSONParser;
import com.voovoo.antlr.json.JSONParser.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

import static java.lang.System.exit;
import static java.lang.System.out;

public class GsonGenerator extends JSONBaseListener {

    private static void printUsage() {
        System.out.println("Invocation parameters:");
        System.out.println("\tMain class name: -c [MainClass]");
        System.out.println("\tJson file path: -f [JsonFile]");
        System.out.println("\tPackage name: -p [package]");
        System.out.println("\tOutput directory: -d [directory]");
    }

    public static void main(String[] args) throws Exception {

        Options options = new Options();

        options.addOption("c", true, "main class name");
        options.addOption("f", true, "path to json file");
        options.addOption("p", true, "package name");
        options.addOption("d", true, "output directory");
        options.addOption("h", false, "usage");

        CommandLineParser parser = new DefaultParser();
        String mainClass = null;
        String packageName = null;
        String outputDirectory = null;
        String jsonPath = null;

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                printUsage();
                exit(0);
            }

            if (!cmd.hasOption("c")) {
                System.out.println("Missing main class name!");
                printUsage();
                exit(1);
            } else {

                mainClass = cmd.getOptionValue("c");
            }

            if (!cmd.hasOption("f")) {
                System.out.println("Missing json file to process!");
                printUsage();
                exit(1);
            } else {
                jsonPath = cmd.getOptionValue("f");
            }

            if (cmd.hasOption("p")) {
                packageName = cmd.getOptionValue("p");
            }

            if (cmd.hasOption("d")) {
                outputDirectory = cmd.getOptionValue("d");
            } else {
                outputDirectory = ".";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        GsonGenerator generator = new GsonGenerator();

        generator.setRootClassName(mainClass);
        generator.setPackageName(packageName);
        generator.setOutputDirectory(outputDirectory);
        generator.setJsonPath(jsonPath);

        generator.run();

    }

    public String getRootClassName() {
        return rootClassName;
    }

    public void setRootClassName(String rootClassName) {
        this.rootClassName = rootClassName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    private String rootClassName = null;
    private String packageName = null;
    private String outputDirectory = null;
    private String jsonPath = null;


    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public ArrayList<ClassDef> generateClasses(String jsonFilePath,
                                               String rootClassName,
                                               String packageName) throws IOException {


        CharStream stream = CharStreams.fromFileName(jsonFilePath);

        JSONLexer lexer = new JSONLexer(stream);
        JSONParser parser = new JSONParser(new CommonTokenStream(lexer));

        parser.addParseListener(this);

        this.rootClassName = rootClassName;
        this.packageName = packageName;

        parser.json();

        return classDefs;
    }

    private void run() throws Exception {

        CharStream stream = CharStreams.fromFileName(jsonPath);

        JSONLexer lexer = new JSONLexer(stream);
        JSONParser parser = new JSONParser(new CommonTokenStream(lexer));

        parser.addParseListener(this);

        parser.json();

        DumpAction dumpClasses = new DumpAction();

        dumpClasses.dump(classDefs, outputDirectory);
    }

    private ArrayDeque<EntityNode> nodes = new ArrayDeque<EntityNode>();
    private ArrayDeque<EntityNode> containers = new ArrayDeque<EntityNode>();


    private EntityNode lastValue = null;
    private EntityNode lastContainer = null;

    private ArrayList<ClassDef> classDefs = null;

    private void prettyPrint(EntityNode root, int level) {

        ClassGenerator generator = new ClassGenerator();

        for (EntityNode node : root.getEntities()) {

            for (int ii = 0; ii < level; ++ii) {
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

        if (rootClassName != null) {
            lastContainer.setName(rootClassName);
        }

        ClassGenerator generator = new ClassGenerator();

        if (this.packageName != null) {
            generator.setPackage(this.packageName);
        }

        ArrayList<ClassDef> defs = generator.findClassDefinitions(lastContainer);

        classDefs = defs;
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
