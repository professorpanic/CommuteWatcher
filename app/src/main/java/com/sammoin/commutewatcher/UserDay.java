package com.sammoin.commutewatcher;

import java.io.Serializable;
import java.util.ArrayList;

public class UserDay implements Serializable
{



	/**
	 * 
	 */
	private ArrayList<UserDayItem> dayItemArrayList;
	private Day dayOfTheWeek;
    private boolean active;

    public UserDay()
    {
        dayItemArrayList = new ArrayList<UserDayItem>();
        dayOfTheWeek=Day.SUNDAY;
        active=false;
    }

    public boolean isActive()
    {
        return  active;
    }

    public void setActive(boolean in)
    {
        active=in;
    }


	public Day getDayOfTheWeek() {
		return dayOfTheWeek;
	}

	public void setDayOfTheWeek(Day dayOfTheWeek) {
		this.dayOfTheWeek = dayOfTheWeek;
	}

	public ArrayList<UserDayItem> getDayItemArrayList() {
		return dayItemArrayList;
	}

	public void setDayItemArrayList(ArrayList<UserDayItem> dayItemArrayList) {
		this.dayItemArrayList = dayItemArrayList;
	}

	public void copy(UserDay inDay)
	{
		setDayOfTheWeek(inDay.getDayOfTheWeek());
		if (this.dayItemArrayList != null)
		{
			dayItemArrayList.clear();
		}
		dayItemArrayList.addAll(inDay.getDayItemArrayList());
        active=inDay.isActive();
	}
	
	
	public boolean addItemToDay(UserDayItem item)
	{
		if (item.getWorkDay().equals(dayOfTheWeek))
		{

			//item.getStartCommuteTime().set
			dayItemArrayList.add(item);
			return true;
		}
		else return false;
	}

	public void clear()
	{
        if (this.dayItemArrayList != null)
        {
            dayItemArrayList.clear();
        }
	}

	public void removeItemFromDay(int index)
	{
		dayItemArrayList.remove(index);
	}

	public void removeItemFromDay(UserDayItem userDayItem)
	{
		for (int counter=0; counter < getDayItemArrayList().size(); counter++)
        {
            if (getDayItemArrayList().get(counter) == userDayItem)
            {
                getDayItemArrayList().remove(counter);
            }
        }
	}



}
