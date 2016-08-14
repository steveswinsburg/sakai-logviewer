package org.sakaiproject.logviewer.pages;

import java.io.File;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.time.Duration;


/**
 * Main page for LogViewerApplication
 * 
 * @author Steve Swinsburg (steve.swinsburg@anu.edu.au)
 *
 */
public class View extends BasePage {
	
	private static final long serialVersionUID = 1L;

	File logFile;
	
	/**
	 * Render the View page
	 */
	public View() {
		//disableLink(viewLink);
		logFile = new File("/Users/erkel/dev/sakai/conf/trunk/sakai.properties");
		
		
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		add(backFillLogData());
		add(createNextLog());
	}
	
	/**
	 * Backfill existing data from the file
	 * 
	 * @return a MultiLineLabel of data to be displayed
	 */
	private MultiLineLabel backFillLogData() {
		
		MultiLineLabel label = new MultiLineLabel("logData");
        
		label.setDefaultModel(new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
            protected Object load() {
               try{
            	   //TODO improvement, get the last few lines from the file (as configured)
					 return "old log lines here";
					 //return FileUtils.readFileToString(logFile, "UTF-8");
               }catch(Exception ex){}
               return "";
            }
        });
		
		label.setMarkupId("logData");
		label.setOutputMarkupId(true);
		return label;
    } 
	
	

	 /**
	  * Get the next portion of the log file and output it.
	  * 
	  * @return a Label with the next part of the log
	  */
	private Label createNextLog() {
		
		Label label = new Label("nextLog");
		
		//if(listenerRunning) {
		
		label.setDefaultModel(new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				//get the data from the tailer
				return businessService.getLogLines();
			}
		});
		
		label.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)) {
			private static final long serialVersionUID = 1L;

				@Override
				protected void onPostProcessTarget(AjaxRequestTarget target) {
					
					if(label.getDefaultModel() != null) {
					
					 /*
					  * We are doing the following here:
					  *   - append the content of "nextLog" to "logData"
					  *   - remove "nextLog"
					  *   - insert "nextLog" after "logData".
					  */
					 target.appendJavaScript("doUpdate();");
					
					}
				}
			});
		//}
		
		label.setMarkupId("nextLog");
		label.setOutputMarkupId(true);
		return label;
	 } 
	
	
}

