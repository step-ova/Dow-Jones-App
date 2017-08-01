package StandAlone;

import org.jfree.data.time.Day;
import org.jfree.data.time.MovingAverage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Write a description of class Stock here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Stock
{
    //This class read the .csv file, extract the data and implement all the algorithm for MV and crossing points.
    //This is the class handle all the calculations of a single Stock.
    //It contains a inner class "Data" which stores the information of a single day.
    //It also has a inner class "CrossPoint" which stores the information of a single crossing point.

    private int[] index = {0, 0, 0, 0, 0, 0};
    private ArrayList<Data> data = new ArrayList<Data>();
    private String stockName;
    private int lastDate; //The latest date of the stock.
    private int[] date;
    private float[] close;
    private float[] adjClose; //These 3 arrays store the date, closing price, adjusted price of the specified period.
    private float[][] movingAvg;
    private float[][] adjAvg; //These 2 arrays store the 4 moving average.
    private ArrayList<ArrayList<CrossPoint>> cross;
    private ArrayList<ArrayList<CrossPoint>> adjCross; //These 2 are the arrayList of arrayList,
                                                       //because each arrayList only contains the crossing points of one pair of MVs.

    public String getStockName()
    {
        return stockName;
    }

    //Initialize the Stock object by providing the stock No. and the location of the .csv file.
    public Stock(String name, String fileLocat) {
        stockName = name;
        loadCSV(fileLocat);
        extractData();
    }

    public float[] getClose(int p ,boolean adj) {
        if (adj) return Arrays.copyOfRange(adjClose, 0, index[p]);
        else return Arrays.copyOfRange(close, 0, index[p]);
    }

    public int getIndex(int p) {
        return index[p];
    }

    public int getLastDate() {return lastDate;}

    public int[] getDate(int p) {
        return Arrays.copyOfRange(date, 0, index[p]);
    }

    //This method will generate all the data including price and all the MVs for the specified period.
    //Period can be 1, 2, 5 or 0 which means all the data.
    //The 3 methods are invoked one by one: extractData() --> extractMoving() --> findCross()
    public void extractData() {
        cross = new ArrayList<ArrayList<CrossPoint>>(6);
        adjCross = new ArrayList<ArrayList<CrossPoint>>(6); //For 4 MVs, there are 6 pairs to cross.
        int num = data.size();
        int currentDate;
        date = new int[num];
        close = new float[num];
        adjClose = new float[num]; //Initiate the size of the array to the largest size possible.
        Iterator<Data> iterator = data.iterator();
        Data temp;

        //Extract all the data.
        int i = 0;
        int period = 1;
        int tarDate = lastDate - period * 10000;
        while (iterator.hasNext())
        {
            temp = iterator.next();
            date[i] = temp.date;
            close[i] = temp.close;
            adjClose[i] = temp.adjClose;

            if (date[i] <= tarDate) {
                index[period] = i;
                switch(period) {
                    case 1: period = 2; tarDate = lastDate - period*10000; break;
                    case 2: period = 5; tarDate = lastDate - period*10000; break;
                    case 5: period = 0; tarDate = 0; break;
                }
            }
            i++;
        }
        if(index[1] == 0) {
            index[1] = i-1;
        }
        if(index[2] == 0) {
            index[1] = i-1;
        }
        if(index[5] == 0) {
            index[1] = i-1;
        }
        index[0] = i - 1;

        extractMoving(); //Calculate the moving average.
    }

    public float[][] getMovingAvg(int p, boolean adj) {
        float[][] result = new float[4][index[p]];
        if (adj) {
            for(int i = 0; i < 4; i++) {
                result[i] = Arrays.copyOfRange(adjAvg[i], 0, index[p]);
            }
            return result;
        }
        else {
            for(int i = 0; i < 4; i++) {
                result[i] = Arrays.copyOfRange(movingAvg[i], 0, index[p]);
            }
            return result;
        }
    }

    //Read .csv file line by line and store it in the arrayList.
    private void loadCSV(String fileLocat) {
        BufferedReader read = null;
        String line = "";
        try{
            read = new BufferedReader(new FileReader(fileLocat));
            line = read.readLine(); // Remove the first line.
            while ((line = read.readLine()) != null)
            {
                storeData(line);
            }
            lastDate = data.get(0).date;
            
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
    }

    //The inner class stores the data for one day of the stock.
    private class Data {
        public int date;
        public float open;
        public float high;
        public float low;
        public float close;
        public int volume;
        public float adjClose;

        public Data(int date, float open, float high, float low,
            float close, int volume, float adjClose)
        {
            this.date = date;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
            this.adjClose = adjClose;
        }
    }

    //The inner class stores the data for one single crossing points.
    public class CrossPoint {
        public long time;
        public float price;
        public boolean buy;
        public int date;

        public CrossPoint(int date1, int date2, float l1, float l2, float s1, float s2, boolean buy) {
            this.date = date1;
            String temp = "" + date1;
            int year = Integer.parseInt(temp.substring(0,4));
            int month = Integer.parseInt(temp.substring(4,6));
            int day = Integer.parseInt(temp.substring(6,8));
            long t1 = (new Day(day, month, year)).getFirstMillisecond();

            temp = "" + date2;
            year = Integer.parseInt(temp.substring(0,4));
            month = Integer.parseInt(temp.substring(4,6));
            day = Integer.parseInt(temp.substring(6,8));
            long t2 = (new Day(day, month, year)).getFirstMillisecond();
            //The date is stored in milliseconds, because the line chart uses this unit.
            this.buy = buy;

            this.price = ((l2-l1)*s2 + (s1-s2)*l2) / (s1-s2-l1+l2);
            this.time = (long)(((t1-t2)*(this.price-l2)) / (l1-l2)) + t2;
        }
    }

    //Store a line from .csv file into arrayList
    private void storeData(String line) {// Store the Data in arrays.
        String[] content;
        content = line.split(","); // Split the data fields.
        String[] d = content[0].split("-");
        String dateString = d[0]+d[1]+d[2];
        data.add(new Data(Integer.parseInt(dateString), Float.parseFloat(content[1]),
                    Float.parseFloat(content[2]), Float.parseFloat(content[3]),
                    Float.parseFloat(content[4]), Integer.parseInt(content[5]),
                    Float.parseFloat(content[6]))); // Create a new object of Data and add it to ArrayList.
    }

    //Calculate the 4 moving average.
    private void extractMoving() {
        int num = date.length;
        adjAvg = new float[4][num];
        movingAvg = new float[4][num];
        for (int j = 0; j < 4; j++) {
            movingAvg[j][num-1] = close[num-1];
            adjAvg[j][num-1] = adjClose[num-1];
        }
        int[] mv = {20,50,100,200};

        //Scanning through each point and update the average.
        for (int i = 1; i < num; i++)
        {
            for (int j = 0; j < 4; j++) {
                if (i < mv[j]) {
                    movingAvg[j][num - 1 - i] = (movingAvg[j][num - i] * i + close[num - 1 - i]) / (i+1);
                    adjAvg[j][num - 1 - i] = (adjAvg[j][num - i] * i + adjClose[num - 1 - i]) / (i+1);
                } else {
                    movingAvg[j][num - 1 - i] = movingAvg[j][num - i] -
                            (close[num + mv[j]-1 - i] - close[num - 1 - i]) / mv[j];
                    adjAvg[j][num - 1 - i] = adjAvg[j][num - i] -
                            (adjClose[num + mv[j]-1 - i] - adjClose[num - 1 - i]) / mv[j];
                }
            }
        }
        findCross(); //Find out all the crossing points.
    }

    //This is to round a small floating number to 0.
    //While finding a crossing point, a very weak crossing will be ignored.
    private float round(float a) {
        if (a < 0.1 && a > -0.1) return 0.0f;
        else return a;
    }

    private void findCross() {
        float [] longTerm;
        float [] shortTerm;
        float last;
        float current;

        //The crossing points is found by a simple algorithm.
        //The differences between the two MVs of each point are calculated.
        //The consecutive differences with the same sign (positive or negative)
        //will be accumulated until a different sign is found.
        //This point with the different sign is considered to be a crossing point.
        for (int i = 0; i < 4; i++) {
            for (int j = i+1; j < 4; j++) {
                    ArrayList<CrossPoint> result = new ArrayList<CrossPoint>();
                    longTerm = movingAvg[j];
                    shortTerm = movingAvg[i];
                    int num = longTerm.length;
                    last = longTerm[num-1] - shortTerm[num-1];
                    for (int n = num-2; n >= 0; n--) {
                        current = longTerm[n] - shortTerm[n];
                        if (current * last >= 0) {
                            last += current;
                        } else {
                            if (current > 0) result.add(new CrossPoint(date[n], date[n+1], longTerm[n],
                                    longTerm[n+1], shortTerm[n], shortTerm[n+1], false));
                            else result.add(new CrossPoint(date[n], date[n+1], longTerm[n],
                                    longTerm[n+1], shortTerm[n], shortTerm[n+1], true));
                            last = current;
                        }
                    }
                    cross.add(result);
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = i+1; j < 4; j++) {
                ArrayList<CrossPoint> result = new ArrayList<CrossPoint>();
                longTerm = adjAvg[j];
                shortTerm = adjAvg[i];
                int num = longTerm.length;
                last = longTerm[num-1] - shortTerm[num-1];
                for (int n = num-2; n >= 0; n--) {
                    current = longTerm[n] - shortTerm[n];
                    if (current * last >= 0) {
                        last += current;
                    } else {
                        if (current > 0) result.add(new CrossPoint(date[n], date[n+1], longTerm[n],
                                longTerm[n+1], shortTerm[n], shortTerm[n+1], false));
                        else result.add(new CrossPoint(date[n], date[n+1], longTerm[n],
                                longTerm[n+1], shortTerm[n], shortTerm[n+1], true));
                        last = current;
                    }
                }
                adjCross.add(result);
            }
        }
    }

    public ArrayList<CrossPoint> getCross(int a, int b, boolean adj) {
        if (a == 0 && b == 1) {
            if (!adj) return cross.get(0); else return adjCross.get(0);
        }
        else if (a == 0 && b == 2) {
            if (!adj) return cross.get(1); else return adjCross.get(1);
        }
        else if (a == 0 && b == 3) {
            if (!adj) return cross.get(2); else return adjCross.get(2);
        }
        else if (a == 1 && b == 2) {
            if (!adj) return cross.get(3); else return adjCross.get(3);
        }
        else if (a == 1 && b == 3) {
            if (!adj) return cross.get(4); else return adjCross.get(4);
        }
        else if (a == 2 && b == 3) {
            if (!adj) return cross.get(5); else return adjCross.get(5);
        }
        else return null;
    }
}






