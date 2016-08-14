/* 
 * Copyright (c) Orchestral Developments Ltd and the Orion Health group of companies (2001 - 2016).
 * 
 * This document is copyright. Except for the purpose of fair reviewing, no part
 * of this publication may be reproduced or transmitted in any form or by any
 * means, electronic or mechanical, including photocopying, recording, or any
 * information storage and retrieval system, without permission in writing from
 * the publisher. Infringers of copyright render themselves liable for
 * prosecution.
 */
package org.sakaiproject.logviewer.business;

import java.io.File;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.lang.StringUtils;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.api.ServerConfigurationService;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Business service for the log viewer app
 */
@Slf4j
public class LogViewerBusinessService {
	
	@Setter
	private SecurityService securityService;
	
	@Setter
	private ServerConfigurationService serverConfigurationService;
	
	Tailer tailer;
	LogTailListener listener;
	Thread thread;
	
	/**
	 * Business service startup
	 */
	public void init() {
		log.info("init");
		
		//TODO guards here
		//maybe set a status
		// if its a large file we dont want to blow the heap
		String filePath = serverConfigurationService.getString("log.viewer.file");
		
		if(StringUtils.isBlank(filePath)) {
			throw new IllegalArgumentException("You haven't specified a filepath for the log viewer");
		}

		File logFile = new File(filePath);
		
		log.info("Configured logviewer with: " + filePath);
		
		listener = new LogTailListener();
		tailer = new Tailer(logFile, listener, 1000, true);
				
	    thread = new Thread(tailer, "logviewer");
	    thread.start();
	    
	    // executor seems to block Sakai startup
		// basic executor
		//Executor executor = new Executor() {
		//	@Override
		//	public void execute(Runnable command) {
		//		command.run();
		//	}
		//};
		//executor.execute(tailer);
	}
	
	public void destroy() {
		thread.interrupt();
		tailer.stop();
	}
	
	public boolean isSuperUser() {
		return securityService.isSuperUser();
	}
	
	public String getLogLines() {
		return listener.getLogLines();
	}
	
	
}

