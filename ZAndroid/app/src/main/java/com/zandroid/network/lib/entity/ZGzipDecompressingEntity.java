package com.zandroid.network.lib.entity;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class ZGzipDecompressingEntity extends HttpEntityWrapper {

	public ZGzipDecompressingEntity(final HttpEntity entity) {
		super(entity);
	}

	public InputStream getContent() throws IOException, IllegalStateException {
		InputStream wrappedin = wrappedEntity.getContent();
		return new GZIPInputStream(wrappedin);
	}

	public long getContentLength() {
		return -1;
	}
}