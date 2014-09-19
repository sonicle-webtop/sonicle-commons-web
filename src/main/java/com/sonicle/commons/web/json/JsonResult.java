/*
 * Sonicle Commons Web is a helper library developed by Sonicle S.r.l.
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
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle@sonicle.com
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
import com.sonicle.commons.web.json.extjs.ExtGridFilter;
import com.sonicle.commons.web.json.extjs.ExtMetaData;
import java.io.PrintWriter;
import java.util.HashMap;

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
		.registerTypeAdapter(java.util.Date.class, new GsonISODateTypeAdapter())
		.registerTypeAdapter(ExtGridFilter.class, new GsonExtJsGridFilterTypeAdapter())
		.create();
	
	public static final Gson gsonWoNulls = new GsonBuilder()
		.registerTypeAdapter(java.util.Date.class, new GsonISODateTypeAdapter())
		.create();
	
	public static final String SUCCESS_PROP = "success";
	public static final String MESSAGE_PROP = "message";
	public static final String DATA_PROP = "data";
	public static final String META_PROP = "meta";
	public static final String METADATA_PROP = "metaData";
	public static final String TOTAL_PROP = "total";
	public static final String START_PROP = "start";
	public static final String LIMIT_PROP = "limit";
	public static final String SELECTED_PROP = "selected";

	/***
	 * Default constructor.
	 * It configures a positive result with a null message.
	 */
	public JsonResult() {
		super();
		this.setSuccess(true);
	}
	
	public JsonResult(Object data) {
		super();
		this.setSuccess(true);
		this.setData(data);
	}
	
	public JsonResult(Object data, int totalCount) {
		super();
		this.setSuccess(true);
		this.setData(data);
		this.setTotal(totalCount);
	}
	
	public JsonResult(Object data, ExtMetaData meta, int totalCount) {
		super();
		this.setSuccess(true);
		this.setData(data);
		this.setTotal(totalCount);
		this.setMetaData(meta);
	}
	
	public JsonResult(Object data, ExtMetaData meta, int totalCount, int start, int limit) {
		super();
		this.setSuccess(true);
		this.setData(data);
		this.setTotal(totalCount);
		this.setMetaData(meta);
		this.setStart(start);
		this.setLimit(limit);
	}
	
	public JsonResult(boolean success, String message) {
		super();
		this.setSuccess(success);
		this.setMessage(message);
	}
	
	public JsonResult(boolean success, String message, Object data) {
		super();
		this.setSuccess(success);
		this.setMessage(message);
		this.setData(data);
	}
	
	public boolean getSuccess() {
		return (Boolean)this.get(SUCCESS_PROP);
	}

	public JsonResult setSuccess(boolean value) {
		this.put(SUCCESS_PROP, value);
		return this;
	}

	public String getMessage() {
		return (String)this.get(MESSAGE_PROP);
	}

	public JsonResult setMessage(String value) {
		this.put(MESSAGE_PROP, value);
		return this;
	}
	
	public ExtMetaData getMetaData() {
		return (ExtMetaData)this.get(METADATA_PROP);
	}

	public JsonResult setMetaData(ExtMetaData value) {
		this.put(METADATA_PROP, value);
		return this;
	}
	
	public Object getData() {
		return (Object)this.get(DATA_PROP);
	}

	public JsonResult setData(Object value) {
		this.put(DATA_PROP, value);
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
			return JsonResult.gson.toJson(this);
		} else {
			return JsonResult.gsonWoNulls.toJson(this);
		}
	}
	
	/**
	 * Serializes this result object and prints its json representation into the writer.
	 * 
	 * @param out The writer
	 */
	public void printTo(PrintWriter out) {
		printTo(out, true);
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
}