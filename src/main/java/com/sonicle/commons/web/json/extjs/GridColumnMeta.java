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

package com.sonicle.commons.web.json.extjs;

import java.util.HashMap;

/**
 * Helper Json-ready class to deal with ExtJs grid column metadata info.
 * @author malbinola
 */
public class GridColumnMeta extends HashMap<String, Object> {
	public static final String HEADER = "header";
	public static final String DATA_INDEX = "dataIndex";
	public static final String HIDDEN  = "hidden";
	public static final String WIDTH  = "width";
	public static final String SORTABLE  = "sortable";
	public static final String HIDEABLE  = "hideable";
	public static final String MENU_DISABLED  = "menuDisabled";
	public static final String DRAGGABLE  = "draggable";
	public static final String GROUPABLE  = "groupable";
	public static final String RENDERER  = "renderer";
	public static final String XTYPE  = "xtype";
	public static final String FORMAT  = "format";
	public static final String TPL  = "tpl";
	public static final String XTYPE_GRID  = "gridcolumn";
	public static final String XTYPE_BOOLEAN  = "booleancolumn";
	public static final String XTYPE_NUMBER  = "numbercolumn";
	public static final String XTYPE_DATE  = "datecolumn";
	public static final String XTYPE_TEMPLATE  = "templatecolumn";
	
	public GridColumnMeta() {
		super();
	}
	
	public GridColumnMeta(String dataIndex) {
		super();
		setDataIndex(dataIndex);
	}
	
	public GridColumnMeta(String dataIndex, String header) {
		super();
		setDataIndex(dataIndex);
		setHeader(header);
	}
	
	public GridColumnMeta setHeader(String value) {
		this.put(HEADER , value);
		return this;
	}
	
	public GridColumnMeta setDataIndex(String value) {
		this.put(DATA_INDEX , value);
		return this;
	}
	
	public GridColumnMeta setHidden(boolean value) {
		this.put(HIDDEN , value);
		return this;
	}
	
	public GridColumnMeta setWidth(int value) {
		this.put(WIDTH , value);
		return this;
	}
	
	public GridColumnMeta setSortable(boolean value) {
		this.put(SORTABLE , value);
		return this;
	}
	
	public GridColumnMeta setHideable(boolean value) {
		this.put(HIDEABLE , value);
		return this;
	}
	
	public GridColumnMeta setMenuDisabled(boolean value) {
		this.put(MENU_DISABLED , value);
		return this;
	}
	
	public GridColumnMeta setDraggable(boolean value) {
		this.put(DRAGGABLE , value);
		return this;
	}
	
	public GridColumnMeta setGroupable(boolean value) {
		this.put(GROUPABLE , value);
		return this;
	}
	
	public GridColumnMeta setRenderer(String value) {
		this.put(RENDERER , value);
		return this;
	}
	
	public GridColumnMeta setXType(String value) {
		this.put(XTYPE, value);
		return this;
	}
	
	public GridColumnMeta setFormat(String value) {
		this.put(FORMAT, value);
		return this;
	}
	
	public GridColumnMeta setTpl(String value) {
		this.put(TPL, value);
		return this;
	}
	
	public GridColumnMeta set(String property, Object value) {
		this.put(property, value);
		return this;
	}
}