package com.notification.model;

public class Lift extends SkiFacility
{
	public Lift(String name, boolean isOpen)
	{
		super(name, isOpen);
	}

	@Override
	public String toString()
	{
		return "Lift: " + super.toString();
	}
}
