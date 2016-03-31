package com.sammoin.commutewatcher;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

@SuppressLint("UseSparseArrays")

public class UserWeek
{
	private static ArrayList<UserDay> sOutboundWorkWeek ;
	private static Context mAppContext;

    public void setContext(Context appContext)
    {
        mAppContext=appContext;
    }




	private UserWeek(Context appContext)
	{
		mAppContext= appContext;
        ArrayList<UserDay> sOutboundWorkWeek = new ArrayList<UserDay>(7);

        for (int count = 0; count < 7; count++)
        {
            Log.i("WORKWEEK", "" + count);
            sOutboundWorkWeek.add(new UserDay());
        }
        sOutboundWorkWeek.get(0).setDayOfTheWeek(Day.SUNDAY);
        sOutboundWorkWeek.get(1).setDayOfTheWeek(Day.MONDAY);
        sOutboundWorkWeek.get(2).setDayOfTheWeek(Day.TUESDAY);
        sOutboundWorkWeek.get(3).setDayOfTheWeek(Day.WEDNESDAY);
        sOutboundWorkWeek.get(4).setDayOfTheWeek(Day.THURSDAY);
        sOutboundWorkWeek.get(5).setDayOfTheWeek(Day.FRIDAY);
        sOutboundWorkWeek.get(6).setDayOfTheWeek(Day.SATURDAY);


    }
	//PART OF NEW DESIGN PLAN

	public UserDay getDay(int day)
	{
		
		return sOutboundWorkWeek.get(day);
	}

    //get the day the item is associated with, use that to find the right index of the workweek arraylist to add the item. no need to worry about sorting since the alarm will handle that.
    public void addItem(UserDayItem item)
    {
        int index = item.getWorkDay().get();

     sOutboundWorkWeek.get(index).addItemToDay(item);


    }

    public void deleteItem(UserDayItem item)
    {

        int index = item.getWorkDay().get();
        sOutboundWorkWeek.get(index).removeItemFromDay(item);

    }

    public void clearDay(int dayToClear)
    {
        sOutboundWorkWeek.set(dayToClear, new UserDay());
    }

}
