package com.blame.mates;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.net.URL;

public class ContactMethodAction extends AnAction {
    private URL contactUrl;

    public ContactMethodAction(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text,
                               @Nls(capitalization = Nls.Capitalization.Sentence) @Nullable String description,
                               @Nullable Icon icon,
                               URL contactUrl) {
        super(text, description, icon);
        this.contactUrl = contactUrl;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        //open contact link
        //will be created by ContactMenuGroup
    }
}
