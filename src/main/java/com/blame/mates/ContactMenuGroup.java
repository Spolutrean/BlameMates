package com.blame.mates;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ContactMenuGroup extends ActionGroup {
    /**
     * Return ContactMethodAction[] for allowed communication methods.
     */
    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent e) {
        UserInformationService userInformationService =
                UserInformationService.getInstance();

        //TODO: find how to get email of blame user in current line
        String userEmail = "";

        return userInformationService.getUserContactMethods(userEmail)
                .stream()
                .map(ContactMethodAction::new)
                .toArray(AnAction[]::new);
    }
}
