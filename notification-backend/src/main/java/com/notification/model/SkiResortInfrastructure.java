package com.notification.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SkiResortInfrastructure
{
	private final String _skiResortName;
	private final ArrayList<Lift> _lifts = new ArrayList<>() ;
	private final ArrayList<Slope> _slopes = new ArrayList<>() ;

	public SkiResortInfrastructure(String skiResortName)
	{
		_skiResortName = skiResortName;
	}

	@Override
	public String toString()
	{
		return "Ski Resort Name: " + _skiResortName + "\nLifts: " + _lifts.toString() + "\nSlopes: " + _slopes.toString();
	}

	public void addLift(Lift lift)
	{
		_lifts.add(lift);
	}

	public void addSlope(Slope slope)
	{
		_slopes.add(slope);
	}
}
