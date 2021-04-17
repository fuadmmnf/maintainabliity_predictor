package metrics;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKNotifier;
import com.github.mauricioaniche.ck.ResultWriter;

import java.io.File;
import java.io.IOException;

public class DatasetGenerator {
    public static void calculateMetricsByDirectory(String rootpath, String datasetRoot, String releasetag, String packageId) throws IOException {

//        if (args == null || args.length < 1) {
//            System.out.println("Usage java -jar ck.jar <projectPath to project> <use Jars=true|false> <max files per partition, 0=automatic selection> <print variables and fields metrics? True|False>");
//            System.exit(1);
//        }

        File projectFile = new File(rootpath);
        if (!projectFile.exists())
            return;

        String projectPath = projectFile.getAbsolutePath();
        File dir = new File(datasetRoot + File.separator + releasetag + File.separator + packageId);
        if (!dir.exists()) dir.mkdirs();
        String out1 = new File(datasetRoot + File.separator + releasetag + File.separator + packageId + File.separator + "class.csv").getAbsolutePath();
        String out2 = new File(datasetRoot + File.separator + releasetag + File.separator + packageId + File.separator + "method.csv").getAbsolutePath();
        String out3 = new File(datasetRoot + File.separator + releasetag + File.separator + packageId + File.separator + "variable.csv").getAbsolutePath();
        String out4 = new File(datasetRoot + File.separator + releasetag + File.separator + packageId + File.separator + "field.csv").getAbsolutePath();

        // use jars?
        boolean useJars = false;
        // number of files per partition?
        int maxAtOnce = 0;
        // variables and field results?
        boolean variablesAndFields = true;

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
