package com.vibaps.merged.safetyreport;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;

import springfox.documentation.spring.web.json.Json;

public class Cookies {

	public static String getcook(HttpServletRequest request,String Key) {
		
		/*
		 * Cookie[] cookies = request.getCookies();
		 * 
		 * String SesId=Arrays.stream(request.getCookies()) .filter(c ->
		 * Key.equals(c.getName())) .map(Cookie::getValue) .findAny().toString();
		 * System.out.println(SesId+"value");
		 */
	        

	        Cookie cookie = WebUtils.getCookie(request,Key);

	        
	        return cookie.getValue();
	        
	        
	        //return SesId;
		
	}
}
