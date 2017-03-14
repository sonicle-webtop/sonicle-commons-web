/* 
 * Copyright (C) 2014 Sonicle S.r.l.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by
 * the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED
 * WORK IN WHICH THE COPYRIGHT IS OWNED BY SONICLE, SONICLE DISCLAIMS THE
 * WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle[at]sonicle[dot]com
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License
 * version 3, these Appropriate Legal Notices must retain the display of the
 * Sonicle logo and Sonicle copyright notice. If the display of the logo is not
 * reasonably feasible for technical reasons, the Appropriate Legal Notices must
 * display the words "Copyright (C) 2014 Sonicle S.r.l.".
 */
package com.sonicle.commons.web.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sonicle.commons.web.gson.GsonExtJsGridFilterTypeAdapter;
import com.sonicle.commons.web.gson.GsonISODateTypeAdapter;
import com.sonicle.commons.web.gson.JodaDateTimeTypeAdapter;
import com.sonicle.commons.web.gson.JodaLocalDateTypeAdapter;
import com.sonicle.commons.web.gson.JodaLocalTimeTypeAdapter;
import com.sonicle.commons.web.json.extjs.ExtGridFilter;
import com.sonicle.commons.web.json.extjs.GridMetadata;
import com.sonicle.commons.web.json.extjs.ResultMeta;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;

/**
 * A convenience class used to easly reply to an async request.
 * This class is designed to be converted to a JSON object using
 * a suitable serializer library (eg. Google Gson library).
 * 
 * @author malbinola
 */
public class JsonResult extends HashMap<String, Object> {
	
	public static final Gson gson = new GsonBuilder()
		.serializeNulls()
		.disableHtmlEscaping()
		.registerTypeAdapter(org.joda.time.DateTime.class, new JodaDateTimeTypeAdapter())
		.registerTypeAdapter(org.joda.time.LocalDate.class, new JodaLocalDateTypeAdapter())
		.registerTypeAdapter(org.joda.time.LocalTime.class, new JodaLocalTimeTypeAdapter())
		.registerTypeAdapter(java.util.Date.class, new GsonISODateTypeAdapter())
		.registerTypeAdapter(ExtGridFilter.class, new GsonExtJsGridFilterTypeAdapter())
		.create();
	
	public static final Gson GSON = new GsonBuilder()
		.serializeNulls()
		.disableHtmlEscaping()
		.registerTypeAdapter(org.joda.time.DateTime.class, new JodaDateTimeTypeAdapter())
		.registerTypeAdapter(org.joda.time.LocalDate.class, new JodaLocalDateTypeAdapter())
		.registerTypeAdapter(org.joda.time.LocalTime.class, new JodaLocalTimeTypeAdapter())
		.registerTypeAdapter(java.util.Date.class, new GsonISODateTypeAdapter())
		//.registerTypeAdapter(ExtGridFilter.class, new GsonExtJsGridFilterTypeAdapter())
		.create();
	
	//public static final Gson gsonNoHtmlEscape_ = new GsonBuilder()
	public static final Gson GSON_WONULLS = new GsonBuilder()
		.serializeNulls()
		.disableHtmlEscaping()
		.registerTypeAdapter(org.joda.time.DateTime.class, new JodaDateTimeTypeAdapter())
		.registerTypeAdapter(org.joda.time.LocalDate.class, new JodaLocalDateTypeAdapter())
		.registerTypeAdapter(org.joda.time.LocalTime.class, new JodaLocalTimeTypeAdapter())
		.registerTypeAdapter(java.util.Date.class, new GsonISODateTypeAdapter())
		//.registerTypeAdapter(ExtGridFilter.class, new GsonExtJsGridFilterTypeAdapter())
		.create();
	
	//public static final Gson gsonWoNulls_ = new GsonBuilder()
	public static final Gson GSON_PLAIN_WONULLS = new GsonBuilder()
		.disableHtmlEscaping()
		.create();
	
	/*
	public static final Gson gsonWoNullsNoEscape_ = new GsonBuilder()
		.registerTypeAdapter(java.util.Date.class, new GsonISODateTypeAdapter())
		.disableHtmlEscaping()
		.create();
	*/
	
	public static final String SUCCESS_PROP = "success";
	public static final String MESSAGE_PROP = "message";
	public static final String DATA_PROP = "data";
	public static final String META_PROP = "meta";
	public static final String METADATA_PROP = "metaData";
	public static final String TOTAL_PROP = "total";
	public static final String START_PROP = "start";
	public static final String LIMIT_PROP = "limit";
	public static final String SELECTED_PROP = "selected";
	protected String dataProperty = DATA_PROP;

	/***
	 * Default constructor.
	 * It configures a positive result with a null message.
	 */
	public JsonResult() {
		super();
		this.setSuccess(true);
	}
	
	public JsonResult(Object data) {
		this(DATA_PROP, data);
	}
	
	public JsonResult(ResultMeta meta) {
		super();
		setSuccess(true);
		setMetaData(meta);
	}
	
	public JsonResult(String dataProperty, Object data) {
		super();
		this.dataProperty = dataProperty;
		setSuccess(true);
		setData(data);
	}
	
	public JsonResult(Object data, int totalCount) {
		this(DATA_PROP, data, totalCount);
	}
	
	public JsonResult(String dataProperty, Object data, int totalCount) {
		super();
		this.dataProperty = dataProperty;
		setSuccess(true);
		setData(data);
		setTotal(totalCount);
	}
	
	public JsonResult(Object data, ResultMeta meta, int totalCount) {
		super();
		setSuccess(true);
		setData(data);
		setTotal(totalCount);
		setMetaData(meta);
	}
	
	public JsonResult(Object data, ResultMeta meta, int totalCount, int start, int limit) {
		super();
		setSuccess(true);
		setData(data);
		setTotal(totalCount);
		setMetaData(meta);
		setStart(start);
		setLimit(limit);
	}
	
	public JsonResult(boolean success, String message) {
		super();
		setSuccess(success);
		setMessage(message);
	}
	
	public JsonResult(boolean success, String message, Object data) {
		super();
		setSuccess(success);
		setMessage(message);
		setData(data);
	}
	
	public JsonResult(Exception ex) {
		super();
		setSuccess(false);
		setMessage(ex.getMessage());
	}
	
	public JsonResult(Exception ex, Object data) {
		super();
		setSuccess(false);
		setMessage(ex.getMessage());
		setData(data);
	}
	
	public boolean getSuccess() {
		return (Boolean)get(SUCCESS_PROP);
	}

	public JsonResult setSuccess(boolean value) {
		put(SUCCESS_PROP, value);
		return this;
	}

	public String getMessage() {
		return (String)this.get(MESSAGE_PROP);
	}

	public JsonResult setMessage(String value) {
		put(MESSAGE_PROP, value);
		return this;
	}
	
	public ResultMeta getMetaData() {
		return (ResultMeta)get(METADATA_PROP);
	}
	
	/**
	 * This method sets a specific meta property ("metaData") that is suitable 
	 * only when working with ExtJs grid components. Otherwise you shoud use
	 * the generic method setMeta that sets the custom "meta" property.
	 * @param value
	 * @return 
	 */
	public JsonResult setMetaData(ResultMeta value) {
		put(METADATA_PROP, value);
		return this;
	}
	
	public Object getDataProperty() {
		return dataProperty;
	}
	
	public Object getData() {
		return (Object)get(dataProperty);
	}

	public JsonResult setData(Object value) {
		put(dataProperty, value);
		return this;
	}
	
	public int getTotal() {
		return (Integer)this.get(TOTAL_PROP);
	}

	public JsonResult setTotal(int value) {
		this.put(TOTAL_PROP, value);
		return this;
	}
	
	public int getStart() {
		return (Integer)this.get(START_PROP);
	}

	public JsonResult setStart(int value) {
		this.put(START_PROP, value);
		return this;
	}
	
	public int getLimit() {
		return (Integer)this.get(LIMIT_PROP);
	}

	public JsonResult setLimit(int value) {
		this.put(LIMIT_PROP, value);
		return this;
	}
	
	public Object getSelected() {
		return (Object)this.get(SELECTED_PROP);
	}

	public JsonResult setSelected(Object value) {
		this.put(SELECTED_PROP, value);
		return this;
	}
	
	public JsonResult set(String key, Object value) {
		this.put(key, value);
		return this;
	}

	/**
	 * Serializes this result object into a json string using a ready GSon instance.
	 * Null values will be serialized.
	 * Calls toJson method specifying to serialize null objects
	 * @return Generated json string
	 */
	public String toJson() {
		return this.toJson(true);
	}
	
	/**
	 * Serializes this result object into a json string using a ready GSon instance.
	 * @param serializeNulls True to keep null values into serialized object, false otherwise
	 * @return Generated json string
	 */
	public String toJson(boolean serializeNulls) {
		if(serializeNulls) {
			return JsonResult.GSON.toJson(this);
		} else {
			return JsonResult.GSON_WONULLS.toJson(this);
		}
	}
	
	public void printTo(HttpServletResponse response, PrintWriter out) {
		printTo(response, out, true);
	}
	
	public void printTo(HttpServletResponse response, PrintWriter out, boolean serializeNulls) {
		String json = toJson(serializeNulls);
		if(response != null) response.setContentLength(json.getBytes().length);
		out.println(json);
	}
	
	/**
	 * Serializes this result object and prints its json representation into the writer.
	 * 
	 * @param out The writer
	 */
	public void printTo(PrintWriter out) {
		printTo(null, out, true);
	}
	
	/**
	 * Serializes this result object and prints its json representation into the writer.
	 * 
	 * @param out The writer
	 * @param serializeNulls True to keep null values into serialized object, false otherwise
	 */
	public void printTo(PrintWriter out, boolean serializeNulls) {
		out.println(toJson(serializeNulls));
	}
	

	//EXEMPLE JSON CLASS PARSING
	public static void main(String args[]) throws ParseException {
		
		//JsonResult.gson.fromJson("2015-01-20T00:00:00Z", Date.class);
		//new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("1970-01-01T00:00:00Z");
		
		
		/*
		String json="{ a: 'aaa', b: 111, c: { d: 'ddd', e: 222, f: 4.5 } }";
		MyTest mt=JsonResult.gson.fromJson(json, MyTest.class);
		MyTest mt2=JsonResult.gson.fromJson(json, MyTest2.class);
		//JsonParser parser=new JsonParser();
		//JsonObject p=parser.parse(json).getAsJsonObject();
		//MyTest mt=new MyTest();
		//mt.a=p.get("a").getAsString();
		//mt.b=p.get("b").getAsInt();
		//mt.c=p.get("c").getAsJsonObject().toString();
		System.out.println("MT  -> "+mt);
		System.out.println("MT2 -> "+mt2);
		*/
	}
	
	public static class MyTest {
		public String a;
		public int b;
		
		@Override
		public String toString() {
			return "a="+a+", b="+b;
		}
	}
	
	public static class MyC {
		String d;
		int e;
		float f;
		
		@Override
		public String toString() {
			return "{ d="+d+", e="+e+" f="+f;
		}		
	}
	
	public static class MyTest2 extends MyTest {
		
		MyC c;
		
		@Override
		public String toString() {
			return "a="+a+", b="+b+", c="+c+"  ["+c.getClass()+"]";
		}
	}
	
}