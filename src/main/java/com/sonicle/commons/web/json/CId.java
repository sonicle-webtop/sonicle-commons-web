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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author malbinola
 */
public class CId {
	protected String separator;
	protected String[] tokens;
	
	public CId(String s) {
		this("|", s);
	}
	
	public CId(String s, int tokens) {
		this("|", s, tokens);
	}
	
	public CId(String separator, String s) {
		this.separator = separator;
		this.tokens = StringUtils.split(s, separator);
	}
	
	public CId(String separator, String s, int tokens) {
		this.separator = separator;
		this.tokens = StringUtils.split(s, separator, tokens);
	}
	
	protected CId(AbstractBuilder builder) {
		this((String[])builder.tokens.toArray(new String[builder.tokens.size()]), builder.separator);
	}
	
	protected CId(String[] tokens, String separator) {
		this.tokens = tokens;
		this.separator = separator;
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
	
	public boolean isTokenEmpty(int index) {
		if (!hasToken(index)) return true;
		return StringUtils.isEmpty(getToken(index));
	}
	
	public Collection<String> getTokens() {
		return (tokens == null) ? new ArrayList<>(0) : new ArrayList<>(Arrays.asList(tokens));
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
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(toString())
			.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CId == false) return false;
		if (this == obj) return true;
		final CId otherObject = (CId)obj;
		return new EqualsBuilder()
			.append(toString(), otherObject.toString())
			.isEquals();
	}
	
	private int indexOfNull() {
		for (int i=0; i<tokens.length; i++) {
			if (tokens[i] == null) return i;
		}
		return -1;
	}
	
	public static CId build(Object... tokens) {
		return new Builder()
				.withTokens(tokens)
				.build();
	}
	
	public static CId build(CId cid, Object... tokens) {
		return new Builder()
				.withTokens(cid, tokens)
				.build();
	}
	
	public static CId buildWithSeparator(String separator, Object... tokens) {
		return new Builder()
				.withSeparator(separator)
				.withTokens(tokens)
				.build();
	}
	
	public static CId build(Collection<String> tokens) {
		return new Builder()
				.withTokens(tokens)
				.build();
	}
	
	public static CId buildWithSeparator(String separator, Collection<String> tokens) {
		return new Builder()
				.withSeparator(separator)
				.withTokens(tokens)
				.build();
	}
	
	public static final class Builder extends AbstractBuilder<Builder, CId> {

		@Override
		public CId build() {
			return new CId(this);
		}
	}
	
	public static abstract class AbstractBuilder<B extends AbstractBuilder, T extends CId> {
		private ArrayList<String> tokens = new ArrayList<>();
		private String separator = "|";
		
		public abstract T build();
		
		public B withSeparator(String separator) {
			this.separator = separator;
			return (B)this;
		}
		
		public B withTokens(Collection<String> tokens) {
			addTokens(tokens);
			return (B)this;
		}
		
		public B withTokens(Object... tokens) {
			addTokens(null, tokens);
			return (B)this;
		}
		
		public B withTokens(CId cid, Object... tokens) {
			addTokens(cid.getTokens(), tokens);
			return (B)this;
		}
		
		public B addToken(Object token) {
			this.tokens.add((token instanceof String) ? (String)token : String.valueOf(token));
			return (B)this;
		}
		
		private void addTokens(Collection<String> tokens1, Object... tokens2) {
			if (tokens1 != null) {
				for (String s : tokens1) addToken(s);
			}
			if (tokens2 != null) {
				for (Object o : tokens2) addToken(o);
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	/*
	public static class Builder<B extends Builder> {
		private ArrayList<String> tokens = new ArrayList<>();
		private String separator = "|";
		
		//public T build() {	
		//	((Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
		//	
		//	return (T)new CId(this);
		//}
		
		public CId build() {
			return new CId(this);
		}
		
		public B withSeparator(String separator) {
			this.separator = separator;
			return (B)this;
		}
		
		public B withTokens(Collection<String> tokens) {
			addTokens(tokens);
			return (B)this;
		}
		
		public B withTokens(Object... tokens) {
			addTokens(null, tokens);
			return (B)this;
		}
		
		public B withTokens(CId cid, Object... tokens) {
			addTokens(cid.getTokens(), tokens);
			return (B)this;
		}
		
		public B addToken(Object token) {
			this.tokens.add((token instanceof String) ? (String)token : String.valueOf(token));
			return (B)this;
		}
		
		private void addTokens(Collection<String> tokens1, Object... tokens2) {
			if (tokens1 != null) {
				for (String s : tokens1) addToken(s);
			}
			if (tokens2 != null) {
				for (Object o : tokens2) addToken(o);
			}
		}
	}
	*/
}
