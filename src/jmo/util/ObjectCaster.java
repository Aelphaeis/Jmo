package jmo.util;

public class ObjectCaster {
	//This method is intended to be unchecked and 
	//therefore the unchecked warning is not relevant
	@SuppressWarnings("unchecked")
	public static <T> T Cast(Object obj){
		try
		{
			return  (T) obj;
		}
		catch(ClassCastException t)
		{
			return null;
		}
	}

}
