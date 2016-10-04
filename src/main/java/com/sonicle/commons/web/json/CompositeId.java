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

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class CompositeId {
	private String[] tokens;
	private String separator = "|";
	
	public CompositeId() {
		tokens = new String[2];
	}
	
	public CompositeId(int maxTokens) {
		tokens = new String[maxTokens];
	}
	
	public CompositeId(Object... tokens) {
		this(tokens.length);
		setTokens(tokens);
	}
	
	public CompositeId parse(String s) {
		return parse(s, false);
	}
	
	public CompositeId parse(String s, boolean keepSize) {
		String[] split = null;
		if(keepSize) {
			split = StringUtils.split(s, separator, tokens.length);
			for(int i=0; i<tokens.length; i++) {
				tokens[i] = (i < split.length) ? split[i] : null;
			}
		} else {
			split = StringUtils.split(s, separator);
		}
		tokens = split;
		return this;
	}
	
	public int getSize() {
		return tokens.length;
	}
	
	public boolean hasToken(int index) {
		return index < tokens.length;
	}
	
	public CompositeId setToken(int index, String value) {
		tokens[index] = value;
		return this;
	}
	
	public String getToken(int index) {
		return tokens[index];
	}
	
	public CompositeId setTokens(Object... tokens) {
		for(int i=0; i<tokens.length; i++) {
			this.tokens[i] = (tokens[i] != null) ? tokens[i].toString() : null;
		}
		return this;
	}
	
	@Override
	public String toString() {
		return StringUtils.join(tokens, "|");
	}
	
	public String toString(boolean compress) {
		if(!compress) {
			return this.toString();
		} else {
			int nullIndex = indexOfNull();
			if(nullIndex == -1) {
				return StringUtils.join(tokens, separator);
			} else {
				return StringUtils.join(tokens, separator, 0, nullIndex);
			}
		}
	}
	
	private int indexOfNull() {
		for(int i=0; i<tokens.length; i++) {
			if(tokens[i] == null) return i;
		}
		return -1;
	}
	
	
	/*
	private String[] tokens;
	
	public CompositeId() {
		tokens = new String[2];
	}
	
	public CompositeId(Object... tokens) {
		this.tokens = new String[tokens.length];
		for(int i=0; i<tokens.length; i++) {
			if(tokens[i] != null) setToken(i, tokens[i].toString());
		}
	}
	return this;
	public CompositeId parse(String value) {
		tokens = StringUtils.split(value, "|");
		return this;
	}
	
	public CompositeId parse(String value, int howManyTokens) {
		tokens = StringUtils.split(value, "|", howManyTokens);
		return this;
	}
	
	public int getHowManyTokens() {
		return tokens.length;
	}
	
	public void setHowManyTokens(int value) {
		tokens = new String[value];
	}
	
	public CompositeId setToken(int index, String value) {
		tokens[index] = value;
		return this;
	}
	
	public String getToken(int index) {
		return tokens[index];
	}
	
	@Override
	public String toString() {
		return StringUtils.join(tokens, "|");
	}
	
	public String toString(boolean compress) {
		if(!compress) {
			return this.toString();
		} else {
			
		}
	}
	*/
}
