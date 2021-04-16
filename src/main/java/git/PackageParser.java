package git;

import models.DiscardedPackage;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import pakage_checker.PackageListCalculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PackageParser {
    public void parsePackage(List<String> releases, Git git) throws GitAPIException {

        System.out.println("package parsing.......");
        ArrayList <DiscardedPackage> unMaintablePackageList = new ArrayList<DiscardedPackage>();
        ArrayList<String> packageList1 = new ArrayList<String>();
        String packageName = "";
        String lifetime = "";
        List<Map<String, Double>> previousReleaseMetices = new ArrayList<Map<String, Double>>();
        DiscardedPackage discardedPackage = new DiscardedPackage(packageName,lifetime,previousReleaseMetices);

        for(int i=0; i<releases.size(); i++){
            System.out.println(releases.get(i));
            System.out.println("Not safe!!");
            git.checkout()
                    .setCreateBranch(false)
                    .setName(releases.get(i))
                    .setStartPoint("refs/tags/"+releases.get(i))
                    .call();

            try {
//                String secondProjectFilePath = "F:\\IIT 8th Semester\\Software Metrics\\elastisearch\\mybatis\\mybatis-3";
//                String firstProjectFilePath = "F:\\IIT 8th Semester\\Software Metrics\\elastisearch\\mybatis\\mybatis-3 - Copy";

                PackageListCalculation packageListCalculation = new PackageListCalculation();

                ArrayList <String> packageList2 = new ArrayList<String>();
                packageList1 = packageListCalculation.getPackageList(ProjectConstants.PATH);

                System.out.println("First = " + packageList1.size());
                System.out.println("Second = " + packageList2.size());
                unMaintablePackageList = packageListCalculation.getDiffenceInArray(packageList1, packageList2);
                System.out.println("Last = " + unMaintablePackageList.size());

                System.out.println("\n\nSafe now!!!!!!!!");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
