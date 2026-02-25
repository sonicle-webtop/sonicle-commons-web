/*
 * Copyright (C) 2026 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2026 Sonicle S.r.l.".
 */
package com.sonicle.commons.web;

import com.sonicle.commons.LangUtils;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import net.sf.qualitycheck.Check;
import org.apache.http.HttpHeaders;

/**
 * https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Cache-Control#directives
 * @author malbinola
 */
public class CacheControl {
	
	public static Configuration configure() {
		return new Configuration();
	}
	
	public static void cacheNotAllowed(final HttpServletResponse response) {
		new Configuration()
			.withMode(Mode.NOT_ALLOWED)
			.apply(response);
	}
	
	public static class Configuration {
		private boolean http10Compatible;
		private Mode mode = Mode.NOT_ALLOWED;
		private Type type = Type.PRIVATE;
		private int maxAge = -1;
		private int sharedMaxAge = -1;
		private boolean freshImmutable = false;
		private boolean allowReuseStaleResponses = true;
		private boolean sharedAllowReuseStaleResponses = true;
		
		public Configuration() {
			this(false);
		}
		
		public Configuration(boolean http10Compatible) {
			this.http10Compatible = http10Compatible;
		}
		
		public Configuration withMode(Mode mode) {
			this.mode = Check.notNull(mode, "mode");
			return this;
		}
		
		public Configuration withType(Type type) {
			this.type = Check.notNull(type, "type");
			return this;
		}
		
		public Configuration withMaxAgeSeconds(int maxAgeSeconds) {
			this.maxAge = Check.greaterOrEqualThan(-1, maxAgeSeconds, "maxAgeSeconds");
			return this;
		}
		
		public Configuration withMaxAgeDays(int maxAgeDays) {
			return withMaxAgeSeconds(maxAgeDays * 60 * 60 * 24);
		}
		
		public Configuration withSharedMaxAgeSeconds(int sharedMaxAgeSeconds) {
			this.sharedMaxAge = Check.greaterOrEqualThan(-1, sharedMaxAgeSeconds, "sharedMaxAgeSeconds");
			return this;
		}
		
		public Configuration sharedMaxMaxAgeDays(int sharedMaxAgeDays) {
			return withSharedMaxAgeSeconds(sharedMaxAgeDays * 60 * 60 * 24);
		}
		
		public Configuration withFreshImmutable(boolean freshImmutable) {
			this.freshImmutable = freshImmutable;
			return this;
		}
		
		public Configuration withAllowReuseStaleResponses(boolean allowReuseStaleResponses) {
			this.allowReuseStaleResponses = allowReuseStaleResponses;
			return this;
		}
		
		public Configuration withSharedAllowReuseStaleResponses(boolean sharedAllowReuseStaleResponses) {
			this.sharedAllowReuseStaleResponses = sharedAllowReuseStaleResponses;
			return this;
		}
		
		public void apply(final HttpServletResponse response) {
			ArrayList<String> directives = new ArrayList<>();
			
			if (Mode.NOT_ALLOWED.equals(mode)) {
				directives.add("no-store");
				directives.add("max-age=0"); // Be conservative: prevents reuse even if some intermediaries ignore no-store!
				directives.add("must-revalidate"); // Be conservative: force revalidation even in borderline scenarios!
				if (http10Compatible) {	
					response.setHeader("Pragma", "no-cache");
					response.setHeader("Expires", "0");
				}
			} else {
				if (Type.PRIVATE.equals(type)) {
					directives.add("private");
				} else if (Type.PUBLIC.equals(type)) {
					directives.add("public");
				}
				if (Mode.ALWAYS_VALIDATE.equals(mode)) {
					if (http10Compatible) {
						directives.add("max-age=0");
						directives.add("must-revalidate");
					} else {
						directives.add("no-cache");
					}
				} else {
					if (maxAge > -1) {
						directives.add("max-age=" + String.valueOf(maxAge));
						if (freshImmutable) directives.add("immutable");
						if (!allowReuseStaleResponses) directives.add("must-revalidate");
					}
					if (sharedMaxAge > -1) {
						directives.add("s-maxage=" + String.valueOf(sharedMaxAge));
						if (!sharedAllowReuseStaleResponses) directives.add("proxy-revalidate");
					}
				}
			}
			response.setHeader(HttpHeaders.CACHE_CONTROL, LangUtils.joinStrings(", ", directives));
		}
	}
	
	public static enum Mode {
		ALLOWED, NOT_ALLOWED, ALWAYS_VALIDATE;
	}
	
	public static enum Type {
		PRIVATE, PUBLIC;
	}
}
