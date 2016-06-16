/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sonicle.commons.web.json;

/**
 * @deprecated Evaluation is necessary to determine if this is really useful
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
	
	public JsonFilterSingle(String type, String field, String comparison, String value) {
		super(type, field, comparison);
		this.value = value;
	}
}