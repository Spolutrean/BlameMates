package com.blame.mates;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.*;

/**
 * A service for interacting with user information.
 */
@Service
public final class UserInformationService {

    private Map<String, List<ContactMethod>> userInformation;

    public UserInformationService() {
    }

    @NotNull
    public List<ContactMethod> getUserContactMethods(String userEmail) {
        if (userInformation == null) {
            String userInformationJson = "";
            try {
                List<String> jsonLines = Files.readAllLines(Paths.get("blameMatesData.json"));
                userInformationJson = String.join("", jsonLines);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                userInformation = objectMapper.
                        readValue(userInformationJson, new TypeReference<>() {
                        });
            } catch (JsonParseException | JsonMappingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<ContactMethod> userContactMethods = userInformation.get(userEmail);
        if (userContactMethods != null) {
            return userContactMethods;
        } else {
            return new ArrayList<>();
        }
    }

    public static UserInformationService getInstance() {
        return ServiceManager.getService(UserInformationService.class);
    }
}
