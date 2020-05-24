package com.blame.mates;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A service for interacting with user information.
 */
@Service
public final class UserInformationService {

    public UserInformationService() {
    }

    public List<ContactMethod> getUserContactMethods(String userEmail) {
        return null;
    }

    public static UserInformationService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, UserInformationService.class);
    }
}
