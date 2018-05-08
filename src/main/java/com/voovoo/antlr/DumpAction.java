package com.voovoo.antlr;

import com.voovoo.antlr.entities.ClassDef;
import com.voovoo.antlr.entities.Printer;
import com.voovoo.antlr.printing.ClassPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DumpAction {

    public void dump(ClassDef def, String outputDir) throws IOException {

        if (Files.isDirectory(Paths.get(outputDir))) {

            final String packageName = def.getPackageName();
            final String packagePath = outputDir + '/' + packageName.replace('.', '/');

            Files.createDirectories(
                    Paths.get(packagePath));
            
            
            if (def.hasValidClassName()) {

                try (PrintWriter printWriter = new PrintWriter(Files.newBufferedWriter(
                        Paths.get(packagePath + "/" + def.getName() + ".java")))) {

                    ClassPrinter classPrinter = new ClassPrinter();

                    printWriter.print(classPrinter.print(def));
                }
            }

        } else {
            throw new IOException(outputDir + " directory does not exist!");
        }
    }

    public void dump(ArrayList<ClassDef> definitions, String outputDir) {
        
    }
}

