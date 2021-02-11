package com.vibaps.merged.safetyreport.util;

import org.apache.commons.io.FilenameUtils;

public class FileUtil {

	public static String fileExtention(String filename)
	{
		return FilenameUtils.getExtension(filename);
	}
}
