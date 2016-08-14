package org.sakaiproject.logviewer.pages;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;
import org.apache.wicket.markup.head.StringHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.logviewer.business.LogViewerBusinessService;

import lombok.extern.slf4j.Slf4j;


/**
 * BasePage for LogViewerApplication
 * 
 * @author Steve Swinsburg (steve.swinsburg@anu.edu.au)
 *
 */
@Slf4j
public class BasePage extends WebPage{

	private static final long serialVersionUID = 1L;
	
	@SpringBean(name="org.sakaiproject.logviewer.business.LogViewerBusinessService")
	protected LogViewerBusinessService businessService;
		
	public BasePage() {
		log.debug("BasePage()");
    }
	

	/**
	 * This block adds the required wrapper markup to style it like a Sakai tool. Add to this any additional CSS or JS references that you
	 * need.
	 *
	 */
	@Override
	public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);

		final String version = ServerConfigurationService.getString("portal.cdn.version", "");

		// get the Sakai skin header fragment from the request attribute
		final HttpServletRequest request = (HttpServletRequest) getRequest().getContainerRequest();

		response.render(new PriorityHeaderItem(JavaScriptHeaderItem
				.forReference(getApplication().getJavaScriptLibrarySettings().getJQueryReference())));

		response.render(StringHeaderItem.forString((String) request.getAttribute("sakai.html.head")));
		response.render(OnLoadHeaderItem.forScript("setMainFrameHeight( window.name )"));

		// Tool additions (at end so we can override if required)
		response.render(StringHeaderItem
				.forString("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />"));

		// Shared stylesheets
		response.render(CssHeaderItem
				.forUrl(String.format("css/logviewer.css?version=%s", version)));

	}
	
	
	/** 
	 * Helper to disable a link. Add the Sakai class 'current'.
	 */
	protected void disableLink(Link<Void> l) {
		l.add(new AttributeAppender("class", new Model<String>("current"), " "));
		l.setRenderBodyOnly(true);
		l.setEnabled(false);
	}
	
	
	
}
