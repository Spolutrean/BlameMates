package com.blame.mates;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ContactMenuGroup extends ActionGroup {
    /**
     * Return ContactMethodAction[] for allowed communication methods.
     */
    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Document document = editor.getDocument();
        VirtualFile file = FileDocumentManager.getInstance().getFile(document);


        int line = editor.getCaretModel().getLogicalPosition().line;
        String userEmail = UserInformationUtil.getUserEmailByFileAndLine(project, file, line);

        return UserInformationService.getInstance().getUserContactMethods(userEmail)
                .stream()
                .map(ContactMethodAction::new)
                .toArray(AnAction[]::new);
    }
}
