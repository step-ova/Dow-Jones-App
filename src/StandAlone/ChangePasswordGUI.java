package StandAlone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Created by huangzhitong on 11/10/2016.
 */
public class ChangePasswordGUI extends GUI {

    private JLabel oldWord, newWord, newWord2;
    private JPasswordField oldPass, newPass, newPass2;
    private JPanel input;
    private JButton submit, cancel;

    //Initialize the GUI by connecting the GUI to GUIControl.
    public ChangePasswordGUI(GUIControl control) {
        super(control, "Change password");
        initialize();
    }

    //The listener which handles the events.
    class Listener implements ActionListener {
        public void actionPerformed (ActionEvent e) {
            if (e.getSource() == cancel) {
                control.enableMain();
                close();
            }
            else if (e.getSource() == submit) {
                performChange();
            }
        }
    }

    private void performChange() {
        String oP = String.valueOf(oldPass.getPassword());
        String nP1 = String.valueOf(newPass.getPassword());
        String nP2 = String.valueOf(newPass2.getPassword());
        String message = "";
        String command = "changepassword";

        if (oP == null || nP1 == null || nP2 == null || oP.isEmpty() || nP1.isEmpty() || nP2.isEmpty()) {
            message = "Please fill in the blank field(s)";
            //Ensure all the text fields is filled.
        }
        else {
            //Ensure the two entered password is the same.
            if (nP1.equals(nP2)) {
                oP = control.getMD5(oP);
                nP1 = control.getMD5(nP1); //Encrypt the old and new passwords using MD5.
                String postData = "command=" + command + "&username=" + control.user.getUsername()
                        + "&password=" + oP + "&newpassword=" + nP1;
                //Construct the post data.
                try { //Post the request to the server.
                    message = control.executePost(postData);
                } catch (IOException error) {
                    message = error.toString();
                }
                if (message.charAt(0) == '1') {
                    //If succeeds, close this GUI.
                    message = message.substring(1);
                }
            }
            else {
                message = "The two entered password are not the same";
            }
        }
        //Finally, show a message to notify user the result of the request.
        control.enableMain();
        JOptionPane.showMessageDialog(new JFrame(), message);
        close();
    }

    //Initialize all the GUI components and the listeners.
    private void initialize() {
        oldWord = new JLabel("Old password:");
        newWord = new JLabel("New password:");
        newWord2 = new JLabel("Type new password again:");

        oldPass = new JPasswordField();
        newPass = new JPasswordField();
        newPass2 = new JPasswordField();
        newPass2.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    performChange();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        submit = new JButton("Submit");
        cancel = new JButton("Cancel");

        input = new JPanel();
        input.setLayout(new GridLayout(4,2,20,20));

        input.add(oldWord);
        input.add(oldPass);
        input.add(newWord);
        input.add(newPass);
        input.add(newWord2);
        input.add(newPass2);
        input.add(cancel);
        input.add(submit);

        cancel.addActionListener(new Listener());
        submit.addActionListener(new Listener());

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