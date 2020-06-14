package com.blame.mates;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.LocalFilePath;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.commands.Git;
import git4idea.commands.GitCommand;
import git4idea.commands.GitCommandResult;
import git4idea.commands.GitLineHandler;
import git4idea.repo.GitRepositoryManager;

public abstract class UserInformationUtil {
    /**
     * Checks that the user email provided is valid
     * @param userEmail String containing user email
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String userEmail) {
        return userEmail != null && userEmail.matches("[^@]+@[^@\\.]+\\..+");
    }

    /**
     * Finds the email of the comitter in concrete line last time.
     * @param project - instance of working project
     * @param workingFile - file, where should find email
     * @param line - concrete line, which author's email is needed
     * @return email of the comitter in concrete line and file, if not comitted return not.committed.yet
     */
    public static String getUserEmailByFileAndLine(Project project, VirtualFile workingFile, int line) {
        VirtualFile vcsRoot = GitRepositoryManager.getInstance(project).getRepositoryForFile(workingFile).getRoot();
        GitLineHandler handler = new GitLineHandler(project, vcsRoot, GitCommand.BLAME);
        handler.addParameters("-p", "-w", "-e", "-L " + (line + 1) + "," + (line + 1));
        handler.endOptions();
        handler.addRelativePaths(new LocalFilePath(workingFile.getCanonicalPath(), false));
        GitCommandResult result = Git.getInstance().runCommand(handler);
        String emailLine = result.getOutput().get(2);
        return emailLine.substring(emailLine.indexOf('<') + 1, emailLine.indexOf('>'));
    }
}
