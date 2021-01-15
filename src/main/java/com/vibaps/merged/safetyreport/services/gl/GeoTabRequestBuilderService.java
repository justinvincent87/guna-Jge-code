package com.vibaps.merged.safetyreport.services.gl;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.vibaps.merged.safetyreport.builder.GeoTabRequestBuilder;

@Service
public class GeoTabRequestBuilderService {

	public String getRequestPayload() {
		
		return GeoTabRequestBuilder.getInstance()
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
	}
}
