package com.blame.mates;

import com.intellij.openapi.project.Project;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.vcs.log.VcsUser;
import icons.SocialMediaIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContactMethodManagerDialogWrapper extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox;
    private JPanel listPanel;

    private JList list;
    private CollectionListModel listModel;
    private final List<VcsUser> vcsUsers;
    private final String PATH_TO_CURRENT_CHANGE_BACKUP = "./~blameMatesData.json";

    private String getSelectedUserEmail() {
        return vcsUsers.get(comboBox.getSelectedIndex()).getEmail();
    }

    private ContactMethod getSelectedContactMethod() {
        return (ContactMethod)list.getSelectedValue();
    }

    private void refreshContactMethodListModel(UserInformationService uis) {
        listModel.replaceAll(uis.getUserContactMethods(getSelectedUserEmail()));
    }

    public ContactMethodManagerDialogWrapper(Project project) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // prepare the list of the vcs users
        List<VcsUser> prepVcsUsers = UserInformationUtil.getAllVcsUsersByProject(project);

        // process a list so that the users are unique by email
        prepVcsUsers = new ArrayList<>(
                prepVcsUsers.stream()
                        .collect(Collectors.toMap(
                                VcsUser::getEmail, user -> user, (user1, user2) -> user1)
                        )
                        .values()
        );

        // move the authorized VCS user to the top of the drop-down list
        VcsUser authorizedVcsUser = UserInformationUtil.getAuthorizedVcsUserByProject(project);
        prepVcsUsers.removeIf(user -> user.getEmail().equals(authorizedVcsUser.getEmail()));
        prepVcsUsers.add(0, authorizedVcsUser);

        vcsUsers = prepVcsUsers;

        // set up the contact method list and operations on it
        listModel = new CollectionListModel();
        list = new JBList(listModel);
        list.setCellRenderer(new ContactMethodCellRenderer());

        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(list);
        toolbarDecorator.setAddAction(anActionButton -> {
            ContactMethodEditorDialogWrapper dialog = new ContactMethodEditorDialogWrapper(getSelectedUserEmail());
            dialog.setSize(new Dimension(500, 300));
            dialog.setResizable(false);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            refreshContactMethodListModel(UserInformationService.getInstance());
        });
        toolbarDecorator.setRemoveAction(anActionButton -> {
            UserInformationService userInformationService = UserInformationService.getInstance();

            userInformationService.removeUserContactMethod(getSelectedUserEmail(), getSelectedContactMethod());
            listModel.remove(list.getSelectedIndex());
        });

        // set up the drop-down author list
        for (VcsUser user : vcsUsers) {
            comboBox.addItem(String.format("%s (%s)", user.getName(), user.getEmail()));
        }
        comboBox.addActionListener(actionEvent -> refreshContactMethodListModel(UserInformationService.getInstance()));

        // create a backup copy of the current json data file to revert changes on cancel/close
        try {
            Files.copy(Paths.get(UserInformationService.PATH_TO_DATA), Paths.get(PATH_TO_CURRENT_CHANGE_BACKUP), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshContactMethodListModel(UserInformationService.getInstance());
        listPanel.add(toolbarDecorator.createPanel());
    }

    private void onOK() {
        try {
            Files.delete(Paths.get(PATH_TO_CURRENT_CHANGE_BACKUP));
        } catch (IOException e) {
            e.printStackTrace();
        }

        dispose();
    }

    private void onCancel() {
        try {
            Files.copy(Paths.get(PATH_TO_CURRENT_CHANGE_BACKUP), Paths.get(UserInformationService.PATH_TO_DATA), StandardCopyOption.REPLACE_EXISTING);
            Files.delete(Paths.get(PATH_TO_CURRENT_CHANGE_BACKUP));
        } catch (IOException e) {
            e.printStackTrace();
        }

        dispose();
    }

    private static class ContactMethodCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList jList, Object o, int i, boolean b, boolean b1) {
            super.getListCellRendererComponent(jList, ((ContactMethod)o).getName(), i, b, b1);

            setIcon(SocialMediaIcons.getIcon(((ContactMethod)o).getType()));

            return this;
        }
    }

}
