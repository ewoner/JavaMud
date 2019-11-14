package Mud;

import java.util.GregorianCalendar;

/**
 * The meat of the game.  This is the main game module.  All Entities exist
 * inside of this module.  In a nut shell, this is the game.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.1
 * 1.  The month count starts off at "0".  This was not taken into account.  Fixed
 * Version 1.0.0
 */
public class MyTimer {

    // this is the system time at which the timer was initialized
    private long inittime;

    // this is the official "starting time" of the timer.
    private long starttime;

    static public long GetTimeMS() {
        return System.currentTimeMillis();
    }

    static public long GetTimeS() {
        return GetTimeMS() / 1000;
    }

    static public long GetTimeM() {
        return GetTimeMS() / 60000;
    }

    static public long GetTimeH() {
        return GetTimeMS() / 3600000;
    }
    // This prints a timestamp in 24 hours hh:mm:ss format

    static public String TimeStamp() {
        GregorianCalendar cal = new GregorianCalendar();
        return "" + cal.get(GregorianCalendar.HOUR_OF_DAY) + ":" + cal.get(GregorianCalendar.MINUTE) + ":" + cal.get(GregorianCalendar.SECOND);
    }
    // This prints a datestamp in YYYY:MM:DD format

    static public String DateStamp() {
        GregorianCalendar cal = new GregorianCalendar();
        return "" + cal.get(GregorianCalendar.YEAR) + ":" + (cal.get(GregorianCalendar.MONTH)+1) + ":" + cal.get(GregorianCalendar.DAY_OF_MONTH);
    }

    public MyTimer() {
        starttime = 0;
        inittime = 0;
    }

    /**
     * Resets the timmer to 0;
     */
    public void reset() {
        reset(0);
    }

    /**
     * Resets the timmer based on time lasped.
     *
     * @param timepassed The time passed in miliseconds
     */
    public void reset(long timepassed) {
        starttime = timepassed;
        inittime = GetTimeMS();
    }

    /**
     * Returns the amount of time that has elapsed since the timer was
     * initialized, plus whatever starting time the timer was given.
     *
     * @return time elaspsed by timer (in miliseconds).
     */
    public long getMS() {
        // return the amount of time that has elapsed since the timer
        // was initialized, plus whatever starting time the timer was given.
        return (GetTimeMS() - inittime) + starttime;
    }

    /**
     * Returns the amount of time that has elapsed since the timer was 
     * initialized, plus whatever starting time the timer was given.
     * 
     * @return Time elasped in seconds.
     */
    public long getSeconds() {
        return getMS() / 1000;
    }

    /**
     * Returns the amount of time that has elapsed since the timer was 
     * initialized, plus whatever starting time the timer was given.
     * 
     * @return time elasped in minutes.
     */
    public long getMinutes() {
        return getMS() / 60000;
    }

    /**
     * Returns the amount of time that has elapsed since the timer was 
     * initialized, plus whatever starting time the timer was given.
     * 
     * @return time elasped in hours.
     */
    public long getHours() {
        return getMS() / 3600000;
    }

    /**
     * Returns the amount of time that has elapsed since the timer was 
     * initialized, plus whatever starting time the timer was given.
     * 
     * @return time elasped in days.
     */
    public long getDays() {
        return getMS() / 86400000;
    }

    /**
     * Returns the amount of time that has elapsed since the timer was 
     * initialized, plus whatever starting time the timer was given.
     * 
     * @return time elasped in years
     */
    public long getYears() {
        return getDays() / 365;
    }

    /**
     * Returns the amount of time that has elapsed since the timer was
     * initialized, plus whatever starting time the timer was given.
     *
     * @return A string representive of the time elasped.
     */
    public String getString() {
        String str = null;
        long y = getYears();
        long d = getDays() % 365;
        long h = getHours() % 24;
        long m = getMinutes() % 60;

        if (y > 0) {
            str += y + " years, ";
        }
        if (d > 0) {
            str += d + " days, ";
        }
        if (h > 0) {
            str += h + " hours, ";
        }

        str += m + " minutes";

        return str;
    }
}
