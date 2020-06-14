package com.blame.mates;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

public final class ContactMethodSimpleFactory {
    public static ContactMethod forTelegram(@NotNull String nickname) throws MalformedURLException {
        return new ContactMethod(
                ContactMethod.Type.TELEGRAM,
                new URL("https://t.me/" + nickname),
                "Telegram"
        );
    }

    public static ContactMethod forEmail(@NotNull String email) throws MalformedURLException {
        return new ContactMethod(
                ContactMethod.Type.EMAIL,
                new URL("mailto:" + email),
                "Email"
        );
    }

    public static ContactMethod forVK(@NotNull String nickname) throws MalformedURLException {
        return new ContactMethod(
                ContactMethod.Type.VK,
                new URL("https://vk.com/" + nickname),
                "VK"
        );
    }
}
