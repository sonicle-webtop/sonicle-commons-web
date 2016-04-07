/*
 * sonicle-commons-web is a library developed by Sonicle S.r.l.
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

import java.util.HashMap;

/**
 *
 * @author malbinola
 */
public class RestJsonResult extends HashMap<String, Object> {
	public static final String SUCCESS_PROP = "success";
	public static final String MESSAGE_PROP = "message";
	public static final String DATA_PROP = "data";
	public static final String TOTAL_PROP = "total";
	protected String dataProperty = DATA_PROP;
	
	public RestJsonResult() {
		super();
		this.setSuccess(true);
	}
	
	public RestJsonResult(Object data) {
		this(DATA_PROP, data);
	}
	
	private RestJsonResult(String dataProperty, Object data) {
		super();
		this.dataProperty = dataProperty;
		setSuccess(true);
		setData(data);
	}
	
	public RestJsonResult(Object data, int totalCount) {
		this(DATA_PROP, data, totalCount);
	}
	
	private RestJsonResult(String dataProperty, Object data, int totalCount) {
		super();
		this.dataProperty = dataProperty;
		setSuccess(true);
		setData(data);
		setTotal(totalCount);
	}
	
	public RestJsonResult(boolean success, String message) {
		super();
		setSuccess(success);
		setMessage(message);
	}
	
	public RestJsonResult(boolean success, String message, Object data) {
		super();
		setSuccess(success);
		setMessage(message);
		setData(data);
	}
	
	public RestJsonResult(Exception ex) {
		super();
		setSuccess(false);
		setMessage(ex.getMessage());
	}
	
	public RestJsonResult(Exception ex, Object data) {
		super();
		setSuccess(false);
		setMessage(ex.getMessage());
		setData(data);
	}
	
	public boolean getSuccess() {
		return (Boolean)get(SUCCESS_PROP);
	}

	public RestJsonResult setSuccess(boolean value) {
		put(SUCCESS_PROP, value);
		return this;
	}

	public String getMessage() {
		return (String)this.get(MESSAGE_PROP);
	}

	public RestJsonResult setMessage(String value) {
		put(MESSAGE_PROP, value);
		return this;
	}
	
	public Object getDataProperty() {
		return dataProperty;
	}
	
	public Object getData() {
		return (Object)get(dataProperty);
	}

	public RestJsonResult setData(Object value) {
		put(dataProperty, value);
		return this;
	}
	
	public int getTotal() {
		return (Integer)this.get(TOTAL_PROP);
	}

	public RestJsonResult setTotal(int value) {
		this.put(TOTAL_PROP, value);
		return this;
	}
	
	/**
	 * Serializes this result object into a json string using a ready GSon instance.
	 * Null values will be serialized.
	 * Calls toJson method specifying to serialize null objects
	 * @return Generated json string
	 */
	public String print() {
		return print(true);
	}
	
	/**
	 * Serializes this result object into a json string using a ready GSon instance.
	 * @param serializeNulls True to keep null values into serialized object, false otherwise
	 * @return Generated json string
	 */
	public String print(boolean serializeNulls) {
		if(serializeNulls) {
			return JsonResult.gson.toJson(this);
		} else {
			return JsonResult.gsonWoNulls.toJson(this);
		}
	}
}
