package com.sammoin.commutewatcher;

import java.io.Serializable;

//just felt like making my own enum for days to see how it was done.
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