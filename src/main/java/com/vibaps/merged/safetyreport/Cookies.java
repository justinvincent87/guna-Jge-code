package com.vibaps.merged.safetyreport;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;

public class Cookies {

	public static String getcook(HttpServletRequest request, String Key) {

		Cookie cookie = WebUtils.getCookie(request, Key);
		return cookie.getValue();
	}
}
