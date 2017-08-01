package StandAlone;

import org.jfree.chart.*;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.AxisChangeListener;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import javax.swing.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.ListIterator;

public class ChartDrawer {
    //This class is responsible to draw the line chart on a JPanel with the user option.

    private ChartPanel chartPanel;
    private Stock stock; //The array which store the reference to a Stock object.
    private XYDataset dataset;
    final private String[] series = {"Closing price", "Moving average(20)", "Moving average(50)",
            "Moving average(100)", "Moving average(200)"};
    private JFreeChart chart;
    private XYPlot plot;
    private final XYLineAndShapeRenderer render = new XYLineAndShapeRenderer(true, true);
    private TimeSeries[] s = new TimeSeries[5];
    private int period = 0;
    private boolean[] mvOption;
    private boolean adjOption;
    private ValueAxis x, y;
    private Color[] lineColor = {Color.GRAY, Color.CYAN, Color.BLUE, Color.ORANGE, Color.MAGENTA};

    //Initialize with a reference of Stock object which provides all the data.
    public ChartDrawer(Stock stock) {
        this.stock = stock;
    }

    public void changeStock(Stock stock) { this.stock = stock; }

    //Create a blank chart panel and return the reference of panel.
    public JPanel createChartPanel() {
        String chartTitle = "Moving Average";
        String xAxisLabel = "Time";
        String yAxisLabel = "Price";
        chart = ChartFactory.createTimeSeriesChart(chartTitle, xAxisLabel,
                yAxisLabel, dataset, true, true, false);
        chartPanel = new ChartPanel(chart);
        plot = chart.getXYPlot();
        x = plot.getDomainAxis();
        y = plot.getRangeAxis();

        y.addChangeListener(new AxisChangeListener() {
            @Override
            public void axisChanged(AxisChangeEvent e) {
                plot.clearAnnotations();
                drawRecommendation();
                chartPanel.repaint();
            }
        }); /*Once a 'zoom' is executed, the Listener will respond to redraw the buy/sell indicators
            because the size of indicators are changed. Cause the axisChange event of y axis comes later
            than x axis, the Listener is only added to y axis to perform the changes. */

        return chartPanel;
    }

    //Extract the data and draw on the panel.
    public void draw(int tempPeriod, boolean[] mvOpt, boolean adjOpt, boolean redraw) {
        if (tempPeriod != period || redraw) { //If the period is different from the previous option, let the Stock
                                    //object extract the data and calculate the MV again.
            period = tempPeriod;

            adjOption = adjOpt;
            createSeries(stock.getClose(period, adjOpt), stock.getMovingAvg(period, adjOpt),
                    stock.getDate(period)); //Create the XY Data Series.
            mvOption = mvOpt;
            x.setAutoRange(true);
            y.setAutoRange(true);
            update(); //Update the line chart.
        }
        else if (adjOption != adjOpt) { //If only the option of adjusted option has changed, only the data series
                                        // need to be updated.
            adjOption = adjOpt;
            createSeries(stock.getClose(period, adjOpt), stock.getMovingAvg(period, adjOpt),
                    stock.getDate(period)); //Create the XY Data Series.
            mvOption = mvOpt;
            x.setAutoRange(true);
            y.setAutoRange(true);
            update(); //Update the line chart.
        }
        else { //If only the MV option has changed, only the line being displayed need to be determined again.
            mvOption = mvOpt;
            update(); //Update the line chart.
        }
    }

    //Create database with data series.
    private XYDataset createDatabase() {
        final TimeSeriesCollection result = new TimeSeriesCollection();
        for (int g = 0; g < 5; g++) {
            if (g != 0 && mvOption[g - 1] == false) {
                continue;
            }
            else {
                result.addSeries(s[g]);
            }
        }
        return result;
    }

    //This method create the data series. Each series store the XY data for one single line.
    private void createSeries(float[] p, float[][] m, int[] d) {
        float[][] data = new float[5][];
        data[0] = p;
        data[1] = m[0]; data[2] = m[1]; data[3] = m[2]; data[4] = m[3];

        s[0] = new TimeSeries("Closing price");
        s[1] = new TimeSeries("Moving average(20)");
        s[2] = new TimeSeries("Moving average(50)");
        s[3] = new TimeSeries("Moving average(100)");
        s[4] = new TimeSeries("Moving average(200)");
        //Totally, there are 5 possible series being displayed.
        int num = p.length;
        String date;

        for (int g = 0; g < 5; g++) {
            for (int i = 0; i < num; i++) {
                date = "" + d[i];
                int year = Integer.parseInt(date.substring(0, 4));
                int month = Integer.parseInt(date.substring(4, 6));
                int day = Integer.parseInt(date.substring(6, 8));
                s[g].add(new Day(day, month, year), data[g][i]);
            }
        }
    }

    //This function update the line chart. The actual action is performed in 'createDatabase()' and 'drawRecommendation()'.
    public void update() {
        plot.clearAnnotations(); //Clear the old buy/sell recommendation.
        dataset = createDatabase(); //Construct the dataset.
        plot.setDataset(dataset); //Reload the new dataset.
        plot.setRenderer(render);
        XYLineAndShapeRenderer r = (XYLineAndShapeRenderer)plot.getRenderer();
        r.setShapesVisible(false); //Just make the line looks better.
        changeLine();
        drawRecommendation(); //Draw the buy/sell recommendation.
        chart.setTitle(stock.getStockName());
        chartPanel.repaint(); //Redraw the whole line chart.
    }

    private void changeLine() {
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.getRenderer().setSeriesPaint(0, lineColor[0]);
        plot.getRenderer().setSeriesStroke(0, new BasicStroke(1.5f));
        int n = 1;
        for (int i = 0; i < 4; i++) {
            if(mvOption[i]) {
                plot.getRenderer().setSeriesPaint(n, lineColor[i+1]);
                plot.getRenderer().setSeriesStroke(n, new BasicStroke(1.5f));
                n ++;
            }
        }
    }

    //This function draw the sell/buy recommendation.
    public void drawRecommendation() {
        //Get the MV option selected by user.
        ArrayList<Stock.CrossPoint> cross; //An arrayList stores the crossing points data.
        Stock.CrossPoint temp;
        XYShapeAnnotation anno;

        double h = (5 * y.getRange().getLength()) / 100;
        double w = (12 * x.getRange().getLength()) / 1000;
        //The height and width of the triangle indicators, relative to the size of the graph.

        int tarDate = stock.getLastDate() - period * 10000;
        if(period == 0) tarDate = 0;

        //Scan through the arrayList
        for (int i = 0; i < 4; i++) {
            for (int j = i+1; j < 4; j++) {
                if (mvOption[i] && mvOption[j]) {
                    cross = stock.getCross(i, j, adjOption); //Get the crossing points data by
                    // providing the MV No. and adjusted price option.
                    ListIterator<Stock.CrossPoint> iter = cross.listIterator(cross.size());
                    while (iter.hasPrevious()) {
                        //Draw the annotation one by one.
                        temp = iter.previous();
                        if (temp.date < tarDate) {
                            break;
                        }
                        if (temp.buy) {
                            //The triangle is draw by three enclosed paths.
                            Path2D upTriangle = new Path2D.Double();
                            upTriangle.moveTo(temp.time - w/2, temp.price - h/2);
                            upTriangle.lineTo(temp.time + w/2, temp.price - h/2);
                            upTriangle.lineTo(temp.time, temp.price + h/2);
                            upTriangle.closePath();
                            anno = new XYShapeAnnotation(upTriangle,
                                    new BasicStroke(0.5f), Color.GREEN, Color.GREEN);
                        }
                        else {
                            Path2D downTriangle = new Path2D.Double();
                            downTriangle.moveTo(temp.time - w/2, temp.price + h/2);
                            downTriangle.lineTo(temp.time + w/2, temp.price + h/2);
                            downTriangle.lineTo(temp.time, temp.price - h/2);
                            downTriangle.closePath();
                            anno = new XYShapeAnnotation(downTriangle,
                                    new BasicStroke(0.5f), Color.RED, Color.RED);
                        }
                        plot.addAnnotation(anno);
                        //The annotation is not drawn immediately, instead it is stored in the plot and then drawn by it.
                    }
                }
            }
        }
    }

}

