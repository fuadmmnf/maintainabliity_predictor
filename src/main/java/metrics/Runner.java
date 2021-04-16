package metrics;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKNotifier;
import com.github.mauricioaniche.ck.ResultWriter;

import java.io.File;
import java.io.IOException;

public class Runner {
    public static void main(String[] args) throws IOException {

//        if (args == null || args.length < 1) {
//            System.out.println("Usage java -jar ck.jar <projectPath to project> <use Jars=true|false> <max files per partition, 0=automatic selection> <print variables and fields metrics? True|False>");
//            System.exit(1);
//        }

        String projectPath = new File("src/main/resources/gitprojects/libgdx-master").getAbsolutePath();
        String out1 = new File("src/main/resources/dataset/class.csv").getAbsolutePath();
        String out2 = new File("src/main/resources/dataset/method.csv").getAbsolutePath();
        String out3 = new File("src/main/resources/dataset/variable.csv").getAbsolutePath();
        String out4 = new File("src/main/resources/dataset/field.csv").getAbsolutePath();

        // use jars?
        boolean useJars = false;
        if(args.length >= 2)
            useJars = Boolean.parseBoolean(args[1]);

        // number of files per partition?
        int maxAtOnce = 0;
        if(args.length >= 3)
            maxAtOnce = Integer.parseInt(args[2]);

        // variables and field results?
        boolean variablesAndFields = true;
        if(args.length >= 4)
            variablesAndFields = Boolean.parseBoolean(args[3]);


        ResultWriter writer = new ResultWriter(out1, out2, out3, out4, variablesAndFields);

        new CK(useJars, maxAtOnce, variablesAndFields).calculate(projectPath, new CKNotifier() {
            @Override
            public void notify(CKClassResult result) {
                try {
                    writer.printResult(result);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void notifyError(String sourceFilePath, Exception e) {
                System.err.println("Error in " + sourceFilePath);
                e.printStackTrace(System.err);
            }
        });

        writer.flushAndClose();
    }
}
