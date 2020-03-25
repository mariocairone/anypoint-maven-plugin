package com.mariocairone.mule.anypoint.service.utils;

import static com.mariocairone.mule.anypoint.service.utils.Console.print;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;

public class FileUtils {

	
    public static String readFile(File file) 
    {
		if(file == null || !file.exists()) {
			print("INFO","Configuration file not found");
			return null;
		}
		
        StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines( file.toPath(), StandardCharsets.UTF_8)) 
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) 
        {
			print("ERROR","An exception accurred reading the file");

        }
 
        return contentBuilder.toString();
    }
}
