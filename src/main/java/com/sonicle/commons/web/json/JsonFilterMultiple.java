/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sonicle.commons.web.json;

import java.util.ArrayList;

/**
 * @deprecated Evaluation is necessary to determine if this is really useful
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
	
	public JsonFilterMultiple(String type, String field, String comparison, String value) {
		super(type, field, comparison);
		this.value = new ArrayList<String>();
		this.value.add(value);
	}
}