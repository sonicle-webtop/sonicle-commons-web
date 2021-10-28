/*
 * Copyright (C) 2021 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2021 Sonicle S.r.l.".
 */
package com.sonicle.commons.web.servlets.assets;

import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author malbinola
 * https://github.com/dropwizard/dropwizard/blob/v2.0.25/dropwizard-servlets/src/main/java/io/dropwizard/servlets/assets/ByteRange.java
 */
@Immutable
public final class ByteRange {
	private final int start;
	private final int end;
	
	public ByteRange(final int start, final int end) {
		this.start = start;
		this.end = end;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}
	
	public static ByteRange parse(final String byteRange, final int resourceLength) {
		// missing separator
		if (!byteRange.contains("-")) {
			final int start = Integer.parseInt(byteRange);
			return new ByteRange(start, resourceLength - 1);
		}
		
		// negative range
		if (byteRange.indexOf("-") == 0) {
			final int start = Integer.parseInt(byteRange);
			return new ByteRange(resourceLength + start, resourceLength - 1);
		}
		
		final String[] parts = byteRange.split("-");
		if (parts.length == 2) {
			final int start = Integer.parseInt(parts[0]);
			int end = Integer.parseInt(parts[1]);
			if (end > resourceLength) {
				end = resourceLength - 1;
			}
			return new ByteRange(start, end);
			
		} else {
			final int start = Integer.parseInt(parts[0]);
			return new ByteRange(start, resourceLength - 1);
		}
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(start)
			.append(end)
			.toHashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj instanceof ByteRange == false) return false;
		if(this == obj) return true;
		final ByteRange otherObject = (ByteRange)obj;
		return new EqualsBuilder()
			.append(start, otherObject.start)
			.append(end, otherObject.end)
			.isEquals();
	}
	
	@Override
	public String toString() {
		return String.format("%d-%d", start, end);
	}
}
