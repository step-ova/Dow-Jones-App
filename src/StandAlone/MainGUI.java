package StandAlone;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Write a description of class MainGUI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MainGUI extends GUI {
    //This is the main GUI which displays the the line chart and implements all the options.

    private JPanel menuPanel, bottom, chartPanel, listPanel;
    private JButton logOut, changePassword, updateProfile;
    private JRadioButton year_1, year_2, year_5, all;
    private JCheckBox mv20, mv50, mv100, mv200;
    private ButtonGroup periodGroup, chooseAdj;
    private JRadioButton adj, noAdj;
    private ChartDrawer drawer;
    private Stock stock;
    private JList list, quickList;
    private DefaultListModel element, quickElement;
    private JScrollPane pane, quickPane;
    private YahooStock yahoo;
    private JLabel downloadInfo;
    Hashtable<String, String> symbol;

    //Initialize with connection to a GUI controller.
    public MainGUI(GUIControl c) {
        super(c, "Stock Technical Analysis System");
        yahoo = new YahooStock(control.dataLocat, this);
        initialize();
        //yahoo.checkUptodate();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                logout();
            }
        }, "Shutdown-thread"));
    }

    //Initialize the GUI and add all the components.
    private void initialize() {
        //Create the Stock object.
        drawer = new ChartDrawer(stock);
        //Create the ChartDrawer object.
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3,5,20,20));
        menuPanel.setBorder(new TitledBorder("Please choose the following Option"));
        year_1 = new JRadioButton("1-year data");
        year_2 = new JRadioButton("2-year data");
        year_5 = new JRadioButton("5-year data");
        all = new JRadioButton("All data");
        mv20 = new JCheckBox("20 days");
        mv50 = new JCheckBox("50 days");
        mv100 = new JCheckBox("100 days");
        mv200 = new JCheckBox("200 days");
        adj = new JRadioButton("Adjusted closing price");
        noAdj = new JRadioButton("Closing price");
        year_1.addActionListener(new Listener());
        year_2.addActionListener(new Listener());
        year_5.addActionListener(new Listener());
        all.addActionListener(new Listener());
        mv20.addActionListener(new Listener());
        mv50.addActionListener(new Listener());
        mv100.addActionListener(new Listener());
        mv200.addActionListener(new Listener());
        adj.addActionListener(new Listener());
        noAdj.addActionListener(new Listener());
        periodGroup = new ButtonGroup();
        periodGroup.add(year_1);
        periodGroup.add(year_2);
        periodGroup.add(year_5);
        periodGroup.add(all);
        chooseAdj = new ButtonGroup();
        chooseAdj.add(adj);
        chooseAdj.add(noAdj);
        adj.setActionCommand("1");
        noAdj.setActionCommand("0");
        year_1.setActionCommand("1");
        year_2.setActionCommand("2");
        year_5.setActionCommand("5");
        all.setActionCommand("0");
        menuPanel.add(new JLabel("Period of data:"));
        menuPanel.add(year_1);
        menuPanel.add(year_2);
        menuPanel.add(year_5);
        menuPanel.add(all);
        menuPanel.add(new JLabel("Moving average period:"));
        menuPanel.add(mv20);
        menuPanel.add(mv50);
        menuPanel.add(mv100);
        menuPanel.add(mv200);
        menuPanel.add(new JLabel("    "));
        menuPanel.add(adj);
        menuPanel.add(noAdj);
        adj.setSelected(true);
        year_1.setSelected(true);

        bottom = new JPanel();
        bottom.setLayout(new GridLayout(1, 3, 20, 20));
        logOut = new JButton("Log out");
        changePassword = new JButton("Change your password");
        updateProfile = new JButton("Update your profile");
        logOut.addActionListener(new Listener());
        updateProfile.addActionListener(new Listener());
        changePassword.addActionListener(new Listener());
        bottom.add(changePassword);
        bottom.add(updateProfile);
        bottom.add(logOut);

        chartPanel = drawer.createChartPanel();
        //Return the reference of the JPanel where the ChartDrawer draws line chart.

        loadList();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.5; gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menuPanel.setPreferredSize(new Dimension(800,115));
        add(menuPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(chartPanel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.0; gbc.weighty = 0.0;
        add(listPanel, gbc);

        bottom.setPreferredSize(new Dimension(800,25));
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 1.0; gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(bottom, gbc);

        setSize(1200, 700);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void loadList() {
        symbol = yahoo.getSymbol();
        downloadInfo = new JLabel("Checking data...", SwingConstants.RIGHT);
        element = new DefaultListModel(); quickElement = new DefaultListModel();
        list = new JList(element);
        quickList = new JList(quickElement);
        pane = new JScrollPane(list); quickPane = new JScrollPane(quickList);
        listPanel = new JPanel();
        quickPane.setPreferredSize(new Dimension(240, 120));
        pane.setPreferredSize(new Dimension(240, 350));

        listPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.0; gbc.weighty = 0.0;
        listPanel.add(new JLabel("Quick Access:"), gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0; gbc.weighty = 0.0;
        listPanel.add(quickPane, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0; gbc.weighty = 0.0;
        listPanel.add(new JLabel("All stocks:"), gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        listPanel.add(pane, gbc);
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.weightx = 0.0; gbc.weighty = 0.0;
        listPanel.add(downloadInfo, gbc);
        listPanel.setPreferredSize(new Dimension(250, 480));
        list.setFont(new Font("Arial Hebrew", Font.BOLD, 13));
        quickList.setFont(new Font("Arial Hebrew", Font.BOLD, 13));
        downloadInfo.setFont(new Font("Arial", Font.PLAIN, 14));

        updateElement();

        list.addMouseListener(new MyMouseAdapter());
        quickList.addMouseListener(new MyMouseAdapter());
    }

    private class MyMouseAdapter extends MouseAdapter {
        public void mouseClicked(MouseEvent evt) {
            JList list = (JList) evt.getSource();
            if (evt.getClickCount() == 2) {
                int index = list.locationToIndex(evt.getPoint());
                String key = (String)list.getModel().getElementAt(index);
                String value = symbol.get(key);
                if (value != null) {
                    control.user.addView(value);
                    updateElement();
                    stock = new Stock(key, control.dataLocat + value + ".csv");
                    drawer.changeStock(stock);
                    redraw(true);
                }
            }
        }
    }

    private class MyCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            String key = symbol.get(value);
            if (key != null) {
                if (control.user.getHistory().get(key).getUpdate()) {
                    setForeground(Color.black);
                } else {
                    setForeground(Color.gray);
                }
            }
            return this;
        }
    }

    private void updateElement() {
        element.removeAllElements();
        quickElement.removeAllElements();

        List<String> quick = control.user.getQuickAccess();
        int num = 0;
        for (String key : quick) {
            if (control.user.getHistory().get(key).getView() > 0)
                quickElement.addElement(control.user.getHistory().get(key).getName());
            num ++;
            if (num >= 5) break;
        }

        Set<String> keys = symbol.keySet();
        List<String> sorted = new ArrayList<String>(keys);
        Collections.sort(sorted);
        for (String key : sorted)
            element.addElement(key);
    }

    public void updateCell(int num, int done) {
        list.setCellRenderer(new MyCellRenderer());
        quickList.setCellRenderer(new MyCellRenderer());

        if (num == 30 - done) {
            downloadInfo.setText("Data is up-to-date!");
        }
        else {
            downloadInfo.setText("Updating:    " + num + " \\ " + (30 - done));
        }

        if (num == 1) {
            List<String> quick = control.user.getQuickAccess();
            String shortName = quick.get(0);
            String name = control.user.getHistory().get(shortName).getName();
            stock = new Stock(name, control.dataLocat + shortName + ".csv");
            drawer.changeStock(stock);
            redraw(true);
        }
    }

    //This function will call the ChartDrawer to redraw the line chart, providing all the user options.
    private void redraw(boolean redraw) {
        boolean[] mvOption = {mv20.isSelected(), mv50.isSelected(), mv100.isSelected(), mv200.isSelected()};

        int adjOrNot = Integer.parseInt(chooseAdj.getSelection().getActionCommand());
        //Get the 'adjusted price or not' option.
        boolean adjOption;
        if (adjOrNot == 1) adjOption = true;
        else adjOption = false;

        int period = Integer.parseInt(periodGroup.getSelection().getActionCommand());

        drawer.draw(period, mvOption, adjOption, redraw);
    }

    private void logout() {
        yahoo.stop();
        String postData = "command=postprofile&username=" + control.user.getUsername()
                + "&profile=" + control.user.toString();
        try {
            control.executePost(postData);
        } catch (IOException error) {
        }
    }

    //Handle all the events.
    class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == logOut) {
                logout();
                close();
                control.redirect("Login");
            }
            else if (e.getSource() == changePassword) {
                setEnabled(false);
                control.redirect("ChangePassword");
            }
            else if (e.getSource() == updateProfile) {
                setEnabled(false);
                control.redirect("UpdateProfile");
            }
            else if (e.getSource() == year_1 || e.getSource() == year_2 || e.getSource() == year_5 ||
                    e.getSource() == all || e.getSource() == adj || e.getSource() == noAdj) {
                redraw(false);
            }
            else if (e.getSource() == mv20 || e.getSource() == mv50 ||
                    e.getSource() == mv100 || e.getSource() == mv200) {
                redraw(false);
            }
        }
    }
}
