package miouge;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public class EpochTool {

	public enum Format {

		STD_YEAR_ONLY("yyyy"),
		STD_SLASH_FULL("yyyy/MM/dd HH:mm:ss"),
		STD_SLASH_DAY("yyyy/MM/dd"),		
		STD_SLASH_MONTH("yyyy/MM"),
		
		STD_SLASH_FULL_FR("dd/MM/yyyy HH:mm:ss"),				
		STD_SLASH_MINUTE_FR("dd/MM/yyyy HH:mm"),
		STD_SLASH_DAY_FR("dd/MM/yyyy"),
		
		STD_DASH_FULL("yyyy-MM-dd HH:mm:ss"),
		STD_DASH_MINUTE("yyyy-MM-dd HH:mm"),
		
		CALENDAR("yyyy-MM-dd E(e) HH:mm:ss"),		
		ISO8601("yyyy-MM-dd'T'HH:mm:ss'Z'");

		private String name = "";

		Format( String name ) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
	}

	public enum Direction {

		forward, backward
	}

	public enum Span {
		Seconds, Minutes, Hours, Days;
	}
	
	//------------------------ UTC calendar -----------------------------------------
	
	private final static Calendar utcCalendar = Calendar.getInstance();
	
	// static initialization
	static {

		utcCalendar.setTimeZone( new SimpleTimeZone(0, "UTC") );
	}

	public static Calendar getUtcCalendar() {
		
		return utcCalendar;
	}	
	
	//--------------------------------------------------------------------------
	
	public static Long getNowEpoch() {
				
		Instant instant = Instant.now();
		return instant.getEpochSecond();
	}	
		
	//-------------------------------- String -> Epoch -------------------------

	public static Long convertToEpoch( String date, Format format, ZoneId zoneId ) {

		if( date == null ) {
			return null;
		}
		
		ZoneId zoneIdUsed = null;
		if( zoneId == null ) {
			zoneIdUsed = ZoneId.of( "UTC" );
		}
		else {
			zoneIdUsed = zoneId;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern( format.toString() );
		LocalDateTime ldt = LocalDateTime.parse( date, formatter );
		ZonedDateTime zdt = ZonedDateTime.of( ldt, zoneIdUsed );
		return zdt.toEpochSecond();
	}


	public static Long convertToEpoch( String date, String customFormat, ZoneId zoneId ) {

		if( date == null ) {
			return null;
		}
		
		ZoneId zoneIdUsed = null;
		if( zoneId == null ) {
			zoneIdUsed = ZoneId.of( "UTC" );
		}
		else {
			zoneIdUsed = zoneId;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern( customFormat );
		LocalDateTime ldt = LocalDateTime.parse( date, formatter );
		ZonedDateTime zdt = ZonedDateTime.of( ldt, zoneIdUsed );
		return zdt.toEpochSecond();
	}

	public static Long convertIsoInstantToEpoch( String isoInstant ) {

		if( isoInstant == null ) {
			return null;
		}

		Instant instant = Instant.parse( isoInstant );
		return instant.getEpochSecond();
	}

	// -------------------------------- Epoch -> String -------------------------

	public static String convertToString( Long epoch, Format format ) throws IllegalArgumentException {

		return convertToString( epoch, ZoneId.of( "UTC" ), format );
	}

	public static String convertToString( Long epoch, DateTimeFormatter format ) throws IllegalArgumentException {

		return convertToString( epoch, ZoneId.of( "UTC" ), format );
	}

	public static String convertToString( Long epoch, String customFormat ) throws IllegalArgumentException {

		return convertToString( epoch, ZoneId.of( "UTC" ), customFormat );
	}

	public static String convertToString( Long epoch, ZoneId zoneId, DateTimeFormatter format ) throws IllegalArgumentException {

		if( epoch == null ) {
			return null;
		}
		
		ZoneId zoneIdUsed = null;
		if( zoneId == null ) {
			zoneIdUsed = ZoneId.of( "UTC" );
		}
		else {
			zoneIdUsed = zoneId;
		}

		ZonedDateTime zdt = epochToZonedDate( epoch, zoneIdUsed );
		return zdt.format( format );
	}

	public static String convertToString( Long epoch, ZoneId zoneId, Format format ) throws IllegalArgumentException {

		if( epoch == null ) {
			return null;
		}
		
		ZoneId zoneIdUsed = null;
		if( zoneId == null ) {
			zoneIdUsed = ZoneId.of( "UTC" );
		}
		else {
			zoneIdUsed = zoneId;
		}

		ZonedDateTime zdt = epochToZonedDate( epoch, zoneIdUsed );
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern( format.toString() );
		return zdt.format( formatter );
	}

	public static String convertToString( Long epoch, ZoneId zoneId, String customFormat ) throws IllegalArgumentException {

		if( epoch == null ) {
			return null;
		}
		
		ZoneId zoneIdUsed = null;
		if( zoneId == null ) {
			zoneIdUsed = ZoneId.of( "UTC" );
		}
		else {
			zoneIdUsed = zoneId;
		}

		ZonedDateTime zdt = epochToZonedDate( epoch, zoneIdUsed );
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern( customFormat );
		return zdt.format( formatter );
	}

	public static String convertToPredefinedFormat( Long epoch, ZoneId zoneId, DateTimeFormatter formatter ) throws IllegalArgumentException {

		if( epoch == null ) {
			return null;
		}

		ZoneId zoneIdUsed = null;
		if( zoneId == null ) {
			zoneIdUsed = ZoneId.of( "UTC" );
		}
		else {
			zoneIdUsed = zoneId;
		}

		ZonedDateTime zdt = epochToZonedDate( epoch, zoneIdUsed );
		return zdt.format( formatter );
	}
	
	public static Integer getTemporalUnit( Long epoch, String internationalStandard ) {
		
		if( epoch == null ) {
			return null;
		}
		
		Instant i = Instant.ofEpochSecond( epoch );
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = ZonedDateTime.ofInstant( i, zoneId );
	
		int year = zdt.getYear();
		int month = zdt.getMonthValue();
		int dayOfMonth = zdt.getDayOfMonth();
		int hour = zdt.getHour();
		int minute = zdt.getMinute();
		
		switch( internationalStandard ) {
		case "year":
			return year;
		case "month":
			return month;
		case "dayOfMonth":
			return dayOfMonth;
		case "hour":
			return hour;
		case "minute":
			return minute;
		}
		return year;
	}

	public static Long adjust( Long epoch, Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second, ZoneId zoneId, Boolean verbose ) throws Exception {

		if( epoch == null ) {
			return null;
		}

		ZoneId zoneIdUsed = null;
		if( zoneId == null ) {
			zoneIdUsed = ZoneId.of( "UTC" );
		}
		else {
			zoneIdUsed = zoneId;
		}

		if( verbose == true ) {

			System.out.print(String.format( "adjust (%d) %s [%s] ->", epoch, convertToString( epoch, zoneIdUsed, Format.STD_SLASH_FULL ), zoneIdUsed.getId() ));
		}

		Instant i = Instant.ofEpochSecond( epoch );
		ZonedDateTime zdt = ZonedDateTime.ofInstant( i, zoneIdUsed );

		int yearDigit = zdt.getYear();
		int monthDigit = zdt.getMonthValue();
		int dayOfMonthDigit = zdt.getDayOfMonth();
		int hourDigit = zdt.getHour();
		int minuteDigit = zdt.getMinute();
		int secondDigit = zdt.getSecond();

		if( year != null ) {
			yearDigit = year;
		}
		if( month != null ) {
			monthDigit = month;
		}
		if( day != null ) {
			dayOfMonthDigit = day;
		}
		if( hour != null ) {
			hourDigit = hour;
		}
		if( minute != null ) {
			minuteDigit = minute;
		}
		if( second != null ) {
			secondDigit = second;
		}

		LocalDateTime lt = LocalDateTime.of( yearDigit, monthDigit, dayOfMonthDigit, hourDigit, minuteDigit, secondDigit );

		zdt = ZonedDateTime.of( lt, zoneIdUsed );

		Long output = zdt.toEpochSecond();

		if( verbose == true ) {

			System.out.print(String.format( " (%d) %s [%s]", epoch, convertToString( output, zoneIdUsed, Format.STD_SLASH_FULL ), zoneIdUsed.getId() ));

		}
		return output;
	}

	public static Long adjustWith( Long epoch, TemporalAdjuster adjuster, ZoneId zoneId, Boolean verbose ) {

		if( epoch == null ) {
			return null;
		}

		ZoneId zoneIdUsed = null;
		if( zoneId == null ) {
			zoneIdUsed = ZoneId.of( "UTC" );
		}
		else {
			zoneIdUsed = zoneId;
		}

		if( verbose == true ) {
			System.out.print(String.format( "adjust (%d) %s [%s] ->", epoch, convertToString( epoch, zoneId, Format.STD_SLASH_FULL ), zoneId.getId() ));
		}

		Instant i = Instant.ofEpochSecond( epoch );
		ZonedDateTime zdt = ZonedDateTime.ofInstant( i, zoneIdUsed );

		//------------ Active Part --------------------

		zdt = zdt.with( adjuster );

		//---------------------------------------------

		Long output = zdt.toEpochSecond();

		if( verbose == true ) {
			System.out.print(String.format( " (%d) %s [%s]", epoch, convertToString( output, zoneId, Format.STD_SLASH_FULL ), zoneId.getId() ));
		}
		return output;
	}

	// -------------------------------- Shifting --------------------------------

	public static ZonedDateTime epochToZonedDate( Long epoch, ZoneId zoneId ) {

		if( epoch == null ) {
			return null;
		}

		ZoneId zoneIdUsed = null;

		if( zoneId == null ) {
			zoneIdUsed = ZoneId.of( "UTC" );
		}
		else {
			zoneIdUsed = zoneId;
		}

		Instant i = Instant.ofEpochSecond( epoch );
		ZonedDateTime zdt = ZonedDateTime.ofInstant( i, zoneIdUsed );
		return zdt;
	}

	public static Long add( Long epoch, Integer years, Integer months, Integer days, Integer hours, Integer minutes, Integer seconds, ZoneId zoneId, Boolean verbose ) throws Exception {

		if( epoch == null ) {
			return null;
		}

		ZoneId zoneIdUsed = null;
		if( zoneId == null ) {
			zoneIdUsed = ZoneId.of( "UTC" );
		}
		else {
			zoneIdUsed = zoneId;
		}

		if( verbose == true ) {

			System.out.print(String.format( "add input  (%d) %s [%s]", epoch, convertToString( epoch, zoneId, Format.STD_SLASH_FULL ), zoneId.getId() ));
		}

		Instant i = Instant.ofEpochSecond( epoch );
		ZonedDateTime zdt = ZonedDateTime.ofInstant( i, zoneIdUsed );

		if( years != null ) {
			zdt = zdt.plusYears( years );
		} ;
		if( months != null ) {
			zdt = zdt.plusMonths( months );
		}
		if( days != null ) {
			zdt = zdt.plusDays( days );
		}
		if( hours != null ) {
			zdt = zdt.plusHours( hours );
		}
		if( minutes != null ) {
			zdt = zdt.plusMinutes( minutes );
		}
		if( seconds != null ) {
			zdt = zdt.plusSeconds( seconds );
		}

		Long output = zdt.toEpochSecond();

		if( verbose == true ) {

			System.out.print(String.format( "add output (%d) %s [%s]", epoch, convertToString( output, zoneId, Format.STD_SLASH_FULL ), zoneId.getId() ));

		}
		return output;
	}

	public static Long shiftFromPeriod( Long dataEpoch, Direction direction, Long period, char periodUnit, ZoneId periodZoneId, Boolean verbose ) throws Exception {

		if( dataEpoch == null ) {
			return null;
		}

		Long output = null;

		ZoneId zoneIdUsed = null;
		if( periodZoneId == null ) {
			zoneIdUsed = ZoneId.of( "UTC" );
		}
		else {
			zoneIdUsed = periodZoneId;
		}

		if( verbose == true ) {

			System.out.print(String.format( "shiftByPeriod (%d%c) input  (%d) %s [%s]", period, periodUnit, dataEpoch, convertToString( dataEpoch, zoneIdUsed, Format.STD_SLASH_FULL ), zoneIdUsed.getId() ));
		}

		switch( periodUnit )
		{
			case 's': {

				if( direction == Direction.forward ) {
					output = dataEpoch + period;
				}
				else {
					output = dataEpoch - period;
				}
				break;
			}

			case 'H': {

				Instant i = Instant.ofEpochSecond( dataEpoch );
				ZonedDateTime zdt = ZonedDateTime.ofInstant( i, zoneIdUsed );

				if( direction == Direction.forward ) {
					zdt = zdt.plusHours( period );
				}
				else {
					zdt = zdt.minusHours( period );
				}
				output = zdt.toEpochSecond();
				break;
			}

			case 'D': {

				Instant i = Instant.ofEpochSecond( dataEpoch );
				ZonedDateTime zdt = ZonedDateTime.ofInstant( i, zoneIdUsed );

				if( direction == Direction.forward ) {
					zdt = zdt.plusDays( period );
				}
				else {
					zdt = zdt.minusDays( period );
				}
				output = zdt.toEpochSecond();
				break;
			}
			
			case 'M': {

				Instant i = Instant.ofEpochSecond( dataEpoch );
				ZonedDateTime zdt = ZonedDateTime.ofInstant( i, zoneIdUsed );

				if( direction == Direction.forward ) {
					zdt = zdt.plusMonths( period );
				}
				else {
					zdt = zdt.minusMonths( period );
				}
				output = zdt.toEpochSecond();
				break;
			}
			
			case 'A': 
			case 'T': {

				Instant i = Instant.ofEpochSecond( dataEpoch );
				ZonedDateTime zdt = ZonedDateTime.ofInstant( i, zoneIdUsed );

				if( direction == Direction.forward ) {
					zdt = zdt.plusYears( period );
				}
				else {
					zdt = zdt.minusYears( period );
				}
				output = zdt.toEpochSecond();
				break;
			}
			
			default : {
				throw new Exception( "unexpected periodUnit value" );
			}
		}

		if( verbose == true ) {

			System.out.print(String.format( "shiftByPeriod (%d%c) output (%d) %s [%s]", period, periodUnit, output, convertToString( output, zoneIdUsed, Format.STD_SLASH_FULL ), zoneIdUsed.getId() ));
		}

		return output;
	}

}
