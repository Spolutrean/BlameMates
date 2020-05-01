package com.blame.mates;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * A service for interacting with user information.
 */
public class UserInformationService {
    public UserInformationService(Project project) {
    }

    public static UserInformationService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, UserInformationService.class);
    }
}
