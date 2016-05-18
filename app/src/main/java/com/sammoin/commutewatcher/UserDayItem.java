package com.sammoin.commutewatcher;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class UserDayItem implements Serializable
{

	/**
	 * 
	 */
	private String startAddress = "";
	private String endAddress = "";
	private GregorianCalendar startCommuteTime = new GregorianCalendar();
	private GregorianCalendar driveToHomeTime = new GregorianCalendar();
	private Day workDay = Day.SUNDAY;
	private boolean active;
	
	//NEW DESIGN PLAN - STATIC ARRAY OF DAYS, CONTAINS COMMUTE IN EACH DAY. SHOULD BE EASIER TO USE WITH ALARMS AND SAVE.
	public Day getWorkDay()
	
	{
		return this.workDay;
	}

	public UserDayItem()
	{
		startAddress="Start Address";
		endAddress="End Address";
        workDay = Day.SUNDAY;
		active=false;
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
		
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa ZZZZ",
				Locale.getDefault());
		
		String toWorkTimeString = sdf.format(startCommuteTime.getTime());
		String toHomeTimeString = sdf.format(driveToHomeTime.getTime());
		
		return workDay.toString() + " from " + startAddress + " at " + toWorkTimeString + "to " + endAddress +  " at " + toHomeTimeString;
	}



	

	public GregorianCalendar getStartCommuteTime()
	{
		return startCommuteTime;
	}

	public void setStartCommuteTime(GregorianCalendar in, int scheduledDay)
	{

		startCommuteTime = in;
		startCommuteTime.set(GregorianCalendar.DAY_OF_WEEK, scheduledDay);
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
		startCommuteTime.setTime(inboundUserDayItem.getStartCommuteTime().getTime());
		startAddress = inboundUserDayItem.getHomeAddress();
		endAddress = inboundUserDayItem.getWorkAddress();
		workDay = inboundUserDayItem.getWorkDay();
		active = inboundUserDayItem.isActive();
		
		
	}

}
