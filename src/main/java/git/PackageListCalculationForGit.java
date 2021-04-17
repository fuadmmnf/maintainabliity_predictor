package git;

import models.DiscardedPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class PackageListCalculationForGit {

    public ArrayList<String> getPackageList(String filePath) {
        // TODO Auto-generated method stub
        ArrayList<String> packageList = new ArrayList<String>();
        File folder = new File(filePath);
        chooseFolder(folder, packageList);
//		String filePath = "F:\\IIT 8th Semester\\Software Metrics\\elastisearch\\librarydx\\ck\\src\\test\\java\\com\\github\\mauricioaniche\\ck\\integration\\IntegrationTest.java";
//		try {
//			chooseFile(filePath);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		printPackage(packageList);
        return packageList;

    }

    public void printPackage(ArrayList<String> packageList) {
        System.out.println("\n\nPakage Array List --------------- \n");
        for (String s : packageList) {
            System.out.println(s);
        }
    }

    boolean isSrcFolder = false;

    private void chooseFolder(final File folder, ArrayList<String> packageList) {
        String temp = "";

        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
//				 System.out.println(fileEntry.getName());
//				 System.out.println(folder.getName());

                chooseFolder(fileEntry, packageList);

            } else {
                if (fileEntry.isFile()) {
                    temp = fileEntry.getName();
                    if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("java")) {

                        // System.out.println("File= " + folder.getAbsolutePath()+ "\\" +
                        // fileEntry.getName());

                        String fileName = folder.getAbsolutePath() + File.separator + fileEntry.getName();
                        String path = folder.getAbsolutePath();
//						System.out.println(path);

                        try {
                            if (folder.getParent().contains(File.separator + "src" + File.separator) && !folder.getParent().contains(File.separator + "test" + File.separator)) {
                                chooseFile(fileName,path, packageList);
                            }

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                }

            }
        }
    }

    private void chooseFile(String fileName, String path,ArrayList<String> packageList) throws IOException {

        File file = new File(fileName);

        BufferedReader br = new BufferedReader(new FileReader(file));
        boolean fileShow = false;

        String st = "";
        int lineNumber = 0;

        while ((st = br.readLine()) != null) {
            lineNumber++;

            String currentLine = st;
            currentLine = currentLine.trim();
            StringBuilder merger = new StringBuilder();
            if (currentLine.contains("package")) {
                merger = new StringBuilder(currentLine);
                String stTemp = st;
                while (st != null) {
                    // for ( ;(st = br.readLine()) != null; ) {

                    st = st.trim();
                    stTemp = stTemp.trim();

                    if (stTemp.contains(";")) {

//						 System.out.println("Line Number " + lineNumber );
                        String result = stTemp.toString();
                        result = result.replace("package", "").replace(";", "").trim();
//						 System.out.println(result);

                        if (!packageList.contains(path + "," + result)) {
                            packageList.add(path + "," + result);
                        }
                        fileShow = true;

                        break;
                    }
                    merger.append(st);

                    lineNumber++;
                    if (stTemp.contains(";")) {
                        stTemp = st;
                        lineNumber--;
                        break;
                    } else {
                        st = br.readLine();

                    }
                    stTemp = st;

                }
                if (fileShow) {
                    break;
                }
                /*
                 * if(merger!="") { System.out.println("Line Number " + lineNumber );
                 * System.out.println("Merger = "+ merger);
                 *
                 * }
                 */
            }

        }
        br.close();
        if (fileShow) {
//			System.out.println(fileName  + "   done!!!!!!!!!!!");
        }

    }

    public ArrayList<String> getDiffenceInArray(ArrayList<String> previousVersionPackageList, ArrayList<String> nextVersionPackageList) {
        ArrayList<String> result = new ArrayList<String>();

        for (String s : previousVersionPackageList) {
            if (!nextVersionPackageList.contains(s)) {
                result.add(s);
            }
        }

        return result;

    }

    public ArrayList<DiscardedPackage> getDiffenceInArrayDiscarded(ArrayList<String> allPackageList,
                                                                   ArrayList<String> localPackageList,
                                                                   ArrayList<DiscardedPackage> allPackageListDiscarded,
                                                                   ArrayList<DiscardedPackage> localPackageListDiscarded,
                                                                   String lastVersion) {
        ArrayList<DiscardedPackage> result = new ArrayList<DiscardedPackage>();

        for (int i = 0; i < allPackageList.size(); i++) {
            if (!localPackageList.contains(allPackageList.get(i))) {
                allPackageListDiscarded.get(i).setLifetime(
                        allPackageListDiscarded.get(i).getLifetime() + "_" + lastVersion
                );
                result.add(allPackageListDiscarded.get(i));

            }
            else{
                allPackageListDiscarded.get(i).setVersionLength(allPackageListDiscarded.get(i).getVersionLength()+1);
            }
        }

        return result;

//        All package = 54
//        Local = 54
//        UnMaintainable = 12
//        {"package_name":["org.apache.ibatis.scripting.defaults"],"lifetime":["mybatis-3.2.0_mybatis-3.2.0"],"length":[0],"metrics":[[]]}
//        {"package_name":["org.apache.ibatis.scripting"],"lifetime":["mybatis-3.2.0_mybatis-3.2.0"],"length":[28],"metrics":[[]]}
//        {"package_name":["org.apache.ibatis.scripting.xmltags"],"lifetime":["mybatis-3.2.0_mybatis-3.2.0"],"length":[0],"metrics":[[]]}
//        {"package_name":["org.apache.ibatis"],"lifetime":["mybatis-3.2.0_mybatis-3.1.0"],"length":[2],"metrics":[[]]}
//        {"package_name":["org.apache.ibatis.migration.commands"],"lifetime":["mybatis-3.0.1_mybatis-3.0.1"],"length":[0],"metrics":[[]]}
//        {"package_name":["org.apache.ibatis.ognl"],"lifetime":["mybatis-3.0.1_mybatis-3.0.3"],"length":[1],"metrics":[[]]}
//        {"package_name":["org.apache.ibatis.type"],"lifetime":["mybatis-3.2.0_mybatis-3.0.4"],"length":[8],"metrics":[[]]}
//        {"package_name":["org.apache.ibatis.builder.xml.dynamic"],"lifetime":["mybatis-3.1.1_mybatis-3.0.4_mybatis-3.2.4"],"length":[5],"metrics":[[]]}
//        {"package_name":["org.apache.ibatis.migration"],"lifetime":["mybatis-3.0.1_mybatis-3.0.4"],"length":[5],"metrics":[[]]}
//        {"package_name":["org.apache.ibatis.ognl"],"lifetime":["mybatis-3.0.2_mybatis-3.0.4"],"length":[20],"metrics":[[]]}
//        {"package_name":["org.apache.ibatis.builder.xml.dynamic"],"lifetime":["mybatis-3.1.1_mybatis-3.0.4_mybatis-3.2.4"],"length":[5],"metrics":[[]]}
//        {"package_name":["org.apache.ibatis.metadata"],"lifetime":["mybatis-3.2.0_mybatis-3.2.5"],"length":[10],"metrics":[[]]}


//        All package = 54
//        Local = 54
//        UnMaintainable = 5
//        {"package_name":["org.apache.ibatis.type"],"lifetime":["mybatis-3.2.0_mybatis-3.0.4"],"length":[8],"metrics":[[]]}
//        {"package_name":["org.apache.ibatis.builder.xml.dynamic"],"lifetime":["mybatis-3.1.1_mybatis-3.0.4_mybatis-3.2.4"],"length":[5],"metrics":[[]]}
//        {"package_name":["org.apache.ibatis.migration"],"lifetime":["mybatis-3.0.1_mybatis-3.0.4"],"length":[5],"metrics":[[]]}
//        {"package_name":["org.apache.ibatis.builder.xml.dynamic"],"lifetime":["mybatis-3.1.1_mybatis-3.0.4_mybatis-3.2.4"],"length":[5],"metrics":[[]]}
//        {"package_name":["org.apache.ibatis.metadata"],"lifetime":["mybatis-3.2.0_mybatis-3.2.5"],"length":[10],"metrics":[[]]}

    }
}
