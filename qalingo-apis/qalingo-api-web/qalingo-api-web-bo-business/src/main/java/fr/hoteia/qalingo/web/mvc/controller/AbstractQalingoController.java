/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package fr.hoteia.qalingo.web.mvc.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import fr.hoteia.qalingo.core.Constants;
import fr.hoteia.qalingo.core.domain.Localization;
import fr.hoteia.qalingo.core.domain.User;
import fr.hoteia.qalingo.core.i18n.message.CoreMessageSource;
import fr.hoteia.qalingo.core.service.UrlService;
import fr.hoteia.qalingo.core.service.UserService;
import fr.hoteia.qalingo.core.web.util.RequestUtil;
import fr.hoteia.qalingo.web.mvc.factory.FormFactory;
import fr.hoteia.qalingo.web.mvc.factory.ModelAndViewFactory;
import fr.hoteia.qalingo.web.mvc.factory.ViewBeanFactory;
import fr.hoteia.qalingo.web.service.BackofficeUrlService;

/**
 * 
 * <p>
 * <a href="AbstractQalingoController.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author Denis Gosset <a href="http://www.hoteia.com"><i>Hoteia.com</i></a>
 * 
 */
public abstract class AbstractQalingoController extends AbstractController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	private static ApplicationContext ctx = null;

	@Autowired
    protected RequestUtil requestUtil;
	
	@Autowired
    protected ModelAndViewFactory modelAndViewFactory;
	
	@Autowired
    protected ViewBeanFactory viewBeanFactory;
	
	@Autowired
    protected FormFactory formFactory;

	@Autowired
	protected CoreMessageSource coreMessageSource;

	@Autowired
	protected UserService userService;

	@Autowired
    protected UrlService urlService;
	
	@Autowired
    protected BackofficeUrlService backofficeUrlService;
	
	/**
	 * 
	 */
	@Override
	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		return null;
	}

	/**
	 * 
	 */
	protected void initPage(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
    }
	
	/**
	 * 
	 */
	protected void initPage(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView, final String titleKeyPrefixSufix) throws Exception {
		initPage(request, response);
		initMessages(request, response, modelAndView);
		initCommon(request, response, modelAndView, titleKeyPrefixSufix);
		initBreadcrumb(request, modelAndView, titleKeyPrefixSufix);
		initSeo(request, modelAndView, titleKeyPrefixSufix);
		initUser(request, modelAndView);
    }

	/**
	 * 
	 */
	protected void initMessages(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView) throws Exception {
		// WE USE SESSION FOR MESSAGES BECAUSE REDIRECT CLEAN REQUEST
		// ERROR MESSAGE
		String errorMessage = (String) request.getSession().getAttribute(Constants.ERROR_MESSAGE);
		if(StringUtils.isNotEmpty(errorMessage)){
			modelAndView.addObject(Constants.ERROR_MESSAGE, errorMessage);
			request.getSession().removeAttribute(Constants.ERROR_MESSAGE);
		}
		// INFO MESSAGE
		String infoMessage = (String) request.getSession().getAttribute(Constants.INFO_MESSAGE);
		if(StringUtils.isNotEmpty(infoMessage)){
			modelAndView.addObject(Constants.INFO_MESSAGE, infoMessage);
			request.getSession().removeAttribute(Constants.INFO_MESSAGE);
		}
		// SUCCESS MESSAGE
		String successMessage = (String) request.getSession().getAttribute(Constants.SUCCESS_MESSAGE);
		if(StringUtils.isNotEmpty(successMessage)){
			modelAndView.addObject(Constants.SUCCESS_MESSAGE, successMessage);
			request.getSession().removeAttribute(Constants.SUCCESS_MESSAGE);
		}
	}
	
	/**
	 * 
	 */
	protected void initCommon(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView, final String titleKeyPrefixSufix) throws Exception {
		final Locale locale  = requestUtil.getCurrentLocale(request);
		
		// Velocity layout mandatory attributes
		modelAndView.addObject(Constants.VELOCITY_LAYOUT_ATTRIBUTE_HEAD_CONTENT, "../_include/head-common-empty-content.vm");
		modelAndView.addObject(Constants.VELOCITY_LAYOUT_ATTRIBUTE_FOOTER_SCRIPT_CONTENT, "../_include/body-footer-empty-script-content.vm");
		
		modelAndView.addObject(Constants.APP_NAME, "Qalingo Business Backoffice");
		modelAndView.addObject(Constants.APP_NAME_HTML, "Qalingo <span style=\"color: #a1cd44;\">Business</span> Backoffice");;

		modelAndView.addObject("localeLanguageCode", locale.getLanguage());
		modelAndView.addObject("contextPath", request.getContextPath());
		modelAndView.addObject("theme", requestUtil.getCurrentTheme(request));
		
		Object[] params = {requestUtil.getEnvironmentName()};
		modelAndView.addObject("envName", coreMessageSource.getMessage("header.env.name", params, locale));
		
		modelAndView.addObject("mainContentTitle", coreMessageSource.getMessage("header.title." + titleKeyPrefixSufix, null, locale));
		modelAndViewFactory.initCommonModelAndView(request, response, modelAndView);
	}
	
	/**
	 * 
	 */
	protected void initBreadcrumb(final HttpServletRequest request, final ModelAndView modelAndView, final String titleKeyPrefixSufix) throws Exception {
	}
	
	/**
	 * 
	 */
	protected void initSeo(final HttpServletRequest request, final ModelAndView modelAndView, final String titleKeyPrefixSufix) throws Exception {
		final Localization currentLocalization = requestUtil.getCurrentLocalization(request);
		final Locale locale = currentLocalization.getLocale();

		String seoPageMetaAuthor = coreMessageSource.getMessage("seo.meta.author", null, locale);
        modelAndView.addObject("seoPageMetaAuthor", seoPageMetaAuthor);

		String seoPageMetaKeywords = coreMessageSource.getMessage("page.meta.keywords", null, locale);
        modelAndView.addObject("seoPageMetaKeywords", seoPageMetaKeywords);

		String seoPageMetaDescription = coreMessageSource.getMessage("page.meta.description", null, locale);
        modelAndView.addObject("seoPageMetaDescription", seoPageMetaDescription);

		String pageTitleKey = "header.title." + titleKeyPrefixSufix;
		String appName = (String) modelAndView.getModelMap().get(Constants.APP_NAME);
		String seoPageTitle = appName + " - " + coreMessageSource.getMessage(pageTitleKey, null, locale);
        modelAndView.addObject("seoPageTitle", seoPageTitle);
	}
	
	/**
	 * 
	 */
	protected void initUser(final HttpServletRequest request, final ModelAndView modelAndView) throws Exception {
		final User user = getUser();
		modelAndView.addObject("user", user);
	}
	
	/**
	 * 
	 */
	public ApplicationContext getContext() {
		if (ctx == null) {
			ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		}
		return ctx;
	}

	/**
	 * 
	 */
	protected User getUser(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = null;
		if(StringUtils.isNotEmpty(username)
				&& !username.equalsIgnoreCase("anonymousUser")){
			user = userService.getUserByLoginOrEmail(username);
		}
		return user;
	}
	
	/**
	 * 
	 */
	protected String getUserId(){
		User user = getUser();
		if(user != null){
			Long userId = user.getId();
			if(userId != null){
				return userId.toString();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * @throws Exception 
	 * 
	 */
	protected Localization getCurrentLocalization(HttpServletRequest request) throws Exception {
		return requestUtil.getCurrentLocalization(request);
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	protected Locale getCurrentLocale(HttpServletRequest request) throws Exception {
		return getCurrentLocalization(request).getLocale();
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	protected String getCurrentTheme(HttpServletRequest request) throws Exception {
		return requestUtil.getCurrentTheme(request);
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	protected String getCurrentDevice(HttpServletRequest request) throws Exception {
		return requestUtil.getCurrentDevice(request);
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	protected String getCurrentVelocityPath(HttpServletRequest request) throws Exception {
		return requestUtil.getCurrentVelocityPrefix(request);
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	protected void addErrorMessage(HttpServletRequest request, String message) throws Exception {
		request.getSession().setAttribute(Constants.ERROR_MESSAGE, message);
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	protected void addInfoMessage(HttpServletRequest request, String message) throws Exception {
		request.getSession().setAttribute(Constants.INFO_MESSAGE, message);
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	protected void addSuccessMessage(HttpServletRequest request, String message) throws Exception {
		request.getSession().setAttribute(Constants.SUCCESS_MESSAGE, message);
	}

}
