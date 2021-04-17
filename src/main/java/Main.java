import git.ReleaseParser;
import metrics.MetricsRunner;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String projectPath = "src/main/resources/gitprojects/seata-develop";
        try {
            ReleaseParser releaseParser = new ReleaseParser(projectPath);
            List<String> releases = releaseParser.parseReleaseHistory("https://api.github.com/repositories/189011689/releases");

            for (String release : releases) {
                releaseParser.checkoutRelease(release);
                MetricsRunner.calculateMetricsByDirectory(projectPath, release);
            }
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }
}
