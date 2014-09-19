/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sonicle.commons.web.json;

import com.sonicle.commons.web.json.JsonFilter;
import java.util.ArrayList;

/**
 *
 * @author dnllr
 */
public class JsonFilterMultiple extends JsonFilter {
	public ArrayList<String> value = null;
	
	public JsonFilterMultiple() {
		super();
	}
	
	public JsonFilterMultiple(String type, String field, String value) {
		super(type, field);
		this.value = new ArrayList<String>();
		this.value.add(value);
	}
}