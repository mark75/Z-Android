package com.zandroid.network.lib;

import java.util.Map;

public class ZJsonHelper {

	private ZJsonParserInterface mJsonParserImpl;

	private static ZJsonHelper instance = null;

	private ZJsonHelper() {
		mJsonParserImpl = new ZGsonParserImpl();
	}

	public static ZJsonHelper instance() {
		if (instance == null) {
			instance = new ZJsonHelper();
		}

		return instance;
	}

	public ZJsonHelper setParserImpl(ZJsonParserInterface impl) {
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
