/*
 * Copyright (C) 2020 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2020 Sonicle S.r.l.".
 */
package com.sonicle.commons.web.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class CompId {
	private String separator = "|";
	private String[] tokens;
	
	public CompId() {
		this(2);
	}
	
	public CompId(int size) {
		tokens = new String[size];
	}
	
	public CompId(Collection<String> tokens) {
		//this((tokens != null) ? tokens.size() : 0);
		//setTokens(tokens);
		applyTokens(tokens);
	}
	
	public CompId(Object... tokens) {
		this((CompId)null, tokens);
	}
	
	public CompId(CompId cid, Object... tokens) {
		//this(((cid != null) ? cid.getTokens().size() : 0) + ((tokens != null) ? tokens.length : 0));
		//setTokens(cid != null ? cid.getTokens() : null, tokens);
		applyTokens(cid != null ? cid.getTokens() : null, tokens);
	}
	
	public CompId withSep(String separator) {
		this.separator = separator;
		return this;
	}
	
	public CompId withTokens(Collection<String> tokens) {
		applyTokens(tokens);
		return this;
	}
	
	public CompId withTokens(Object... tokens) {
		return withTokens((CompId)null, tokens);
	}
	
	public CompId withTokens(CompId cid, Object... tokens) {
		applyTokens(cid.getTokens(), tokens);
		return this;
	}
	
	public int getSize() {
		return tokens.length;
	}
	
	public boolean hasToken(int index) {
		return (index > -1) && (index < tokens.length);
	}
	
	public String getToken(int index) {
		return tokens[index];
	}
	
	public Collection<String> getTokens() {
		return (tokens == null) ? new ArrayList<>(0) : new ArrayList<>(Arrays.asList(tokens));
	}
	
	public CompId setToken(int index, Object value) {
		if (hasToken(index)) {
			tokens[index] = (value instanceof String) ? (String)value : String.valueOf(value);
		}
		return this;
	}
	
	public CompId parse(String s) {
		return parse(s, true);
	}
	
	public CompId parse(String s, boolean adaptSize) {
		tokens = StringUtils.split(s, separator, adaptSize ? -1 : tokens.length);
		return this;
	}
	
	@Override
	public String toString() {
		return StringUtils.join(tokens, separator);
	}
	
	public String toString(boolean compress) {
		if (!compress) {
			return toString();
		
		} else {
			int nullIndex = indexOfNull();
			if (nullIndex == -1) {
				return StringUtils.join(tokens, separator);
			} else {
				return StringUtils.join(tokens, separator, 0, nullIndex);
			}
		}
	}
	
	private int indexOfNull() {
		for (int i=0; i<tokens.length; i++) {
			if (tokens[i] == null) return i;
		}
		return -1;
	}
	
	private void applyTokens(Collection<String> tokens1, Object... tokens2) {
		int size = ((tokens1 != null) ? tokens1.size() : 0) + ((tokens2 != null) ? tokens2.length : 0);
		tokens = new String[size];
		
		int i = -1;
		if (tokens1 != null) {
			for (String s : tokens1) {
				i++;
				if (i >= tokens.length) break;
				tokens[i] = s;
			}
		}
		if (tokens2 != null) {
			for (Object o : tokens2) {
				i++;
				if (i >= tokens.length) break;
				tokens[i] = (o instanceof String) ? (String)o : String.valueOf(o);
			}
			
		}
	}
	
	/*
	public CompositeId setTokens(Collection<String> tokens) {
		int i = -1;
		for (String token : tokens) {
			i++;
			this.tokens[i] = token;
		}
		return this;
	}
	
	public CompositeId setTokens(Object... tokens) {
		for(int i=0; i<tokens.length; i++) {
			this.tokens[i] = (tokens[i] != null) ? tokens[i].toString() : null;
		}
		return this;
	}
	*/
}
