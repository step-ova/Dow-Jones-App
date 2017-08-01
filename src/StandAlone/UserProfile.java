package StandAlone;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;


/**
 * Write a description of class userProfile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class UserProfile
{
    // instance variables - replace the example below with your own
    private String username;
    private String name;
    private String email;
    private Hashtable<String, History> history;

    public class History {
        private String name;
        private int view;
        private boolean uptodate;

        public History(String name, int view) {
            this.name = name;
            this.view = view;
            uptodate = false;
        }

        public void divView() {
            view = (view * 5) / 6;
        }

        public void setView(int view) {
            this.view = view;
        }

        public void addView() {
            view += 30;
        }

        public int getView() {
            return view;
        }

        public String getName() {
            return name;
        }

        public boolean getUpdate() {
            return uptodate;
        }

        public void update() {
            uptodate = true;
        }

        public String toString() {
            return "" + view;
        }
    }

    public void addView(String symbol) {
        history.get(symbol).addView();
        Set<String> keys = history.keySet();
        for (String key : keys) {
            history.get(key).divView();
        }
    }

    public void downloaded(String symbol) {
        history.get(symbol).update();
    }

    private void createNewHistory() {
        history = new Hashtable<String, History>();
        history.put("AXP", new History("American Express Company", 0));
        history.put("AAPL", new History("Apple Inc.", 0));
        history.put("BA", new History("The Boeing Company", 0));
        history.put("CAT", new History("Caterpillar Inc.", 0));
        history.put("CSCO", new History("Cisco Systems, Inc.", 0));
        history.put("CVX", new History("Chevron Corporation", 0));
        history.put("DD", new History("E. I. du Pont de Nemours and Company", 0));
        history.put("DIS", new History("The Walt Disney Company", 0));
        history.put("GE", new History("General Electric Company", 0));
        history.put("GS", new History("The Goldman Sachs Group, Inc.", 0));
        history.put("HD", new History("The Home Depot, Inc.", 0));
        history.put("IBM", new History("International Business Machines Corp.", 0));
        history.put("INTC", new History("Intel Corporation", 0));
        history.put("JNJ", new History("Johnson & Johnson", 0));
        history.put("JPM", new History("JPMorgan Chase & Co.", 0));
        history.put("KO", new History("The Coca-Cola Company", 0));
        history.put("MCD", new History("McDonald's Corp.", 0));
        history.put("MMM", new History("3M Company", 0));
        history.put("MRK", new History("Merck & Co., Inc.", 0));
        history.put("MSFT", new History("Microsoft Corporation", 0));
        history.put("NKE", new History("NIKE, Inc.", 0));
        history.put("PFE", new History("Pfizer Inc.", 0));
        history.put("PG", new History("The Procter & Gamble Company", 0));
        history.put("TRV", new History("The Travelers Companies, Inc.", 0));
        history.put("UNH", new History("UnitedHealth Group Incorporated", 0));
        history.put("UTX", new History("United Technologies Corporation", 0));
        history.put("V", new History("Visa Inc.", 0));
        history.put("VZ", new History("Verizon Communications Inc.", 0));
        history.put("WMT", new History("Wal-Mart Stores Inc.", 0));
        history.put("XOM", new History("Exxon Mobil Corporation", 0));
    }

    private void loadHistory(String str) {
        String[] h = str.split("-");
        for (int i = 0; i < 60; i+=2){
            history.get(h[i]).setView(Integer.parseInt(h[i+1]));
        }
    }

    public List<String> getQuickAccess() {
        Set<String> keys = history.keySet();
        List<String> sorted = new ArrayList<String>(keys);

        Collections.sort(sorted, new Comparator<String>() {
            public int compare(String a, String b) {
                if (history.get(a).getView() < history.get(b).getView()) {
                    return 1;
                }
                else if (history.get(a).getView() == history.get(b).getView()) {
                    int result = 0;
                    int i = 0;
                    int length;
                    String aName = history.get(a).getName();
                    String bName = history.get(b).getName();
                    if (aName.length() <= bName.length()) length = aName.length();
                    else length = bName.length();

                    while (result == 0 && i < length) {
                        char aChar = history.get(a).getName().charAt(i);
                        char bChar = history.get(b).getName().charAt(i);
                        if (aChar > bChar) result = 1;
                        else if (aChar < bChar) result = -1;
                        i++;
                    }
                    return result;
                }
                else {
                    return -1;
                }
            }
        });
        return sorted;
    }
    
    /**
     * Constructor for objects of class userProfile
     */
    //Initialize a user profile with four parameters:
    public UserProfile(String u, String n, String e) {
        username = u;
        name = n;
        email = e;
        createNewHistory();
    }

    //Initialize a user profile with username and the encoded string from user information database:
    public UserProfile(String u, String str) {
        username = u;
        createNewHistory();
        fromString(str);
    }

    //Update the value of email and name.
    public void updateProfile(String name, String email) {
        this.name = name;
        this.email = email;
    }

    //Encode the name, email and history into a string.
    public String toString() {
        String content = name + "&" + email + "&";
        Set<String> keys = history.keySet();
        for (String key : keys) {
            content += key + "-" + history.get(key).toString() + "-";
        }
        String [] str = divtoMul8(content);
        String result = "";
        for (int i=0; i<str.length; i++)
        {
            result += encrypt(str[i], username);
        }
        return result;
    }

    //Decoded the string to get name, email and history.
    public void fromString(String str) {
        String decodeText = "";
        int n = str.length() / 24;
        for (int i = 0; i < n; i++)
        {
            decodeText += decrypt(str.substring(i*24, i*24+24), username);
        }
        decodeText = decodeText.replaceAll("~", "");
        String[] splited = decodeText.split("&");
        name = splited[0];
        email = splited[1];
        loadHistory(splited[2]);
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public Hashtable<String, History> getHistory()
    {
        return history;
    }
    
    public String getUsername()
    {
        return username;
    }

    //Encrypt the data before sending to the server(The encryption only supports 8-byte string)
    //After encryption, 8-byte string becomes 24-byte string.
    //The encryption key is the username.
    private String encrypt(String message, String secretKey){
        
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainTextBytes = message.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte [] base64Bytes = Base64.getEncoder().encode(buf);

            return new String(base64Bytes, "utf-8");
        } catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    //Decrypt the 24-bate data to 8-byte.
    private String decrypt(String encryptedText, String secretKey) {
        try{
                byte[] message = Base64.getDecoder().decode(encryptedText.getBytes
                ("utf-8"));
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
                byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
                SecretKey key = new SecretKeySpec(keyBytes, "DESede");

                Cipher decipher = Cipher.getInstance("DESede");
                decipher.init(Cipher.DECRYPT_MODE, key);

                byte[] plainText = decipher.doFinal(message);

                return new String(plainText, "UTF-8");
        } catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    //This function covert a string of any number of bytes to an array of string.
    //Each element contains 8-byte. If the input string is not multiple of 8, padding with '~'.
    private static String[] divtoMul8(String str) {
        int l = str.length();
        int new_l = 0;
        if (l % 8 > 0)
        {
            new_l = (l/8 + 1) * 8;
        }
        else if (l % 8 == 0)
        {
            new_l = l;
        }
        
        int n = new_l - l;
        
        for (int i = 0; i < n; i++)
        {
            str += "~";
        }
        
        int size = new_l / 8;
        String[] result = new String[size];
        for (int i = 0; i < size; i++)
        {
            result[i] = str.substring(i*8, i*8 + 8);
        }
        return result;
    }
}

