package git;

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
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReleaseParser {
    public static void main(String[] args) throws InvalidRemoteException, TransportException, GitAPIException, IOException {

        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        Repository repository = repositoryBuilder.setGitDir(new File("H:\\musa_all\\project\\spl3\\.git"))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .setMustExist(true)
                .build();

        Git git = new Git(repository);

        RevWalk walk = new RevWalk(repository);

        List<Ref> branches = new Git(repository).branchList().setListMode(ListBranchCommand.ListMode.ALL).call();

        for (Ref branch : branches) {
            String branchName = branch.getName();

            System.out.println("Commits of branch: " + branch.getName());
            System.out.println("-------------------------------------");

            Iterable<RevCommit> commits = git.log().all().call();

            for (RevCommit commit : commits) {
                boolean foundInThisBranch = false;

                RevCommit targetCommit = walk.parseCommit(repository.resolve(
                        commit.getName()));

//                for (Map.Entry<String, Ref> e : repository.getAllRefs().entrySet()) {
//                    if (e.getKey().startsWith(Constants.R_HEADS)) {
//                        if (walk.isMergedInto(targetCommit, walk.parseCommit(
//                                e.getValue().getObjectId()))) {
//                            String foundInBranch = e.getValue().getName();
//                            if (branchName.equals(foundInBranch)) {
//                                foundInThisBranch = true;
//                                break;
//                            }
//                        }
//                    }
//                }
//
//                if (foundInThisBranch) {
//                    System.out.println(commit.getName());
//                    System.out.println(commit.getAuthorIdent().getName());
//                    System.out.println(new Date(commit.getCommitTime() * 1000L));
//                    System.out.println(commit.getFullMessage());
//                }
//            }
            }
        }

//        System.out.println(repository.getRemoteNames());
//        System.out.println(git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call());
//        ObjectId lastCommitId = repository.resolve(Constants.HEAD);
//        System.out.println(lastCommitId);
//        try (RevWalk revWalk = new RevWalk(repository)) {
//            RevCommit commit = revWalk.parseCommit(lastCommitId);
//            // and using commit's tree find the path
//            RevTree tree = commit.getTree();
//            System.out.println("Having tree: " + tree);
//        }


//        int c = 0;

//        List<Ref> call = new Git(repository).branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
//        for (Ref ref : call) {
//            System.out.println("Branch: " + ref + " " + ref.getName() + " "
//                    + ref.getObjectId().getName());
//            c++;
//        }
//        System.out.println("Number of branches: " + c);
    }
}
