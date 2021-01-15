package com.vibaps.merged.safetyreport.builder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * This class used to build a request for GeoTab API call. It generate only
 * request payload for the request. Once you build this class it returns "Json"
 * type of payload.
 * </p>
 *
 * @author Justin Vincent
 * @version 1.0
 * @since Jan 15, 2021
 */
@Log4j2
public class GeoTabRequestBuilder implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final ObjectMapper	MAPPER			= new ObjectMapper();
	private static final String			FROM_TS_SUFFIX	= "T01:00:00.000Z";
	private static final String			TO_TS_SUFFIX	= "T03:59:59.000Z";

	static {
		// Added field visibility for this builer class
		MAPPER.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	}

	@JsonIgnore
	private Params	parentParams;
	private String	method;
	private Params	params;

	/**
	 * @param parentParams - It use to do step back
	 */
	public GeoTabRequestBuilder(Params parentParams) {
		this.parentParams = parentParams;
	}

	/**
	 * Get default instance for this builder class
	 * 
	 * @return initial instance of {@code GeoTabRequestBuilder}
	 */
	public static GeoTabRequestBuilder getInstance() {
		return new GeoTabRequestBuilder(null);
	}

	/**
	 * Add geo tab api method name
	 * 
	 * @param methodName - Which api method need to call
	 * @return
	 */
	public GeoTabRequestBuilder method(String methodName) {
		this.method = methodName;
		return this;
	}

	/**
	 * Initiate params instance if it doesn't exist
	 * @return
	 */
	public Params params() {
		if (Objects.isNull(params)) {
			params = new Params(this);
		}
		return params;
	}

	/**
	 * Step back to initiate next call
	 * @return
	 */
	public Params done() {
		return this.parentParams;
	}

	/**
	 * Build this instance and generate this as a JSON
	 * 
	 * @return
	 */
	public String build() {

		if (Objects.nonNull(this.parentParams)) {
			return this.parentParams.build();
		}

		try {
			return MAPPER.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			String message = "Error while build geo tab request";
			log.error(message, e);
			throw new RuntimeException(message);
		}
	}

	/**
	 * <p>
	 *  It holds parms information
	 * </p>
	 */
	public static class Params {

		@JsonIgnore
		private GeoTabRequestBuilder		parent;
		@JsonInclude(NON_NULL)
		private List<GeoTabRequestBuilder>	calls;
		@JsonInclude(NON_NULL)
		private Argument					argument;
		@JsonInclude(NON_NULL)
		private Credentials					credentials;
		@JsonInclude(NON_NULL)
		private String						typeName;

		/**
		 * Create instance with parent instance
		 * 
		 * @param parent - It use to do step back
		 */
		public Params(GeoTabRequestBuilder parent) {
			this.parent = parent;
		}

		public GeoTabRequestBuilder addCalls() {
			if (Objects.isNull(calls)) {
				calls = new ArrayList<>();
			}
			GeoTabRequestBuilder call = new GeoTabRequestBuilder(this);
			calls.add(call);
			return call;
		}

		@JsonIgnore
		public GeoTabRequestBuilder getFirstCall() {
			if (Objects.isNull(calls) || calls.isEmpty()) {
				throw new RuntimeException("Calls doesnot exist");
			}
			return calls.get(0);
		}

		public Argument argument() {
			if (Objects.isNull(argument)) {
				argument = new Argument(this);
			}
			return argument;
		}

		public Credentials credentials() {
			if (Objects.isNull(credentials)) {
				credentials = new Credentials(this);
			}
			return credentials;
		}

		public Params typeName(String typeName) {
			this.typeName = typeName;
			return this;
		}

		public GeoTabRequestBuilder and() {
			return this.parent;
		}

		public String build() {
			return this.parent.build();
		}

		@Override
		public String toString() {
			return "Params [parent=" + parent + ", calls=" + calls + ", argument=" + argument + ", credentials="
			        + credentials + ", typeName=" + typeName + "]";
		}
	}

	public static class Credentials {

		@JsonIgnore
		private Params	parent;
		private String	database;
		private String	sessionId;
		private String	userName;

		public Credentials(Params parent) {
			this.parent = parent;
		}

		public Credentials database(String database) {
			this.database = database;
			return this;
		}

		public Credentials sessionId(String sessionId) {
			this.sessionId = sessionId;
			return this;
		}

		public Credentials userName(String userName) {
			this.userName = userName;
			return this;
		}

		public Params and() {
			return this.parent;
		}

		public String build() {
			return this.parent.build();
		}

		@Override
		public String toString() {
			return "Credentials [database=" + database + ", sessionId=" + sessionId + ", userName=" + userName + "]";
		}

	}

	public static class Argument {

		@JsonIgnore
		private Params		parent;
		private Integer		runGroupLevel;
		private Boolean		isNoDrivingActivityHidden;
		private String		fromUtc;
		private String		toUtc;
		private String		entityType;
		private String		reportArgumentType;
		private List<Group>	groups;
		private String		reportSubGroup;
		private List<Rule>	rules;

		public Argument(Params parent) {
			this.parent = parent;
		}

		public Argument runGroupLevel(Integer runGroupLevel) {
			this.runGroupLevel = runGroupLevel;
			return this;
		}

		public Argument isNoDrivingActivityHidden(Boolean isNoDrivingActivityHidden) {
			this.isNoDrivingActivityHidden = isNoDrivingActivityHidden;
			return this;
		}

		public Argument fromUtc(String fromUtc) {
			this.fromUtc = fromUtc + FROM_TS_SUFFIX;
			return this;
		}

		public Argument toUtc(String toUtc) {
			this.toUtc = toUtc + TO_TS_SUFFIX;
			return this;
		}

		public Argument entityType(String entityType) {
			this.entityType = entityType;
			return this;
		}

		public Argument reportArgumentType(String reportArgumentType) {
			this.reportArgumentType = reportArgumentType;
			return this;
		}

		public Argument reportSubGroup(String reportSubGroup) {
			this.reportSubGroup = reportSubGroup;
			return this;
		}

		public Argument groups(List<String> groups) {
			this.groups = groups.stream().map(Group::new).collect(Collectors.toList());
			return this;
		}

		public Argument rules(List<String> rules) {
			this.rules = rules.stream().map(Rule::new).collect(Collectors.toList());
			return this;
		}

		public Params and() {
			return this.parent;
		}

		public String build() {
			return this.parent.build();
		}

		@Override
		public String toString() {
			return "Argument [parent=" + parent + ", runGroupLevel=" + runGroupLevel + ", isNoDrivingActivityHidden="
			        + isNoDrivingActivityHidden + ", fromUtc=" + fromUtc + ", toUtc=" + toUtc + ", entityType="
			        + entityType + ", reportArgumentType=" + reportArgumentType + ", reportSubGroup=" + reportSubGroup
			        + ", groups=" + groups + ", rules=" + rules + "]";
		}
	}

	public static class Group {
		private String id;

		public Group(String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}
	}

	public static class Rule {
		private String id;

		public Rule(String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}
	}

	@Override
	public String toString() {
		return "GeoTabRequestBuilder [method=" + method + ", params=" + params + "]";
	}
}
