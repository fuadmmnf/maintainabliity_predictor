package git;

import models.DiscardedPackage;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.JSONObject;
import pakage_checker.PackageListCalculation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PackageParser {

    private ArrayList<DiscardedPackage> arrayStringToDiscardedArray(String version, ArrayList<String> packageListString) {
        ArrayList<DiscardedPackage> packageListDiscarded = new ArrayList<DiscardedPackage>();
        for (int i = 0; i < packageListString.size(); i++) {
            var split =  packageListString.get(i).split(",");
            String packageName = split[1];
            String packagePath = split[0];
            String lifetime = version;
            List<Map<String, Double>> previousReleaseMetices = new ArrayList<Map<String, Double>>();
            DiscardedPackage discardedPackage = new DiscardedPackage(packageName, packagePath,lifetime, previousReleaseMetices);
            packageListDiscarded.add(discardedPackage);
        }

        return packageListDiscarded;
    }

    private ArrayList<String> addLocalPackageToAllPackageList(
            ArrayList<String> allPackageList,ArrayList<String> localPackageList) {

        Iterator<String> iterator = allPackageList.iterator();
        while (iterator.hasNext()) {
            String packageName =iterator.next();
            if(!localPackageList.contains(packageName)){
                iterator.remove();
            }
        }

//
//        ArrayList<String> allPackageListCopy = new ArrayList<String>();
//        allPackageListCopy.addAll(allPackageList);
//        for (int i = 0; i < allPackageListCopy.size(); i++) {
//            if(!localPackageList.contains(allPackageListCopy.get(i))){
//                allPackageList.remove(i);
//            }
////            allPackageList.add(localPackageList.get(i));
//        }


        for (int i = 0; i < localPackageList.size(); i++) {
            if(!allPackageList.contains(localPackageList.get(i))){
                allPackageList.add(localPackageList.get(i));
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
        Iterator<DiscardedPackage> iterator = allPackageListDiscarded.iterator();
        while (iterator.hasNext()) {
            DiscardedPackage discardedPackage = iterator.next(); // must be called before you can call i.remove()
            String packageName = discardedPackage.getPackagePath() + "," +  discardedPackage.getPackageName();
            if(!localPackageList.contains(packageName)){
                iterator.remove();
            }
        }

//        ArrayList<String> allPackageListCopy = new ArrayList<String>();
//        allPackageListCopy.addAll(allPackageList);
//        for (int i = 0; i < allPackageListCopy.size(); i++) {
//            if(!localPackageList.contains(allPackageListCopy.get(i))){
//                allPackageList.remove(i);
//
//            }
////            allPackageList.add(localPackageList.get(i));
//        }
        for (int i = 0; i < localPackageList.size(); i++) {
            if(!allPackageList.contains(localPackageList.get(i))){
                allPackageList.add(localPackageList.get(i));
                allPackageListDiscarded.add(localPackageListDiscarded.get(i));
            }
        }
        return allPackageListDiscarded;
    }

    public void parsePackage(List<String> releases, Git git) throws GitAPIException {

        System.out.println("package parsing.......");
        ArrayList<DiscardedPackage> unMaintablePackageList = new ArrayList<DiscardedPackage>();
        ArrayList<String> allPackageList = new ArrayList<String>();
        ArrayList<DiscardedPackage> allPackageListDiscarded = new ArrayList<DiscardedPackage>();

        for (int i = 0; i < releases.size(); i++) {
            System.out.println(releases.get(i));
            System.out.println("\n\nNot safe!!");
            git.reset().setMode(ResetCommand.ResetType.HARD).call();
            git.checkout()
                    .setCreateBranch(false)
                    .setName(releases.get(i))
                    .setStartPoint("refs/tags/" + releases.get(i))
                    .call();

            System.out.println("Finish checkout!!");
            try {
                System.out.println("\n\nSafe now!!!!!!!!");
//                Thread.sleep(5000);
                Thread.sleep(1);
                System.out.println("Start Package calculation");

//                Thread.sleep(1);
//                String secondProjectFilePath = "F:\\IIT 8th Semester\\Software Metrics\\elastisearch\\mybatis\\mybatis-3";
//                String firstProjectFilePath = "F:\\IIT 8th Semester\\Software Metrics\\elastisearch\\mybatis\\mybatis-3 - Copy";

                PackageListCalculationForGit packageListCalculation = new PackageListCalculationForGit();

                ArrayList<String> localPackageList = new ArrayList<String>();
                ArrayList<DiscardedPackage> localPackageListDiscarded = new ArrayList<DiscardedPackage>();

                localPackageList = packageListCalculation.getPackageList(ProjectConstants.PATH);
                localPackageListDiscarded = arrayStringToDiscardedArray(releases.get(i), localPackageList);

                System.out.println("All package = " + allPackageList.size());

//                for (int j = 0; j < allPackageListDiscarded.size(); j++) {
//                    JSONObject json = allPackageListDiscarded.get(j).toJSON();
//                    System.out.println(json.toString());
////                    System.out.println(allPackageList.get(j));
//                }
                System.out.println("Local = " + localPackageList.size());

//                for (int j = 0; j < localPackageList.size(); j++) {
//                    System.out.println(localPackageList.get(j));
//                }
                if (!allPackageList.isEmpty() && i!=0) {
                    unMaintablePackageList.addAll(packageListCalculation.getDiffenceInArrayDiscarded(allPackageList,localPackageList,allPackageListDiscarded,
                            localPackageListDiscarded,releases.get(i-1)));
                }
                allPackageListDiscarded = addLocalPackageToAllPackageListDiscarded(allPackageList,localPackageList,
                        allPackageListDiscarded,localPackageListDiscarded);
                allPackageList = addLocalPackageToAllPackageList(allPackageList,localPackageList);

                if(i == releases.size()-1){
                    System.out.println("Last Release!");

                    for(int j = 0 ; j<allPackageListDiscarded.size() ; j++){
                        allPackageListDiscarded.get(j).setLifetime(
                                allPackageListDiscarded.get(j).getLifetime() + "_" + releases.get(i)
                        );
                    }
                }

//                unMaintablePackageList = packageListCalculation.getDiffenceInArray(packageListDiscarded,);
                System.out.println("UnMaintainable = " + unMaintablePackageList.size());


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        System.out.println("All package = " + allPackageListDiscarded.size());

//        for (int i = 0; i < allPackageListDiscarded.size(); i++) {
//            JSONObject json = allPackageListDiscarded.get(i).toJSON();
//            System.out.println(json.toString());
//
//        }


//        for (int i = 0; i < unMaintablePackageList.size(); i++) {
//            JSONObject json = unMaintablePackageList.get(i).toJSON();
//            System.out.println(json.toString());
//
//        }

        System.out.println("\n\nProblem packages ----- " );

        for (int i = 0; i < unMaintablePackageList.size(); i++) {
            var split = unMaintablePackageList.get(i).getLifetime().split("_");
            if(split.length >2){
                JSONObject json = unMaintablePackageList.get(i).toJSON();
                System.out.println(json.toString());
            }
        }

        ArrayList<DiscardedPackage> finalUnmaintainablePackageList = new ArrayList<DiscardedPackage>();

        for (int i = 0; i < unMaintablePackageList.size(); i++) {
            if(
//                    Integer.compare(unMaintablePackageList.get(i).getVersionLength(),3) != 1
                  unMaintablePackageList.get(i).getVersionLength()>3
            ){
                finalUnmaintainablePackageList.add( unMaintablePackageList.get(i));
            }
        }
        System.out.println("\n\nFinal UnMaintainable = " + finalUnmaintainablePackageList.size());

//        for (int i = 0; i < finalUnmaintainablePackageList.size(); i++) {
//            JSONObject json = finalUnmaintainablePackageList.get(i).toJSON();
//            System.out.println(json.toString());
//        }

    }
}
