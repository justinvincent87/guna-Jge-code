package com.vibaps.merged.safetyreport.util;

import java.util.Arrays;

import com.vibaps.merged.safetyreport.builder.GeoTabRequestBuilder;

public class Test {

	public static void main(String[] args) {
		
		String json = GeoTabRequestBuilder.getInstance()
					.method("ExecuteMultiCall")
					.params()
						.credentials()
							.database("brigiottas_farmland")
							.sessionId("dANtfx3XTXLYc1uyfKVwVg")
							.userName("atiadmin@assuredtelematics.com")
						.and()
						.addCalls()
							.method("GetReportData")
							.params()
								.argument()
									.runGroupLevel(-1)
									.isNoDrivingActivityHidden(true)
									.fromUtc("2021-01-03")
									.toUtc("2021-01-09")
									.entityType("Device")
									.reportArgumentType("RiskManagement")
									.groups(Arrays.asList("b28B3", "b28B2"))
									.reportSubGroup("None")
									.rules(Arrays.asList("RulePostedSpeedingId", "axPobRlJuZ0GR5ib_JCe5Gg"))
									.and()
								.and()
							.done()
						.addCalls()
							.method("Get")
							.params()
								.typeName("SystemSettings").build();
					
							
		System.out.println(json);
	}
}
