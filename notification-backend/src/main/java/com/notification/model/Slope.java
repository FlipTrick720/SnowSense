package com.notification.model;

public class Slope extends SkiFacility
{
	public Slope(String name, boolean isOpen)
	{
		super(name, isOpen);
	}

	@Override
	public String toString()
	{
		return "Slope: " + super.toString();
	}
}
