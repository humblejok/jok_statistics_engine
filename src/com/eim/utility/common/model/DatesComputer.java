package com.eim.utility.common.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.eim.util.date.DateUtil;

/**
 * Utility class that provides methods handling dates computations.
 * 
 * @author sdj
 *
 */
public class DatesComputer {
	
	/**
	 * Indicates whether the given <code>date</code> is valid according to the given <code>frequency</code>.
	 * @param date The date
	 * @param frequency The frequency
	 * @return <code>true</code> if the date matchess
	 */
	public static boolean isValidDate(Date date, ComputationFrequency frequency) {
		switch (frequency) {
		case DAILY:
			return true;
		case WEEKLY:
			return DateUtil.getEndOfWeek(date).equals(date);
		case MONTHLY:
			return DateUtil.isEOM(date);
		case QUARTERLY:
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar.get(Calendar.MONTH)%3==2 && DateUtil.isEOM(date);
		case YEARLY:
			return DateUtil.getEndOfYear(date).equals(date);
		default:
			break;
		}
		
		return true;
	}
	
	/**
	 * Computes the next valid date using the given <code>date</code> and the given <code>frequency</code>.
	 * @param date The date
	 * @param frequency The frequency
	 * @return The computed date
	 */
	public static Date getNextValidDate(Date date, ComputationFrequency frequency) {
		Date newDate = date;
		switch (frequency) {
		case DAILY:
			newDate = DateUtil.isFridayDate(newDate)?DateUtil.addOrRemoveDays(newDate, 3):DateUtil.addOneDay(newDate);
			break;
		case WEEKLY:
			if (DatesComputer.isValidDate(newDate, frequency)) {
				newDate = DateUtil.addOrRemoveDays(newDate, 7);
			} else {
				newDate = DateUtil.getEndOfWeek(newDate);
			}
			break;
		case MONTHLY:
			if (DatesComputer.isValidDate(newDate, frequency)) {
				newDate = DateUtil.getEndOfNextMonth(newDate);
			} else {
				newDate = DateUtil.getEndOfMonth(newDate);
			}
			break;
		case QUARTERLY:
			while (!isValidDate(newDate, frequency)) {
				if (newDate.before(DateUtil.getEndOfMonth(newDate))) {
					newDate = DateUtil.getEndOfMonth(newDate);
				} else {
					newDate = DateUtil.getEndOfPreviousMonth(newDate);
				}
			}
			newDate = DateUtil.addOrRemoveMonths(newDate, 3);
			break;
		case YEARLY:
			if (DatesComputer.isValidDate(newDate, frequency)) {
				newDate = DateUtil.addOneYear(newDate);
			} else {
				newDate = DateUtil.getEndOfYear(newDate);
				newDate = DateUtil.addOneYear(newDate);
			}
			break;
		default:
			break;
		}
		
		return DateUtil.roundToDay(newDate);
	}
	
	/**
	 * Computes the next valid date using the given <code>date</code> and the given <code>frequency</code>.
	 * @param date The date
	 * @param frequency The frequency
	 * @return The computed date
	 */
	public static Date getPreviousValidDate(Date date, ComputationFrequency frequency) {
		Date newDate = date;
		switch (frequency) {
		case DAILY:
			newDate = DateUtil.isMondayDate(newDate)?DateUtil.addOrRemoveDays(newDate, -3):DateUtil.removeOneDay(newDate);
			break;
		case WEEKLY:
			newDate = DateUtil.addOrRemoveDays(newDate, -7);
			break;
		case MONTHLY:
			newDate = DateUtil.getEndOfPreviousMonth(newDate);
			break;
		case QUARTERLY:
			newDate = DateUtil.addOrRemoveMonths(newDate, -3);
			break;
		case YEARLY:
			newDate = DateUtil.addOrRemoveMonths(newDate, -12);
			break;
		default:
			break;
		}
		
		return DateUtil.roundToDay(newDate);
	}
	
	/**
	 * Computes all the dates between a start and end date using the given frequency.
	 * @param from The start date
	 * @param to The end date
	 * @param frequency The frequency
	 * @return The collection of <code>Date</code>
	 */
	public static Collection<Date> getAllDatesWithinRange(Date from, Date to, ComputationFrequency frequency) {
		ArrayList<Date> dates = new ArrayList<Date>();
		if (from!=null && to!=null) {
			Date workingDate = from;
			while (workingDate.before(to) || workingDate.equals(to)) {
				dates.add(workingDate);
				workingDate = DatesComputer.getNextValidDate(workingDate, frequency);
			}
		}
		return dates;
	}
	
	/**
	 * Returns the already elapsed ratio of a date regarding its frequency.
	 * @param frequency The frequency 
	 * @param date The date
	 * @return The elapsed ratio
	 */
	public static Double getFrequencyPeriodRatio(ComputationFrequency frequency, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = 0;

		switch (frequency) {
		case DAILY:
			return Double.NaN;
		case WEEKLY:
			day = calendar.get(Calendar.DAY_OF_WEEK);
			return new Integer(Calendar.FRIDAY - day + 1).doubleValue()/5;
		case MONTHLY:
			day = calendar.get(Calendar.DAY_OF_MONTH);
			return new Integer(calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) - day + 1).doubleValue()/new Integer(calendar.getActualMaximum( Calendar.DAY_OF_MONTH )).doubleValue();
		case QUARTERLY:
			return getDaysOfQuarter(date).doubleValue()/getDaysNumberInQuarter(date);
		case YEARLY:
			day = calendar.get(Calendar.DAY_OF_YEAR);
			return new Integer(calendar.getActualMaximum( Calendar.DAY_OF_YEAR ) - day + 1).doubleValue()/new Integer(calendar.getActualMaximum( Calendar.DAY_OF_YEAR )).doubleValue();
		default:
			break;
		}
		
		return Double.NaN;
	}
	
	/**
	 * Returns the total number of days in a quarter (represented as a date within a quarter).
	 * @param dateInQuarter The date
	 * @return The number of days
	 */
	public static Integer getDaysNumberInQuarter(Date dateInQuarter) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateInQuarter);
		int month = calendar.get(Calendar.MONTH);
		int quarter = month / 3;
		int daysNumber = 0;
		for (int i = 0;i<3;i++) {
			calendar.set(Calendar.MONTH, i * quarter + i);
			daysNumber += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		return daysNumber; 
	}
	
	/**
	 * Returns the number of days elapsed in the quarter up to the given date
	 * @param dateInQuarter The date
	 * @return The elpased days
	 */
	public static Integer getDaysOfQuarter(Date dateInQuarter) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateInQuarter);
		int month = calendar.get(Calendar.MONTH);
		int monthInQuarter = month % 3;
		int quarter = month / 3;
		int daysNumber = 0;
		for (int i = 0;i<monthInQuarter;i++) {
			calendar.set(Calendar.MONTH, i * quarter + i);
			daysNumber += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		daysNumber += calendar.get(Calendar.DAY_OF_MONTH);
		return daysNumber; 
	}
}
