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
package com.sonicle.commons.web;

import com.sonicle.commons.URIUtils;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public abstract class URIPath {
	public final String[] tokens;
	
	public URIPath(String path, int howMany) throws MalformedURLException {
		this(StringUtils.split(URIUtils.removeTrailingSeparator(path), "/", howMany));
		if (tokens.length < howMany) {
			throw new MalformedURLException(MessageFormat.format("Invalid URL: at least {0} parts are required [{1}]", tokens, path));
		}
	}
	
	public URIPath(String[] tokens) {
		this.tokens = tokens;
	}
	
	public String getTokenAt(int index) {
		return (index < tokens.length) ? tokens[index] : null;
	}
	
	public String getRemainingPath() {
		return URIUtils.ensureLeadingSeparator(doGetRemainingPath());
	}
	
	protected abstract String doGetRemainingPath();
}
