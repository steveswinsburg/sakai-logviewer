package org.sakaiproject.logviewer.business;

import org.apache.commons.io.input.TailerListenerAdapter;
import org.apache.commons.lang.StringUtils;

/**
 * This is our Listener class which will handle the management of the log.
 * <p>
 * When the log is read, its lines are put into a (synchronised) StringBuffer
 * We can then retrieve those lines from the original thread and update the view.
 *
 * @author Steve Swinsburg (steve.swinsburg@gmail.com)
 *
 */
public class LogTailListener extends TailerListenerAdapter {

	/**
	 * Holds the current set of log lines
	 */
	private static StringBuffer logLines;
	
	public LogTailListener() {
		if(logLines == null) {
			logLines = new StringBuffer();
		}
		
	}
	
	@Override
	public void handle(String line) {
		if(StringUtils.isNotBlank(line)) {
			logLines.append(line);
			logLines.append('\n');
		}
    }

	/**
	 * Get the latest set of log lines and then clear the buffer ready for the next set
	 * 
	 * @return a string of lines
	 */
	public String getLogLines() {
		String s = logLines.toString();
		logLines.setLength(0); //flush
		return s;
	}
	
	
}
