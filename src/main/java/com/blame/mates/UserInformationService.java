package com.blame.mates;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
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

import static com.blame.mates.UserInformationUtil.isValidEmail;

/**
 * A service for interacting with user information.
 */
@Service
public final class UserInformationService {

    private final String PATH_TO_DATA = "./blameMatesData.json";

    private Map<String, List<ContactMethod>> userInformation;

    public UserInformationService() {
    }

    /**
     * Reads the user information from the json data file into the userInformation map
     */
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
        if (userInformationJson.isEmpty()) {
            userInformationJson = "{}"; // jackson freaks out when you try to parse an empty string ¯\_(ツ)_/¯
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

    /**
     * Writes the user information from the userInformation map into the json data file
     */
    private void writeUserInformation() {
        //TODO: implement proper exception handling with popups

        File userInformationFile = new File(PATH_TO_DATA);
        if (!userInformationFile.isFile()) {
            try {
                userInformationFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String userInformationJson = "{}";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            userInformationJson = objectMapper.writeValueAsString(userInformation);
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

    /**
     * Gets a list of contact methods, corresponding to the user with the provided email, currently present
     * in the json data file
     * @param userEmail String containing user email
     * @return List<ContactMethod>, corresponding to the user with the provided email. If the provided email is
     * invalid, the list will be empty
     */
    @NotNull
    public List<ContactMethod> getUserContactMethods(String userEmail) {
        if (!isValidEmail(userEmail)) {
            return new ArrayList<>();
        }

        readUserInformation();

        List<ContactMethod> userContactMethods = userInformation.get(userEmail);
        if (userContactMethods != null) {
            return userContactMethods;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Adds a contact method for a user with the provided email to the json data file
     * @param userEmail String containing user email
     * @param contactMethod ContactMethod to add
     */
    public void addUserContactMethod(String userEmail, ContactMethod contactMethod) {
        if (contactMethod == null || !isValidEmail(userEmail)) {
            return;
        }

        readUserInformation();

        if (userInformation.get(userEmail) == null) {
            userInformation.put(userEmail, Arrays.asList(contactMethod));
        }
        else {
            if (userInformation.get(userEmail)
                    .stream()
                    .noneMatch(method ->
                            method.getIntentionUrl().equals(contactMethod.getIntentionUrl()))) {
                userInformation.get(userEmail).add(contactMethod);
            }
        }

        writeUserInformation();
    }

    /**
     * Removes a contact method for a user with the provided email from the json data file
     * @param userEmail String containing user email
     * @param contactMethod ContactMethod to remove
     */
    public void removeUserContactMethod(String userEmail, ContactMethod contactMethod) {
        if (contactMethod == null || !isValidEmail(userEmail)) {
            return;
        }

        readUserInformation();

        if (userInformation.get(userEmail) == null) {
            return;
        } else {
            userInformation.get(userEmail)
                    .removeIf(method -> method.getIntentionUrl().equals(contactMethod.getIntentionUrl()));
        }

        writeUserInformation();
    }

    /**
     * Gets a list of user emails, currently present in the json data file
     * @return List<String> containing user emails
     */
    @NotNull
    public List<String> getUserEmails() {
        readUserInformation();

        return new ArrayList<>(userInformation.keySet());
    }

    /**
     * Adds an empty entry for a user to the json data file with the provided email as a key
     * @param userEmail String containing user email
     */
    public void addUser(String userEmail) {
        if (!isValidEmail(userEmail)) {
            return;
        }

        readUserInformation();

        if (userInformation.get(userEmail) != null) {
            return;
        }

        userInformation.put(userEmail, new ArrayList<>());

        writeUserInformation();
    }

    /**
     * Removes the entry for the user with the provided email from the json data file
     * @param userEmail String containing user email
     */
    public void removeUser(String userEmail) {
        if (!isValidEmail(userEmail)) {
            return;
        }

        readUserInformation();

        userInformation.remove(userEmail);

        writeUserInformation();
    }

    /**
     * Returns an instance of UserInformationService
     * @return UserInformationService instance
     */
    public static UserInformationService getInstance() {
        return ServiceManager.getService(UserInformationService.class);
    }
}
