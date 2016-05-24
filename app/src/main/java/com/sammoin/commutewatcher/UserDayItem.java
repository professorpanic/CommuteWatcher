package com.sammoin.commutewatcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.joda.time.LocalTime;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class UserDayItem implements Serializable
{

	/**
	 * 
	 */
	private String startAddress = "";
	private String endAddress = "";
	private LocalTime startCommuteTime = LocalTime.now();
	private GregorianCalendar driveToHomeTime = new GregorianCalendar();

	private Day workDay = Day.SUNDAY;
	private boolean active;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
	

	public Day getWorkDay()
	
	{
		return this.workDay;
	}



    public UserDayItem(Context context)
	{
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        startAddress=prefs.getString(context.getString(R.string.pref_default_start_key), "");
		endAddress="";
        workDay = Day.SUNDAY;
		active=false;
        startCommuteTime = LocalTime.now();

	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public void setWorkDay(Day workDay)
	{
		this.workDay = workDay;
	}

    public void setWorkDay(int workDayInt)
    {

        switch (workDayInt) {
            case 1:  this.workDay = Day.SUNDAY;
                break;
            case 2:  this.workDay = Day.MONDAY;
                break;
            case 3:  this.workDay = Day.TUESDAY;
                break;
            case 4:  this.workDay = Day.WEDNESDAY;
                break;
            case 5:  this.workDay = Day.THURSDAY;
                break;
            case 6:  this.workDay = Day.FRIDAY;
                break;
            case 7:  this.workDay = Day.SATURDAY;
                break;

            default: this.workDay = Day.SUNDAY;
                break;
        }

    }




	@Override 
	public String toString()
	{
		return workDay.toString() + " from " + startAddress + " at " + startCommuteTime.toString("hh:mm aa") + "to " + endAddress;
	}



	

	public LocalTime getStartCommuteTime()
	{
		return startCommuteTime;
	}

	public void setStartCommuteTime(LocalTime in, int scheduledDay)
	{

		startCommuteTime= in;
		setWorkDay(scheduledDay);
	}

    public void setStartCommuteTime(long in)
    {

        startCommuteTime = startCommuteTime.millisOfDay().setCopy((int)in);
    }

	public GregorianCalendar getDriveToHomeTime()
	{
		return driveToHomeTime;
	}

	public void setDriveToHomeTime(GregorianCalendar in)
	{
		driveToHomeTime = in;
	}



	public String getHomeAddress()
	{
		return startAddress;
	}

	public void setHomeAddress(String in)
	{
		startAddress = in;
	}

	public String getWorkAddress()
	{
		return endAddress;
	}


	public void setWorkAddress(String in)
	{
		endAddress = in;
	}

	@SuppressWarnings("unchecked")
	public void copyUserData(UserDayItem inboundUserDayItem)
	{
		driveToHomeTime.setTime(inboundUserDayItem.getDriveToHomeTime().getTime());
		startCommuteTime=inboundUserDayItem.getStartCommuteTime();
		startAddress = inboundUserDayItem.getHomeAddress();
		endAddress = inboundUserDayItem.getWorkAddress();
		workDay = inboundUserDayItem.getWorkDay();
		active = inboundUserDayItem.isActive();
		
		
	}

}
