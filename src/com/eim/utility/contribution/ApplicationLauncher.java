package com.eim.utility.contribution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.eim.util.format.DateFormatter;
import com.eim.utility.common.model.EIMCompanies;
import com.eim.utility.common.model.EntityType;
import com.eim.utility.contribution.runners.SimpleContributionsRunner;

public class ApplicationLauncher {

	static double [] _36monthsTrack_1 = new double [] {
	    0.0415,
	    0.0506,
	   -0.0273,
	    0.0513,
	    0.0232,
	   -0.0302,
	   -0.0122,
	    0.0147,
	    0.0558,
	    0.0565,
	   -0.0242,
	    0.0571,
	    0.0557,
	    0.0085,
	    0.0400,
	   -0.0258,
	    0.0022,
	    0.0516,
	    0.0392,
	    0.0559,
	    0.0256,
	   -0.0364,
	    0.0449,
	    0.0534,
	    0.0279,
	    0.0358,
	    0.0343,
	   -0.0008,
	    0.0255,
	   -0.0229,
	    0.0306,
	   -0.0368,
	   -0.0123,
	   -0.0354,
	   -0.0303,
	    0.0423
	};
	static double [] _36monthsTrack_2 = new double [] {
	   -0.0286,
	   -0.0249,
	    0.0328,
	   -0.0198,
	    0.0338,
	    0.0467,
	   -0.0191,
	    0.0329,
	    0.0213,
	    0.0113,
	    0.0559,
	   -0.0216,
	   -0.0199,
	    0.0259,
	    0.0339,
	   -0.0026,
	    0.0576,
	   -0.0365,
	   -0.0072,
	    0.0251,
	   -0.0361,
	   -0.0264,
	   -0.0161,
	    0.0459,
	    0.0514,
	   -0.0392,
	   -0.0154,
	    0.0210,
	    0.0585,
	    0.0579,
	    0.0219,
	    0.0548,
	   -0.0026,
	   -0.0172,
	    0.0168,
	    0.0160
	};
	
	static double [] _132monthsTrack = new double [] {
	    0.0026,
	    0.0225,
	    0.0200,
	    0.0581,
	    0.0364,
	    0.0319,
	    0.0481,
	   -0.0389,
	    0.0172,
	    0.0572,
	    0.0134,
	   -0.0337,
	    0.0165,
	   -0.0172,
	    0.0041,
	   -0.0301,
	   -0.0273,
	    0.0402,
	   -0.0145,
	    0.0100,
	    0.0123,
	   -0.0380,
	    0.0477,
	    0.0060,
	    0.0580,
	    0.0169,
	    0.0474,
	   -0.0158,
	   -0.0282,
	   -0.0249,
	   -0.0341,
	   -0.0200,
	    0.0468,
	    0.0371,
	    0.0532,
	   -0.0319,
	   -0.0330,
	    0.0577,
	    0.0122,
	    0.0388,
	   -0.0090,
	   -0.0281,
	    0.0085,
	    0.0015,
	    0.0516,
	   -0.0241,
	   -0.0196,
	   -0.0250,
	    0.0558,
	    0.0349,
	   -0.0194,
	    0.0266,
	   -0.0180,
	    0.0049,
	    0.0540,
	    0.0182,
	   -0.0252,
	    0.0230,
	   -0.0216,
	   -0.0317,
	   -0.0186,
	    0.0126,
	    0.0267,
	    0.0059,
	   -0.0185,
	   -0.0180,
	    0.0490,
	   -0.0098,
	   -0.0150,
	    0.0497,
	   -0.0280,
	    0.0265,
	   -0.0073,
	   -0.0074,
	    0.0425,
	    0.0150,
	    0.0245,
	    0.0445,
	    0.0195,
	   -0.0069,
	   -0.0189,
	    0.0226,
	   -0.0137,
	    0.0576,
	   -0.0239,
	    0.0395,
	    0.0524,
	    0.0066,
	    0.0214,
	    0.0010,
	    0.0569,
	    0.0253,
	    0.0371,
	    0.0015,
	   -0.0251,
	    0.0094,
	    0.0449,
	    0.0278,
	    0.0046,
	    0.0097,
	   -0.0244,
	    0.0298,
	   -0.0195,
	    0.0277,
	   -0.0275,
	    0.0195,
	    0.0373,
	    0.0180,
	    0.0284,
	    0.0027,
	   -0.0123,
	    0.0578,
	    0.0565,
	    0.0328,
	    0.0325,
	    0.0245,
	   -0.0314,
	    0.0128,
	    0.0452,
	   -0.0190,
	    0.0427,
	   -0.0194,
	    0.0075,
	   -0.0100,
	   -0.0055,
	    0.0528,
	   -0.0370,
	    0.0320,
	   -0.0024,
	    0.0188,
	   -0.0070,
	    0.0465
	};
	
	static {
		Logger rootLogger = Logger.getLogger("com");
		Logger hibernateLogger = Logger.getLogger("org.hibernate");
		rootLogger.setLevel( Level.INFO );
		rootLogger.addAppender( new ConsoleAppender( new PatternLayout( "%d{dd MMM yy  HH:mm:ss} %-48C{1} %-5p:  %m%n" ) ) );
		hibernateLogger.setLevel( Level.INFO );
		hibernateLogger.addAppender( new ConsoleAppender( new PatternLayout( "%d{dd MMM yy  HH:mm:ss} %-48C{1} %-5p:  %m%n" ) ) );
		try {
			rootLogger.addAppender( new FileAppender( new PatternLayout( "%d{dd MMM yy  HH:mm:ss} %-48C{1} %-5p:  %m%n" ),"c:\\temp\\Statistics-[" + DateFormatter.toUsString(new Date())+ "].log" , false) );
		} catch (IOException e) {
			// Ignored
		}
	}
	
//	public static void storeStatistics(Collection<?> list) {
//		Session openSession = HibernateUtil.getSessionFactory().openSession();
//		Transaction beginTransaction = openSession.beginTransaction();
//		for (Object o : list) {
//			openSession.save(o);
//		}
//		beginTransaction.commit();
//		openSession.close();
//	}
	
	public static void main(String argv[]) {
		
//		// ----- Simple contribution selection -----
//		
//		// Set parameters
//		String id = "01.02.12.1269";
//		Date date = new Date();
//		ComputationFrequency frequency = ComputationFrequency.MONTHLY;
//		
//		System.out.println("Top Level Returns - Looking for computation after " + DateFormatter.toInputString(date) + " for " + id + " with frequency " + frequency);
//		
//		// Prepare results
//		ArrayList<IContribution> effectiveList = new ArrayList<IContribution>();
//		
//		// 
//		Session session = HibernateUtil.getSessionFactory().openSession();
//		Transaction transaction = session.beginTransaction();
//		Query query = session.createQuery(
//			"SELECT sc " +
//			"FROM Contribution sc " +
//			"WHERE sc.targetEntityId = ? " +
//			"AND sc.applicationDate >= ? " +
//			"AND sc.frequency = ? " +
//			"ORDER BY sc.applicationDate DESC"
//		); // N.B.: Use corresponding class name instead of table
//		// Fill query parameters
//		query.setString(0, id);
//		query.setDate(1, date);
//		query.setString(2, frequency.name());
//		// Get results
//		List<Contribution> results = query.list();
//		
//		for (Contribution stat : results) {
//			if (DatesComputer.isValidDate(stat.getApplicationDate(), frequency)) {
//				effectiveList.add(stat);
//			}
//		}
//
//		transaction.commit();
//		session.close();
//		
//		System.out.println("Top Level Returns - Get all already existing top level returns for " + id + " with frequency " + frequency + " after " + DateFormatter.toInputString(date));
//		
//		System.out.println("List = " + effectiveList.toString());
		
//		// ----- Simple contribution computation - Defined portfolio within period /// START -----
//		
//		ArrayList<Long> portfolioIDs = new ArrayList<Long>();
//		portfolioIDs.add(new Long(67)); // Luxumbrella
////		portfolioIDs.add(new Long(188)); // Wuestenrot
////		portfolioIDs.add(new Long(237)); // SAMA #2
////		portfolioIDs.add(new Long(286)); // CAP Genève
//		
////		// Make start date
////		Calendar cal = Calendar.getInstance(); 
////		cal.set(2012, Calendar.AUGUST, 30, 0, 0, 0);
////		Date startDate = new Date(cal.getTime().getTime());
////		// Make end date
////		cal.set(2012, Calendar.SEPTEMBER, 1, 0, 0, 0);
////		Date endDate = new Date(cal.getTime().getTime());
//		
////		// On 2012-01-31
////		// Make start date
////		Calendar cal = Calendar.getInstance(); 
////		cal.set(2012, Calendar.JANUARY, 30, 0, 0, 0);
////		Date startDate = new Date(cal.getTime().getTime());
////		// Make end date
////		cal.set(2012, Calendar.FEBRUARY, 1, 0, 0, 0);
////		Date endDate = new Date(cal.getTime().getTime());
//		
//		// On 2012-04-30
//		// Make start date
//		Calendar cal = Calendar.getInstance(); 
//		cal.set(2012, Calendar.APRIL, 29, 0, 0, 0);
//		Date startDate = new Date(cal.getTime().getTime());
//		// Make end date
//		cal.set(2012, Calendar.MAY, 1, 0, 0, 0);
//		Date endDate = new Date(cal.getTime().getTime());
//		
//		SimpleContributionsRunner runner = new SimpleContributionsRunner(portfolioIDs, startDate, endDate);
//		runner.compute();
//		
//		// ----- Simple contribution computation - Defined portfolio within period /// END -----
		
		
//		// ----- Simple contribution computation - Defined portfolio before date /// START -----
//		
//		ArrayList<Long> portfolioIDs = new ArrayList<Long>();
////		portfolioIDs.add(new Long(188)); // Wuestenrot
//		portfolioIDs.add(new Long(237)); // SAMA #2
//		// Make start date
//		Calendar cal = Calendar.getInstance();
////		cal.set(2012, Calendar.AUGUST, 30, 0, 0, 0);
////		Date startDate = new Date(cal.getTime().getTime());
//		// Make end date
//		cal.set(2012, Calendar.SEPTEMBER, 1, 0, 0, 0);
//		Date endDate = new Date(cal.getTime().getTime());
//		
//		SimpleContributionsRunner runner = new SimpleContributionsRunner(portfolioIDs, null, endDate);
//		runner.compute();
//		
//		// ----- Simple contribution computation - Defined portfolio before date /// END -----
		
		
//		// ----- Simple contribution computation - Defined portfolio /// START -----
//		
//		ArrayList<Long> portfolioIDs = new ArrayList<Long>();
////		portfolioIDs.add(new Long(20)); // PV Promea
//		portfolioIDs.add(new Long(67)); // Luxumbrella
////		portfolioIDs.add(new Long(145)); // A.A.A. Long Only Fund - Asian Equities ex-Japan
////		portfolioIDs.add(new Long(188)); // Wuestenrot
////		portfolioIDs.add(new Long(237)); // SAMA #2
//		//portfolioIDs.add(new Long(286)); // CAP Genève
////		// Make start date
////		Calendar cal = Calendar.getInstance(); 
////		cal.set(2012, Calendar.AUGUST, 30, 0, 0, 0);
////		Date startDate = new Date(cal.getTime().getTime());
////		// Make end date
////		cal.set(2012, Calendar.SEPTEMBER, 1, 0, 0, 0);
////		Date endDate = new Date(cal.getTime().getTime());
//		
//		SimpleContributionsRunner runner = new SimpleContributionsRunner(portfolioIDs, null, null);
//		runner.compute();
//		
//		// ----- Simple contribution computation - Defined portfolio /// END -----
		
		
//		// ----- Active portfolios contribution computation within period /// START -----
//		
//		// Make start date
//		Calendar cal = Calendar.getInstance(); 
//		cal.set(2012, Calendar.AUGUST, 30, 0, 0, 0);
//		Date startDate = new Date(cal.getTime().getTime());
//		// Make end date
//		cal.set(2012, Calendar.SEPTEMBER, 1, 0, 0, 0);
//		Date endDate = new Date(cal.getTime().getTime());
//		
//		ActivePortfoliosContributionsRunner runner = new ActivePortfoliosContributionsRunner(startDate, endDate);
//		runner.compute();
//		
//		// ----- Active portfolios contribution computation within period /// END -----
		
		
//		// ----- Active portfolios contribution computation after date /// START -----
//		
//		// Make start date
//		Calendar cal = Calendar.getInstance();
//		cal.set(2012, Calendar.AUGUST, 30, 0, 0, 0);
//		Date startDate = new Date(cal.getTime().getTime());
////		// Make end date
////		cal.set(2012, Calendar.SEPTEMBER, 1, 0, 0, 0);
////		Date endDate = new Date(cal.getTime().getTime());
//		
//		ActivePortfoliosContributionsRunner runner = new ActivePortfoliosContributionsRunner(startDate, null);
//		runner.compute();
//		
//		// ----- Active portfolios contribution computation after date /// END -----
		
		
		// ----- Active Swiss portfolios /// START -----
		
//		// Make start date
//		Calendar cal = Calendar.getInstance();
//		cal.set(2012, Calendar.JANUARY, 1, 0, 0, 0);
//		Date startDate = new Date(cal.getTime().getTime());
//		// Make end date
////		cal.set(2012, Calendar.SEPTEMBER, 1, 0, 0, 0);
////		Date endDate = new Date(cal.getTime().getTime());
//		
//		ActivePortfoliosContributionsRunner runner = new ActiveCHPortfoliosContributionsRunner(startDate, null);
//		runner.compute();
		
		// ----- Active Swiss portfolios /// END -----
		
		
//		// ----- Simple contribution computation - Defined evaluation /// START -----
//		
//		ArrayList<Long> entityIDs = new ArrayList<Long>();
////		entityIDs.add(new Long(1551940)); // Luxumbrella at 2012-04-30
////		entityIDs.add(new Long(1573729)); // ECHO at 2012-10-13
////		entityIDs.add(new Long(1488312)); // CAP Genève at 2012-01-31
////		entityIDs.add(new Long(1579190)); // CB-ACCENT LUX at 2012-10-31 (Estimated)
////		entityIDs.add(new Long(1579543)); // CB-ACCENT LUX at 2012-10-31 (Estimated)
////		entityIDs.add(new Long(1586184)); // CB-ACCENT LUX at 2012-11-30 (Estimated)
//		entityIDs.add(new Long(1595820)); // CB-ACCENT LUX at 2012-12-31 (Estimated)
//		
//		SimpleContributionsRunner runner = new SimpleContributionsRunner(EntityType.EVALUATION, EIMCompanies.EIM_SWITZERLAND, entityIDs, null, null);
//		runner.compute();
//		
//		// ----- Simple contribution computation - Defined evaluation /// END -----
		
		
		// ----- All Swiss portfolios /// START -----
		
//		// Make start date
////		Calendar cal = Calendar.getInstance();
////		cal.set(2012, Calendar.AUGUST, 30, 0, 0, 0);
////		Date startDate = new Date(cal.getTime().getTime());
////		// Make end date
////		cal.set(2012, Calendar.SEPTEMBER, 1, 0, 0, 0);
////		Date endDate = new Date(cal.getTime().getTime());
//		
//		PortfoliosContributionsRunner chRunner = new CHPortfoliosContributionsRunner(null, null);
//		chRunner.compute();
//		
//		// ----- All Swiss portfolios /// END -----
//		
//		
//		// ----- All British portfolios /// START -----
//		
//		// Make start date
////		Calendar cal = Calendar.getInstance();
////		cal.set(2012, Calendar.AUGUST, 30, 0, 0, 0);
////		Date startDate = new Date(cal.getTime().getTime());
////		// Make end date
////		cal.set(2012, Calendar.SEPTEMBER, 1, 0, 0, 0);
////		Date endDate = new Date(cal.getTime().getTime());
//		
//		UKPortfoliosContributionsRunner ukRunner = new UKPortfoliosContributionsRunner(null, null);
//		ukRunner.compute();
		
		// ----- All British portfolios /// END -----
		
		
		// ----- Active British portfolios /// START -----
		
//		// Make start date
////		Calendar cal = Calendar.getInstance();
////		cal.set(2012, Calendar.JANUARY, 1, 0, 0, 0);
////		Date startDate = new Date(cal.getTime().getTime());
//		// Make end date
////		cal.set(2012, Calendar.SEPTEMBER, 1, 0, 0, 0);
////		Date endDate = new Date(cal.getTime().getTime());
//		
//		ActivePortfoliosContributionsRunner runner = new ActiveUKPortfoliosContributionsRunner(null, null);
//		runner.compute();
		
		// ----- Active British portfolios /// END -----
		
		
		// ----- British evaluation /// START -----
		
//		ArrayList<Long> entityIDs = new ArrayList<Long>();
//		entityIDs.add(new Long(1455237)); // MSO at 2012-09-30
//		
//		SimpleContributionsRunner runner = new SimpleContributionsRunner(EntityType.EVALUATION, EIMCompanies.EIM_UK, entityIDs, null, null);
//		runner.compute();
		
		// ----- British evaluation /// END -----
		
		
//		// ----- Simple evaluation /// START -----
//		
//		ArrayList<Long> entityIDs = new ArrayList<Long>();
////		entityIDs.add(new Long(1601811)); // PNPF at 2013-01-25
//		entityIDs.add(new Long(1615160)); // IFC at 2013-02-28
//		
//		SimpleContributionsRunner runner = new SimpleContributionsRunner(EntityType.EVALUATION, EIMCompanies.EIM_SWITZERLAND, entityIDs, null, null);
//		runner.compute();
//		
//		// ----- Simple evaluation /// END -----
		
		
//		// ----- Simple contribution computation - Tuesday CH portfolios /// START -----
//		
//		ArrayList<Long> entityIDs = new ArrayList<Long>();
//		entityIDs.add(new Long(370)); // Albion Alternative Designated Investment (Albion DI)
//		entityIDs.add(new Long(403)); // Albion Alternative Designated Investment No 2 (Albion DI No. 2)
//		entityIDs.add(new Long(327)); // Golden Peak Alternative Strategies (Golden Peak)
//		entityIDs.add(new Long(427)); // Sea Preservation Fund Ltd (Sea Preservation)
//		entityIDs.add(new Long(238)); // Lemania Strategic Hedge Funds (Lemania)
//		entityIDs.add(new Long(278)); // MC Opportunities Fund Ltd (MC Opportunities)
//		entityIDs.add(new Long(211)); // IFC (D) Trust (IFC(D))
//		entityIDs.add(new Long(359)); // Vincal Future (Vincal)
//		entityIDs.add(new Long(452)); // Fondation de Prévoyance Skycare
//		
////		entityIDs.add(new Long(20)); // PV Promea
////		entityIDs.add(new Long(67)); // Luxumbrella
////		entityIDs.add(new Long(145)); // A.A.A. Long Only Fund - Asian Equities ex-Japan
////		entityIDs.add(new Long(188)); // Wuestenrot
////		entityIDs.add(new Long(237)); // SAMA #2
//		//entityIDs.add(new Long(286)); // CAP Genève
//		// Make start date
//		Calendar cal = Calendar.getInstance();
//		cal.set(2013, Calendar.FEBRUARY, 27, 0, 0, 0);
//		Date startDate = new Date(cal.getTime().getTime());
//		// Make end date
//		cal.set(2013, Calendar.MARCH, 20, 0, 0, 0);
//		Date endDate = new Date(cal.getTime().getTime());
//		
//		SimpleContributionsRunner runner = new SimpleContributionsRunner(EntityType.PORTFOLIO, EIMCompanies.EIM_SWITZERLAND, entityIDs, startDate, endDate);
//		runner.compute();
//		
//		// ----- Simple contribution computation - Tuesday CH portfolios /// END -----
//		
//		
//		// ----- Simple contribution computation - Tuesday UK portfolios /// START -----
//		
////		ArrayList<Long> entityIDs = new ArrayList<Long>();
//		entityIDs.clear();
//		entityIDs.add(new Long(434)); // Brandenburg Fund Ltd (Brandenburg)
//		entityIDs.add(new Long(435)); // AAA ALTERNATIVE FUND - Multi-Strategy Opportunities Pool (AAA MSO)
//		
////		entityIDs.add(new Long(20)); // PV Promea
////		entityIDs.add(new Long(67)); // Luxumbrella
////		entityIDs.add(new Long(145)); // A.A.A. Long Only Fund - Asian Equities ex-Japan
////		entityIDs.add(new Long(188)); // Wuestenrot
////		entityIDs.add(new Long(237)); // SAMA #2
//		//entityIDs.add(new Long(286)); // CAP Genève
//		// Make start date
////		Calendar cal = Calendar.getInstance(); 
////		cal.set(2012, Calendar.NOVEMBER, 29, 0, 0, 0);
////		Date startDate = new Date(cal.getTime().getTime());
////		// Make end date
////		cal.set(2012, Calendar.DECEMBER, 1, 0, 0, 0);
////		Date endDate = new Date(cal.getTime().getTime());
//		
//		runner = new SimpleContributionsRunner(EntityType.PORTFOLIO, EIMCompanies.EIM_UK, entityIDs, startDate, endDate);
//		runner.compute();
//		
//		// ----- Simple contribution computation - Tuesday UK portfolios /// END -----
		
		
		// ----- Simple contribution computation - Wednesday CH portfolios /// START -----
		
		ArrayList<Long> entityIDs = new ArrayList<Long>();
		entityIDs.add(new Long(76)); // Longbow Finance SA (Longbow)
		entityIDs.add(new Long(397)); // Stichting-Pensioenfonds TNO (EUR) (TNO)
		entityIDs.add(new Long(229)); // Stichting Rabobank Pensioenfonds (Rabo)
		entityIDs.add(new Long(262)); // P.N.P.F. Trust Company Limited (P.N.P.F.)
		entityIDs.add(new Long(237)); // SAMA - Absolute Diversified Portfolio #2 (SAMA #2)
		entityIDs.add(new Long(90)); // SAMA - Absolute Diversified Portfolio #4 (SAMA #4)
		entityIDs.add(new Long(296)); // SAMA - Absolute Diversified Portfolio #6 (SAMA #6)
		entityIDs.add(new Long(337)); // Max-Planck (Max-Planck USD)
		entityIDs.add(new Long(391)); // Max-Planck EUR (Max-Planck EUR)
		entityIDs.add(new Long(67)); // Luxumbrella VBV MM EIM Hedge (Luxumbrella)
//		entityIDs.add(new Long(370)); // Albion Alternative Designated Investment (Albion DI)
//		entityIDs.add(new Long(403)); // Albion Alternative Designated Investment No 2 (Albion DI No. 2)
//		entityIDs.add(new Long(327)); // Golden Peak Alternative Strategies (Golden Peak)
//		entityIDs.add(new Long(427)); // Sea Preservation Fund Ltd (Sea Preservation)
//		entityIDs.add(new Long(238)); // Lemania Strategic Hedge Funds (Lemania)
//		entityIDs.add(new Long(278)); // MC Opportunities Fund Ltd (MC Opportunities)
//		entityIDs.add(new Long(211)); // IFC (D) Trust (IFC(D))
//		entityIDs.add(new Long(359)); // Vincal Future (Vincal)
		
//		entityIDs.add(new Long(20)); // PV Promea
//		entityIDs.add(new Long(67)); // Luxumbrella
//		entityIDs.add(new Long(145)); // A.A.A. Long Only Fund - Asian Equities ex-Japan
//		entityIDs.add(new Long(188)); // Wuestenrot
//		entityIDs.add(new Long(237)); // SAMA #2
		//entityIDs.add(new Long(286)); // CAP Genève
		// Make start date
		Calendar cal = Calendar.getInstance();
		cal.set(2013, Calendar.FEBRUARY, 27, 0, 0, 0);
		Date startDate = new Date(cal.getTime().getTime());
		// Make end date
		cal.set(2013, Calendar.MARCH, 21, 0, 0, 0);
		Date endDate = new Date(cal.getTime().getTime());
		
		SimpleContributionsRunner runner = new SimpleContributionsRunner(EntityType.PORTFOLIO, EIMCompanies.EIM_SWITZERLAND, entityIDs, startDate, endDate);
		runner.compute();
		
		// ----- Simple contribution computation - Wednesday CH portfolios /// END -----
		
		
		// ----- Simple contribution computation - Wednesday UK portfolios /// START -----
		
//		ArrayList<Long> entityIDs = new ArrayList<Long>();
		entityIDs.clear();
		entityIDs.add(new Long(430)); // The K Opportunity Fund Ltd. (K Opportunity)
		entityIDs.add(new Long(443)); // Rutland Alternative Fund - Global (RAF)
//		entityIDs.add(new Long(434)); // Brandenburg Fund Ltd (Brandenburg)
//		entityIDs.add(new Long(435)); // AAA ALTERNATIVE FUND - Multi-Strategy Opportunities Pool (AAA MSO)
		
//		entityIDs.add(new Long(20)); // PV Promea
//		entityIDs.add(new Long(67)); // Luxumbrella
//		entityIDs.add(new Long(145)); // A.A.A. Long Only Fund - Asian Equities ex-Japan
//		entityIDs.add(new Long(188)); // Wuestenrot
//		entityIDs.add(new Long(237)); // SAMA #2
		//entityIDs.add(new Long(286)); // CAP Genève
		// Make start date
//		Calendar cal = Calendar.getInstance(); 
//		cal.set(2012, Calendar.NOVEMBER, 29, 0, 0, 0);
//		Date startDate = new Date(cal.getTime().getTime());
//		// Make end date
//		cal.set(2012, Calendar.DECEMBER, 1, 0, 0, 0);
//		Date endDate = new Date(cal.getTime().getTime());
		
		runner = new SimpleContributionsRunner(EntityType.PORTFOLIO, EIMCompanies.EIM_UK, entityIDs, startDate, endDate);
		runner.compute();
		
		// ----- Simple contribution computation - Wednesday UK portfolios /// END -----
		
		
//		Calendar cal = Calendar.getInstance();
//		// 12 months ago
//		cal.add(Calendar.MONTH, -12);
//		Date startDate = new Date(cal.getTime().getTime());
//		
//		// PT Portfolios
//		PTPortfoliosContributionsRunner ptRunner = new PTPortfoliosContributionsRunner(startDate, null);
//		ptRunner.compute();
//		
//		// CH Portfolios
//		CHPortfoliosContributionsRunner chRunner = new CHPortfoliosContributionsRunner(startDate, null);
//		chRunner.compute();
//		
//		// UK Portfolios
//		UKPortfoliosContributionsRunner ukRunner = new UKPortfoliosContributionsRunner(startDate, null);
//		ukRunner.compute();
		
//		Calendar cal = Calendar.getInstance();
//		// 12 months ago
//		cal.add(Calendar.MONTH, -12);
//		Date startDate = new Date(cal.getTime().getTime());
//		
//		// CH Portfolios
//		CHPortfoliosContributionsRunner chRunner = new CHPortfoliosContributionsRunner(startDate, null);
//		chRunner.compute();
//		
//		// UK Portfolios
//		UKPortfoliosContributionsRunner ukRunner = new UKPortfoliosContributionsRunner(startDate, null);
//		ukRunner.compute();
		
//		ArrayList<String> ids = new ArrayList<String>();
//		// EFFECTIVE PRODUCT
//		ids.add("01.01.04.0311"); // DMF
//		ids.add("01.01.01.0327"); // DMF
//		ids.add("01.01.01.0312"); // DMF
//		ids.add("01.01.12.1239"); // AMES
//		ids.add("07.01.03.0006"); // MSO
//		ids.add("01.01.12.0313"); // LSDA
//		ids.add("01.01.12.1282"); // LSDA D EUR
//		ids.add("01.01.12.1285"); // LSDA S EUR
//		ids.add("01.01.12.1178"); // LOW VOL TL
//		ids.add("01.01.12.1217"); // LOW VOL CHF
//		ids.add("01.01.12.1218"); // LOW VOL CHF II
//		ids.add("01.01.12.1259"); // LOW VOL SP INV
//		ids.add("01.01.12.1260"); // LOW VOL SP RED
//		ids.add("01.01.12.0329"); // ECHO TL
//		ids.add("01.01.12.0334"); // ECHO P CHF
//		ids.add("01.01.12.0001"); // ECHO P EUR
//		ids.add("01.01.12.0330"); // ECHO P USD
//		ids.add("01.01.12.1179"); // LONG SHORT
//		ids.add("01.01.12.1237"); // LONG SHORT CHF
//		ids.add("01.01.12.1238"); // LONG SHORT EUR
//		ids.add("01.01.12.0308"); // LONG SHORT EUR P
//		ids.add("01.01.12.1239"); // LONG SHORT USD
//		ids.add("01.01.12.0309"); // LONG SHORT USD P
//		
//		ids.add("01.99.14.0298"); // LIQUID ALTERNATIVE
//		
//		ids.add("01.02.09.1259"); // NAT RESOURCES
//		
//		ids.add("01.02.09.1261"); // EUROPEAN EQUI
//		
//		ids.add("07.01.03.1001"); // MSO CLASS S
//		ids.add("07.01.12.1001"); // MSO CLASS S
//		
//		ids.add("01.02.12.1225"); // OBLIG INTERN
//		
//		ids.add("01.02.12.1264");// ACTIONS SUISSES
//		
//		ids.add("01.99.14.0298"); // UCITS 3
//		
//		ids.add("01.02.09.1258"); // USA EQUITIES
//		
//		ids.add("01.02.09.1263"); // LCO
//		
//		ids.add("01.02.09.1262"); // ASIAN EX JAPAN
//		ContributionsTestRunner runner = new ContributionsTestRunner(EntityType.PRODUCT, ids);
//		runner.compute();
//		ids.clear();
//		// INDICES
//		ids.add("370000000000071");
//		ids.add("370000000000073");
//		ids.add("370000000000104");
//		ids.add("370000000000121");
//		ids.add("370000000000122");
//		ids.add("370000000000127");
//		ids.add("370000000000131");
//		ids.add("370000000000140");
//		ids.add("370000000000141");
//		ids.add("370000000000143");
//		ids.add("370000000000203");
//		ids.add("370000000000208");
//		ids.add("370000000000236");
//		ids.add("370000000000241");
//		ids.add("370000000000243");
//		ids.add("370000000000257");
//		ids.add("370000000000282");
//		ids.add("370000000000384");
//		ids.add("370000000000392");
//		ids.add("370000000000405");
//		ids.add("370000000000537");
//		ids.add("370000000000538");
//		ids.add("370000000000545");
//		ids.add("370000000000627");
//		ids.add("370000000000635");
//		ids.add("370000000000652");
//		ids.add("370000000000140");
//		ids.add("370000000000709");
//		ids.add("370000000000714");
//		ids.add("370000000000730");
//		ids.add("370000000000748");
//		ids.add("370000000000783");
//		ids.add("370000000000796");		
//		ids.add("370000000000797");
//		ids.add("370000000000800");
//		runner = new UserDefinedWorkingSetStatisticsRunner(EntityType.INDEX, ids);
//		runner.compute();
//		ids.clear();
//		ids.add("50000000001511"); // ASIAN EX JAPAN
//		ids.add("50000000001512"); // USA EQUITIES
//		ids.add("50000000001516"); // OBLIG INTERN.
//		ids.add("50000000001565"); // EUR EQUITIES		
//		ids.add("50000000003108"); // NAT RESOURCES
//		ids.add("50000000005957"); // DMF
//		ids.add("50000000007225"); // ACTIONS SUISSES		
//		ids.add("50000000007231"); // EUR EQUITIES
//		ids.add("50000000014126"); // LCO
//		ids.add("50000000015084"); // LONG SHORT
//		ids.add("50000000015824"); // DMF
//		ids.add("50000000016507"); // SDA
//		ids.add("50000000016508"); // SDA
//		ids.add("50000000018013"); // AAA AMES
//		ids.add("50000000016562"); // LIQUID ALT
//		ids.add("50000000018131"); // AAA ECHO
//		ids.add("50000000018133"); // ECHO		
//		ids.add("50000000018957"); // ECHO I GBP
//		ids.add("50000000018745"); // ECHO P EUR
//		ids.add("50000000018744"); // ECHO P USD
//		ids.add("50000000018235"); // LOW VOL
//		ids.add("50000000018244"); // SDA
//		ids.add("50000000018266"); // DMF
//		ids.add("50000000019143"); // DMF I EUR
//		ids.add("50000000018523"); // MSO S USD
//		ids.add("50000000018959"); // ECHO I GBP 
//
//		runner = new UserDefinedWorkingSetStatisticsRunner(EntityType.TOP_LEVEL_FUND, ids);
//		runner.compute();
	}
	
}
