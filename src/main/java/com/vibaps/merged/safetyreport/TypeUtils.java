package com.vibaps.merged.safetyreport;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.internal.util.SerializationHelper;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lytx.dto.EventsInfoV5;

public final class TypeUtils {

	private static final Logger LOGGER = Logger.getLogger(TypeUtils.class);

	private SecureRandom random = new SecureRandom();

	private TypeUtils() {

	}

	public  Integer toIntegerValue(Object object) {

		Integer value = null;
		if (object != null) {
			if (object instanceof Integer) {
				value = (Integer) object;
			} else if (object instanceof String) {
				value = Integer.valueOf(((String) object).trim());
			} else {
				try {
					value = Integer.parseInt(object.toString());
				} catch (Exception e) {
					LOGGER.warnf("Unable to parse %s as Integer", object.toString());
				}
			}
		}

		return value;
	}

	public Integer toInt0Value(Object object) {

		Integer value = toIntegerValue(object);
		return value == null ? 0 : value;
	}

	public long toLongValue(Object object) {

		long value = 0L;
		if (object != null) {
			if (object instanceof String) {
				value = Long.valueOf(((String) object).trim());
			} else {
				try {
					value = Long.parseLong(object.toString());
				} catch (Exception e) {
					LOGGER.warnf("Unable to parse %s as Long", object.toString());
				}
			}
		}

		return value;
	}

	public String toStringValue(Object object) {

		String value = "";
		if (object != null) {
			if (object instanceof String) {
				value = (String) object;
			} else {
				try {
					value = "" + object.toString().trim();
				} catch (Exception e) {
					LOGGER.warnf("Unable to parse %s as String", object.toString());
				}
			}
		}

		return value;
	}

	@SuppressWarnings("rawtypes")
	public Boolean isNotBlank(Object object) {

		if (object instanceof List) {
			List list = (List) object;
			return list != null && !list.isEmpty();
		} else if (object instanceof Map) {
			Map map = (Map) object;
			return map != null && !map.isEmpty();
		} else {
			return object != null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object readObject(Class cls, String payload) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(payload, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes" })
	public Object readMapObject(Class cls, Map<String, Object> payload) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonPayload = mapper.writeValueAsString(payload);
			return readObject(cls, jsonPayload);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Boolean isBlank(Object object) {
		return !isNotBlank(object);
	}

	public String decapitalize(String string) {

		if (string == null || string.length() == 0) {
			return string;
		}
		char c[] = string.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		return new String(c);
	}

	public String getField(String column) {

		return getSuffix(column, "");
	}
 
	public String getSuffix(String string, String splitter) {

		if (string == null || string.length() == 0 || splitter == null || splitter.length() == 0) {
			return string;
		}
		String[] strings = string.split(splitter);
		return strings[(strings.length - 1)];
	}

	public Date getDateWithoutTime(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public Date getFormattedFromDateTime(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public Date getFormattedToDateTime(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	public Date getFormattedFromDateTime(Integer year) {

		Calendar cal = Calendar.getInstance();
		cal.set(year, 0, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public Date getFormattedToDateTime(Integer year) {

		Calendar cal = Calendar.getInstance();
		cal.set(year, 11, 31);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	/**
	 * <pre>
	 * Get random number
	 * </pre>
	 * 
	 * @return
	 */
	public String nextSessionId() {

		return new BigInteger(50, random).toString(32);
	}

	/** 
	 * <pre>
	 * Get next week data
	 * </pre>
	 * @param date
	 * @return
	 */
	public Date getOneWeekDateTime(Date date) {
		return addDays(date, Calendar.DAY_OF_WEEK);
	}
	
	public Date addDays(Date date, Integer days) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
        System.out.println("NextWeekdate"+cal.getTime());
		return cal.getTime();
	}

	public int numberOfDaysInMonth(int month, int year) {
		Calendar monthStart = new java.util.GregorianCalendar(year, month - 1, 1);
		return monthStart.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public Date toSqlDate(Date date) {
		
		java.util.Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime()); 
		return sqlDate;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Date> getMonthOfDates(Date monthDate) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(monthDate);
		// int month = cal.get(Calendar.MONTH);
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		List<Date> days = new ArrayList();
		for (int i = 0; i < maxDay; i++) {
			cal.set(Calendar.DAY_OF_MONTH, i + 1);
			days.add(cal.getTime());
		}
		return days;
	}
	
	/*
	 * public static Object getClone(Object trueObject) {
	 * 
	 * try { Cloner cloner = new Cloner(); return cloner.deepClone(trueObject);
	 * }catch(Exception e) { LOGGER.error("Error while cloning object", e); } return
	 * null; }
	 */
	public Object getEntityClone(Object trueEntityObject) {
		
		try {
			return SerializationHelper.clone((Serializable) trueEntityObject);
		}catch(Exception e) {
			LOGGER.error("Error while cloning object", e);
		}
		return null;
	}

	public Object getlyval(Object events,Object jv) {
		// TODO Auto-generated method stub
		
		Map<Long,ArrayList> result=new HashMap<Long,ArrayList>();
		Map<Long,String> vmap=new HashMap<Long, String>();
		
		JSONObject lytxEventsJOv = new JSONObject(jv);
        JSONArray lytxEventsArrayvn = lytxEventsJOv.getJSONArray("vehicles"); 
        
        for (int i = 0; i < lytxEventsArrayvn.length(); i++) 
        {
            Long eventsVehicleId = lytxEventsArrayvn.getJSONObject(i).getLong("vehicleId");
            vmap.put(lytxEventsArrayvn.getJSONObject(i).getLong("vehicleId"), lytxEventsArrayvn.getJSONObject(i).getString("name"));
        }

		   JSONObject lytxEventsJO = new JSONObject(events);
	        JSONArray lytxEventsArray = lytxEventsJO.getJSONArray("events");
	       
	        for (int i = 0; i < lytxEventsArray.length(); i++) {
	        	//System.out.println(i+"----"+lytxEventsArray.getJSONObject(i).getLong("vehicleId"));
	        	 ArrayList resulta = new ArrayList();
	            Long eventsVehicleId = lytxEventsArray.getJSONObject(i).getLong("vehicleId");
	            if(!result.containsKey(eventsVehicleId))
	            {
	            resulta.add(vmap.get(eventsVehicleId));
	            }
	            JSONArray lytxBehavioursArray = lytxEventsArray.getJSONObject(i).getJSONArray("behaviors");
	          
	            for(int j = 0; j < lytxBehavioursArray.length(); j++) {
	            	
	        		resulta.add(lytxBehavioursArray.getJSONObject(j).getInt("behavior"));
	        	}
	            if(result.containsKey(eventsVehicleId))
	            {
	            	for(int s=0;s<resulta.size();s++)
	            	{
	            	result.get(eventsVehicleId).add(resulta.get(s));
	            	}
	            }
	            else
	            {
	        result.put(eventsVehicleId,resulta);
	            }
	            
	           
	        }
		     Map flis=new HashMap();   
		        ArrayList asp=new ArrayList();

	        for ( Long key : result.keySet() ) 
	        {

	            asp.add(result.get(key));
		        flis.put("values",asp);

	        }
		return flis;
	}

}
