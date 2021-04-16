package 
pakage_checker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CheckPackageMain {

	
	public static void main(String[] args) {
		String firstProjectFilePath = "F:\\IIT 8th Semester\\Software Metrics\\elastisearch\\librarydx\\ck";
		String secondProjectFilePath = "F:\\IIT 8th Semester\\Software Metrics\\elastisearch\\librarydx\\ck - Copy";

		PackageListCalculation packageListCalculation = new PackageListCalculation();
		ArrayList <String> packageList1 = new ArrayList<String>();
		ArrayList <String> packageList2 = new ArrayList<String>();
		packageList1 = packageListCalculation.getPackageList(firstProjectFilePath);
		packageList2 = packageListCalculation.getPackageList(secondProjectFilePath);

		ArrayList <String> unMaintablePackageList = new ArrayList<String>();
		unMaintablePackageList = packageListCalculation.getDiffenceInArray(packageList1, packageList2);
		packageListCalculation.printPackage(unMaintablePackageList);
		
	}
}
