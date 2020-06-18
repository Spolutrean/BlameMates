package com.blame.mates;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ShowContactMethodManagerAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ContactMethodManagerDialogWrapper dialog = new ContactMethodManagerDialogWrapper(e.getProject());
        dialog.setResizable(false);
        dialog.setSize(new Dimension(600, 500));
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

}
