package com.voovoo.antlr.printing;

import com.voovoo.antlr.entities.ClassDef;

import java.util.HashMap;

public class ClassPrinter {

    public String print(ClassDef def) {

        StringBuilder output = new StringBuilder();

        if (def.hasValidPackageName()) {
            output.append(def.getPackageName() + "\n\n");
        }

        output.append("class " + def.getName() + " {");

        HashMap<String, String> fields = def.getFields();

        for (String name :
                fields.keySet()) {

            String type = fields.get(name);

            output.append("\n\tprivate " + type + " " + name + ";");
        }

        output.append("\n}");

        return output.toString();

    }

    public String printField(String name, ClassDef classDef) {

        String type = classDef.getField(name);

        StringBuilder output = new StringBuilder("private ");

        output.append(type);
        output.append(" ");
        output.append(name);
        output.append(";");

        return output.toString();

    }

}
