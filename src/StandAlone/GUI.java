package StandAlone;

/**
 * Created by 30812 on 2016-10-25.
 */

import javax.swing.*;

public class GUI extends JFrame {
    protected GUIControl control; //This GUIControl holds the user profile data
                                //and controls the transition between multiple GUIs.

    //Connect to the GUI controller.
    GUI(GUIControl control, String title) {
        super(title);
        this.control = control;
    }

    //Close the GUI.
    protected void close(){
        setVisible(false);
    }

    //This function will make the GUI visible.
    public void activate() {
        setVisible(true);
    }
}
