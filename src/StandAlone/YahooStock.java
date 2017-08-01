package StandAlone;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.Hashtable;
import java.util.Set;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

/**
 * Created by 30812 on 2016-11-02.
 */
public class YahooStock implements Runnable{
    private String fileLocat;
    private Hashtable<String, String> symbolNames;
    private static String HISTORIC_DATA = "http://ichart.finance.yahoo.com/table.csv?s=";
    LocalDate fromDate, toDate;
    private int fromDay, fromMonth, fromYear;
    private int toMonth, toDay, toYear;
    private Thread t;
    private FileOutputStream fos = null;
    private int done;
    private MainGUI update;
    private String content;

    YahooStock(String fileLocat, MainGUI update) {
        this.fileLocat = fileLocat;
        this.update = update;
        toDate = LocalDate.now();
        fromDate = LocalDate.of(1602, 1, 1);
        fromDay = fromDate.getDayOfMonth();
        fromMonth = fromDate.getMonthValue() - 1; //YahooAPI months are numbered from 0 to 11
        fromYear = fromDate.getYear();
        toDay = toDate.getDayOfMonth();
        toMonth = toDate.getMonthValue() - 1; //YahooAPI months are numbered from 0 to 11
        toYear = toDate.getYear();
        loadSymbol();
    }

    public Hashtable<String, String> getSymbol() {
        return symbolNames;
    }

    public void checkUptodate() {
        BufferedReader br = null;
        LocalDate local = null;
        String[] info = {"a"};
        File folder = new File(fileLocat);
        if(!folder.exists()) {
            folder.mkdir();
        }

        try {
            File f = new File(fileLocat + "LogFile.txt");
            if(f.exists()) {
                String line;
                br = new BufferedReader(new FileReader(fileLocat + "LogFile.txt"));
                line = br.readLine();
                info = line.split("\t");
                done = info.length - 3;
                local = LocalDate.of(Integer.parseInt(info[0]), Integer.parseInt(info[1]) + 1, Integer.parseInt(info[2]));
            }
            else {
                local = LocalDate.of(1600, 10, 10);
                done = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try {
            LocalDate temp = toDate.minusDays(30);
            URL website = null;
            ReadableByteChannel rbc = null;
            website = new URL(HISTORIC_DATA + "MSFT"
                    + "&a=" + (temp.getMonthValue() - 1)
                    + "&b=" + temp.getDayOfMonth()
                    + "&c=" + temp.getYear()
                    + "&d=" + toMonth
                    + "&e=" + toDay
                    + "&f=" + toYear
                    + "&g=d&ignore=.csv");
            rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(fileLocat + "update.csv");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedReader read = null;
        String ms = "";
        try{
            read = new BufferedReader(new FileReader(fileLocat + "update.csv"));
            ms = read.readLine(); // Remove the first line.
            ms = read.readLine();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            if (read != null) {
                try {
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] first = ms.split(",");
        String[] date = first[0].split("-");
        LocalDate latest = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        content = latest.getYear() + "\t" + (latest.getMonthValue() - 1) + "\t" + latest.getDayOfMonth() + "\t";
        if (local.compareTo(latest) != 0) {
            done = 0;
            start();
        }
        else {
            if (done != 0) {
                for (int j = 3; j < info.length; j++) {
                    update.control.user.getHistory().get(info[j]).update();
                    content += (info[j] + "\t");
                }
            }
            start();
        }
    }

    public void stop() {
        if (t != null)
            t.interrupt();
    }

    public void run() {
        int i = 1;
        List<String> downloadList = update.control.user.getQuickAccess();
        File oldFile, newFile, logFile;

        if (done == 30) {
            update.updateCell(i, done - 1);
        }
        else {
            try {
                for (String symbol : downloadList) {
                    if (!update.control.user.getHistory().get(symbol).getUpdate()) {
                        URL website = null;
                        ReadableByteChannel rbc = null;
                        website = new URL(makeURL(symbol));
                        rbc = Channels.newChannel(website.openStream());
                        fos = new FileOutputStream(fileLocat + symbol + "_temp.csv");
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                        try {
                            fos.close();
                            oldFile = new File(fileLocat + symbol + ".csv");
                            newFile = new File(fileLocat + symbol + "_temp.csv");
                            oldFile.delete();
                            newFile.renameTo(new File(fileLocat + symbol + ".csv"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        update.control.user.downloaded(symbol);
                        update.updateCell(i, done);
                        content += symbol + "\t";
                        logFile = new File(fileLocat + "LogFile.txt");
                        FileWriter fw = new FileWriter(logFile.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(content);
                        bw.close();
                        i++;
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    public void start () {
        if (t == null) {
            t = new Thread (this, "Download");
            t.start ();
        }
    }

    private String makeURL(String symbol) {
        return HISTORIC_DATA + symbol
                + "&a=" + fromMonth
                + "&b=" + fromDay
                + "&c=" + fromYear
                + "&d=" + toMonth
                + "&e=" + toDay
                + "&f=" + toYear
                + "&g=d&ignore=.csv";
    }

    private void loadSymbol() {
        symbolNames = new Hashtable<String, String>();
        symbolNames.put("American Express Company", "AXP");
        symbolNames.put("Apple Inc.", "AAPL");
        symbolNames.put("The Boeing Company", "BA");
        symbolNames.put("Caterpillar Inc.", "CAT");
        symbolNames.put("Cisco Systems, Inc.", "CSCO");
        symbolNames.put("Chevron Corporation", "CVX");
        symbolNames.put("E. I. du Pont de Nemours and Company", "DD");
        symbolNames.put("The Walt Disney Company", "DIS");
        symbolNames.put("General Electric Company", "GE");
        symbolNames.put("The Goldman Sachs Group, Inc.", "GS");
        symbolNames.put("The Home Depot, Inc.", "HD");
        symbolNames.put("International Business Machines Corp.", "IBM");
        symbolNames.put("Intel Corporation", "INTC");
        symbolNames.put("Johnson & Johnson", "JNJ");
        symbolNames.put("JPMorgan Chase & Co.", "JPM");
        symbolNames.put("The Coca-Cola Company", "KO");
        symbolNames.put("McDonald's Corp.", "MCD");
        symbolNames.put("3M Company", "MMM");
        symbolNames.put("Merck & Co., Inc.", "MRK");
        symbolNames.put("Microsoft Corporation", "MSFT");
        symbolNames.put("NIKE, Inc.", "NKE");
        symbolNames.put("Pfizer Inc.", "PFE");
        symbolNames.put("The Procter & Gamble Company", "PG");
        symbolNames.put("The Travelers Companies, Inc.", "TRV");
        symbolNames.put("UnitedHealth Group Incorporated", "UNH");
        symbolNames.put("United Technologies Corporation", "UTX");
        symbolNames.put("Visa Inc.", "V");
        symbolNames.put("Verizon Communications Inc.", "VZ");
        symbolNames.put("Wal-Mart Stores Inc.", "WMT");
        symbolNames.put("Exxon Mobil Corporation", "XOM");
    }

}
