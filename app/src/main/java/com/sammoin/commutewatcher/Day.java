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

}