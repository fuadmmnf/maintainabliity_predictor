import git.PackageParser;
import git.ReleaseParser;
import metrics.DatasetGenerator;
import metrics.MetricsRunner;
import models.DiscardedPackage;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
//            System.out.println(discardedPackages.get(0).toJSON());

            //calculate metrics by release
            for (String release : releases) {
                releaseParser.checkoutRelease(release);
                //generate package to find metric in release
//                for (DiscardedPackage discardedPackage : discardedPackages) {
//                    if (discardedPackage.isAvailaleInRelease(release)) {
//                        DatasetGenerator.calculateMetricsByDirectory(discardedPackage.getPackagePath(), datasetPath, release, discardedPackage.getPackageName().replaceAll("\\.", "_"));
//                    }
//                }

                //trying with parallel streaming
                Stream<DiscardedPackage> stream = discardedPackages.parallelStream(); // true means use parallel stream
                stream.forEach(discardedPackage -> {
                    if (discardedPackage.isAvailaleInRelease(release)) {
                        try {
                            DatasetGenerator.calculateMetricsByDirectory(discardedPackage.getPackagePath(), datasetPath, release, discardedPackage.getPackageName().replaceAll("\\.", "_"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            if (releases.size() != 0) {
                releaseParser.checkoutRelease(releases.get(releases.size() - 1));
            }


        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }
}
