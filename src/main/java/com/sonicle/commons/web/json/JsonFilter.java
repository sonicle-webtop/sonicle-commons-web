/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sonicle.commons.web.json;

/**
 * @deprecated Evaluation is necessary to determine if this is really useful
 * @author dnllr
 */
public class JsonFilter {
	public String type = null;
	public String field = null;
	public String comparison = null;
	
	public JsonFilter() {}
	
	public JsonFilter(String type, String field) {
		this.type = type;
		this.field = field;
	}
	
	public JsonFilter(String type, String field, String comparison) {
		this.type = type;
		this.field = field;
		this.comparison = comparison;
	}
}