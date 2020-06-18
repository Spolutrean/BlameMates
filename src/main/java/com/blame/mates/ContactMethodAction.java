package com.blame.mates;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import icons.SocialMediaIcons;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class ContactMethodAction extends AnAction {
    private URL contactUrl;

    public ContactMethodAction(ContactMethod contactMethod) {
        super(contactMethod.getName(), null, SocialMediaIcons.getIcon(contactMethod.getType()));
        this.contactUrl = contactMethod.getIntentionUrl();
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        BrowserUtil.browse(contactUrl);
    }
}
