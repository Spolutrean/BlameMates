package com.blame.mates;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A service for interacting with user information.
 */
@Service
public final class UserInformationService {

    public UserInformationService() {
    }

    @NotNull
    public List<ContactMethod> getUserContactMethods(String userEmail) {
        return new ArrayList<>();
    }

    public static UserInformationService getInstance() {
        return ServiceManager.getService(UserInformationService.class);
    }
}
