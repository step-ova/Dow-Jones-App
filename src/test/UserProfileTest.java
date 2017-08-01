package test;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.junit.*;

import StandAlone.UserProfile;

public class UserProfileTest {

	UserProfile userProfile;

	/*
	 * This tests if the symbol has the right contents and if it is correctly
	 * sorted This method originally returns all of the symbols in sorted order
	 * according to the values they have.
	 * 
	 * We test this by sorting all of the values related to the symbols, and use
	 * the symbols returned sorted by the program to see if they are correctly
	 * arranged
	 */
	@Test
	public void getQuickAccessTest() {
		userProfile = new UserProfile(null, null, null);
		List<String> sortedValuesProgram = userProfile.getQuickAccess();
		List<String> sortedValuesTest = new DowJonesSymbols().getSortedValues();

		if (sortedValuesProgram.size() != sortedValuesTest.size()) {
			fail("not same size");
		}

		for (int i = 0; i < sortedValuesProgram.size(); i++) {
			String symbol = sortedValuesProgram.get(i);
			String program = new DowJonesSymbols().symbolNames.get(symbol);
			String test = sortedValuesTest.get(i);
			if (!program.equals(test)) {
				fail("problem with sorted values");
			}
		}

		// Test passes if all values are equal
		return;

	}

	public class DowJonesSymbols {

		public static final String AXP = "AXP";
		public static final String AAPL = "AAPL";
		public static final String BA = "BA";
		public static final String CAT = "CAT";
		public static final String CSCO = "CSCO";
		public static final String CVX = "CVX";
		public static final String DD = "DD";
		public static final String DIS = "DIS";
		public static final String GE = "GE";
		public static final String GS = "GS";
		public static final String HD = "HD";
		public static final String IBM = "IBM";
		public static final String INTC = "INTC";
		public static final String JNJ = "JNJ";
		public static final String JPM = "JPM";
		public static final String KO = "KO";
		public static final String MCD = "MCD";
		public static final String MMM = "MMM";
		public static final String MRK = "MRK";
		public static final String MSFT = "MSFT";
		public static final String NKE = "NKE";
		public static final String PFE = "PFE";
		public static final String PG = "PG";
		public static final String TRV = "TRV";
		public static final String UNH = "UNH";
		public static final String UTX = "UTX";
		public static final String V = "V";
		public static final String VZ = "VZ";
		public static final String WMT = "WMT";
		public static final String XOM = "XOM";

		public final Hashtable<String, String> symbolNames = getInstance();

		private DowJonesSymbols() {

		}

		private Hashtable<String, String> getInstance() {
			Hashtable<String, String> symbolNames = new Hashtable<String, String>();

			symbolNames.put(DowJonesSymbols.AXP, "American Express Company");
			symbolNames.put(DowJonesSymbols.AAPL, "Apple Inc.");
			symbolNames.put(DowJonesSymbols.BA, "The Boeing Company");
			symbolNames.put(DowJonesSymbols.CAT, "Caterpillar Inc.");
			symbolNames.put(DowJonesSymbols.CSCO, "Cisco Systems, Inc.");
			symbolNames.put(DowJonesSymbols.CVX, "Chevron Corporation");
			symbolNames.put(DowJonesSymbols.DD, "E. I. du Pont de Nemours and Company");
			symbolNames.put(DowJonesSymbols.DIS, "The Walt Disney Company");
			symbolNames.put(DowJonesSymbols.GE, "General Electric Company");
			symbolNames.put(DowJonesSymbols.GS, "The Goldman Sachs Group, Inc.");
			symbolNames.put(DowJonesSymbols.HD, "The Home Depot, Inc.");
			symbolNames.put(DowJonesSymbols.IBM, "International Business Machines Corporation");
			symbolNames.put(DowJonesSymbols.INTC, "Intel Corporation");
			symbolNames.put(DowJonesSymbols.JNJ, "Johnson & Johnson");
			symbolNames.put(DowJonesSymbols.JPM, "JPMorgan Chase & Co.");
			symbolNames.put(DowJonesSymbols.KO, "The Coca-Cola Company");
			symbolNames.put(DowJonesSymbols.MCD, "McDonald's Corp.");
			symbolNames.put(DowJonesSymbols.MMM, "3M Company");
			symbolNames.put(DowJonesSymbols.MRK, "Merck & Co., Inc.");
			symbolNames.put(DowJonesSymbols.MSFT, "Microsoft Corporation");
			symbolNames.put(DowJonesSymbols.NKE, "NIKE, Inc.");
			symbolNames.put(DowJonesSymbols.PFE, "Pfizer Inc.");
			symbolNames.put(DowJonesSymbols.PG, "The Procter & Gamble Company");
			symbolNames.put(DowJonesSymbols.TRV, "The Travelers Companies, Inc.");
			symbolNames.put(DowJonesSymbols.UNH, "UnitedHealth Group Incorporated");
			symbolNames.put(DowJonesSymbols.UTX, "United Technologies Corporation");
			symbolNames.put(DowJonesSymbols.V, "Visa Inc.");
			symbolNames.put(DowJonesSymbols.VZ, "Verizon Communications Inc.");
			symbolNames.put(DowJonesSymbols.WMT, "Wal-Mart Stores Inc.");
			symbolNames.put(DowJonesSymbols.XOM, "Exxon Mobil Corporation");

			return symbolNames;

		}

		public String getSymbolName(String symbol) {
			return symbolNames.get(symbol);
		}

		/*
		 * Returns all of the values of the hashtable in sorted order
		 */
		public List<String> getSortedValues() {
			List<String> listOfSymbolElements = Collections.list(symbolNames.elements());
			Collections.sort(listOfSymbolElements);
			return listOfSymbolElements;
		}

	}

}
