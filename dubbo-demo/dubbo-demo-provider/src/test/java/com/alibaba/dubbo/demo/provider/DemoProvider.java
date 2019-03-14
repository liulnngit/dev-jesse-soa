/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.demo.provider;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nbtv.commons.context.AppContext;

public class DemoProvider {
	
	private static volatile boolean running = true;
	protected static Log log = LogFactory.getLog(DemoProvider.class);
	
	public static void main(String[] args) {
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					AppContext.stop();
					
					System.out.println(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date()) + " Main server stopped!");
				}catch (Throwable t) {
                    System.out.println("Main stop error:"+t);
                }
				
				synchronized (DemoProvider.class) {
	                running = false;
	                DemoProvider.class.notify();
	            }
			}			
		});
		
		
		AppContext.start();

		log.info(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date()) + " Main server started!");
		
		synchronized (DemoProvider.class) {
			while (running) {
	            try {
	            	DemoProvider.class.wait();
	            } catch (Throwable e) {
	            }
	        }
		}
	}

}