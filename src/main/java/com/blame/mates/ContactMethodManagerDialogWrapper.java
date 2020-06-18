package com.blame.mates;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.*;
import com.intellij.vcs.log.VcsUser;

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
        int selectedIndex = comboBox.getSelectedIndex();
        return vcsUsers.get(selectedIndex).getEmail();
    }

    private ContactMethod getSelectedContactMethod(UserInformationService uis) {
        int selectedIndex = list.getSelectedIndex();
        return uis.getUserContactMethods(getSelectedUserEmail()).get(selectedIndex);
    }

    private void refreshContactMethodListModel(UserInformationService uis) {
        listModel.replaceAll(
                uis.getUserContactMethods(getSelectedUserEmail()).stream()
                .map(ContactMethod::getName)
                .collect(Collectors.toList()
            )
        );
    }

    public ContactMethodManagerDialogWrapper(Project project) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // prepare the list of the vcs users

        List<VcsUser> prepVcsUsers = UserInformationUtil.getAllVcsUsersByProject(project);
        prepVcsUsers.add(0, UserInformationUtil.getAuthorizedVcsUserByProject(project));

        // process a list so that the users are unique by email
        prepVcsUsers = new ArrayList<>(
                prepVcsUsers.stream()
                        .collect(Collectors.toMap(
                                VcsUser::getEmail, user -> user, (user1, user2) -> user1)
                        )
                        .values()
        );

        vcsUsers = prepVcsUsers;

        // set up the contact method list and operations on it

        listModel = new CollectionListModel();
        list = new JList(listModel);

        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(list);
        toolbarDecorator.setAddAction(new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                ContactMethodEditorDialogWrapper dialog = new ContactMethodEditorDialogWrapper(getSelectedUserEmail());
                dialog.setSize(new Dimension(500,300));
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                refreshContactMethodListModel(ServiceManager.getService(UserInformationService.class));
            }
        });
        toolbarDecorator.setRemoveAction(new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                UserInformationService uis = ServiceManager.getService(UserInformationService.class);

                uis.removeUserContactMethod(getSelectedUserEmail(), getSelectedContactMethod(uis));
                listModel.remove(list.getSelectedIndex());
            }
        });

        // set up the drop-down author list

        for (VcsUser user : vcsUsers) {
            comboBox.addItem(String.format("%s (%s)", user.getName(), user.getEmail()));
        }
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                refreshContactMethodListModel(ServiceManager.getService(UserInformationService.class));
            }
        });

        // create a backup copy of the current json data file to revert changes on cancel/close

        try {
            Files.copy(Paths.get(UserInformationService.PATH_TO_DATA), Paths.get(PATH_TO_CURRENT_CHANGE_BACKUP), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshContactMethodListModel(ServiceManager.getService(UserInformationService.class));
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

}
