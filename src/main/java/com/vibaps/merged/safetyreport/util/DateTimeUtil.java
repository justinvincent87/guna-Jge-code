package com.vibaps.merged.safetyreport.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import com.vibaps.merged.safetyreport.exception.GeoTabException;

public final class DateTimeUtil {

	private static final String				JAVA			= "java";
	private static final SimpleDateFormat	DF_YYYY_MM_DD	= new SimpleDateFormat("yyyy-MM-dd");

	private DateTimeUtil() throws IllegalAccessException {
		throw new IllegalAccessException("Illegal access to DateTimeUtil");
	}

	/**
	 * Convert String date as {@code LocalDateTime}
	 * 
	 * @param date
	 * @return
	 */
	public static LocalDateTime convertToLocalDateTime(String date) {

		if (Objects.isNull(date)) {
			return null;
		}

		if (date.contains(JAVA)) {
			long dateInMilliSec = Long.parseLong(date.substring(33, date.indexOf(',')));
			return LocalDateTime.ofInstant(Instant.ofEpochSecond(dateInMilliSec), TimeZone.getDefault().toZoneId());
		}
		return LocalDateTime.parse(date.substring(0, date.indexOf('.')));
	}

	/**
	 * Get previous week from the input date
	 * 
	 * @param date
	 * @return
	 */
	public LocalDateTime[] getPreviousWeek(LocalDateTime date) {

		if (Objects.isNull(date)) {
			return null;
		}

		final int			dayOfWeek	= date.getDayOfWeek().getValue();
		final LocalDateTime	from		= date.minusDays(dayOfWeek + 6);
		final LocalDateTime	to			= date.minusDays(dayOfWeek);

		return new LocalDateTime[] { from, to };
	}

	/**
	 * Get previous month from input date
	 * 
	 * @param date
	 * @return
	 */
	public LocalDateTime[] getPreviousMonth(LocalDateTime date) {

		if (Objects.isNull(date)) {
			return null;
		}

		final LocalDateTime	from	= date.minusDays(date.getDayOfMonth() - 1).minusMonths(1);
		final LocalDateTime	to		= from.plusMonths(1).minusDays(1);
		return new LocalDateTime[] { from, to };
	}

	/**
	 * Parse string "YYYY-MM-DD" format as {@code java.util.Date}
	 * 
	 * @param date
	 * @return
	 */
	public static Date parseUtilDate(String date) {

		if (Objects.isNull(date)) {
			return null;
		}
		
		try {
			return DF_YYYY_MM_DD.parse(date);
		} catch (ParseException e) {
			throw new GeoTabException("Error while parse string to date", e);
		}
	}
}
