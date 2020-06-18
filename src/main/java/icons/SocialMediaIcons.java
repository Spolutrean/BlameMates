package icons;

import com.blame.mates.ContactMethod.Type;
import com.intellij.openapi.util.IconLoader;
import javax.swing.*;
import java.util.Map;

public final class SocialMediaIcons {
    private static final Map<Type, Icon> icons = Map.ofEntries(
            Map.entry(Type.EMAIL, IconLoader.getIcon("/icons/email-ico.svg")),
            Map.entry(Type.TELEGRAM, IconLoader.getIcon("/icons/telegram-ico.svg")),
            Map.entry(Type.VK, IconLoader.getIcon("/icons/vk-ico.svg")),
            Map.entry(Type.OTHER, IconLoader.getIcon("/icons/asterisk-ico.svg"))
    );

    public static Icon getIcon(Type type) {
        return icons.get(type);
    }
}