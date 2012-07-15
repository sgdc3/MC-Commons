package net.minecraft.src;

import java.util.ArrayList;

import eu.ha3.matmos.engine.MAtmosData;

/*
            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE 
                    Version 2, December 2004 

 Copyright (C) 2004 Sam Hocevar <sam@hocevar.net> 

 Everyone is permitted to copy and distribute verbatim or modified 
 copies of this license document, and changing it is allowed as long 
 as the name is changed. 

            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE 
   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION 

  0. You just DO WHAT THE FUCK YOU WANT TO. 
*/

public abstract class MAtProcessorModel
{
	private MAtMod mod;
	private MAtmosData data;
	
	private String normalName;
	private String deltaName;
	
	private ArrayList<Integer> normalSheet;
	private ArrayList<Integer> deltaSheet;
	
	MAtProcessorModel(MAtMod modIn, MAtmosData dataIn,
			String normalNameIn,
			String deltaNameIn)
			{
		mod = modIn;
		data = dataIn;
		normalName = normalNameIn;
		deltaName = deltaNameIn;
		
			}
	
	public MAtMod mod()
	{
		return mod;
		
	}
	
	public MAtmosData data()
	{
		return data;
		
	}
	
	abstract void doProcess();
	
	public void process()
	{
		normalSheet = data().sheets.get(normalName);
		if (deltaName != null)
			deltaSheet = data().sheets.get(deltaName);
		
		doProcess();
		
	}
	
	void setValue(int index, int newValue)
	{
		int previousValue = normalSheet.get(index);
		normalSheet.set(index, newValue);
		
		if (deltaName != null)
			deltaSheet.set(index, newValue - previousValue);
		
	}
	
}
