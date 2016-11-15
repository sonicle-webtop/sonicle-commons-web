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
package com.sonicle.commons.web.manager;

import com.sonicle.commons.PathUtils;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author malbinola
 */
public class TomcatManager {
	private final URI managerUri;
	private final String username;
	private final String password;
	
	public TomcatManager(String uri) throws URISyntaxException {
		this(new URI(uri));
	}
	
	public TomcatManager(URI uri) {
		this.managerUri = stripUserInfo(uri);
		String[] tokens = StringUtils.split(uri.getUserInfo(), ":", 2);
		this.username = (tokens.length > 0) ? tokens[0] : null;
		this.password = (tokens.length > 1) ? tokens[1] : null;
	}
	
	/*
	public static void main(String args[]) throws Exception {
		new TomcatManager("http://tomcat:tomcat@localhost:8084/manager/text").listDeployedApplications();
	}
	*/
	
	public void testConnection() throws Exception {
		doGetRequest("test");
	}
	
	public List<DeployedApp> listDeployedApplications() throws Exception {
		return listDeployedApplications(null);
	}
	
	public List<DeployedApp> listDeployedApplications(String name) throws Exception {
		HttpResponse response = doGetRequest("list");
		List<String> lines = IOUtils.readLines(response.getEntity().getContent(), "UTF-8");
		checkResponse(lines);
		
		ArrayList<DeployedApp> apps = new ArrayList<>();
		for(int i=1; i<lines.size(); i++) {
			final DeployedApp da = new DeployedApp(lines.get(i));
			if(name == null) {
				apps.add(da);
			} else {
				if(StringUtils.startsWith(da.name, name)) apps.add(da);
			}
		}
		return apps;
	}
	
	private void checkResponse(List<String> lines) throws Exception {
		if(lines.isEmpty()) throw new Exception("Bad response");
		if(!StringUtils.startsWith(lines.get(0), "OK")) throw new Exception(lines.get(0));
	}
	
	private HttpResponse doGetRequest(String commandUrl) throws IOException, Exception {
		HttpClient client = createClient();
		String uri = PathUtils.concatPaths(managerUri.toString(), commandUrl);
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		
		int status = response.getStatusLine().getStatusCode();
		if(status == 200) {
			return response;
		} else {
			if(status == 401 || status == 403) {
				throw new Exception("Login failed");
			} else if(status == 404) {
				throw new Exception("Tomcat Manager not found");
			} else {
				throw new Exception(response.getStatusLine().getReasonPhrase());
			}
		}
	}
	
	private HttpClient createClient() {
		String scheme = managerUri.getScheme();
		String host = managerUri.getHost();
		int port = (managerUri.getPort() == -1) ? 80 : managerUri.getPort();
		
		HttpHost targetHost = new HttpHost(host, port, scheme);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
		
		AuthCache authCache = new BasicAuthCache();
		authCache.put(targetHost, new BasicScheme());
		
		return HttpClientBuilder.create().setDefaultCredentialsProvider(credsProvider).build();
	}
	
	private URI stripUserInfo(URI uri) {
		try {
			return new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
		} catch(URISyntaxException ex) {
			return null;
		}
	}
	
	public static class DeployedApp {
		public String name;
		public String path;
		public String homeDirectory;
		public String status;
		public boolean isRunning;
		public int activeSessions;
		
		public DeployedApp(String line) {
			String[] tokens = StringUtils.split(line, ":", 4);
			name = FilenameUtils.getName(tokens[3]);
			path = tokens[0];
			homeDirectory = tokens[3];
			status = tokens[1];
			isRunning = StringUtils.equals(status, "running");
			activeSessions = Integer.valueOf(tokens[2]);
		}
	}
}
