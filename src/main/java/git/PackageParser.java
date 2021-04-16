package git;

import models.DiscardedPackage;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.JSONObject;
import pakage_checker.PackageListCalculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PackageParser {

    private ArrayList<DiscardedPackage> arrayStringToDiscardedArray(String version, ArrayList<String> packageListString) {
        ArrayList<DiscardedPackage> packageListDiscarded = new ArrayList<DiscardedPackage>();
        for (int i = 0; i < packageListString.size(); i++) {
            String packageName = packageListString.get(i);
            String lifetime = version;
            List<Map<String, Double>> previousReleaseMetices = new ArrayList<Map<String, Double>>();
            DiscardedPackage discardedPackage = new DiscardedPackage(packageName, lifetime, previousReleaseMetices);
            packageListDiscarded.add(discardedPackage);
        }

        return packageListDiscarded;
    }

    private ArrayList<String> addLocalPackageToAllPackageList(ArrayList<String> localPackageList) {
        ArrayList<String> allPackageList = new ArrayList<String>();
        for (int i = 0; i < allPackageList.size(); i++) {
            if(!localPackageList.contains(allPackageList.get(i))){
                allPackageList.remove(i);
            }
//            allPackageList.add(localPackageList.get(i));
        }
        for (int i = 0; i < localPackageList.size(); i++) {
            if(!allPackageList.contains(localPackageList.get(i))){
                allPackageList.add(localPackageList.get(i));
            }
        }
        return allPackageList;
    }

    public void parsePackage(List<String> releases, Git git) throws GitAPIException {

        System.out.println("package parsing.......");
        ArrayList<DiscardedPackage> unMaintablePackageList = new ArrayList<DiscardedPackage>();
        ArrayList<String> allPackageList = new ArrayList<String>();
        ArrayList<DiscardedPackage> allPackageListDiscarded = new ArrayList<DiscardedPackage>();
        String packageName = "";
        String lifetime = "";
        List<Map<String, Double>> previousReleaseMetices = new ArrayList<Map<String, Double>>();
        DiscardedPackage discardedPackage = new DiscardedPackage(packageName, lifetime, previousReleaseMetices);

        for (int i = 0; i < releases.size(); i++) {
            System.out.println(releases.get(i));
            System.out.println("Not safe!!");
            git.checkout()
                    .setCreateBranch(false)
                    .setName(releases.get(i))
                    .setStartPoint("refs/tags/" + releases.get(i))
                    .call();

            try {
//                String secondProjectFilePath = "F:\\IIT 8th Semester\\Software Metrics\\elastisearch\\mybatis\\mybatis-3";
//                String firstProjectFilePath = "F:\\IIT 8th Semester\\Software Metrics\\elastisearch\\mybatis\\mybatis-3 - Copy";

                PackageListCalculationForGit packageListCalculation = new PackageListCalculationForGit();

                ArrayList<String> localPackageList = new ArrayList<String>();
                ArrayList<DiscardedPackage> localPackageListDiscarded = new ArrayList<DiscardedPackage>();

                localPackageList = packageListCalculation.getPackageList(ProjectConstants.PATH);
                localPackageListDiscarded = arrayStringToDiscardedArray(releases.get(i), localPackageList);

                System.out.println("All = " + allPackageList.size());
                System.out.println("Local = " + localPackageList.size());
                if (!allPackageList.isEmpty()) {
                    unMaintablePackageList = packageListCalculation.getDiffenceInArrayDiscarded(allPackageListDiscarded,
                            localPackageListDiscarded,releases.get(i-1));
                }
                allPackageList = addLocalPackageToAllPackageList(localPackageList);
                allPackageListDiscarded = arrayStringToDiscardedArray(releases.get(i), allPackageList);
//                unMaintablePackageList = packageListCalculation.getDiffenceInArray(packageListDiscarded,);
                System.out.println("UnMaintainable = " + unMaintablePackageList.size());

                System.out.println("\n\nSafe now!!!!!!!!");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        for (int i = 0; i < unMaintablePackageList.size(); i++) {
            JSONObject json = unMaintablePackageList.get(i).toJSON();
            System.out.println(json.toString());

        }

    }
}
