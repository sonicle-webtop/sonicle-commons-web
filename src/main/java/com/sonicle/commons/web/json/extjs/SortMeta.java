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
package com.sonicle.commons.web.json.extjs;

import com.google.gson.annotations.SerializedName;
import com.sonicle.commons.EnumUtils;
import com.sonicle.commons.web.json.JsonResult;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Helper object in ExtJS style to deal with SortInfo data.
 * @author malbinola
 */
public class SortMeta extends HashMap<String, Object> {
	public static final String FIELD = "field";
	public static final String DIRECTION = "direction";
	
	public SortMeta() {
		super();
	}
	
	public SortMeta(String field, String direction) {
		super();
		this.setField(field);
		this.setDirection(direction);
	}
	
	public SortMeta(String field) {
		this(field, Direction.ASC);
	}
	
	public SortMeta(String field, Direction direction) {
		this(field, EnumUtils.toSerializedName(direction));
	}
	
	public String getField() {
		return (String)get(FIELD);
	}
	
	public SortMeta setField(String field) {
		this.put(FIELD , field);
		return this;
	}
	
	public Object getDirection() {
		return (String) get(DIRECTION);
	}
	
	public SortMeta setDirection(String direction) {
		this.put(DIRECTION , direction);
		return this;
	}
	
	public SortMeta setDirection(Direction direction) {
		this.put(DIRECTION , direction);
		return this;
	}
	
	public SortMeta set(String property, Object value) {
		this.put(property, value);
		return this;
	}
	
	public static String toJson(SortMeta value) throws Exception {
		return JsonResult.gson().toJson(value);
	}
	
	public static SortMeta fromJson(String value) throws Exception {
		return JsonResult.gson().fromJson(value, SortMeta.class);
	}
	
	public static class SortMetaList extends ArrayList<SortMeta> {
		public SortMetaList() {
			super();
		}
		
		public static String toJson(SortMetaList value) throws Exception {
			return JsonResult.gson().toJson(value);
		}

		public static SortMetaList fromJson(String value) throws Exception {
			return JsonResult.gson().fromJson(value, SortMetaList.class);
		}
	}
	
	public static enum Direction {
		@SerializedName("ASC") ASC,
		@SerializedName("DESC") DESC;
	}
}
