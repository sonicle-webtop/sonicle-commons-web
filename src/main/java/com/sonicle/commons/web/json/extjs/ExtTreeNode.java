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

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author malbinola
 */
public class ExtTreeNode extends HashMap<String, Object> {
	
	public static final String ID_PROP = "id";
	public static final String TEXT_PROP = "text";
	public static final String LEAF_PROP = "leaf";
	public static final String ICON_CLASS_PROP = "iconCls";
	public static final String TOOLTIP_PROP = "qtip";
	public static final String EXPANDED_PROP = "expanded";
	public static final String LOADED_PROP = "loaded";
	public static final String CHECKED_PROP = "checked";
	public static final String CHILDREN_PROP = "children";
	
	public ExtTreeNode() {
		
	}
	
	public ExtTreeNode(Object id, String text, boolean leaf) {
		this(id, text, leaf, null, null);
	}
	
	public ExtTreeNode(Object id, String text, boolean leaf, String iconClass) {
		this(id, text, leaf, iconClass, null);
	}
	
	public ExtTreeNode(Object id, String text, boolean leaf, String iconClass, String tooltip) {
		setId(id);
		setText(text);
		setLeaf(leaf);
		setIconClass(iconClass);
		setTooltip(tooltip);
	}
	
	public Object getId() {
		return this.get(ID_PROP);
	}
	
	public final ExtTreeNode setId(Object value) {
		this.put(ID_PROP, value);
		return this;
	}
	
	public String getText() {
		return (String)this.get(TEXT_PROP);
	}
	
	public final ExtTreeNode setText(String value) {
		this.put(TEXT_PROP, value);
		return this;
	}
	
	public Boolean getLeaf() {
		return (Boolean)this.get(LEAF_PROP);
	}
	
	public final ExtTreeNode setLeaf(boolean value) {
		this.put(LEAF_PROP, value);
		if(value) this.remove(CHILDREN_PROP);
		return this;
	}
	
	public String getIconClass() {
		return (String)this.get(ICON_CLASS_PROP);
	}
	
	public final ExtTreeNode setIconClass(String value) {
		this.put(ICON_CLASS_PROP, value);
		return this;
	}
	
	public String getTooltip() {
		return (String)this.get(TOOLTIP_PROP);
	}
	
	public final ExtTreeNode setTooltip(String value) {
		this.put(TOOLTIP_PROP, value);
		return this;
	}
	
	public Boolean getExpanded() {
		return (Boolean)this.get(EXPANDED_PROP);
	}
	
	public final ExtTreeNode setExpanded(boolean value) {
		if(value && !getLeaf()) this.put(EXPANDED_PROP, value);
		return this;
	}
	
	public Boolean getChecked() {
		return (Boolean)this.get(CHECKED_PROP);
	}
	
	public final ExtTreeNode setChecked(boolean value) {
		this.put(CHECKED_PROP, value);
		return this;
	}
	
	public Boolean getLoaded() {
		return (Boolean)this.get(LOADED_PROP);
	}
	
	public final ExtTreeNode setLoaded(boolean value) {
		if(value && !getLeaf()) this.put(LOADED_PROP, value);
		return this;
	}
	
	public ArrayList<ExtTreeNode> getChildren() {
		return (ArrayList<ExtTreeNode>)this.get(CHILDREN_PROP);
	}
	
	public final ExtTreeNode setChildren(ArrayList<ExtTreeNode> value) {
		this.put(CHILDREN_PROP, value);
		return this;
	}
}
