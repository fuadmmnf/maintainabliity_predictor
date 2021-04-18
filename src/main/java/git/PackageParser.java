package git;

import models.DiscardedPackage;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.json.JSONObject;
import pakage_checker.PackageListCalculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PackageParser {

    private ArrayList<DiscardedPackage> arrayStringToDiscardedArray(String version, ArrayList<String> packageListString) {
        ArrayList<DiscardedPackage> packageListDiscarded = new ArrayList<DiscardedPackage>();
        for (String s : packageListString) {
            var split = s.split(",");
            String packageName = split[1];
            String packagePath = split[0];
            List<Map<String, Double>> previousReleaseMetices = new ArrayList<Map<String, Double>>();
            DiscardedPackage discardedPackage = new DiscardedPackage(packageName, packagePath, version, previousReleaseMetices);
            packageListDiscarded.add(discardedPackage);
        }

        return packageListDiscarded;
    }

    private ArrayList<String> addLocalPackageToAllPackageList(
            ArrayList<String> allPackageList, ArrayList<String> localPackageList) {
        for (int i = 0; i < allPackageList.size(); i++) {
            if (!localPackageList.contains(allPackageList.get(i))) {
                allPackageList.remove(i);
            }
//            allPackageList.add(localPackageList.get(i));
        }
        for (String s : localPackageList) {
            if (!allPackageList.contains(s)) {
                allPackageList.add(s);
            }
        }
        return allPackageList;
    }

    private ArrayList<DiscardedPackage> addLocalPackageToAllPackageListDiscarded(
            ArrayList<String> allPackageList,
            ArrayList<String> localPackageList,
            ArrayList<DiscardedPackage> allPackageListDiscarded,
            ArrayList<DiscardedPackage> localPackageListDiscarded
    ) {
        for (int i = 0; i < allPackageList.size(); i++) {
            if (!localPackageList.contains(allPackageList.get(i))) {
                allPackageList.remove(i);
                allPackageListDiscarded.remove(i);
            }
//            allPackageList.add(localPackageList.get(i));
        }
        for (int i = 0; i < localPackageList.size(); i++) {
            if (!allPackageList.contains(localPackageList.get(i))) {
                allPackageList.add(localPackageList.get(i));
                allPackageListDiscarded.add(localPackageListDiscarded.get(i));
            }
        }
        return allPackageListDiscarded;
    }

    public List<DiscardedPackage> generateDiscardedPackages(ReleaseParser releaseParser, String projectPath, List<String> releases)  {

        ArrayList<DiscardedPackage> unMaintablePackageList = new ArrayList<DiscardedPackage>();
        ArrayList<String> allPackageList = new ArrayList<String>();
        ArrayList<DiscardedPackage> allPackageListDiscarded = new ArrayList<DiscardedPackage>();

        for (int i = 0; i < releases.size(); i++) {
            try {
                releaseParser.checkoutRelease(releases.get(i).split("~")[1]);
                PackageListCalculationForGit packageListCalculation = new PackageListCalculationForGit();


                ArrayList<String> localPackageList = new ArrayList<String>();
                ArrayList<DiscardedPackage> localPackageListDiscarded = new ArrayList<DiscardedPackage>();


                localPackageList = packageListCalculation.getPackageList(projectPath);
                localPackageListDiscarded = arrayStringToDiscardedArray(releases.get(i), localPackageList);

                if (!allPackageList.isEmpty() && i != 0) {
                    unMaintablePackageList.addAll(packageListCalculation.getDiffenceInArrayDiscarded(allPackageList, localPackageList, allPackageListDiscarded,
                            localPackageListDiscarded, releases.get(i - 1)));
                }
                allPackageListDiscarded = addLocalPackageToAllPackageListDiscarded(allPackageList, localPackageList,
                        allPackageListDiscarded, localPackageListDiscarded);
                allPackageList = addLocalPackageToAllPackageList(allPackageList, localPackageList);

//                unMaintablePackageList = packageListCalculation.getDiffenceInArray(packageListDiscarded,);
//                System.out.println("UnMaintainable = " + unMaintablePackageList.size());


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (releases.size() != 0) {
            try {
                releaseParser.checkoutRelease(releases.get(releases.size() - 1).split("~")[1]);
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }


        ArrayList<DiscardedPackage> finalUnmaintablePackageList = new ArrayList<DiscardedPackage>();

        for (DiscardedPackage discardedPackage : unMaintablePackageList) {
            if (
//                    Integer.compare(unMaintablePackageList.get(i).getVersionLength(),3) != 1
                    discardedPackage.getVersionLength() > 3
            ) {
                finalUnmaintablePackageList.add(discardedPackage);
            }
        }
        System.out.println("\n\nFinal UnMaintainable = " + finalUnmaintablePackageList.size());

//        for (DiscardedPackage discardedPackage : finalUnmaintablePackageList) {
//            JSONObject json = discardedPackage.toJSON();
//            System.out.println(json.toString());
//
//        }

        return finalUnmaintablePackageList;
    }
}
