package 
pakage_checker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CheckPackageMain {

	
	public static void main(String[] args) {
		String secondProjectFilePath = "F:\\IIT 8th Semester\\Software Metrics\\elastisearch\\mybatis\\mybatis-3";
		String firstProjectFilePath = "F:\\IIT 8th Semester\\Software Metrics\\elastisearch\\mybatis\\mybatis-3 - Copy";

		PackageListCalculation packageListCalculation = new PackageListCalculation();
		ArrayList <String> packageList1 = new ArrayList<String>();
		ArrayList <String> packageList2 = new ArrayList<String>();
		packageList1 = packageListCalculation.getPackageList(firstProjectFilePath);
		packageList2 = packageListCalculation.getPackageList(secondProjectFilePath);

		System.out.println("First = " + packageList1.size());
		System.out.println("Second = " + packageList2.size());
		ArrayList <String> unMaintablePackageList = new ArrayList<String>();
		unMaintablePackageList = packageListCalculation.getDiffenceInArray(packageList1, packageList2);
		System.out.println("Last = " + unMaintablePackageList.size());
//		packageListCalculation.printPackage(unMaintablePackageList);
		
	}
}
