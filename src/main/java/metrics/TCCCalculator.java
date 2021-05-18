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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.stream.Collectors;

public class TCCCalculator {

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
            for (int i = 0; i < publicMethods.size(); i++) {
                MethodDeclaration method1 = publicMethods.get(i);

                if (!method1.asMethodDeclaration().getModifiers().get(0).getKeyword().asString().equals("public") || method1.getBody().isEmpty())
                    continue;
                List<String> method1ClassAttributesUsed = getUsedFieldsInMethod(method1.getBody().get());
                method1ClassAttributesUsed.removeAll(getScopeVariables(method1.getBody().get()));


                for (int j = i + 1; j < publicMethods.size(); j++) {
                    MethodDeclaration method2 = publicMethods.get(j);
                    if (!method2.asMethodDeclaration().getModifiers().get(0).getKeyword().asString().equals("public") || method2.getBody().isEmpty())
                        continue;
                    List<String> method2ClassAttributesUsed = getUsedFieldsInMethod(method2.getBody().get());
                    method2ClassAttributesUsed.removeAll(getScopeVariables(method2.getBody().get()));


                    boolean isTightCohesion = method1ClassAttributesUsed.stream()
                            .anyMatch(
                                    new HashSet<>(method2ClassAttributesUsed)::contains);
                    if (isTightCohesion) {
//                        System.out.println(method2ClassAttributesUsed);
                        directlyConnectedMethodCount++;
                    }
                }
            }

            double tcc = (directlyConnectedMethodCount) / ((publicMethods.size() * (publicMethods.size() - 1)) / 2.0);
            System.out.println("Tight Class Cohesion: " + tcc);

            super.visit(n, arg);
        }
    }

}

