package StandAlone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Created by 30812 on 2016-10-11.
 */
public class UpdateProfileGUI extends GUI {

    private JButton submit, cancel;
    private JTextField name, email;
    private JPasswordField password;
    private JPanel input;

    public UpdateProfileGUI(GUIControl control) {
        super(control, "Update profile");
        initialize();
    }

    //Handle all the events.
    class Listener implements ActionListener {
        public void actionPerformed (ActionEvent e) {
            if (e.getSource() == cancel) {
                control.enableMain();
                close();
            }
            else if (e.getSource() == submit) {
                performUpdate();
            }
        }
    }

    private void performUpdate() {
        String n = name.getText();
        String em = email.getText();
        String p = String.valueOf(password.getPassword());
        String message = "";
        if (n == null || em == null || p == null || n.isEmpty() || em.isEmpty() || p.isEmpty()) {
            message = "Please fill in the blank field(s)";
        }
        else {
            control.user.updateProfile(n, em);
            p = control.getMD5(p);
            String postData = "command=updateprofile&username=" + control.user.getUsername()
                    + "&password=" + p + "&profile=" + control.user.toString();
            try {
                message = control.executePost(postData);
            } catch (IOException error) {
                message = error.toString();
            }
            if (message.charAt(0) == '1') {
                message = message.substring(1);
                message += "\nName: " + control.user.getName() + "\nEmail: " + control.user.getEmail();
            }
        }
        control.enableMain();
        JOptionPane.showMessageDialog(new JFrame(), message);
        close();
    }

    //Initialize all the GUI components and the listeners.
    private void initialize() {
        input = new JPanel();
        input.setLayout(new GridLayout(4,2,20,20));

        submit = new JButton("Submit");
        cancel = new JButton("Cancel");
        name = new JTextField();
        email = new JTextField();
        password = new JPasswordField();
        password.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    performUpdate();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        input.add(new JLabel("Name:"));
        input.add(name);
        input.add(new JLabel("Email:"));
        input.add(email);
        input.add(new JLabel("Password:"));
        input.add(password);
        input.add(cancel);
        input.add(submit);
        submit.addActionListener(new Listener());
        cancel.addActionListener(new Listener());

        //Add a window closing listener.
        //Enable the main GUI again.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                control.enableMain();
            }
        });
        add(input);
        setSize(300,170);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
