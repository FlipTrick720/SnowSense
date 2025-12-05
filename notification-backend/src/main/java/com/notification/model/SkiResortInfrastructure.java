package com.notification.model;

import java.util.ArrayList;

public class SkiResortInfrastructure
{
	private final SkiResort _skiResort;
	private final ArrayList<SkiResortLift> _lifts = new ArrayList<>();
	private final ArrayList<SkiResortSlope> _slopes = new ArrayList<>();

	public SkiResortInfrastructure(SkiResort skiResort)
	{
		_skiResort = skiResort;
	}

	public void addLift(SkiResortLift lift)
	{
		_lifts.add(lift);
	}

	public void addSlope(SkiResortSlope slope)
	{
		_slopes.add(slope);
	}

	public ArrayList<SkiResortLift> getLifts()
	{
		return _lifts;
	}

	public ArrayList<SkiResortSlope> getSlopes()
	{
		return _slopes;
	}
}
