package com.sammoin.commutewatcher;

import java.io.Serializable;

// TODO Need to re-do how we store days of the week to make it more friendly
// with the calendar. possibly use a map with enum relating to boolean
// values
public enum Day implements Serializable
{
	SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(4), THURSDAY(5), FRIDAY(6), SATURDAY(
			7);
	

	private final int dayNum;

	private Day(int dayNum)
	{
		this.dayNum = dayNum;

	}

	public int get()
	{
		return dayNum;
	}

//	public String toString()
//	{
//        String daystring;
//		switch (dayNum)
//				{
//                    case 0:
//                        daystring = "Sunday";
//                    case 1:
//                        daystring="Monday";
//                    case 2:
//                        daystring="Tuesday";
//                    case 3:
//                        daystring="Wednesday";
//                    case 4:
//                        daystring="Thursday";
//                    case 5:
//                        daystring="Friday";
//                    case 6:
//                        daystring="Saturday";
//                    default:
//                        daystring="No day selected";
//
//				}
//        return  daystring;
//	}

}