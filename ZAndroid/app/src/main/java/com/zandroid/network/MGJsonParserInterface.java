package com.zandroid.network;

import java.util.Map;

public interface MGJsonParserInterface {

	public <T> T jsonToModel(String json, Class<T> clazz);

	public Map<String, Object> jsonToMap(String json);

	public String objToJson(Object obj);
}
