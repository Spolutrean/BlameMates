package com.blame.mates;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A service for interacting with user information.
 */
public class UserInformationService {
    public UserInformationService(Project project) {
    }

    public List<ContactMethod> getUserContactMethods(String userEmail) {
        return null;
    }

    public static UserInformationService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, UserInformationService.class);
    }
}
