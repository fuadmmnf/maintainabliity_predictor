import git.PackageParser;
import git.ReleaseParser;
import metrics.DatasetGenerator;
import models.DiscardedPackage;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String projectName = "hibernate-orm";
        String projectOwner = "hibernate";

        String projectPath = "src/main/resources/gitprojects/".replaceAll("/", File.separator) + projectName;
        String datasetPath = "src/main/resources/dataset/".replaceAll("/", File.separator) + projectName;
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
                releaseParser.checkoutRelease(release.split("~")[1]);
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
                releaseParser.checkoutRelease(releases.get(releases.size() - 1).split("~")[1]);
            }


        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }
}
