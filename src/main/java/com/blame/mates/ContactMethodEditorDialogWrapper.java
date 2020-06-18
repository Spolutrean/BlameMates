package com.blame.mates;

import javax.swing.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;

public class ContactMethodEditorDialogWrapper extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox;
    private JTextField identificator;
    private JTextField name;
    private JLabel identificatorHint;

    private final String userEmail;

    public ContactMethodEditorDialogWrapper(String userEmail) {
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

        this.userEmail = userEmail;

        for (ContactMethod.Type type : ContactMethod.Type.values()) {
            comboBox.addItem(type.toString().substring(0, 1) + type.toString().substring(1).toLowerCase());
        }
        comboBox.addActionListener(actionEvent -> {
            ContactMethod.Type type = ContactMethod.Type.valueOf(
                    comboBox.getSelectedItem().toString().toUpperCase()
            );

            String hintText = "";
            switch (type) {
                case EMAIL:
                    hintText = "Enter your email:";
                    break;
                case VK:
                    hintText = "Enter your VK nickname:";
                    break;
                case TELEGRAM:
                    hintText = "Enter your Telegram nickname:";
                    break;
                case OTHER:
                    hintText = "Enter a link for communication via this method:";
                    break;
            }
            identificatorHint.setText(hintText);
        });
    }

    private void onOK() {
        ContactMethod.Type type = ContactMethod.Type.values()[comboBox.getSelectedIndex()];

        ContactMethod contactMethod = null;
        String identificatorText = identificator.getText();
        String nameText = name.getText();
        try {
            switch (type) {
                case EMAIL:
                    contactMethod = ContactMethodSimpleFactory.forEmail(identificatorText, nameText);
                    break;
                case VK:
                    contactMethod = ContactMethodSimpleFactory.forVK(identificatorText, nameText);
                    break;
                case TELEGRAM:
                    contactMethod = ContactMethodSimpleFactory.forTelegram(identificatorText, nameText);
                    break;
                case OTHER:
                    contactMethod = new ContactMethod(type, new URL(identificatorText), nameText);
                    break;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        UserInformationService uis = UserInformationService.getInstance();
        uis.removeUserContactMethod(userEmail, contactMethod);
        uis.addUserContactMethod(userEmail, contactMethod);

        dispose();
    }

    private void onCancel() {
        dispose();
    }

}
