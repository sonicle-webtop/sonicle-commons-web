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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author malbinola
 */
public class ExtManifest {
	public String name;
	public String framework;
	public String theme;
	public String id;
	public Map<String, String> paths = new LinkedHashMap<>();
	public List<JsEntry> js = new ArrayList<>();
	public List<CssEntry> css = new ArrayList<>();
	public List<ResourceEntry> resources = new ArrayList<>();
	public Loader loader;
	
	public ExtManifest() {
		this.loader = new Loader(true);
	}
	
	public void addPath(String namespace, String path) {
		this.paths.put(namespace, path);
	}
	
	public void addJs(String path) {
		this.js.add(new JsEntry(path));
	}
	
	public void addCss(String path) {
		this.css.add(new CssEntry(path));
	}
	
	public void addResource(String path) {
		this.resources.add(new ResourceEntry(path));
	}
	
	public static class JsEntry {
		public String path;
		
		public JsEntry(String path) {
			this.path = path;
		}
	}
	
	public static class CssEntry {
		public String path;
		
		public CssEntry(String path) {
			this.path = path;
		}
	}
	
	public static class ResourceEntry {
		public String path;
		
		public ResourceEntry(String path) {
			this.path = path;
		}
	}
	
	public static class Loader {
		public Object cache;
		
		public Loader(boolean allowCaching) {
			this.cache = allowCaching;
		}
		
		public Loader(String buster) {
			this.cache = buster;
		}
	}
}
