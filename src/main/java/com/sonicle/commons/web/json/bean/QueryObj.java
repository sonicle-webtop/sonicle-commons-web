/*
 * Copyright (C) 2019 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2019 Sonicle S.r.l.".
 */
package com.sonicle.commons.web.json.bean;

import com.sonicle.commons.web.json.JsonResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import net.sf.qualitycheck.Check;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author malbinola
 */
public class QueryObj {
	protected String allText;
	protected ArrayList<ConditionEntry> conditions;
	
	public QueryObj() {
		this.conditions = new ArrayList<>();
	}
	
	public String getAllText() {
		return allText;
	}
	
	public List<ConditionEntry> getConditions() {
		return Collections.unmodifiableList(conditions);
	}
	
	public boolean hasCondition(final String keyword) {
		Check.notNull(keyword, "keyword");
		if (conditions == null) return false;
		for (ConditionEntry condition : conditions) {
			if (keyword.equals(condition.keyword)) return true;
		}
		return false;
	}
	
	public boolean hasCondition(final String keyword, final String value) {
		Check.notNull(keyword, "keyword");
		if (conditions == null) return false;
		for (ConditionEntry condition : conditions) {
			if (keyword.equals(condition.keyword) && StringUtils.equals(condition.value, value)) return true;
		}
		return false;
	}
	
	public QueryObj addCondition(final String keyword, final String value, final boolean negated) {
		if (conditions != null) {
			conditions.add(new ConditionEntry(Check.notNull(keyword, "keyword"), value, negated));
		}
		return this;
	}
	
	public boolean removeCondition(final String keyword) {
		Check.notNull(keyword, "keyword");
		if (conditions == null) return false;
		return conditions.removeIf(c -> keyword.equals(c.keyword));
	}
	
	public boolean removeCondition(final String keyword, final String value) {
		Check.notNull(keyword, "keyword");
		if (conditions == null) return false;
		return conditions.removeIf(c -> keyword.equals(c.keyword) && StringUtils.equals(c.value, value));
	}
	
	/**
	 * Groups values by condition keyword
	 * @return Ordered Map that groups values by its ConditionEntry object.
	 */
	public Map<Condition, List<String>> groupConditions() {
		return groupConditions(null);
	}
	
	/**
	 * Groups values by condition keyword and optionally promotes values as keywords: 
	 * useful for those "verb" keywords (like 'is', 'has'), typically used for booleans, 
	 * where the real topic is in value.
	 * @param promoteValueAsKeywordFor Collection of keywords whose values will be promoted as keywords
	 * @return Ordered Map that groups values by its ConditionEntry object.
	 */
	public Map<Condition, List<String>> groupConditions(Collection<String> promoteValueAsKeywordFor) {
		HashSet<String> promoteMap = promoteValueAsKeywordFor != null ? new LinkedHashSet<>(promoteValueAsKeywordFor) : null;
		LinkedHashMap<Condition, List<String>> mvm = new LinkedHashMap<>();
		for (ConditionEntry condition : conditions) {
			Condition key = null;
			String value = null;
			
			if (promoteMap != null && promoteMap.contains(condition.keyword)) {
				key = new Condition(condition.value, condition.negated);
			} else {
				key = new Condition(condition.keyword, condition.negated);
				value = condition.value;
			}
			
			if (!mvm.containsKey(key)) mvm.put(key, new ArrayList<>());
			if (value != null) mvm.get(key).add(value);
		}
		return mvm;
	}
	
	/**
	 * @deprecated Use groupConditions() instead.
	 */
	@Deprecated
	public Map<String, Collection<ConditionEntry>> getConditionsMap() {
		LinkedHashMap<String, Collection<ConditionEntry>> mvm = new LinkedHashMap<>();
		for (ConditionEntry condition : conditions) {
			if (!mvm.containsKey(condition.keyword)) mvm.put(condition.keyword, new ArrayList<>());
			mvm.get(condition.keyword).add(condition);
		}
		return mvm;
	}
	
	public static class ConditionEntry {
		public String keyword;
		public String value;
		public boolean negated;
		
		public ConditionEntry() {}
		
		public ConditionEntry(String keyword, String value, boolean negated) {
			this.keyword = keyword;
			this.value = value;
			this.negated = negated;
		}
	}
	
	public static class Condition {
		public final String keyword;
		public final boolean negated;
		
		public Condition(String keyword, boolean negated) {
			this.keyword = keyword;
			this.negated = negated;
		}
		
		@Override
		public int hashCode() {
			return new HashCodeBuilder()
				.append(keyword)
				.toHashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Condition == false) return false;
			if (this == obj) return true;
			final Condition otherObject = (Condition)obj;
			return new EqualsBuilder()
				.append(keyword, otherObject.keyword)
				.isEquals();
		}
	}
	
	public static QueryObj fromJson(String value) {
		return JsonResult.gson().fromJson(value, QueryObj.class);
	}

	public static String toJson(QueryObj value) {
		return JsonResult.gson().toJson(value, QueryObj.class);
	}
}
