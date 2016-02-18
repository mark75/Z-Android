/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zandroid.network.lib.entity;

import java.util.List;

// TODO: Auto-generated Javadoc

/**
 * ? 2012 amsoft.cn
 * ���ƣ�AbHttpListener.java
 * ������Http��Ӧ������
 *
 * @author ����һ����
 * @version v1.0
 * @date��2014-08-05 ����9:00:52
 */
public abstract class ZHttpListener {

    /**
     * ����.
     */
	public ZHttpListener() {
		super();
	}

	/**
	 * ����ɹ�.
	 *
	 * @param content the content
	 */
    public void onSuccess(String content){};

    /**
	 * ����ɹ�.
	 *
	 * @param list the list
	 */
    public void onSuccess(List<?> list){};

    /**
     * ����ʧ��.
     * @param content the content
     */
    public abstract void onFailure(String content);


    /**
	 * ��������ȡ���ݿ�ʼ.
	 */
    public void onStart(){};


    /**
	 * ��ɺ���ã�ʧ�ܣ��ɹ�������.
	 */
    public void onFinish(){};

}
