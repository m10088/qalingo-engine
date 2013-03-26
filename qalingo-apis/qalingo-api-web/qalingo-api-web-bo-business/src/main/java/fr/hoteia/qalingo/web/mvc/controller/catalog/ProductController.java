/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package fr.hoteia.qalingo.web.mvc.controller.catalog;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import fr.hoteia.qalingo.core.Constants;
import fr.hoteia.qalingo.core.domain.Localization;
import fr.hoteia.qalingo.core.domain.MarketArea;
import fr.hoteia.qalingo.core.domain.ProductMarketing;
import fr.hoteia.qalingo.core.domain.ProductSku;
import fr.hoteia.qalingo.core.domain.Retailer;
import fr.hoteia.qalingo.core.rest.util.JsonFactory;
import fr.hoteia.qalingo.core.service.ProductMarketingService;
import fr.hoteia.qalingo.core.service.ProductSkuService;
import fr.hoteia.qalingo.core.web.servlet.ModelAndViewThemeDevice;
import fr.hoteia.qalingo.web.mvc.controller.AbstractQalingoController;
import fr.hoteia.qalingo.web.mvc.form.ProductMarketingForm;
import fr.hoteia.qalingo.web.mvc.form.ProductSkuForm;
import fr.hoteia.qalingo.web.service.WebBackofficeService;

/**
 * 
 */
@Controller
public class ProductController extends AbstractQalingoController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected JsonFactory jsonFactory;
	
	@Autowired
	protected ProductMarketingService productMarketingService;

	@Autowired
	protected ProductSkuService productSkuService;

	@Autowired
	protected WebBackofficeService webBackofficeService;
	
	@RequestMapping(value = "/catalog-product-details.html*", method = RequestMethod.GET)
	public ModelAndView productMarketingDetails(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		ModelAndViewThemeDevice modelAndView = new ModelAndViewThemeDevice(getCurrentVelocityPath(request), "catalog/product-marketing-details");
		
		final MarketArea currentMarketArea = requestUtil.getCurrentMarketArea(request);
		final Retailer currentRetailer = requestUtil.getCurrentRetailer(request);
		final String productMarketingCode = request.getParameter(Constants.REQUEST_PARAM_PRODUCT_MARKETING_CODE);
		final ProductMarketing productMarketing = productMarketingService.getProductMarketingByCode(currentMarketArea.getId(), currentRetailer.getId(), productMarketingCode);
		
		final String titleKeyPrefixSufix = "business.product.marketing.details";
		initPage(request, response, modelAndView, titleKeyPrefixSufix);
		modelAndViewFactory.initProductMarketingModelAndView(request, modelAndView, productMarketing);
		initSpecificSeo(request, modelAndView, titleKeyPrefixSufix, productMarketing.getBusinessName());
		
        return modelAndView;
	}
	
	@RequestMapping(value = "/catalog-product-edit.html*", method = RequestMethod.GET)
	public ModelAndView productMarketingEdit(final HttpServletRequest request, final HttpServletResponse response, ModelMap modelMap) throws Exception {
		ModelAndViewThemeDevice modelAndView = new ModelAndViewThemeDevice(getCurrentVelocityPath(request), "catalog/product-marketing-form");
		
		final MarketArea currentMarketArea = requestUtil.getCurrentMarketArea(request);
		final Retailer currentRetailer = requestUtil.getCurrentRetailer(request);
		
		final String productMarketingCode = request.getParameter(Constants.REQUEST_PARAM_PRODUCT_MARKETING_CODE);
		final ProductMarketing productMarketing = productMarketingService.getProductMarketingByCode(currentMarketArea.getId(), currentRetailer.getId(), productMarketingCode);

		final String titleKeyPrefixSufix = "business.product.marketing.edit";

		initPage(request, response, modelAndView, titleKeyPrefixSufix);
		modelAndViewFactory.initProductMarketingModelAndView(request, modelAndView, productMarketing);
		modelAndView.addObject("productMarketingForm", formFactory.buildProductMarketingForm(request, productMarketing));
		initSpecificSeo(request, modelAndView, titleKeyPrefixSufix, productMarketing.getBusinessName());

//		modelAndView.addObject("productMarketingDetails", viewBeanFactory.buildUserEditViewBean(request, currentLocalization, user));

		return modelAndView;
	}
	
	@RequestMapping(value = "/catalog-product-form.html*", method = RequestMethod.POST)
	public ModelAndView productMarketingEdit(final HttpServletRequest request, final HttpServletResponse response, @Valid ProductMarketingForm productMarketingForm,
								BindingResult result, ModelMap modelMap) throws Exception {

		final String titleKeyPrefixSufix = "business.product.marketing.edit";
		
		final MarketArea currentMarketArea = requestUtil.getCurrentMarketArea(request);
		final Retailer currentRetailer = requestUtil.getCurrentRetailer(request);
		
		final String productMarketingCode = productMarketingForm.getCode();

		if(StringUtils.isNotEmpty(productMarketingCode)){
			if (result.hasErrors()) {
				ModelAndViewThemeDevice modelAndView = new ModelAndViewThemeDevice(getCurrentVelocityPath(request), "catalog/product-marketing-form");
				final Localization currentLocalization = requestUtil.getCurrentLocalization(request);
				final ProductMarketing productMarketing = productMarketingService.getProductMarketingByCode(currentMarketArea.getId(), currentRetailer.getId(), productMarketingCode);
				initPage(request, response, modelAndView, titleKeyPrefixSufix);
				modelAndViewFactory.initProductMarketingModelAndView(request, modelAndView, productMarketing);
				modelAndView.addObject("productMarketingForm", formFactory.buildProductMarketingForm(request, productMarketing));
				initSpecificSeo(request, modelAndView, titleKeyPrefixSufix, productMarketing.getBusinessName());
				return modelAndView;
			}
			
			// SANITY CHECK
			final ProductMarketing productMarketing = productMarketingService.getProductMarketingByCode(currentMarketArea.getId(), currentRetailer.getId(), productMarketingCode);
			if(productMarketing != null){
				// UPDATE PRODUCT MARKETING
				webBackofficeService.updateProductMarketing(productMarketing, productMarketingForm);
				
			} else {
				// CREATE PRODUCT MARKETING
				webBackofficeService.createProductMarketing(productMarketing, productMarketingForm);

			}
		}
		
		final String urlRedirect = backofficeUrlService.buildProductMarketingDetailsUrl(productMarketingCode);
		
        return new ModelAndView(new RedirectView(urlRedirect));
	}
    
	@RequestMapping(value = "/catalog-product-sku-details.html*", method = RequestMethod.GET)
	public ModelAndView productSkuDetails(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		ModelAndViewThemeDevice modelAndView = new ModelAndViewThemeDevice(getCurrentVelocityPath(request), "catalog/product-sku-details");
		
		final MarketArea currentMarketArea = requestUtil.getCurrentMarketArea(request);
		final Retailer currentRetailer = requestUtil.getCurrentRetailer(request);
		final String productSkuCode = request.getParameter(Constants.REQUEST_PARAM_PRODUCT_SKU_CODE);
		final ProductSku productSku = productSkuService.getProductSkuByCode(currentMarketArea.getId(), currentRetailer.getId(), productSkuCode);

		final String titleKeyPrefixSufix = "business.product.sku.details";
		initPage(request, response, modelAndView, titleKeyPrefixSufix);
		modelAndViewFactory.initProductSkuModelAndView(request, modelAndView, productSku);
		modelAndView.addObject("productSkuForm", formFactory.buildProductSkuForm(request, productSku));
		initSpecificSeo(request, modelAndView, titleKeyPrefixSufix, productSku.getBusinessName());
		
        return modelAndView;
	}
	
	@RequestMapping(value = "/catalog-product-sku-edit.html*", method = RequestMethod.GET)
	public ModelAndView productSkuEdit(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		ModelAndViewThemeDevice modelAndView = new ModelAndViewThemeDevice(getCurrentVelocityPath(request), "catalog/product-sku-form");
		
		final MarketArea currentMarketArea = requestUtil.getCurrentMarketArea(request);
		final Retailer currentRetailer = requestUtil.getCurrentRetailer(request);
		final String productSkuCode = request.getParameter(Constants.REQUEST_PARAM_PRODUCT_SKU_CODE);
		final ProductSku productSku = productSkuService.getProductSkuByCode(currentMarketArea.getId(), currentRetailer.getId(), productSkuCode);
		
		final String titleKeyPrefixSufix = "business.product.sku.edit";
		initPage(request, response, modelAndView, titleKeyPrefixSufix);
		modelAndViewFactory.initProductSkuModelAndView(request, modelAndView, productSku);
		modelAndView.addObject("productSkuForm", formFactory.buildProductSkuForm(request, productSku));
		initSpecificSeo(request, modelAndView, titleKeyPrefixSufix, productSku.getBusinessName());
		
        return modelAndView;
	}
	
	@RequestMapping(value = "/catalog-product-sku-form.html*", method = RequestMethod.POST)
	public ModelAndView productSkuEdit(final HttpServletRequest request, final HttpServletResponse response, @Valid ProductSkuForm productSkuForm,
								BindingResult result, ModelMap modelMap) throws Exception {

		final String titleKeyPrefixSufix = "business.product.marketing.edit";
		
		final MarketArea currentMarketArea = requestUtil.getCurrentMarketArea(request);
		final Retailer currentRetailer = requestUtil.getCurrentRetailer(request);
		
		final String productSkuCode = productSkuForm.getCode();

		if(StringUtils.isNotEmpty(productSkuCode)){
			if (result.hasErrors()) {
				ModelAndViewThemeDevice modelAndView = new ModelAndViewThemeDevice(getCurrentVelocityPath(request), "catalog/product-sku-form");
//				final Localization currentLocalization = requestUtil.getCurrentLocalization(request);
				final ProductSku productSku = productSkuService.getProductSkuByCode(currentMarketArea.getId(), currentRetailer.getId(), productSkuCode);
				initPage(request, response, modelAndView, titleKeyPrefixSufix);
				modelAndViewFactory.initProductSkuModelAndView(request, modelAndView, productSku);
				modelAndView.addObject("productMarketingForm", formFactory.buildProductSkuForm(request, productSku));
				initSpecificSeo(request, modelAndView, titleKeyPrefixSufix, productSku.getBusinessName());
				return modelAndView;
			}
			
			// SANITY CHECK
			final ProductSku productSku = productSkuService.getProductSkuByCode(currentMarketArea.getId(), currentRetailer.getId(), productSkuCode);
			if(productSku != null){
				// UPDATE PRODUCT MARKETING
				webBackofficeService.updateProductSku(productSku, productSkuForm);
				
			} else {
				// CREATE PRODUCT MARKETING
				webBackofficeService.createProductSku(productSku, productSkuForm);

			}
		}
		
		final String urlRedirect = backofficeUrlService.buildProductSkuDetailsUrl(productSkuCode);
        return new ModelAndView(new RedirectView(urlRedirect));
	}
	
	/**
	 * 
	 */
	protected void initSpecificSeo(final HttpServletRequest request, final ModelAndView modelAndView, final String titleKeyPrefixSufix, String productName) throws Exception {
		final Localization currentLocalization = requestUtil.getCurrentLocalization(request);
		final Locale locale = currentLocalization.getLocale();

		String pageTitleKey = "header.title." + titleKeyPrefixSufix;
		String appName = (String) modelAndView.getModelMap().get(Constants.APP_NAME);
		Object[] params = {productName};
		String headerTitle = coreMessageSource.getMessage(pageTitleKey, params, locale);
        modelAndView.addObject("seoPageTitle", appName + " - " + headerTitle);
        modelAndView.addObject("mainContentTitle", headerTitle);
	}
}
