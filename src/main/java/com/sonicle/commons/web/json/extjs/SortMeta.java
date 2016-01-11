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
package com.sonicle.commons.web.json.extjs;

import com.sonicle.commons.web.json.JsonResult;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author malbinola
 */
public class SortMeta extends HashMap<String, Object> {
	public static final String FIELD = "field";
	public static final String DIRECTION = "direction";
	public static final String DIRECTION_ASC = "ASC";
	public static final String DIRECTION_DESC = "DESC";
	
	public SortMeta() {
		super();
	}
	
	public SortMeta(String field) {
		super();
		this.setField(field);
		this.setDirection(DIRECTION_ASC);
	}
	
	public SortMeta(String field, String direction) {
		super();
		this.setField(field);
		this.setDirection(direction);
	}
	
	public String getField() {
		return (String) get(FIELD);
	}
	
	public SortMeta setField(String value) {
		this.put(FIELD , value);
		return this;
	}
	
	public String getDirection() {
		return (String) get(DIRECTION);
	}
	
	public SortMeta setDirection(String value) {
		this.put(DIRECTION , value);
		return this;
	}
	
	public SortMeta set(String property, Object value) {
		this.put(property, value);
		return this;
	}
	
	public static String toJson(SortMeta value) throws Exception {
		return JsonResult.gson.toJson(value);
	}
	
	public static SortMeta fromJson(String value) throws Exception {
		return JsonResult.gson.fromJson(value, SortMeta.class);
	}
	
	public static class SortMetaList extends ArrayList<SortMeta> {
		public SortMetaList() {
			super();
		}
		
		public static String toJson(SortMetaList value) throws Exception {
			return JsonResult.gson.toJson(value);
		}

		public static SortMetaList fromJson(String value) throws Exception {
			return JsonResult.gson.fromJson(value, SortMetaList.class);
		}
	}
}
