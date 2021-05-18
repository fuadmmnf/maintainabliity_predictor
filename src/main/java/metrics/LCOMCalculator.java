package metrics;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class LCOMCalculator {

    public static void main(String[] args) throws FileNotFoundException {
        CompilationUnit compilationUnit = StaticJavaParser.parse(new File("src/main/java/metrics/test/TCCTest.java"));
        compilationUnit.accept(new ClassVisitor(), null);
    }

    private static class ClassVisitor extends VoidVisitorAdapter<Void> {
        private final List<VariableDeclarator> attributes = new ArrayList<>();

        private List<String> getScopeVariables(BlockStmt block) {
            List<String> scopedVariables = new ArrayList<>();
            block.findAll(VariableDeclarationExpr.class).forEach(field -> {
                scopedVariables.addAll(field.getVariables().stream().map(f -> f.getName().asString()).collect(Collectors.toList()));
            });

            return scopedVariables;
        }

        private List<String> getUsedFieldsInMethod(BlockStmt block) {
            List<String> usedFields = new ArrayList<>();
            block.findAll(FieldAccessExpr.class).forEach(field -> {
                usedFields.add(field.getName().asString());
            });
//            System.out.println(usedFields);
            return usedFields;
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        /* here you can access the attributes of the method.
         this method will be called for all methods in this
         CompilationUnit, including inner class methods */
            for (FieldDeclaration field : n.getFields()) {
                attributes.addAll(field.getVariables());
            }


            List<MethodDeclaration> publicMethods = n.getMethods();

//            for (MethodDeclaration method : publicMethods
//            ) {
////                System.out.println(method.getName());
//                if (method.getBody().isEmpty())
//                    continue;
//                List<String> methodClassAttributesUsed = getUsedFieldsInMethod(method.getBody().get());
//                System.out.println(methodClassAttributesUsed);
//                List<String> methodClassAttributesUsed2 = (getScopeVariables(method.getBody().get()));
//                System.out.println(methodClassAttributesUsed2);
//            }


            int directlyConnectedMethodCount = 0;
            Map<String, Integer> fieldAccessMethodCount = new HashMap<>();
            for (VariableDeclarator field : attributes) {
                fieldAccessMethodCount.put(field.getName().asString(), 0);
            }
            for (MethodDeclaration method1 : publicMethods) {
                if (method1.getBody().isEmpty())
                    continue;
                List<String> method1ClassAttributesUsed = getUsedFieldsInMethod(method1.getBody().get());
                method1ClassAttributesUsed.removeAll(getScopeVariables(method1.getBody().get()));

                for (String fieldUsed : method1ClassAttributesUsed) {
                    fieldAccessMethodCount.put(fieldUsed, fieldAccessMethodCount.get(fieldUsed) + 1);
                }
            }

            int totalFieldAccess = 0;
            for (VariableDeclarator field : attributes) {
                totalFieldAccess += fieldAccessMethodCount.get(field.getName().asString());
            }
            double lcom = 1.0 - ((double) totalFieldAccess / (publicMethods.size() * attributes.size()));
            System.out.println("Lack of Cohesion of Methods: " + lcom);

            super.visit(n, arg);
        }
    }

}

