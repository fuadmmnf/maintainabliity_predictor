import git.PackageParser;
import git.ReleaseParser;
import metrics.MetricsRunner;
import models.DiscardedPackage;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String projectPath = "src/main/resources/gitprojects/seata";
        PackageParser packageParser = new PackageParser();
        try {

            //collect releases
            ReleaseParser releaseParser = new ReleaseParser(projectPath);
            List<String> releases = releaseParser.parseReleaseHistory("https://api.github.com/repos/seata/seata/releases");

            //generate discarded package list
            List<DiscardedPackage> discardedPackages = packageParser.generateDiscardedPackages(releaseParser, projectPath, releases);
            System.out.println(discardedPackages.get(0).toJSON());

            System.exit(1);
            //calculate metrics by release
            for (String release : releases) {
                releaseParser.checkoutRelease(release);
                MetricsRunner.calculateMetricsByDirectory(projectPath, release);
            }
            if (releases.size() != 0) {
                releaseParser.checkoutRelease(releases.get(releases.size() - 1));
            }


        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }
}
