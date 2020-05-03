package com.blame.mates;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.popup.list.ListPopupImpl;
import org.jetbrains.annotations.NotNull;

public class ShowContactMenuAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        ListPopupImpl listPopup = (ListPopupImpl) JBPopupFactory
                .getInstance()
                .createActionGroupPopup(
                        null,
                        new ContactMenuGroup(),
                        e.getDataContext(),
                        JBPopupFactory.ActionSelectionAid.NUMBERING,
                        false);

        listPopup.showCenteredInCurrentWindow(e.getProject());
    }
}
