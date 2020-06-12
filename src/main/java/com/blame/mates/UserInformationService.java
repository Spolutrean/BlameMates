package com.blame.mates;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import com.fasterxml.jackson.databind.*;

/**
 * A service for interacting with user information.
 */
@Service
public final class UserInformationService {

    private static final String PATH_TO_DATA = "blameMatesData.json";
    private Map<String, List<ContactMethod>> userInformation;

    public UserInformationService() {
    }

    private void readUserInformation() {
        //TODO: implement proper exception handling with popups

        File userInformationFile = new File(PATH_TO_DATA);
        if (!userInformationFile.isFile()) {
            try {
                userInformationFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String userInformationJson = "";
        try {
            userInformationJson = String.join("", Files.readAllLines(userInformationFile.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            userInformation = new ObjectMapper().
                    readValue(userInformationJson, new TypeReference<Map<String, List<ContactMethod>>>() {
                    });
        } catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userInformation == null) {
            userInformation = new HashMap<String, List<ContactMethod>>();
        }
    }

    private void writeUserInformation() {
        //TODO: implement proper exception handling with popups

        File userInformationFile = new File(PATH_TO_DATA);
        /*
        if (!userInformationFile.isFile()) {
            try {
                userInformationFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */

        String userInformationJson = "";
        try {
            userInformationJson = new ObjectMapper().writeValueAsString(userInformation);
        } catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (FileWriter fileWriter = new FileWriter(userInformationFile, false)) {
            fileWriter.write(userInformationJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    public List<ContactMethod> getUserContactMethods(String userEmail) {
        readUserInformation();

        List<ContactMethod> userContactMethods = userInformation.get(userEmail);
        if (userContactMethods != null) {
            return userContactMethods;
        } else {
            return new ArrayList<>();
        }
    }

    public void addUserContactMethod(String userEmail, ContactMethod contactMethod) {
        if (contactMethod == null) {
            return;
        }

        readUserInformation();
        if (userInformation.get(userEmail) == null) {
            userInformation.put(userEmail, Arrays.asList(contactMethod));
        }
        else {
            userInformation.get(userEmail).add(contactMethod);
        }
        writeUserInformation();
    }

    public void removeUserContactMethod(String userEmail, ContactMethod contactMethod) {
        //TODO: implement contact method removal; think about user removal implementation
    }

    public static UserInformationService getInstance() {
        return ServiceManager.getService(UserInformationService.class);
    }
}
