package com.eim.utility.statistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.eim.util.format.DateFormatter;
import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.EntityType;
import com.eim.utility.common.utility.HibernateUtil;
import com.eim.utility.statistics.runners.AbstractStatisticsRunner;
import com.eim.utility.statistics.runners.SequoiaSecurityStatisticsRunner;
import com.eim.utility.statistics.runners.UserDefinedWorkingSetStatisticsRunner;

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
			rootLogger.addAppender( new FileAppender( new PatternLayout( "%d{dd MMM yy  HH:mm:ss} %-48C{1} %-5p:  %m%n" ),"/tmp/Statistics-[" + DateFormatter.toUsString(new Date())+ "].log" , false) );
		} catch (IOException e) {
			// Ignored
		}
	}
	
	public static void storeStatistics(Collection<?> list) {
		Session openSession = HibernateUtil.getSessionFactory().openSession();
		Transaction beginTransaction = openSession.beginTransaction();
		for (Object o : list) {
			openSession.save(o);
		}
		beginTransaction.commit();
		openSession.close();
	}
	
	
	public static void main(String argv[]) {
		ArrayList<String> ids = new ArrayList<String>();
		ids.add("203"); // MODEL EARTH
		ids.add("211"); // MODEL EARTH EUR
		ids.add("205"); // MODEL WATER
		ids.add("214"); // MODEL WATER EUR
		ids.add("206"); // MODEL WIND
		ids.add("210"); // MODEL WIND EUR
		ids.add("207"); // MODEL FIRE
		ids.add("209"); // MODEL FIRE EUR
		
		
		ids.add("1130");
		ids.add("1131");
		ids.add("1144");
		ids.add("1146");
		ids.add("1149");
		ids.add("1152");
		ids.add("1156");
		ids.add("1157");
		ids.add("1158");
		ids.add("1159");
		ids.add("1162");
		ids.add("1163");
		ids.add("1173");
		ids.add("1180");
		ids.add("1182");
		ids.add("1186");
		ids.add("1200");
		ids.add("1203");
		ids.add("1206");
		ids.add("1207");
		ids.add("1211");
		ids.add("1213");
		ids.add("1214");
		ids.add("1216");
		ids.add("1229");
		ids.add("1230");
		ids.add("1235");
		ids.add("1236");
		ids.add("1238");
		ids.add("1242");
		ids.add("1244");
		ids.add("1246");
		ids.add("1249");
		ids.add("1252");
		ids.add("1253");
		ids.add("1256");
		ids.add("1260");
		ids.add("1345");
		ids.add("1346");
		ids.add("1347");
		ids.add("1350");
		ids.add("1352");
		ids.add("1354");
		ids.add("1360");
		ids.add("1362");
		ids.add("1365");
		ids.add("1366");
		ids.add("1367");
		ids.add("1563");
		ids.add("1579");
		ids.add("1583");
		ids.add("1584");
		ids.add("1618");
		ids.add("1625");
		ids.add("1633");
		ids.add("1643");
		ids.add("1646");
		ids.add("1652");
		ids.add("1653");
		ids.add("1655");
		ids.add("795");
		ids.add("857");
		ids.add("1103");
		ids.add("1106");
		ids.add("1664");
		ids.add("1665");

		AbstractStatisticsRunner runner = null;
		runner = new UserDefinedWorkingSetStatisticsRunner(EntityType.PORTFOLIO,ComputationFrequency.MONTHLY, ids);
		//runner = new SequoiaSecurityStatisticsRunner(ComputationFrequency.MONTHLY);
		runner.compute();		
	}
}
