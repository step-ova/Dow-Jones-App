package StandAlone;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;


public class GUIControl
{
    //This controller links all the GUIs.
    //Any of the GUIs can send a 'redirect' request to open another GUI.
    //It also provides commonly used method in GUI.

    public UserProfile user;
    private LoginGUI login;
    private RegisterGUI register;
    private MainGUI mainFace;
    private ChangePasswordGUI changePassword;
    private UpdateProfileGUI updateProfile;
    final private String address = 
        "http://localhost:8081/LoginServer"; //The URL of your server. Change it to fit your configuration.
    public final String dataLocat = (new File("").getAbsolutePath().toString()) + "\\Data\\";

    //Initialize the controller by activating the Login GUI.
    //The starting point of the whole program.
    public GUIControl() {
        //login = new LoginGUI(this);
        //login.activate(false);
        user = new UserProfile("a", "a", "a");
        mainFace = new MainGUI(this);
        mainFace.activate();
    }

    //Redirect to a GUI. 'dest' indicates which GUI to be activated.
    //Each GUI is created again before activation to clear the older operation on that GUI.
    public void redirect(String dest) {
        if (dest.equals("Login")) { //Redirect to Login GUI without showing the username.
            login = new LoginGUI(this);
            login.activate(false);
        }
        else if (dest.equals("LoginSuccess")) { //This is called after registration succeeds.
                                                //The Login GUI will automatically show the username which is just registered.
            login = new LoginGUI(this);
            login.activate(true);
        }
        else if (dest.equals("Register")) {
            register = new RegisterGUI(this);
            register.activate();
        }
        else if (dest.equals("Main")) {
            mainFace = new MainGUI(this);
            mainFace.activate();
        }
        else if (dest.equals("ChangePassword")) {
            changePassword = new ChangePasswordGUI(this);
            changePassword.activate();
        }
        else if (dest.equals("UpdateProfile")) {
            updateProfile = new UpdateProfileGUI(this);
            updateProfile.activate();
        }
    }

    //Enable the main GUI.
    public void enableMain() {
        mainFace.setEnabled(true);
    }

    //Post the request to the Server.
    public String executePost(String postData) throws IOException {

        // Connect to URL
        URL url = new URL(address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postData.length()));

        // Write data
        OutputStream os = connection.getOutputStream();
        os.write(postData.getBytes());

        // Read response
        StringBuilder responseSB = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        while ( (line = br.readLine()) != null)
            responseSB.append(line);
        // Close streams
        br.close();
        os.close();
        
        String message = responseSB.toString();
        
        return message;
    }

    //Encrypt the data using MD5.
    public String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
