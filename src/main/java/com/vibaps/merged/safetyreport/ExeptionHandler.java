package com.vibaps.merged.safetyreport;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ExeptionHandler extends Exception {

	public void ExeptionHandler(Throwable cause)
	{
		log.debug("Exeption {}",cause);
	}
}
