/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sonicle.commons.web.json;

/**
 *
 * @author dnllr
 */
public class JsonFilter {
	public String type = null;
	public String field = null;
	
	public JsonFilter() {
		
	}
	
	public JsonFilter(String type, String field) {
		this.type = type;
		this.field = field;
	}
}