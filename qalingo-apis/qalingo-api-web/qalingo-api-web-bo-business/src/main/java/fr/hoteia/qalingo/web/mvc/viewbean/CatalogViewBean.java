/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package fr.hoteia.qalingo.web.mvc.viewbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CatalogViewBean extends AbstractViewBean implements Serializable {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 906812037579319564L;
	
	protected String businessName;
	protected String code;

	protected List<ProductCategoryViewBean> categories = new ArrayList<ProductCategoryViewBean>();

	protected String addRootCategoryUrl;
	protected String addRootCategoryUrlLabel;

	public String getBusinessName() {
		return businessName;
	}
	
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public List<ProductCategoryViewBean> getCategories() {
		return categories;
	}
	
	public void setCategories(List<ProductCategoryViewBean> categories) {
		this.categories = categories;
	}
	
	public String getAddRootCategoryUrl() {
		return addRootCategoryUrl;
	}
	
	public void setAddRootCategoryUrl(String addRootCategoryUrl) {
		this.addRootCategoryUrl = addRootCategoryUrl;
	}
	
	public String getAddRootCategoryUrlLabel() {
		return addRootCategoryUrlLabel;
	}
	
	public void setAddRootCategoryUrlLabel(String addRootCategoryUrlLabel) {
		this.addRootCategoryUrlLabel = addRootCategoryUrlLabel;
	}
	
}
