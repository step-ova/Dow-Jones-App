package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDate;
import java.util.Scanner;


import StandAlone.GUIControl;


import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

/*
 * This test will check if the stock downloaded is correct
 */
public class DownloadYahooFinanceTest {
	
	public final String dataLocat = (new File("").getAbsolutePath().toString()) + "\\Data\\";
	
	@Before
	public void setup(){
		
		GUIControl control = new GUIControl();
		downloadYahooFinanceData();
	}
	
	//
	@Test
	public void testDownloadData(){
		File fileDownloaded = new File("AAPL.csv");
		File originalFile = new File(dataLocat + "AAPL.csv");
		
		String[] downloadedStrings = getFirst30Lines(fileDownloaded);
		String[] originalStrings = getFirst30Lines(originalFile);
		
		if(originalStrings.length != downloadedStrings.length){
			fail();
		}
		
		for(int i = 0; i< originalStrings.length; i++){
			if(!originalStrings[i].equals(downloadedStrings[i])){
				fail();
			}
		}
		
		return;
	}
	
	
	/*
	 * Delete the file created in the setup
	 */
	@After
	public void teardown(){
		new File("AAPL.csv").delete();
	}
	
	
	/*
	 * Download methods
	 */
	
	
	private static String HISTORIC_DATA = "http://ichart.finance.yahoo.com/table.csv?s=";
	
	private void downloadYahooFinanceData(){
		LocalDate toDate = LocalDate.now();
		LocalDate fromDate = LocalDate.now().minusYears(500);
		
		String historicData = makeHistoricDate("AAPL", fromDate, toDate);
		
		URL website = null;
		ReadableByteChannel rbc = null;
		FileOutputStream fos = null;
		
		try{
		website = new URL(historicData);
		rbc = Channels.newChannel(website.openStream());
		fos = new FileOutputStream("AAPL.csv");
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
		
		//Close open streams
		finally{
			try {
				fos.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		
	}
	
	private String makeHistoricDate(String symbol, LocalDate fromDate, LocalDate toDate){
		
		
		int fromDay = fromDate.getDayOfMonth();
		int fromMonth = fromDate.getMonthValue() - 1; //YahooAPI months are numbered from 0 to 11
		int fromYear = fromDate.getYear();
		

		int toDay = toDate.getDayOfMonth();
		int toMonth = toDate.getMonthValue() - 1; //YahooAPI months are numbered from 0 to 11
		int toYear = toDate.getYear();
		
		
		//Make url
		return HISTORIC_DATA + symbol 
				+ "&a=" + fromMonth
				+ "&b=" + fromDay
				+ "&c=" + fromYear
				+ "&d=" + toMonth
				+ "&e=" + toDay
				+ "&f=" + toYear
				+ "&g=d&ignore=.csv";
	}
	
	/*
	 * ReadFile methods
	 */
	
	public String[] getFirst30Lines(File file){
		Scanner sc = null;
		
		String[] values = new String[30];
		
		try{
			sc = new Scanner(new FileInputStream(file));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		for(int i=0; i<values.length; i++){
			values[i] = sc.nextLine();
		}
		
		
		return values;
	}
	
}
