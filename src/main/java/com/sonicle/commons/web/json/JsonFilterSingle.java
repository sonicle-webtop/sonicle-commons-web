/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sonicle.commons.web.json;

/**
 *
 * @author dnllr
 */
public class JsonFilterSingle extends JsonFilter {
	public String value = null;
	
	public JsonFilterSingle() {
		super();
	}
	
	public JsonFilterSingle(String type, String field, String value) {
		super(type, field);
		this.value = value;
	}
}