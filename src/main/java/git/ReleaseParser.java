package git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Constants;
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
    private final Git git;

    public ReleaseParser(String projectPath) throws IOException {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        Repository repository = repositoryBuilder.setGitDir(new File(projectPath + File.separator + ".git"))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .setMustExist(true)
                .build();

        this.git = new Git(repository);
    }

    public List<String> parseReleaseHistory(String gitApiReleasesURL) throws InvalidRemoteException, TransportException, GitAPIException, IOException {

        List<String> releases = new ArrayList<>();


        var request = HttpRequest.newBuilder().GET().uri(URI.create(gitApiReleasesURL)).build();
        var client = HttpClient.newBuilder().build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsArray = new JSONArray(response.body());


            for (int i = jsArray.length() - 1; i >= 0; i--) {
                JSONObject jsonObject = jsArray.getJSONObject(i);
                releases.add(jsonObject.get("tag_name").toString());
            }
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return releases;
    }

    public void checkoutRelease(String release) throws GitAPIException {
//        git.reset().setMode(ResetCommand.ResetType.HARD).setRef(Constants.HEAD);
        git.checkout()
                .setCreateBranch(false)
                .setName(release)
                .setStartPoint("refs/tags/" + release)
                .call();
    }
}
