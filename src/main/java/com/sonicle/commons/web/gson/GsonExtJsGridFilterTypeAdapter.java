/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sonicle.commons.web.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.sonicle.commons.web.json.extjs.ExtGridFilter;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class GsonExtJsGridFilterTypeAdapter implements JsonDeserializer<ExtGridFilter> {

	@Override
	public ExtGridFilter deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
		
		JsonObject jo = je.getAsJsonObject();
		String filterType = jo.get("type").getAsString();
		
		try {
			String className = MessageFormat.format("com.sonicle.commons.web.json.extjs.ExtGrid{0}Filter", StringUtils.capitalize(filterType));
			return jdc.deserialize(je, Class.forName(className));
		} catch(ClassNotFoundException ex) {
			throw new JsonParseException("Unknown element type: " + filterType, ex);
		}
	}
}
