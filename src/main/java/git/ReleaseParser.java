package git;

import org.codehaus.plexus.util.Scanner;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReleaseParser {
    public static void main(String[] args) throws InvalidRemoteException, TransportException, GitAPIException, IOException {

//        File myObj = new File("filename.txt");
//        Scanner myReader = new Scanner(myObj);

        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        Repository repository = repositoryBuilder.setGitDir(new File("H:\\ES_git\\elasticsearch\\.git"))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .setMustExist(true)
                .build();

        Git git = new Git(repository);

        git.checkout()
                .setCreateBranch(false)
                .setName("Elasticsearch")
                .setStartPoint("refs/tags/v7.11.2")
                .call();

//        List<Ref> tags = git.tagList().call();
//
//        System.out.println(git.tagList().call());
//        for (Ref tag : tags) {
//            System.out.println("Tag: " + tag.getName());
//        }
//
//        List<Ref> branches = new Git(repository).branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
//
//        int branchCount = 0;
//        for (Ref branch : branches) {
//
//            System.out.println("Branch: " + branch + " " + branch.getName() + " "
//                    + branch.getObjectId().getName());
//            System.out.println("-------------------------------------");
//            branchCount++;
//
//            Iterable<RevCommit> commits = git.log().all().call();
//
//            for (RevCommit commit : commits) {
//                    System.out.println(commit.getName());
//                    System.out.println(commit.getAuthorIdent().getName());
//                    System.out.println(new Date(commit.getCommitTime() * 1000L));
//                    System.out.println(commit.getFullMessage());
//            }
//        }
    }
}
