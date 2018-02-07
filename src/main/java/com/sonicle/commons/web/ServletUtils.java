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

import com.sonicle.commons.EnumUtils;
import com.sonicle.commons.web.json.PayloadAsListRecords;
import com.sonicle.commons.web.json.MapItem;
import com.sonicle.commons.web.json.Payload;
import com.sonicle.commons.web.json.PayloadAsList;
import com.sonicle.commons.LangUtils;
import com.sonicle.commons.net.IPUtils;
import com.sonicle.commons.validation.Validator;
import com.sonicle.commons.validation.ValidatorException;
import com.sonicle.commons.web.json.JsonResult;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.GZIPOutputStream;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.ContentDisposition;
import javax.mail.internet.ParseException;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbinola
 */
public class ServletUtils {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(ServletUtils.class);
	
	/**
	 * Note that gzipping is only beneficial for larger resources. 
	 * Due to the overhead and latency of compression and decompression, 
	 * you should only gzip files above a certain size threshold; 
	 * we recommend a minimum range between 150 and 1000 bytes. 
	 * Gzipping files below 150 bytes can actually make them larger.
	 */
	public static final int GZIP_MIN_THRESHOLD = 860;
	private static final Set<String> compressibleMediaTypes = new HashSet<>();
	
	static {
		compressibleMediaTypes.add("application/alto-costmap+json");
		compressibleMediaTypes.add("application/alto-costmapfilter+json");
		compressibleMediaTypes.add("application/alto-directory+json");
		compressibleMediaTypes.add("application/alto-endpointcost+json");
		compressibleMediaTypes.add("application/alto-endpointcostparams+json");
		compressibleMediaTypes.add("application/alto-endpointprop+json");
		compressibleMediaTypes.add("application/alto-endpointpropparams+json");
		compressibleMediaTypes.add("application/alto-error+json");
		compressibleMediaTypes.add("application/alto-networkmap+json");
		compressibleMediaTypes.add("application/alto-networkmapfilter+json");
		compressibleMediaTypes.add("application/atom+xml");
		compressibleMediaTypes.add("application/calendar+json");
		compressibleMediaTypes.add("application/coap-group+json");
		compressibleMediaTypes.add("application/csvm+json");
		compressibleMediaTypes.add("application/dart");
		compressibleMediaTypes.add("application/dicom+json");
		compressibleMediaTypes.add("application/ecmascript");
		compressibleMediaTypes.add("application/fido.trusted-apps+json");
		compressibleMediaTypes.add("application/geo+json");
		compressibleMediaTypes.add("application/javascript");
		compressibleMediaTypes.add("application/jf2feed+json");
		compressibleMediaTypes.add("application/jose+json");
		compressibleMediaTypes.add("application/jrd+json");
		compressibleMediaTypes.add("application/json");
		compressibleMediaTypes.add("application/json-patch+json");
		compressibleMediaTypes.add("application/jsonml+json");
		compressibleMediaTypes.add("application/jwk+json");
		compressibleMediaTypes.add("application/jwk-set+json");
		compressibleMediaTypes.add("application/ld+json");
		compressibleMediaTypes.add("application/manifest+json");
		compressibleMediaTypes.add("application/merge-patch+json");
		compressibleMediaTypes.add("application/mud+json");
		compressibleMediaTypes.add("application/postscript");
		compressibleMediaTypes.add("application/ppsp-tracker+json");
		compressibleMediaTypes.add("application/problem+json");
		compressibleMediaTypes.add("application/raml+yaml");
		compressibleMediaTypes.add("application/rdap+json");
		compressibleMediaTypes.add("application/rdf+xml");
		compressibleMediaTypes.add("application/reputon+json");
		compressibleMediaTypes.add("application/rss+xml");
		compressibleMediaTypes.add("application/rtf");
		compressibleMediaTypes.add("application/scim+json");
		compressibleMediaTypes.add("application/soap+xml");
		compressibleMediaTypes.add("application/tar");
		compressibleMediaTypes.add("application/vcard+json");
		compressibleMediaTypes.add("application/vnd.api+json");
		compressibleMediaTypes.add("application/vnd.apothekende.reservation+json");
		compressibleMediaTypes.add("application/vnd.avalon+json");
		compressibleMediaTypes.add("application/vnd.bekitzur-stech+json");
		compressibleMediaTypes.add("application/vnd.capasystems-pg+json");
		compressibleMediaTypes.add("application/vnd.collection+json");
		compressibleMediaTypes.add("application/vnd.collection.doc+json");
		compressibleMediaTypes.add("application/vnd.collection.next+json");
		compressibleMediaTypes.add("application/vnd.coreos.ignition+json");
		compressibleMediaTypes.add("application/vnd.dart");
		compressibleMediaTypes.add("application/vnd.datapackage+json");
		compressibleMediaTypes.add("application/vnd.dataresource+json");
		compressibleMediaTypes.add("application/vnd.document+json");
		compressibleMediaTypes.add("application/vnd.drive+json");
		compressibleMediaTypes.add("application/vnd.geo+json");
		compressibleMediaTypes.add("application/vnd.google-earth.kml+xml");
		compressibleMediaTypes.add("application/vnd.hal+json");
		compressibleMediaTypes.add("application/vnd.hc+json");
		compressibleMediaTypes.add("application/vnd.heroku+json");
		compressibleMediaTypes.add("application/vnd.hyper-item+json");
		compressibleMediaTypes.add("application/vnd.hyperdrive+json");
		compressibleMediaTypes.add("application/vnd.ims.lis.v2.result+json");
		compressibleMediaTypes.add("application/vnd.ims.lti.v2.toolconsumerprofile+json");
		compressibleMediaTypes.add("application/vnd.ims.lti.v2.toolproxy+json");
		compressibleMediaTypes.add("application/vnd.ims.lti.v2.toolproxy.id+json");
		compressibleMediaTypes.add("application/vnd.ims.lti.v2.toolsettings+json");
		compressibleMediaTypes.add("application/vnd.ims.lti.v2.toolsettings.simple+json");
		compressibleMediaTypes.add("application/vnd.las.las+json");
		compressibleMediaTypes.add("application/vnd.mason+json");
		compressibleMediaTypes.add("application/vnd.micro+json");
		compressibleMediaTypes.add("application/vnd.miele+json");
		compressibleMediaTypes.add("application/vnd.mozilla.xul+xml");
		compressibleMediaTypes.add("application/vnd.ms-fontobject");
		compressibleMediaTypes.add("application/vnd.ms-opentype");
		compressibleMediaTypes.add("application/vnd.nearst.inv+json");
		compressibleMediaTypes.add("application/vnd.oftn.l10n+json");
		compressibleMediaTypes.add("application/vnd.oma.lwm2m+json");
		compressibleMediaTypes.add("application/vnd.oracle.resource+json");
		compressibleMediaTypes.add("application/vnd.pagerduty+json");
		compressibleMediaTypes.add("application/vnd.siren+json");
		compressibleMediaTypes.add("application/vnd.sun.wadl+xml");
		compressibleMediaTypes.add("application/vnd.tableschema+json");
		compressibleMediaTypes.add("application/vnd.vel+json");
		compressibleMediaTypes.add("application/vnd.xacml+json");
		compressibleMediaTypes.add("application/wasm");
		compressibleMediaTypes.add("application/webpush-options+json");
		compressibleMediaTypes.add("application/x-httpd-php");
		compressibleMediaTypes.add("application/x-javascript");
		compressibleMediaTypes.add("application/x-ns-proxy-autoconfig");
		compressibleMediaTypes.add("application/x-sh");
		compressibleMediaTypes.add("application/x-tar");
		compressibleMediaTypes.add("application/x-virtualbox-hdd");
		compressibleMediaTypes.add("application/x-virtualbox-ova");
		compressibleMediaTypes.add("application/x-virtualbox-ovf");
		compressibleMediaTypes.add("application/x-virtualbox-vbox");
		compressibleMediaTypes.add("application/x-virtualbox-vdi");
		compressibleMediaTypes.add("application/x-virtualbox-vhd");
		compressibleMediaTypes.add("application/x-virtualbox-vmdk");
		compressibleMediaTypes.add("application/x-web-app-manifest+json");
		compressibleMediaTypes.add("application/x-www-form-urlencoded");
		compressibleMediaTypes.add("application/xhtml+xml");
		compressibleMediaTypes.add("application/xml");
		compressibleMediaTypes.add("application/xml-dtd");
		compressibleMediaTypes.add("application/xop+xml");
		compressibleMediaTypes.add("application/yang-data+json");
		compressibleMediaTypes.add("application/yang-patch+json");
		compressibleMediaTypes.add("font/otf");
		compressibleMediaTypes.add("image/bmp");
		compressibleMediaTypes.add("image/svg+xml");
		compressibleMediaTypes.add("image/vnd.adobe.photoshop");
		compressibleMediaTypes.add("image/x-icon");
		compressibleMediaTypes.add("image/x-ms-bmp");
		compressibleMediaTypes.add("message/imdn+xml");
		compressibleMediaTypes.add("message/rfc822");
		compressibleMediaTypes.add("model/gltf+json");
		compressibleMediaTypes.add("model/gltf-binary");
		compressibleMediaTypes.add("model/x3d+xml");
		compressibleMediaTypes.add("text/cache-manifest");
		compressibleMediaTypes.add("text/calender");
		compressibleMediaTypes.add("text/cmd");
		compressibleMediaTypes.add("text/css");
		compressibleMediaTypes.add("text/csv");
		compressibleMediaTypes.add("text/html");
		compressibleMediaTypes.add("text/javascript");
		compressibleMediaTypes.add("text/jsx");
		compressibleMediaTypes.add("text/markdown");
		compressibleMediaTypes.add("text/n3");
		compressibleMediaTypes.add("text/plain");
		compressibleMediaTypes.add("text/richtext");
		compressibleMediaTypes.add("text/rtf");
		compressibleMediaTypes.add("text/tab-separated-values");
		compressibleMediaTypes.add("text/uri-list");
		compressibleMediaTypes.add("text/vcard");
		compressibleMediaTypes.add("text/vtt");
		compressibleMediaTypes.add("text/x-gwt-rpc");
		compressibleMediaTypes.add("text/x-jquery-tmpl");
		compressibleMediaTypes.add("text/x-markdown");
		compressibleMediaTypes.add("text/x-org");
		compressibleMediaTypes.add("text/x-processing");
		compressibleMediaTypes.add("text/x-suse-ymp");
		compressibleMediaTypes.add("text/xml");
		compressibleMediaTypes.add("x-shader/x-fragment");
		compressibleMediaTypes.add("x-shader/x-vertex");
	}
	
	/**
	 * Explicitly update the lastAccessTime of the passed session.
	 * This method can be used to ensure a session does not time out.
	 * @param session The HTTP session
	 */
	public static void touchSession(HttpSession session) {
		try {
			Field field = session.getClass().getDeclaredField("session");
			field.setAccessible(true);
			HttpSession realSession = (HttpSession)field.get(session);
			realSession.getClass().getMethod("access").invoke(realSession);
		} catch(Throwable t) {
			logger.error("Error touching session", t);
		}
	}
	
	/**
	 * Returns the request URL until the servlet path (excluded).
	 * This URL may differ from the orginal browser URL due to rewrites
	 * (eg. Apache url_rewrite) potentially applied in the chain.
	 * @param request The HttpServletRequest object.
	 * @return 
	 */
	public static String getRequestURL(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		int iof = url.indexOf(request.getServletPath());
		return (iof == -1) ? url.toString() : url.substring(0, iof);
	}
	
	public static boolean isForwarded(HttpServletRequest request) {
		return DispatcherType.FORWARD.equals(request.getDispatcherType());
	}
	
	public static String getHost(HttpServletRequest request) throws MalformedURLException {
		return new URL(request.getRequestURL().toString()).getHost();
	}
	
	public static String getInternetName(HttpServletRequest request) throws MalformedURLException {
		String host = getHost(request);
		int ix1 = host.indexOf('.');
		int ix2 = host.lastIndexOf('.');
		return (ix1 == ix2) ? host : host.substring(ix1 + 1);
	}
	
	public static String getContextPath(HttpServletRequest request) {
		//TODO: controllare se il metodo funziona anche con il proxypass
		String uri = StringUtils.defaultString(request.getRequestURI());
		return uri.substring(0, uri.indexOf(request.getServletPath()));
	}
	
	/**
	 * Gets request's attribute value.
	 * @param request The HttpServletRequest.
	 * @param name Attribute name.
	 * @return Value as string.
	 */
	public static String getStringAttribute(ServletRequest request, String name) {
		return getStringAttribute(request, name, null);
	}
	
	/**
	 * Gets request's attribute value.
	 * @param request The HttpServletRequest.
	 * @param name Attribute name.
	 * @param defaultValue Attribute defaultValue.
	 * @return Value as string.
	 */
	public static String getStringAttribute(ServletRequest request, String name, String defaultValue) {
		String value = String.valueOf(request.getAttribute(name));
		return LangUtils.value(value, defaultValue);
	}
	
	/**
	 * Gets request's parameter value.
	 * @param request The HttpServletRequest.
	 * @param name Parameter name.
	 * @param defaultValue Desired defaultValue if undefined.
	 * @return Value as String.
	 */
	public static String getStringParameter(ServletRequest request, String name, String defaultValue) {
		try {
			return ServletUtils.getStringParameter(request, name, true);
		} catch(Exception ex) {
			return defaultValue;
		}
	}
	
	/**
	 * Gets request's parameter value.
	 * @param request The HttpServletRequest.
	 * @param name Parameter name.
	 * @param required True to generate an exception if undefined.
	 * @return Value as String.
	 * @throws java.lang.Exception
	 */
	public static String getStringParameter(ServletRequest request, String name, boolean required) throws Exception {
		return getStringParameter(request, name, required, true);
	}
	
	/**
	 * Gets request's parameter value.
	 * @param request The HttpServletRequest.
	 * @param name Parameter name.
	 * @param required True to generate an exception if undefined.
	 * @param emptyAsNull True to treat empty string as undefined.
	 * @return Value as String.
	 * @throws java.lang.Exception
	 */
	public static String getStringParameter(ServletRequest request, String name, boolean required, boolean emptyAsNull) throws Exception {
		try {
			String value = request.getParameter(name);
			return Validator.validateString(required, value, emptyAsNull);
		} catch(ValidatorException ex) {
			throw new Exception(MessageFormat.format("Error getting parameter [{0}]", name), ex);
		}
	}
	
	public static ArrayList<String> getStringParameterBySeparator(ServletRequest request, String name, String separator) throws Exception {
		try {
			String value = request.getParameter(name);
			String value2 = Validator.validateString(false, value, true);
			if(Validator.isNull(value2)) return new ArrayList<>();
			String[] values = StringUtils.split(value2, separator);
			return new ArrayList<>(Arrays.asList(values));
		} catch(ValidatorException ex) {
			throw new Exception(MessageFormat.format("Error getting parameter [{0}]", name), ex);
		}
	}
	
	public static ArrayList<String> getStringParameters(ServletRequest request, String name) throws Exception {
		try {
			String[] values = request.getParameterValues(name);
			if(Validator.isNull(values)) return new ArrayList<>();
			for(String value : values) {
				Validator.validateString(false, value, false);
			}
			return new ArrayList<>(Arrays.asList(values));
		} catch(ValidatorException ex) {
			throw new Exception(MessageFormat.format("Error getting parameter [{0}]", name), ex);
		}
	}
	
	/**
	 * Gets request's parameter value.
	 * @param request The HttpServletRequest.
	 * @param name Parameter name.
	 * @param defaultValue Desired defaultValue if undefined.
	 * @return Value as Integer.
	 */
	public static Integer getIntParameter(ServletRequest request, String name, Integer defaultValue) {
		try {
			return ServletUtils.getIntParameter(request, name, true);
		} catch(Exception ex) {
			return defaultValue;
		}
	}
	
	/**
	 * Gets request's parameter value.
	 * @param request The HttpServletRequest.
	 * @param name Parameter name.
	 * @param required True to generate an exception if undefined.
	 * @return Value as Integer.
	 * @throws java.lang.Exception
	 */
	public static Integer getIntParameter(ServletRequest request, String name, boolean required) throws Exception {
		try {
			String value = StringUtils.defaultIfBlank(request.getParameter(name), null);
			return Validator.validateInteger(required, value);
		} catch(ValidatorException ex) {
			throw new Exception(MessageFormat.format("Error getting parameter [{0}]", name), ex);
		}
	}
	
	/**
	 * Gets request's parameter value.
	 * @param request The HttpServletRequest.
	 * @param name Parameter name.
	 * @param defaultValue Desired defaultValue if undefined.
	 * @return Value as Long.
	 */

    public static Long getLongParameter(ServletRequest request, String name, Long defaultValue) {
		try {
			return ServletUtils.getLongParameter(request, name, true);
		} catch(Exception ex) {
			return defaultValue;
		}
	}
	
	/**
	 * Gets request's parameter value.
	 * @param request The HttpServletRequest.
	 * @param name Parameter name.
	 * @param required True to generate an exception if undefined.
	 * @return Value as Long.
	 * @throws java.lang.Exception
	 */
	public static Long getLongParameter(ServletRequest request, String name, boolean required) throws Exception {
		try {
			String value = StringUtils.defaultIfBlank(request.getParameter(name), null);
			return Validator.validateLong(required, value);
		} catch(ValidatorException ex) {
			throw new Exception(MessageFormat.format("Error getting parameter [{0}]", name), ex);
		}
	}

    /**
	 * Gets request's parameter value.
	 * @param request The HttpServletRequest.
	 * @param name Parameter name.
	 * @param defaultValue Desired defaultValue if undefined.
	 * @return Value as Boolean.
	 */
	public static Boolean getBooleanParameter(ServletRequest request, String name, Boolean defaultValue) {
		try {
			String value = StringUtils.defaultIfBlank(request.getParameter(name), null);
			return Validator.validateBoolean(true, value);
		} catch(ValidatorException ex) {
			return defaultValue;
		}
	}
	
	/**
	 * Gets request's parameter value.
	 * @param request The HttpServletRequest.
	 * @param name Parameter name.
	 * @param locale Locale to use for decimal format.
	 * @param defaultValue Desired defaultValue if undefined.
	 * @return Value as Float.
	 */
	public static Float getFloatParameter(ServletRequest request, String name, Locale locale, Float defaultValue) {
		try {
			return ServletUtils.getFloatParameter(request, name, locale, true);
		} catch(Exception ex) {
			return defaultValue;
		}
	}
	
	/**
	 * Gets request's parameter value.
	 * @param request The HttpServletRequest.
	 * @param name Parameter name.
	 * @param locale Locale to use for decimal format.
	 * @param required True to generate an exception if undefined.
	 * @return Value as Float.
	 * @throws java.lang.Exception
	 */
	public static Float getFloatParameter(ServletRequest request, String name, Locale locale, boolean required) throws Exception {
		try {
			String value = StringUtils.defaultIfBlank(request.getParameter(name), null);
			return Validator.validateFloat(required, value, locale);
		} catch(ValidatorException ex) {
			throw new Exception(MessageFormat.format("Error getting parameter [{0}]", name), ex);
		}
	}
	
	/**
	 * Gets request's parameter value.
	 * @param <E>
	 * @param request The HttpServletRequest.
	 * @param name Parameter name.
	 * @param defaultValue Desired defaultValue if undefined.
	 * @param enumClass The enum type class.
	 * @return Value as Float.
	 */
	public static <E extends Enum<E>> E getEnumParameter(ServletRequest request, String name, E defaultValue, Class<E> enumClass) {
		String value = getStringParameter(request, name, null);
		return (value == null) ? defaultValue : EnumUtils.forSerializedName(value, defaultValue, enumClass);
	}
	
	/**
	 * Gets request's parameter value.
	 * @param <E>
	 * @param request The HttpServletRequest.
	 * @param name Parameter name.
	 * @param required True to generate an exception if undefined.
	 * @param enumClass The enum type class.
	 * @return Value as Float.
	 */
	public static <E extends Enum<E>> E getEnumParameter(ServletRequest request, String name, boolean required, Class<E> enumClass) throws Exception {
		E value = getEnumParameter(request, name, null, enumClass);
		if (required && (value == null)) throw new Exception(MessageFormat.format("Error getting parameter [{0}]", name));
		return value;
	}
	
	/**
	 * Gets request's parameter value.
	 * @param request The HttpServletRequest.
	 * @param name Parameter name.
	 * @param defaultValue Desired defaultValue if undefined.
	 * @return Value as Date.
	 */
	public static Date getDateParameter(ServletRequest request, String name, Date defaultValue) {
		try {
			return ServletUtils.getDateParameter(request, name, true);
		} catch(Exception ex) {
			return defaultValue;
		}
	}
	
	/**
	 * Gets request's parameter value.
	 * @param request The HttpServletRequest.
	 * @param name Parameter name.
	 * @param required True to generate an exception if undefined.
	 * @return Value as Date.
	 * @throws java.lang.Exception
	 */
	public static Date getDateParameter(ServletRequest request, String name, boolean required) throws Exception {
		try {
			String value = StringUtils.defaultIfBlank(request.getParameter(name), null);
			//return Validator.validateDate(required, value, "yyyy-MM-dd'T'HH:mm:ss'Z'");
			return Validator.validateDate(required, value, "yyyy-MM-dd");
		} catch(ValidatorException ex) {
			throw new Exception(MessageFormat.format("Error getting parameter [{0}]", name), ex);
		}
	}
	
	public static <T>T getObjectParameter(ServletRequest request, String name, Class<T> type, boolean required) throws Exception {
		T value = getObjectParameter(request, name, null, type);
		if(required && (value == null)) throw new Exception(MessageFormat.format("Error getting parameter [{0}]", name));
		return value;
	}
	
	public static <T>T getObjectParameter(ServletRequest request, String name, T defaultValue, Class<T> type) throws Exception {
		String value = getStringParameter(request, name, false);
		return LangUtils.value(value, defaultValue, type);
	}
	
	/**
	 * Extracts request payload.
	 * @param request The HttpServletRequest.
	 * @return Payload string.
	 * @throws IOException 
	 */
	public static String getPayload(HttpServletRequest request) throws IOException {
		String line = null;
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}
	
	/**
	 * Unserializes the HTTP request's payload string into a Payload object 
	 * containing a map property object for testing field presence and a data
	 * bean representing the unserialized fields data.
	 * @param <D> Type of data property.
	 * @param request The HttpServletRequest.
	 * @param dataType Class type of data property.
	 * @return Payload object that contains unserialized data.
	 * @throws IOException 
	 */
	public static <D>Payload getPayload(HttpServletRequest request, Class<D> dataType) throws IOException {
		return getPayload(request, MapItem.class, dataType);
	}
	
	/**
	 * Unserializes the HTTP request's payload string into a Payload object 
	 * containing a map property object for testing field presence and a data
	 * bean representing the unserialized fields data.
	 * @param <D> Type of data property.
	 * @param payload The HttpServletRequest's payload raw string.
	 * @param dataType Class type of data property.
	 * @return Payload object that contains unserialized data.
	 * @throws IOException 
	 */
	public static <D>Payload getPayload(String payload, Class<D> dataType) throws IOException {
		return getPayload(payload, MapItem.class, dataType);
	}
	
	/**
	 * Unserializes the HTTP request's payload string into a Payload object 
	 * containing a map property for testing field presence and a data
	 * bean representing the unserialized fields data.
	 * @param <M> Type of map property.
	 * @param <D> Type of data property.
	 * @param request The HttpServletRequest.
	 * @param mapType Class type of map property.
	 * @param dataType Class type of data property.
	 * @return Payload object that contains unserialized data.
	 * @throws IOException
	 */
	public static <M, D>Payload getPayload(HttpServletRequest request, Class<M> mapType, Class<D> dataType) throws IOException {
		String payload = ServletUtils.getPayload(request);
		/*
		M map = JsonResult.gson.fromJson(payload, mapType);
		D data = JsonResult.gson.fromJson(payload, dataType);
		return new Payload<>(map, data);
		*/
		return getPayload(payload, mapType, dataType);
	}
	
	/**
	 * Unserializes the HTTP request's payload string into a Payload object 
	 * containing a map property for testing field presence and a data
	 * bean representing the unserialized fields data.
	 * @param <M> Type of map property.
	 * @param <D> Type of data property.
	 * @param payload The HttpServletRequest's payload raw string.
	 * @param mapType Class type of map property.
	 * @param dataType Class type of data property.
	 * @return Payload object that contains unserialized data.
	 * @throws IOException
	 */
	public static <M, D>Payload getPayload(String payload, Class<M> mapType, Class<D> dataType) throws IOException {
		M map = JsonResult.gson.fromJson(payload, mapType);
		D data = JsonResult.gson.fromJson(payload, dataType);
		return new Payload<>(map, data);
	}
	
	/**
	 * @deprecated 
	 * @param <T>
	 * @param request
	 * @param type
	 * @return
	 * @throws IOException 
	 */
	public static <T>PayloadAsList getPayloadAsList(HttpServletRequest request, Class<T> type) throws IOException {
		String payload = ServletUtils.getPayload(request);
		PayloadAsListRecords records = JsonResult.gson.fromJson(payload, PayloadAsListRecords.class);
		T data = JsonResult.gson.fromJson(payload, type);
		return new PayloadAsList<>(records, data);
	}
	
	public static String getUserAgent(HttpServletRequest request) {
		return request.getHeader("user-agent");
	}
	
	public static String getReferer(HttpServletRequest request) {
		return request.getHeader("referer");
	}
	
	/**
	 * Returns if client accepts gzip encoding.
	 * @param request The HttpServletRequest.
	 * @return True if can deflate, false otherwise
	 */
	public static boolean acceptsDeflate(HttpServletRequest request) {
		return StringUtils.containsIgnoreCase(request.getHeader("Accept-Encoding"), "gzip");
	}
	
	/**
	 * Returns if a mediaType is generally compressible.
	 * @param mediaType A content mediaType.
	 * @return True if content can be compressed, false otherwise.
	 */
	public static boolean isCompressible(String mediaType) {
		return compressibleMediaTypes.contains(mediaType);
	}
	
	/**
	 * Tries to guess MediaType from filename.
	 * @deprecated Use {@link #guessMediaType()} instead.
	 * @param fileName The file name
	 * @return MediaType string
	 */
	@Deprecated
	public static String guessMimeType(String fileName) {
		return guessMediaType(fileName);
	}
	
	/**
	 * Tries to guess MediaType from filename.
	 * @param fileName The file name
	 * @return MediaType string
	 */
	public static String guessMediaType(String fileName) {
		//MimeUtil.registerMimeDetector(ExtensionMimeDetector.class.getName());
		//MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
		MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.ExtensionMimeDetector");
		//MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.OpendesktopMimeDetector");
		MimeType mime = MimeUtil.getMostSpecificMimeType(MimeUtil.getMimeTypes(fileName));
		return (mime == null) ? null : mime.toString();
	}
	
	/**
	 * Translates a string into an URL-encoded string.
	 * @param s <code>String</code> to be translated.
	 * @return The translated <code>String</code>.
	 */
	public static String toURLEncodedString(String s) {
		try {
			return new URI(null, null, s, null).toASCIIString();
		} catch(URISyntaxException ex) {
			return null;
		}
	}
	
	/**
	 * Sets the <code>Content-Type</code> header as <code>text/html</code>.
	 * @param response The HttpServletResponse.
	 */
	public static void setHtmlContentType(HttpServletResponse response) {
		setContentTypeHeader(response, "text/html");
	}
	
	/**
	 * Sets the <code>Content-Type</code> header as <code>application/json</code>.
	 * @param response The HttpServletResponse.
	 */
	public static void setJsonContentType(HttpServletResponse response) {
		setContentTypeHeader(response, "application/json");
	}
	
	/**
	 * Sets the <code>Content-Encoding</code> header as <code>gzip</code>.
	 * @param response The HttpServletResponse.
	 */
	public static void setCompressedContentHeader(HttpServletResponse response) {
		response.setHeader("Content-Encoding", "gzip");
	}
	
	/**
	 * Sets the <code>Location</code> header using provided URL.
	 * URL value is expected to be escaped and may contain only ASCII characters.
	 * @param response The HttpServletResponse
	 * @param url The URL to redirect to.
	 */
	public static void setLocationHeader(HttpServletResponse response, String url) {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Location", url);
	}
	
	public static String getContentTypeHeader(HttpServletResponse response) {
		return StringUtils.substringBefore(response.getContentType(), ";");
	}
	
	/**
	 * Sets the <code>Content-Type</code> header using the provided mediaType. 
	 * If not provided <code>application/octet-stream</code> will be used instead.
	 * Character encoding will be set to UTF-8.
	 * @param response The HttpServletResponse.
	 * @param mediaType The chosen mediaType.
	 */
	public static void setContentTypeHeader(HttpServletResponse response, String mediaType) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType(StringUtils.isEmpty(mediaType) ? "application/octet-stream" : mediaType);
	}
	
	/**
	 * Sets the <code>Content-Disposition</code> header using provided 
	 * disposition and filename. Character encoding will be set to UTF-8.
	 * https://stackoverflow.com/questions/18050718/utf-8-encoding-name-in-downloaded-file
	 * https://stackoverflow.com/questions/5325322/java-servlet-download-filename-special-characters
	 * http://test.greenbytes.de/tech/tc2231/#attwithfn2231utf8
	 * @param response The HttpServletResponse.
	 * @param dispositionType The disposition mode.
	 * @param filename The choosen filename.
	 */
	public static void setContentDispositionHeader(HttpServletResponse response, DispositionType dispositionType, String filename) {
		StringBuilder sb = new StringBuilder(dispositionType.toString());
		CharsetEncoder enc = StandardCharsets.US_ASCII.newEncoder();
		if (enc.canEncode(filename)) {
			sb.append("; filename=\"").append(filename).append("\"");
			
		} else {
			enc.onMalformedInput(CodingErrorAction.IGNORE);
			enc.onUnmappableCharacter(CodingErrorAction.IGNORE);
			
			String normFilename = Normalizer.normalize(filename, Form.NFKD);
			CharBuffer cbuf = CharBuffer.wrap(normFilename);
			
			ByteBuffer bbuf;
			try {
				bbuf = enc.encode(cbuf);
			} catch (CharacterCodingException e) {
				bbuf = ByteBuffer.allocate(0);
			}
			
			String encFilename = new String(bbuf.array(), bbuf.position(), bbuf.limit(), StandardCharsets.US_ASCII);
			if (!StringUtils.isEmpty(encFilename)) {
				sb.append("; filename=\"").append(encFilename).append("\"");
			}
			
			String uencFilename = toURLEncodedString(filename);
			if (uencFilename != null) {
				sb.append("; filename*=UTF-8''").append(uencFilename);
			}
		}
		
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", sb.toString());
	}
	
	public static void setFileStreamHeadersForceDownload(HttpServletResponse response, String filename) {
		setFileStreamHeaders(response, "application/octet-stream", DispositionType.ATTACHMENT, filename);
	}
	
	public static void setFileStreamHeaders(HttpServletResponse response, String fileName) {
		setFileStreamHeaders(response, guessMimeType(fileName), DispositionType.INLINE, fileName);
	}
	
	public static void setFileStreamHeaders(HttpServletResponse response, String mimeType, String filename) {
		setFileStreamHeaders(response, mimeType, DispositionType.INLINE, filename);
	}
	
	public static void setFileStreamHeaders(HttpServletResponse response, String mimeType, DispositionType dispositionType, String filename) {
		setContentTypeHeader(response, mimeType);
		setContentDispositionHeader(response, dispositionType, filename);
	}
	
	public static void setContentLengthHeader(HttpServletResponse response, long length) {
		if (length <= Integer.MAX_VALUE) {
			response.setContentLength((int)length);
		} else {
			response.addHeader("Content-Length", Long.toString(length));
		}
	}
	
	public static void sendError(HttpServletResponse response, int error) {
		try {
			response.sendError(error);
		} catch (IOException ex) { /* Do nothing... */ }
	}
	
	
	
	public static void setCacheControlPrivate(HttpServletResponse response) {
		response.setHeader("Cache-Control", "private");
	}
	
	public static void setCacheControlPrivateNoCache(HttpServletResponse response) {
		response.setHeader("Cache-Control", "private, no-cache");
	}
	
	public static void setCacheControlPrivateMaxAge(HttpServletResponse response, int maxAge) {
		response.setHeader("Cache-Control", MessageFormat.format("private, max-age={0}", maxAge));//, must-revalidate
	}
	
	public static OutputStream prepareForStreamCopy(HttpServletRequest request, HttpServletResponse response, String mediaType, long contentLength, long gzipMinThreshold) throws IOException {
		final int BUFFER_SIZE = 4*1024;
		
		boolean willDeflate = acceptsDeflate(request) && isCompressible(mediaType) && (contentLength >= gzipMinThreshold);
		if (willDeflate) {
			setCompressedContentHeader(response);
			return new GZIPOutputStream(response.getOutputStream(), BUFFER_SIZE);
			// Content length is not directly predictable in case of GZIP.
			// So only add it if there is no means of GZIP, else browser will hang.
		} else {
			if (contentLength >= 0) setContentLengthHeader(response, contentLength);
			return response.getOutputStream();
		}
	}
	
	public static void transferStreams(InputStream is, OutputStream os) throws IOException {
		final int BUFFER_SIZE = 4*1024;
		
		byte[] buf = new byte[BUFFER_SIZE];
		int bytesRead;
		while ((bytesRead = is.read(buf)) != -1) {
			os.write(buf, 0, bytesRead);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void writeErrorHandlingJs(HttpServletResponse response) throws IOException {
		ServletUtils.writeErrorHandlingJs(response, null);
	}
	
	public static void writeErrorHandlingJs(HttpServletResponse response, String message) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<script type='text/javascript'>");
		if(message == null) {
			sb.append("function load(){ window.close(); }");
		} else {
			sb.append("function load(){ if(window.opener.WT) window.opener.WT.error('");
			sb.append(StringEscapeUtils.escapeEcmaScript(message));
			sb.append("');window.close();}");
		}
		sb.append("</script>");
		sb.append("</head>");
		sb.append("<body onload='load()'>");
		sb.append("</body>");
		sb.append("</html>");
		
		try {
			response.reset();
			byte[] bytes = sb.toString().getBytes();
			setHtmlContentType(response);
			setContentLengthHeader(response, bytes.length);
			ServletUtils.writeInputStream(response, new ByteArrayInputStream(sb.toString().getBytes()));
		} catch(IOException ex) { /* Do nothing! */}
	}
	
	public static void writeFileStream(HttpServletResponse response, String filename, InputStream fileStream) {
		writeFileStream(response, filename, fileStream, false);
	}
	
	public static void writeFileStream(HttpServletResponse response, String filename, InputStream fileStream, boolean dispAsAttachment) {
		//String fext = FilenameUtils.getExtension(filename);
		//String ctype = URLConnection.guessContentTypeFromName(filename);
		String ctype = guessMimeType(filename);
		String dispositionType = (dispAsAttachment) ? "attachment" : "inline";
		ServletUtils.setFileStreamHeaders(response, ctype, dispositionType, filename);
		try {
			ServletUtils.writeInputStream(fileStream, response.getOutputStream());
		} catch(IOException ex) { /* Do nothing! */}
	}
	
	/*
	public static void writeFileStream(WebTopApp wtApp, HttpServletResponse response, String filename, InputStream fileStream) {
		writeFileStream(wtApp, response, filename, fileStream, false);
	}
	
	public static void writeFileStream(WebTopApp wtApp, HttpServletResponse response, String filename, InputStream fileStream, boolean dispAsAttachment) {
		String fext = FilenameUtils.getExtension(filename);
		String ctype = wtApp.getContentType(fext);
		String dispositionType = (dispAsAttachment) ? "attachment" : "inline";
		ServletHelper.setFileStreamHeaders(response, ctype, dispositionType, filename);
		try {
			ServletHelper.writeInputStream(fileStream, response.getOutputStream());
		} catch(IOException ex) { * Do nothing! *}
	}
	*/
	
	
	
	public static void writeInputStream(HttpServletResponse response, InputStream is) throws IOException {
		ServletUtils.writeInputStream(is, response.getOutputStream());
	}
	
	public static void writeInputStream(InputStream is, OutputStream os) throws IOException {
		byte[] b = new byte[64 * 1024];
		int len = 0;
		
		while((len = is.read(b)) != -1) {
			os.write(b, 0, len);
		}
		os.flush();
		os.close();
		is.close();
	}
	
	public static void writeContent(HttpServletResponse response, InputStream is, String contentType) throws IOException {
		byte[] bytes = IOUtils.toByteArray(is);
		writeContent(response, bytes, bytes.length, contentType);
	}
	
	/**
	 * Writes an output stream to response's output stream, specifying also desired content type.
	 * 
	 * @param response Response object to write to.
	 * @param baos Output stream to write.
	 * @param contentType Data length.
	 * @throws IOException 
	 */
	public static void writeContent(HttpServletResponse response, ByteArrayOutputStream baos, String contentType) throws IOException {
		writeContent(response, baos.toByteArray(), baos.size(), contentType);
	}
	
	/**
	 * Writes bytes to response's output stream, specifying also desired content type.
	 * 
	 * @param response Response object to write to.
	 * @param data Bytes to write.
	 * @param contentType Data length.
	 * @throws IOException 
	 */
	public static void writeContent(HttpServletResponse response, byte[] data, int length, String contentType) throws IOException {
		response.setContentType(contentType);
		response.setContentLength(length);
		IOUtils.write(data, response.getOutputStream());
	}
	
	public static void writeError(HttpServletResponse response, int errorNo) {
		writeError(response, errorNo, null);
	}
	
	public static void writeError(HttpServletResponse response, int errorNo, String message) {
		try {
			if (StringUtils.isBlank(message)) {
				response.sendError(errorNo);
			} else {
				response.sendError(errorNo, message);
			}
		} catch(IOException ex) {
			logger.error("Unable to send error", ex);
		}
	}
	
	/**
	 * Discovers the remote client IP address from a request.
	 * This method also take into account possible headers that a proxy or a
	 * gateway could apply (eg. X-Forwarded-For, HTTP_X_FORWARDED_FOR, ect).
	 * @param request The HTTP request.
	 * @return The client IP address.
	 */
	public static String getClientIP(HttpServletRequest request) {
		String ip = null;
		
		if(logger.isTraceEnabled()) {
			String hName = null;
			Enumeration<String> hNames = request.getHeaderNames();
			logger.trace("Listing current request headers:");
			while (hNames.hasMoreElements()) {
				hName = hNames.nextElement();
				logger.trace("{}: {}", hName, request.getHeader(hName));
			}
		}
		
		ip = getIPFromHeader(request, "X-Forwarded-For", true);
		if(ip != null) return ip;
		ip = getIPFromHeader(request, "Proxy-Client-IP", true);
		if(ip != null) return ip;
		ip = getIPFromHeader(request, "WL-Proxy-Client-IP", true);
		if(ip != null) return ip;
		ip = getIPFromHeader(request, "HTTP_CLIENT_IP", true);
		if(ip != null) return ip;
		ip = getIPFromHeader(request, "HTTP_X_FORWARDED_FOR", true);
		if(ip != null) return ip;
		return request.getRemoteAddr();
	}
	
	/**
	 * Return the first valid IP address associated within 
	 * the specified HTTP header, excluding private IP ranges.
	 * 
	 * @param request The HTTP request.
	 * @param header The HTTP header to look into.
	 * @return The IP address.
	 */
	public static String getIPFromHeader(HttpServletRequest request, String header) {
		return getIPFromHeader(request, header, false);
	}
	
	/**
	 * Return the first valid IP address associated within 
	 * the specified HTTP header.
	 * 
	 * @param request The HTTP request.
	 * @param header The HTTP header to look into.
	 * @param allowPrivate Specifies if a private address must be considered valid.
	 * @return The IP address.
	 */
	public static String getIPFromHeader(HttpServletRequest request, String header, boolean allowPrivate) {
		StringTokenizer tokenizer = null;
		String value = null, ip = null;
		
		if ((value = request.getHeader(header)) != null) {
			tokenizer = new StringTokenizer(value, ",");
			while (tokenizer.hasMoreTokens()) {
				ip = tokenizer.nextToken().trim();
				if (IPUtils.isIPv4Valid(ip) && (allowPrivate || !IPUtils.isIPv4Private(ip))) return ip;
			}
		}
		return null;
	}
	
	public static void redirectRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		redirectRequest(request, response, request.getContextPath());
	}
	
	public static void redirectRequest(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		response.sendRedirect(url);
	}
	
	public static void forwardRequest(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}
	
	public static Cookie getCookieObject(HttpServletRequest request, String name) {
		Cookie cookies[] = request.getCookies();
		if(cookies == null) return null;
		for (Cookie cookie: cookies) {
			if(cookie.getName().equals(name)) return cookie;
		}
		return null;
	}
	
	public static void eraseCookie(HttpServletResponse response, String name) {
		setCookie(response, name, StringUtils.EMPTY, 0);
	}
	
	public static String getCookie(HttpServletRequest request, String name) {
		try {
			Cookie coo = getCookieObject(request, name);
			if(coo == null) return null;
			return URLDecoder.decode(coo.getValue(), "UTF-8");
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static <T>T getCookie(HttpServletRequest request, String name, Class<T> type) {
		String cookie = getCookie(request, name);
		return LangUtils.deserialize(cookie, type);
	}
	
	public static String getEncryptedCookie(String encryptionKey, HttpServletRequest request, String name) {
		String value = getCookie(request, name);
		if(value == null) return null;
		return decryptCookieValue(value, encryptionKey);
	}
	
	public static <T>T getEncryptedCookie(String encryptionKey, HttpServletRequest request, String name, Class<T> type) {
		String cookie = getEncryptedCookie(encryptionKey, request, name);
		return LangUtils.deserialize(cookie, type);
	}
	
	/**
	 * Sets a simple string value into a cookie with provided name.
	 * 
	 * @param response The HTTP response.
	 * @param name The cookie name.
	 * @param value The string value.
	 * @param duration The duration in sec.
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int duration) {
		try {
			Cookie coo = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
			coo.setMaxAge(duration);
			coo.setHttpOnly(true);
			response.addCookie(coo);
			
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Sets a complex object into a cookie with provided name.
	 * 
	 * @param response The HTTP response.
	 * @param name The cookie name.
	 * @param value The object to be serialized.
	 * @param type The object type.
	 * @param duration The duration in sec.
	 */
	public static void setCookie(HttpServletResponse response, String name, Object value, Class type, int duration) {
		String cookie = LangUtils.serialize(value, type);
		setCookie(response, name, cookie, duration);
	}
	
	/**
	 * Sets a simple string value into an encrypted cookie with provided name.
	 * 
	 * @param encryptionKey The encryption key.
	 * @param response The HTTP response.
	 * @param name The cookie name.
	 * @param value The string value.
	 * @param duration The duration in sec.
	 */
	public static void setEncryptedCookie(String encryptionKey, HttpServletResponse response, String name, String value, int duration) {
		String cookie = encryptCookieValue(value, encryptionKey);
		setCookie(response, name, cookie, duration);
	}
	
	/**
	 * Sets a complex object value into an encrypted cookie with provided name.
	 * 
	 * @param encryptionKey The encryption key.
	 * @param response The HTTP response.
	 * @param name The cookie name.
	 * @param value The object to be serialized.
	 * @param type The object type.
	 * @param duration The duration in sec.
	 */
	public static void setEncryptedCookie(String encryptionKey, HttpServletResponse response, String name, Object value, Class type, int duration) {
		String cookie = LangUtils.serialize(value, type);
		setEncryptedCookie(encryptionKey, response, name, cookie, duration);
	}
	
	private static String encryptCookieValue(String value, String cryptKey) {
		try {
			Cipher aes = createChiper(Cipher.ENCRYPT_MODE, cryptKey);
			byte[] encBytes = aes.doFinal(value.getBytes());
			//return Base64.encodeBase64String(encBytes);
			return new String(new Base32().encode(encBytes));
		} catch(Exception ex) {
			logger.error("Unable to encrypt cookie value", ex);
			return null;
		}
	}
	
	private static String decryptCookieValue(String value, String cryptKey) {
		try {
			Cipher aes = createChiper(Cipher.DECRYPT_MODE, cryptKey);
			byte[] encBytes = new Base32().decode(value.getBytes());
			//byte[] encBytes = Base64.decodeBase64(value);
			byte[] bytes = aes.doFinal(encBytes);
			return new String(bytes);
		} catch(Exception ex) {
			logger.error("Unable to dencrypt cookie value", ex);
			return null;
		}
	}
	
	private static Cipher createChiper(int mode, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
		aes.init(mode, new SecretKeySpec(key.getBytes(), "AES"), new IvParameterSpec(new byte[16]));
		return aes;
	}
	
	public static class StringArray extends ArrayList<String> {
		public StringArray() {
			super();
		}
		
		public static StringArray fromJson(String value) {
			if(value == null) return null;
			return JsonResult.gson.fromJson(value, StringArray.class);
		}

		public static String toJson(StringArray value) {
			if(value == null) return null;
			return JsonResult.gson.toJson(value, StringArray.class);
		}
	}
	
	public static class IntegerArray extends ArrayList<Integer> {
		public IntegerArray() {
			super();
		}
		
		public static IntegerArray fromJson(String value) {
			if(value == null) return null;
			return JsonResult.gson.fromJson(value, IntegerArray.class);
		}

		public static String toJson(IntegerArray value) {
			if(value == null) return null;
			return JsonResult.gson.toJson(value, IntegerArray.class);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @deprecated use setContentDispositionHeader(HttpServletResponse response, DispositionType dispositionType, String fileName) instead
	 */
	@Deprecated
	public static void setContentDispositionHeader(HttpServletResponse response, String dispositionType, String fileName) {
		response.addHeader("Content-Disposition", MessageFormat.format("{0}; filename=\"{1}\"", dispositionType, fileName));
	}
	
	/**
	 * @deprecated use setFileStreamHeaders(HttpServletResponse response, String mimeType, DispositionType dipositionType, String fileName) instead
	 */
	@Deprecated
	public static void setFileStreamHeaders(HttpServletResponse response, String mimeType, String dispositionType, String filename) {
		setContentTypeHeader(response, mimeType);
		setContentDispositionHeader(response, dispositionType, filename);
	}
}
