package com.sammoin.commutewatcher;

import java.util.Comparator;

public class WorkWeekComparator implements Comparator<UserDayItem>
{

	public WorkWeekComparator()
	{
		// TODO Auto-generated constructor stub
	}
	//to-do, create custom comparitor to always sort workweek. we'll need this so we can access the userdata for each day over in the service alarm.
	@Override
	public int compare(UserDayItem lhs, UserDayItem rhs)
	{
		if (lhs.getWorkDay().get() < rhs.getWorkDay().get())
		{
		return -1;
		}
		if (lhs.getWorkDay().get() > rhs.getWorkDay().get())
		{
			return 1;
		}
		
		
		return 0;
	}

}
