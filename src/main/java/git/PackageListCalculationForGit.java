package git;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PackageListCalculationForGit {

	public ArrayList<String> getPackageList(String filePath) {
		// TODO Auto-generated method stub
		ArrayList<String> packageList = new ArrayList<String>();
		File folder = new File(filePath);
		chooseFolder(folder,packageList);
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
		for (int i = 0; i < packageList.size(); i++) {
			System.out.println(packageList.get(i));
		}
	}

	boolean isSrcFolder = false;

	private void chooseFolder(final File folder,ArrayList<String> packageList) {
		String temp = "";

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
//				 System.out.println(fileEntry.getName());
//				 System.out.println(folder.getName());

				chooseFolder(fileEntry,packageList);

			} else {
				if (fileEntry.isFile()) {
					temp = fileEntry.getName();
					if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("java")) {

						// System.out.println("File= " + folder.getAbsolutePath()+ "\\" +
						// fileEntry.getName());

						String path = folder.getAbsolutePath() + "\\" + fileEntry.getName();
//						System.out.println(path);

						try {
							if (folder.getParent().contains("src") && !folder.getParent().contains("test")) {
								chooseFile(path,packageList);
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

	private void chooseFile(String fileName,ArrayList<String> packageList) throws IOException {

		File file = new File(fileName);

		BufferedReader br = new BufferedReader(new FileReader(file));
		boolean fileShow = false;

		String st = "";
		int lineNumber = 0;

		while ((st = br.readLine()) != null) {
			lineNumber++;

			String currentLine = st;
			currentLine = currentLine.trim();
			String merger = "";
			if (currentLine.contains("package")) {
				merger = currentLine;
				String stTemp = st;
				for (; st != null;) {
					// for ( ;(st = br.readLine()) != null; ) {

					st = st.trim();
					stTemp = stTemp.trim();

					if (stTemp.contains(";")) {

//						 System.out.println("Line Number " + lineNumber );
						String result = stTemp.toString();
						result = result.replace("package", "").replace(";", "").trim();
//						 System.out.println(result);

						if (!packageList.contains(result)) {
							packageList.add(result);
						}
						fileShow = true;

						break;
					}
					merger = merger + st;

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
				if (fileShow == true) {
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
		if (fileShow == true) {
//			System.out.println(fileName  + "   done!!!!!!!!!!!");
		}

	}
	public  ArrayList<String> getDiffenceInArray(ArrayList<String> previousVersionPackageList,ArrayList<String> nextVersionPackageList){
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i=0;i<previousVersionPackageList.size();i++) {
			if(!nextVersionPackageList.contains(previousVersionPackageList.get(i))) {
				result.add(previousVersionPackageList.get(i));
			}
		}
		
		return result;
		
	}
}
