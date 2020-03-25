package com.mariocairone.mule.anypoint.service.utils;

public class Console {

	
	public static void print( String prefix, CharSequence content )
    {
        System.out.println( "[" + prefix + "] " + content.toString() );
    }
}
