package com.notification.model;

public class SkiFacility
{
	private String _name;
	private boolean _isOpen;

	SkiFacility(String name, boolean isOpen)
	{
		_name = name;
		_isOpen = isOpen;
	}

	@Override
	public String toString()
	{
		return _name + ", Status: " + (_isOpen ? "Open" : "Closed");
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public boolean isOpen()
	{
		return _isOpen;
	}

	public void setIsOpen(boolean isOpen)
	{
		_isOpen = isOpen;
	}
}
