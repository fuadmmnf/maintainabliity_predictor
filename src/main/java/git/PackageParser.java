package git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.util.List;

public class PackageParser {
    public void parsePackage(List<String> releases, Git git) throws GitAPIException {

        System.out.println("package parsing.......");
        for(int i=0; i<releases.size(); i++){
            git.checkout()
                    .setCreateBranch(false)
                    .setName(releases.get(i))
                    .setStartPoint("refs/tags/"+releases.get(i))
                    .call();

            System.out.println(releases.get(i));
        }

    }
}
