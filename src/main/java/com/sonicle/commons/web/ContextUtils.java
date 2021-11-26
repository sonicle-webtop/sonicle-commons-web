/*
 * Copyright (C) 2017 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2017 Sonicle S.r.l.".
 */
package com.sonicle.commons.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.Manifest;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class ContextUtils {
	
	/**
	 * Returns the real context name of the passed servletContext, as it 
	 * as it appears on file-system.
	 * @param context The servlet context.
	 * @return The context name.
	 */
	public static String getWebappFullName(ServletContext context) {
		return getWebappFullName(context, false);
	}
	
	/**
	 * Returns the real context name of the passed servletContext, as it 
	 * as it appears on file-system.
	 * @param context The servlet context.
	 * @param stripVersion True to strip version info, false otherwise.
	 * @return The context name.
	 */
	public static String getWebappFullName(ServletContext context, boolean stripVersion) {
		final String name = new File(context.getRealPath("/")).getName();
		return stripVersion ? StringUtils.substringBeforeLast(name, "##") : name;
	}
	
	public static String getWebappVersion(ServletContext context) {
		return getWebappVersion(getWebappFullName(context));
	}
	
	public static String getWebappVersion(String contextName) {
		return StringUtils.substringAfterLast(contextName, "##");
	}
	
	public static String stripWebappVersion(String contextName) {
		return StringUtils.substringBeforeLast(contextName, "##");
	}
	
	/**
	 * Returns, if present, the object representation of Manifest (/META-INF/MANIFEST.MF) file.
	 * @param context The ServletContext
	 * @return Manifest object
	 * @throws IOException 
	 */
	public static Manifest getManifest(ServletContext context) throws IOException {
		final URL url = context.getResource("/META-INF/MANIFEST.MF");
		InputStream is = null;
		try {
			is = url.openStream();
			return new Manifest(is);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
}
