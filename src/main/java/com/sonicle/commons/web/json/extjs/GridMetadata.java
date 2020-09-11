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

import java.util.List;

/**
 *
 * @author malbinola
 */
public class GridMetadata extends ResultMeta<GridMetadata> {
	public static final String META_STORE_ROOT = "root";
	public static final String META_FIELDS = "fields";
	public static final String META_COLUMNS_INFO = "colsInfo";
	public static final String META_SORT_INFO = "sortInfo";
	public static final String META_GROUP_INFO = "groupInfo";
	public static final String META_ID_PROPERTY = "idProperty";
	public static final String META_TOTAL_PROPERTY = "totalProperty";
	public static final String META_MESSAGE_PROPERTY = "messageProperty";
	
	public static final String META_TOTAL_COUNT = "total";
	public static final String META_SELECTED = "selected";
	
	public GridMetadata() {
		super();
	}
	
	public GridMetadata(boolean defaults) {
		this();
		this.setRoot("data");
		this.setTotalProperty("total");
		this.setMessageProperty("message");
	}

	public String getRoot() {
		return (String)this.get(META_STORE_ROOT);
	}

	public GridMetadata setRoot(String root) {
		this.put(META_STORE_ROOT, root);
		return this;
	}

	public Object getFields() {
		return (Object)this.get(META_FIELDS);
	}

	public GridMetadata setFields(Object fields) {
		this.put(META_FIELDS, fields);
		return this;
	}
	
	public GridMetadata setFields(String[] fields) {
		this.put(META_FIELDS, fields);
		return this;
	}
	
	public GridMetadata setFields(List<FieldMeta> fields) {
		this.put(META_FIELDS, fields);
		return this;
	}

	public Object getColumnsInfo() {
		return (Object)this.get(META_COLUMNS_INFO);
	}

	public GridMetadata setColumnsInfo(Object columnsInfo) {
		this.put(META_COLUMNS_INFO, columnsInfo);
		return this;
	}
	
	public GridMetadata setColumnsInfo(List<GridColumnMeta> columnsInfo) {
		this.put(META_COLUMNS_INFO, columnsInfo);
		return this;
	}
	
	public Object getSortInfo() {
		return (Object)this.get(META_SORT_INFO);
	}

	public GridMetadata setSortInfo(Object sortInfo) {
		this.put(META_SORT_INFO, sortInfo);
		return this;
	}
	
	public GridMetadata setSortInfo(SortMeta sortInfo) {
		this.put(META_SORT_INFO, sortInfo);
		return this;
	}
	
	public GridMetadata setSortInfo(List<SortMeta> sortInfo) {
		this.put(META_SORT_INFO, sortInfo);
		return this;
	}
	
	public Object getGroupInfo() {
		return (Object)this.get(META_GROUP_INFO);
	}

	public GridMetadata setGroupInfo(Object groupInfo) {
		this.put(META_GROUP_INFO, groupInfo);
		return this;
	}
	
	public GridMetadata setGroupInfo(GroupMeta groupInfo) {
		this.put(META_GROUP_INFO, groupInfo);
		return this;
	}
	
	public String getIdProperty() {
		return (String)this.get(META_ID_PROPERTY);
	}

	public GridMetadata setIdProperty(String idProperty) {
		this.put(META_ID_PROPERTY, idProperty);
		return this;
	}
	
	public String getTotalProperty() {
		return (String)this.get(META_TOTAL_PROPERTY);
	}
	
	public GridMetadata setTotalProperty(String totalProperty) {
		this.put(META_TOTAL_PROPERTY, totalProperty);
		return this;
	}
	
	public String getMessageProperty() {
		return (String)this.get(META_MESSAGE_PROPERTY);
	}

	public GridMetadata setMessageProperty(String messageProperty) {
		this.put(META_MESSAGE_PROPERTY, messageProperty);
		return this;
	}
	
	public Integer getTotalCount() {
		return (Integer)this.get(META_TOTAL_COUNT);
	}

	public GridMetadata setTotalCount(Integer totalCount) {
		this.put(META_TOTAL_COUNT, totalCount);
		return this;
	}

	public Object getSelected() {
		return (Object)this.get(META_SELECTED);
	}

	public GridMetadata setSelected(Object selected) {
		this.put(META_SELECTED, selected);
		return this;
	}
}