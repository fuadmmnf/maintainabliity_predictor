package metrics;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.lang3.StringUtils;
import com.github.javaparser.ast.*;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WMCCalculator {

    public static void main(String[] args) throws FileNotFoundException {
        CompilationUnit compilationUnit = StaticJavaParser.parse(new File("src/main/java/metrics/test/WMCTest.java"));
        compilationUnit.accept(new ClassVisitor(), null);
    }

    private static class ClassVisitor extends VoidVisitorAdapter<Void> {
        private final List<VariableDeclarator> attributes = new ArrayList<>();
        private Integer cycometricComplexity = 0;

        private int getConditionalStatement(Node node) {
            int complexity = 0;

            for (IfStmt ifStmt : node.getChildNodesByType(IfStmt.class)) {
                // We found an "if" - cool, add one.
                complexity++;
                if (ifStmt.getElseStmt().isPresent()) {
                    // This "if" has an "else"
                    if (ifStmt.getElseStmt().get() instanceof IfStmt) {
                        // it's an "else-if". We already count that by counting the "if" above.
                    } else {
                        // it's an "else-something". Add it.
                        complexity++;
                    }
                }
            }
            return complexity;
        }

        private int increaseCCFromExpression(Node expression) {
            if (expression == null) {
                increaseCc();
                return 0;
            }

            containsIfTenary(expression);
//            if(!containsIfTenary(expression)) {
//                increaseCc();
//            }

            String expr = expression.toString().replace("&&", "&").replace("||", "|");
            int ands = StringUtils.countMatches(expr, "&");
            int ors = StringUtils.countMatches(expr, "|");

            increaseCc(ands + ors);
            return ands + ors;
        }


        private void containsIfTenary(Node expression) {

            var childNodes = expression.getChildNodes();
            int i = 0;
            for (IfStmt ifStmt : expression.getChildNodesByType(IfStmt.class)) {
                increaseCc();
                containsIfTenary(childNodes.get(i));

                i++;
//                    System.out.println("\nchild Nodes: "+ i + "\n"  + ( childNodes.get(i)).toString());

            }

//            if(expression.get instanceof IfStmt ) {
//                ParenthesizedExpression x = (ParenthesizedExpression) expression;
//                return containsIfTenary(x.getExpression());
//            } else if(expression instanceof InfixExpression) {
//                InfixExpression x = (InfixExpression) expression;
//                return containsIfTenary(x.getLeftOperand()) || containsIfTenary(x.getRightOperand());
//            } else if (expression instanceof ConditionalExpression) {
//                return true;
//            }

            return;

        }

        private void increaseCc(int increased) {
            cycometricComplexity += increased;
        }

        private void increaseCc() {
            cycometricComplexity++;
        }

        private void checkInnerLoop(Node node) {
            for (var statement : node.getChildNodes()) {
                if(statement instanceof IfStmt){
                    String expr = statement.toString().replace("&&", "&").replace("||", "|");
                    int andCount = StringUtils.countMatches(expr, "&");
                    int orCount = StringUtils.countMatches(expr, "|");

                    increaseCc(andCount + orCount);
                }
                if (statement instanceof ForStmt || statement instanceof IfStmt ||
                        statement instanceof ForEachStmt || statement instanceof WhileStmt ||
                        statement instanceof DoStmt || statement instanceof SwitchStmt ||
                        statement instanceof ThrowStmt || statement instanceof TryStmt ||
                        statement instanceof YieldStmt ) {
//
//                    System.out.println("Statement");
//                    System.out.println(statement.getClass());
//                    System.out.println(statement);
//                    System.out.println();
                    cycometricComplexity++;
//                    increaseCc();

                }

                checkInnerLoop(statement);
            }
        }

        private int getStatementCount(Node node) {
            int cc = 0;
            var childNodes = node.getChildNodes();

            for (int i = 0; i < childNodes.size(); i++) {
//                System.out.println();
//                System.out.println();
//                System.out.println(i);
//                System.out.println("\nchild Nodes: " + i + " \n" + (childNodes.get(i).toString()));
                if (childNodes.get(i) instanceof BlockStmt) {
//                    System.out.println(childNodes.get(i).getClass());
                    checkInnerLoop(childNodes.get(i));

                }
            }
            System.out.println("cc = " + cc);
            return cc;
        }

        private int getFor(Node node) {
            int cc = 0;

//            increaseCCFromExpression(node);
//            for (ForStmt ifStmt : node.getChildNodesByType(ForStmt.class)){
//                    increaseCc();
//
//
//                    System.out.println("\nchild Nodes: \n"  + ( ifStmt.toString()));
//
//            }
//            cycometricComplexity += getStatementCount(node);
//            increaseCCFromExpression(node);
            checkInnerLoop(node);
            return cycometricComplexity;
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
//
////            for (MethodDeclaration method : publicMethods
////            ) {
//////                System.out.println(method.getName());
////                if (method.getBody().isEmpty())
////                    continue;
////                List<String> methodClassAttributesUsed = getUsedFieldsInMethod(method.getBody().get());
////                System.out.println(methodClassAttributesUsed);
////                List<String> methodClassAttributesUsed2 = (getScopeVariables(method.getBody().get()));
////                System.out.println(methodClassAttributesUsed2);
////            }
//
//            int directlyConnectedMethodCount = 0;
//
//            for (int i = 0; i < publicMethods.size(); i++) {
//                MethodDeclaration method1 = publicMethods.get(i);
//
////                if (!method1.asMethodDeclaration().getModifiers().get(0).getKeyword().asString().equals("public") || method1.getBody().isEmpty())
////                    continue;
//
//                List<String> method1ClassAttributesUsed = getUsedFieldsInMethod(method1.getBody().get());
//                method1ClassAttributesUsed.removeAll(getScopeVariables(method1.getBody().get()));
//
//
//                for (int j = i + 1; j < publicMethods.size(); j++) {
//                    MethodDeclaration method2 = publicMethods.get(j);
//                    if (!method2.asMethodDeclaration().getModifiers().get(0).getKeyword().asString().equals("public") || method2.getBody().isEmpty())
//                        continue;
//                    List<String> method2ClassAttributesUsed = getUsedFieldsInMethod(method2.getBody().get());
//                    method2ClassAttributesUsed.removeAll(getScopeVariables(method2.getBody().get()));
//
//
//                    boolean isTightCohesion = method1ClassAttributesUsed.stream()
//                            .anyMatch(
//                                    new HashSet<>(method2ClassAttributesUsed)::contains);
//                    if (isTightCohesion) {
////                        System.out.println(method2ClassAttributesUsed);
//                        directlyConnectedMethodCount++;
//                    }
//                }
//            }
//
//            double tcc = (directlyConnectedMethodCount) / ((publicMethods.size() * (publicMethods.size() - 1)) / 2.0);
//            System.out.println("Tight Class Cohesion: " + tcc);


            for (int i = 0; i < publicMethods.size(); i++) {
                MethodDeclaration method1 = publicMethods.get(i);
                System.out.println(method1.getName());
                int condtionalStatementCount = getConditionalStatement(method1);
                getFor(method1);
                System.out.println("cycometricComplexity: " + cycometricComplexity);
                cycometricComplexity = 0;
//                System.out.println(method1.getName());
//                System.out.println("condtionalStatementCount: " + condtionalStatementCount);
            }
            super.visit(n, arg);
        }
    }

}

