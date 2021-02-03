/*
 * Copyright (C) 2020 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2020 Sonicle S.r.l.".
 */
package com.sonicle.commons.web.json.ipstack;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 *
 * @author malbinola
 */
public class IPLookupResponse {
	protected String ip;
	protected String hostname;
	protected String type;
	@SerializedName("continent_code")
	protected String continentCode;
	@SerializedName("continent_name")
	protected String continentName;
	@SerializedName("country_code")
	protected String countryCode;
	@SerializedName("country_name")
	protected String countryName;
	protected String city;
	protected String zip;
	protected Double latitude;
	protected Double longitude;
	protected Location location;
	
			/*
		{
  "time_zone": {
    "id": "America/Los_Angeles",
    "current_time": "2018-03-29T07:35:08-07:00",
    "gmt_offset": -25200,
    "code": "PDT",
    "is_daylight_saving": true
  },
  "currency": {
    "code": "USD",
    "name": "US Dollar",
    "plural": "US dollars",
    "symbol": "$",
    "symbol_native": "$"
  },
  "connection": {
    "asn": 25876,
    "isp": "Los Angeles Department of Water & Power"
  },
  "security": {
    "is_proxy": false,
    "proxy_type": null,
    "is_crawler": false,
    "crawler_name": null,
    "crawler_type": null,
    "is_tor": false,
    "threat_level": "low",
    "threat_types": null
  }
}
		*/

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContinentCode() {
		return continentCode;
	}

	public void setContinentCode(String continentCode) {
		this.continentCode = continentCode;
	}

	public String getContinentName() {
		return continentName;
	}

	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public static class Location {
		@SerializedName("geonameId")
		protected String geonameId;
		protected String capital;
		protected java.util.List<Language> languages;
		@SerializedName("country_flag")
		protected String countryFlag;
		@SerializedName("country_flag_emoji")
		protected String countryFlagEmoji;
		@SerializedName("country_flag_emoji_unicode")
		protected String countryFlagEmojiUnicode;
		@SerializedName("calling_code")
		protected String callingCode;
		@SerializedName("is_eu")
		protected Boolean isEu;

		public String getGeonameId() {
			return geonameId;
		}

		public void setGeonameId(String geonameId) {
			this.geonameId = geonameId;
		}

		public String getCapital() {
			return capital;
		}

		public void setCapital(String capital) {
			this.capital = capital;
		}

		public java.util.List<Language> getLanguages() {
			return languages;
		}

		public void setLanguages(java.util.List<Language> languages) {
			this.languages = languages;
		}

		public String getCountryFlag() {
			return countryFlag;
		}

		public void setCountryFlag(String countryFlag) {
			this.countryFlag = countryFlag;
		}

		public String getCountryFlagEmoji() {
			return countryFlagEmoji;
		}

		public void setCountryFlagEmoji(String countryFlagEmoji) {
			this.countryFlagEmoji = countryFlagEmoji;
		}

		public String getCountryFlagEmojiUnicode() {
			return countryFlagEmojiUnicode;
		}

		public void setCountryFlagEmojiUnicode(String countryFlagEmojiUnicode) {
			this.countryFlagEmojiUnicode = countryFlagEmojiUnicode;
		}

		public String getCallingCode() {
			return callingCode;
		}

		public void setCallingCode(String callingCode) {
			this.callingCode = callingCode;
		}

		public Boolean getIsEu() {
			return isEu;
		}

		public void setIsEu(Boolean isEu) {
			this.isEu = isEu;
		}
		
		public static class Language {
			protected String code;
			protected String name;
			@SerializedName("native")
			protected String nativeName;

			public String getCode() {
				return code;
			}

			public void setCode(String code) {
				this.code = code;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getNativeName() {
				return nativeName;
			}

			public void setNativeName(String nativeName) {
				this.nativeName = nativeName;
			}
		}
	}
	
	public static class List extends ArrayList<IPLookupResponse> {
		public List() {
			super();
		}
	}
}
