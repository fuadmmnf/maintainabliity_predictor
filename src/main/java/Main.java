import git.PackageParser;
import git.ReleaseParser;
import metrics.DatasetGenerator;
import metrics.MetricsRunner;
import models.DiscardedPackage;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String projectName = "seata";
        String projectOwner = "seata";

        String projectPath = "src/main/resources/gitprojects/" + projectName;
        String datasetPath = "src/main/resources/dataset/" + projectName;
        PackageParser packageParser = new PackageParser();
        try {

            //collect releases
            ReleaseParser releaseParser = new ReleaseParser(projectPath);
            List<String> releases = releaseParser.parseReleaseHistory("https://api.github.com/repos/" + projectOwner + "/" + projectName + "/releases");

            //generate discarded package list
            List<DiscardedPackage> discardedPackages = packageParser.generateDiscardedPackages(releaseParser, projectPath, releases);
            System.out.println(discardedPackages.get(0).toJSON());

            System.exit(1);
            //calculate metrics by release
            for (String release : releases) {
                releaseParser.checkoutRelease(release);
                //generate package to find metric in release
                for (DiscardedPackage discardedPackage : discardedPackages) {
                    if (discardedPackage.isAvailaleInRelease(release)) {
                        DatasetGenerator.calculateMetricsByDirectory(discardedPackage.getPackagePath(), datasetPath, release, discardedPackage.getPackageName().replaceAll("\\.", "_"));
                    }
                }
            }
            if (releases.size() != 0) {
                releaseParser.checkoutRelease(releases.get(releases.size() - 1));
            }


        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }
}
