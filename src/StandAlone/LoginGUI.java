package StandAlone;

import java.io.IOException;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;


public class LoginGUI extends GUI {

    private JPanel input;
    private JTextField username;
    private JButton login, register;
    private JPasswordField password;

    //Connect the GUI to the GUI controller.
    public LoginGUI(GUIControl c) {
        super(c, "Login");
        initialize();
    }

    //Initialize the GUI and add all the GUI components.
    private void initialize() {
        setLayout(new BorderLayout(1,50));
        input = new JPanel();
        input.setLayout(new GridLayout(3,2,20,20));
        input.setBorder(new TitledBorder("Please Login"));
        input.add(new JLabel("Username:"));
        username = new JTextField(8);
        input.add(username);
        input.add(new JLabel("Password:"));
        password = new JPasswordField(8);
        password.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    performLogin();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        input.add(password);
        
        register = new JButton("Register Now");
        input.add(register);
        login = new JButton("Login");
        input.add(login);
        login.addActionListener(new Listener());
        register.addActionListener(new Listener());
        
        add(input);
        setSize(300,170);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    //Activate the GUI. "info" indicates whether the username should be displayed.
    //Because after the registration, the new registered username should be displayed.
    public void activate(boolean info) {
        if (info)
        {
            username.setText(control.user.getUsername());
        }
        setVisible(true);
    }
    
    class Listener implements ActionListener {
        public void actionPerformed (ActionEvent e)
        {
            if (e.getSource() == login)
            {
                performLogin();
            }
            
            else if (e.getSource() == register)
            {
                close();
                control.redirect("Register");
            }
        }
    }

    private void performLogin() {
        String command = "login";
        String u = username.getText();
        String p = String.valueOf(password.getPassword());
        p = control.getMD5(p);
        String message = null;

        String postData = "command=" + command + "&username=" +
            u + "&password=" + p;

        try {
            message = control.executePost(postData);
        } catch (IOException error) {
            message = error.toString();
        }

        if (message.charAt(0) == '1')
        {
            message = message.substring(1).replaceAll(" ", "+");
            control.user = new UserProfile(u, message);
            message = "Welcome! " + control.user.getName() + ".";
            close();
            control.redirect("Main");
        }
        JOptionPane.showMessageDialog(new JFrame(), message);
    }
}
