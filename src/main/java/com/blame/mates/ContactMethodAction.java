package com.blame.mates;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class ContactMethodAction extends AnAction {
    private URL contactUrl;

    public ContactMethodAction(ContactMethod contactMethod) {
        super(contactMethod.getName(), null, contactMethod.getIcon());
        this.contactUrl = contactMethod.getIntentionUrl();
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        //open contact link
        //will be created by ContactMenuGroup
    }
}
