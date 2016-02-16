package com.zandroid.network;

import java.util.Map;

public class MGJsonHelper {

	private MGJsonParserInterface mJsonParserImpl;

	private static MGJsonHelper instance = null;

	private MGJsonHelper() {
		mJsonParserImpl = new MGGsonParserImpl();
	}

	public static MGJsonHelper instance() {
		if (instance == null) {
			instance = new MGJsonHelper();
		}

		return instance;
	}

	public MGJsonHelper setParserImpl(MGJsonParserInterface impl) {
		if (impl != null) {
			mJsonParserImpl = impl;
		}
		return this;
	}

	public <T> T jsonToModel(String json, Class<T> clazz) {
		return mJsonParserImpl.jsonToModel(json, clazz);
	}

	public Map<String, Object> jsonToMap(String json) {
		return mJsonParserImpl.jsonToMap(json);
	}

	public String objToJson(Object obj) {
		return mJsonParserImpl.objToJson(obj);
	}
}
