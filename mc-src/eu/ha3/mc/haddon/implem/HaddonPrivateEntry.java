package eu.ha3.mc.haddon.implem;

import eu.ha3.mc.haddon.PrivateAccessException;

/*
--filenotes-placeholder
*/

public class HaddonPrivateEntry implements PrivateEntry
{
	private final String name;
	@SuppressWarnings("rawtypes")
	private final Class target;
	private final int zero;
	private final String[] fieldNames;
	
	@SuppressWarnings("rawtypes")
	public HaddonPrivateEntry(String name, Class target, int zero, String... lessToMoreImportantFieldName)
	{
		this.name = name;
		this.target = target;
		this.zero = zero;
		this.fieldNames = lessToMoreImportantFieldName.clone();
	}
	
	@Override
	public String getName()
	{
		return this.name;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public Class getTarget()
	{
		return this.target;
	}
	
	@Override
	public int getZero()
	{
		return this.zero;
	}
	
	@Override
	public String[] getFieldNames()
	{
		return this.fieldNames;
	}
	
	@Override
	public Object get(Object instance) throws PrivateAccessException
	{
		int i = this.fieldNames.length - 1;
		while (i >= 0)
		{
			try
			{
				return HaddonUtilitySingleton.getInstance().getPrivateValueViaName(
					this.target, instance, this.fieldNames[i]);
			}
			catch (PrivateAccessException e)
			{
			}
			i = i - 1;
		}
		if (this.zero >= 0)
		{
			try
			{
				return HaddonUtilitySingleton.getInstance().getPrivateValue(this.target, instance, this.zero);
			}
			catch (PrivateAccessException e)
			{
			}
		}
		
		generateError(); // will throw an exception
		return null;
	}
	
	@Override
	public void set(Object instance, Object value) throws PrivateAccessException
	{
		int i = this.fieldNames.length - 1;
		while (i >= 0)
		{
			try
			{
				HaddonUtilitySingleton.getInstance().setPrivateValueViaName(
					this.target, instance, this.fieldNames[i], value);
				return;
			}
			catch (PrivateAccessException e)
			{
			}
			i = i - 1;
		}
		if (this.zero >= 0)
		{
			try
			{
				HaddonUtilitySingleton.getInstance().setPrivateValue(this.target, instance, this.zero, value);
				return;
			}
			catch (PrivateAccessException e)
			{
			}
		}
		
		generateError(); // will throw an exception
	}
	
	private void generateError() throws PrivateAccessException
	{
		StringBuilder sb = new StringBuilder();
		for (int j = this.fieldNames.length - 1; j >= 0; j--)
		{
			sb.append(this.fieldNames[j]);
			sb.append(",");
		}
		sb.append("[").append(this.zero).append("]");
		throw new PrivateAccessException(this.name + "(" + sb + ") could not be resolved");
	}
}
