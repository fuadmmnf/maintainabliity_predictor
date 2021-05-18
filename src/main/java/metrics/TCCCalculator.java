package metrics;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TCCCalculator {
    public static void main(String[] args) {
        CompilationUnit compilationUnit = StaticJavaParser.parse("test/TCCTest.java");
        compilationUnit.accept(new ClassVisitor(), null);
    }

    private static class ClassVisitor extends VoidVisitorAdapter<Void> {
        final Pattern pattern = Pattern.compile("^((private|public|protected)?\\s+)?.*\\s+(\\w+);$");

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        /* here you can access the attributes of the method.
         this method will be called for all methods in this
         CompilationUnit, including inner class methods */
            for (FieldDeclaration field : n.getFields()) {
                // create a regex-matcher
                final Matcher matcher = pattern.matcher(field.getVariable(0).getName().asString());

                // if field matches regex
                if (matcher.matches()) {
                    // get the last group -> the fieldName
                    final String name = matcher.group(matcher.groupCount());
                    System.out.println("FieldName1: " + name);
                }
            }
            super.visit(n, arg);
        }
    }

}

