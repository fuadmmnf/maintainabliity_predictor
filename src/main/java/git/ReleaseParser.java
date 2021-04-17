package git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ReleaseParser {

    public static void main(String[] args) throws InvalidRemoteException, TransportException, GitAPIException, IOException {

        PackageParser packageParser = new PackageParser();
        List<String> releases = new ArrayList<>();

        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        Repository repository = repositoryBuilder.setGitDir(new File(ProjectConstants.PATH + "\\.git"))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .setMustExist(true)
                .build();

        Git git = new Git(repository);

//        var api =  "https://api.github.com/repositories/189011689/releases";
//        var api =  "https://api.github.com/repos/elastic/elasticsearch/releases";
        var api = "https://api.github.com/repos/mybatis/mybatis-3/releases";
        var request = HttpRequest.newBuilder().GET().uri(URI.create(api)).build();
        var client = HttpClient.newBuilder().build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsArray = new JSONArray(response.body());


            for (int i = jsArray.length() - 1; i >= 0; i--) {
                JSONObject jsonObject = jsArray.getJSONObject(i);
                releases.add(jsonObject.get("tag_name").toString());
            }

            packageParser.parsePackage(releases, git);

        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
