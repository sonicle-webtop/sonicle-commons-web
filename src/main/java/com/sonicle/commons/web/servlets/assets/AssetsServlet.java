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

import com.sonicle.commons.ResourceUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.CRC32;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;

/**
 *
 * @author malbinola
 * https://github.com/dropwizard/dropwizard/blob/v2.0.25/dropwizard-servlets/src/main/java/io/dropwizard/servlets/assets/AssetServlet.java
 * https://stackoverflow.com/questions/37196465/how-to-serve-static-content-and-resource-at-same-base-url-with-grizzly
 */
public class AssetsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String INIT_PARAM_RESOURCEPATH = "resourcePath";
	public static final String INIT_PARAM_URIPATH = "uriPath";
	public static final String INIT_PARAM_INDEXFILE = "indexFile";
	public static final String INIT_PARAM_DEFAULTMEDIATYPE = "defaultMediaType";
	public static final String INIT_PARAM_DEFAULTCHARSET = "defaultCharset";
	
	private static final String DEFAULT_MEDIA_TYPE = "text/html";
	protected String resourcePath;
	protected String uriPath;
	protected String indexFile;
	protected String defaultMediaType;
	protected Charset defaultCharset;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		final String trimmedPath = trimSlashes(this.getInitParameter(INIT_PARAM_RESOURCEPATH));
		this.resourcePath = StringUtils.isEmpty(trimmedPath) ? trimmedPath : trimmedPath + "/";
		final String trimmedUri = trimTrailingSlashes(this.getInitParameter(INIT_PARAM_URIPATH));
		this.uriPath = trimmedUri.isEmpty() ? "/" : trimmedUri;
		
		String pindex = this.getInitParameter(INIT_PARAM_INDEXFILE);
		this.indexFile = StringUtils.isBlank(pindex) ? null : pindex;
		String pmtype = this.getInitParameter(INIT_PARAM_DEFAULTMEDIATYPE);
		this.defaultMediaType = StringUtils.isBlank(pmtype)? DEFAULT_MEDIA_TYPE : defaultMediaType;
		String pcharset = this.getInitParameter(INIT_PARAM_DEFAULTCHARSET);
		this.defaultCharset = StringUtils.isBlank(pcharset) ? null : Charset.forName(pcharset);
	}
	
	public URL getResourceURL() {
		return ResourceUtils.getResource(resourcePath);
	}
	
	public String getUriPath() {
		return uriPath;
	}
	
	public String getIndexFile() {
		return indexFile;
	}
	
	public String getDefaultMediaType() {
		return defaultMediaType;
	}
	
	protected String buildAssetPath(HttpServletRequest request) {
		final StringBuilder builder = new StringBuilder(request.getServletPath());
		if (request.getPathInfo() != null) {
			builder.append(request.getPathInfo());
		}
		return builder.toString();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			// Replaced by buildAssetPath call that allow overrides!
			//final StringBuilder builder = new StringBuilder(req.getServletPath());
			//if (req.getPathInfo() != null) {
			//	builder.append(req.getPathInfo());
			//}
			final CachedAsset cachedAsset = loadAsset(buildAssetPath(req));
			if (cachedAsset == null) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			if (isCachedClientSide(req, cachedAsset)) {
				resp.sendError(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}

			final String rangeHeader = req.getHeader(HttpHeaders.RANGE);

			final int resourceLength = cachedAsset.getResource().length;
			List<ByteRange> ranges = Collections.emptyList();

			boolean usingRanges = false;
			// Support for HTTP Byte Ranges
			// http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
			if (rangeHeader != null) {

				final String ifRange = req.getHeader(HttpHeaders.IF_RANGE);

				if (ifRange == null || cachedAsset.getETag().equals(ifRange)) {

					try {
						ranges = parseRangeHeader(rangeHeader, resourceLength);
					} catch (NumberFormatException e) {
						resp.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
						return;
					}

					if (ranges.isEmpty()) {
						resp.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
						return;
					}

					resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
					usingRanges = true;

					final String byteRanges = ranges.stream()
							.map(ByteRange::toString)
							.collect(Collectors.joining(","));
					resp.addHeader(HttpHeaders.CONTENT_RANGE, "bytes " + byteRanges + "/" + resourceLength);
				}
			}

			resp.setDateHeader(HttpHeaders.LAST_MODIFIED, cachedAsset.getLastModifiedTime());
			resp.setHeader(HttpHeaders.ETAG, cachedAsset.getETag());

			final String mediaType = Optional.ofNullable(req.getServletContext().getMimeType(req.getRequestURI()))
					.orElse(defaultMediaType);
			if (mediaType.startsWith("video") || mediaType.startsWith("audio") || usingRanges) {
				resp.addHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
			}

			resp.setContentType(mediaType);
			if (defaultCharset != null) {
				resp.setCharacterEncoding(defaultCharset.toString());
			}

			try (ServletOutputStream output = resp.getOutputStream()) {
				if (usingRanges) {
					for (ByteRange range : ranges) {
						output.write(cachedAsset.getResource(), range.getStart(), range.getEnd() - range.getStart() + 1);
					}
				} else {
					output.write(cachedAsset.getResource());
				}
			}
		} catch (RuntimeException | URISyntaxException ignored) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	private CachedAsset loadAsset(String key) throws URISyntaxException, IOException {
		if (!key.startsWith(uriPath)) {
			throw new IllegalArgumentException("Cache key must start with " + uriPath);
		}

		final String requestedResourcePath = trimSlashes(key.substring(uriPath.length()));
		final String absoluteRequestedResourcePath = trimSlashes(this.resourcePath + requestedResourcePath);

		URL requestedResourceURL = getResourceUrl(absoluteRequestedResourcePath);
		if (ResourceURL.isDirectory(requestedResourceURL)) {
			if (indexFile != null) {
				requestedResourceURL = getResourceUrl(absoluteRequestedResourcePath + '/' + indexFile);
			} else {
				// directory requested but no index file defined
				return null;
			}
		}

		long lastModified = ResourceURL.getLastModified(requestedResourceURL);
		if (lastModified < 1) {
			// Something went wrong trying to get the last modified time: just use the current time
			lastModified = System.currentTimeMillis();
		}

		// zero out the millis since the date we get back from If-Modified-Since will not have them
		lastModified = (lastModified / 1000) * 1000;
		return new CachedAsset(readResource(requestedResourceURL), lastModified);
	}
	
	protected URL getResourceUrl(String absoluteRequestedResourcePath) {
		return ResourceUtils.getResource(absoluteRequestedResourcePath);
	}
	
	protected byte[] readResource(URL requestedResourceURL) throws IOException {
		return ResourceUtils.toByteArray(requestedResourceURL);
	}
	
	private boolean isCachedClientSide(HttpServletRequest req, CachedAsset cachedAsset) {
		return cachedAsset.getETag().equals(req.getHeader(HttpHeaders.IF_NONE_MATCH)) || (req.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE) >= cachedAsset.getLastModifiedTime());
	}
	
	/**
	 * Parses a given Range header for one or more byte ranges.
	 * @param rangeHeader Range header to parse
	 * @param resourceLength Length of the resource in bytes
	 * @return List of parsed ranges
	 */
	private List<ByteRange> parseRangeHeader(final String rangeHeader, final int resourceLength) {
		final List<ByteRange> byteRanges;
		if (rangeHeader.contains("=")) {
			final String[] parts = rangeHeader.split("=", -1);
			if (parts.length > 1) {
				byteRanges = Arrays.stream(parts[1].split(",", -1))
						.map(String::trim)
						.map(s -> ByteRange.parse(s, resourceLength))
						.collect(Collectors.toList());
			} else {
				byteRanges = Collections.emptyList();
			}
		} else {
			byteRanges = Collections.emptyList();
		}
		return byteRanges;
	}
	
	private static String trimSlashes(String s) {
		final Matcher matcher = Pattern.compile("^/*(.*?)/*$").matcher(s);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return s;
		}
	}

	private static String trimTrailingSlashes(String s) {
		final Matcher matcher = Pattern.compile("(.*?)/*$").matcher(s);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return s;
		}
	}
	
	private static class CachedAsset {
		private final byte[] resource;
		private final String eTag;
		private final long lastModifiedTime;

		private CachedAsset(byte[] resource, long lastModifiedTime) {
			this.resource = resource;
			this.eTag = '"' + hash(resource) + '"';
			this.lastModifiedTime = lastModifiedTime;
		}

		private static String hash(byte[] resource) {
			final CRC32 crc32 = new CRC32();
			crc32.update(resource);
			return Long.toHexString(crc32.getValue());
		}

		public byte[] getResource() {
			return resource;
		}

		public String getETag() {
			return eTag;
		}

		public long getLastModifiedTime() {
			return lastModifiedTime;
		}
	}
}
