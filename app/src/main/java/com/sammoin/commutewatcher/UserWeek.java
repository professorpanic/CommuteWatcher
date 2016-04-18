package com.sammoin.commutewatcher;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressLint("UseSparseArrays")

public class UserWeek implements Serializable {
    private ArrayList<UserDay> sWorkWeek;
    private Context mAppContext;

    public  ArrayList<UserDay> getWorkWeek() {
        return sWorkWeek;
    }

    public  void setWorkWeek(ArrayList<UserDay> sWorkWeek) {
        this.sWorkWeek = sWorkWeek;
    }


    public void setContext(Context appContext)
    {
        mAppContext=appContext;
    }

    public void set(UserDay inboundDay)
    {
        int dayToReplace = inboundDay.getDayOfTheWeek().get()-1; //this feels silly to do, but it should work.

        sWorkWeek.get(dayToReplace).copy(inboundDay);
    }




	public UserWeek()
	{
		sWorkWeek = new ArrayList<UserDay>(7);

        for (int count = 0; count < 7; count++)
        {
            Log.i("WORKWEEK", "" + count);
            sWorkWeek.add(new UserDay());
        }
        sWorkWeek.get(0).setDayOfTheWeek(Day.SUNDAY);
        sWorkWeek.get(1).setDayOfTheWeek(Day.MONDAY);
        sWorkWeek.get(2).setDayOfTheWeek(Day.TUESDAY);
        sWorkWeek.get(3).setDayOfTheWeek(Day.WEDNESDAY);
        sWorkWeek.get(4).setDayOfTheWeek(Day.THURSDAY);
        sWorkWeek.get(5).setDayOfTheWeek(Day.FRIDAY);
        sWorkWeek.get(6).setDayOfTheWeek(Day.SATURDAY);


    }
	//PART OF NEW DESIGN PLAN

	public UserDay getDay(int day)
	{
		
		return sWorkWeek.get(day);
	}

    //get the day the item is associated with, use that to find the right index of the workweek arraylist to add the item. no need to worry about sorting since the alarm will handle that.
    public void addItem(UserDayItem item)
    {
        int index = item.getWorkDay().get();

     sWorkWeek.get(index).addItemToDay(item);


    }

    public void deleteItem(UserDayItem item)
    {

        int index = item.getWorkDay().get();
        sWorkWeek.get(index).removeItemFromDay(item);

    }

    public void copy(UserWeek weekToCopy)
    {
        sWorkWeek= (ArrayList<UserDay>)weekToCopy.getWorkWeek().clone();
    }

    public void clearAll()
    {
        for (UserDay ud : sWorkWeek)
        {
            ud.clear();
        }
    }

    public void clearDay(int dayToClear)
    {
        sWorkWeek.set(dayToClear, new UserDay());
    }

}
