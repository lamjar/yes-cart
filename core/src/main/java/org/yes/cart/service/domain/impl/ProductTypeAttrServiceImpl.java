/*
 * Copyright 2009 Denys Pavlov, Igor Azarnyi
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.yes.cart.service.domain.impl;

import org.yes.cart.dao.GenericDAO;
import org.yes.cart.domain.entity.ProdTypeAttributeViewGroup;
import org.yes.cart.domain.entity.ProductTypeAttr;
import org.yes.cart.service.domain.ProductTypeAttrService;

import java.util.List;

/**
 * User: Igor Azarny iazarny@yahoo.com
 * Date: 09-May-2011
 * Time: 14:12:54
 */
public class ProductTypeAttrServiceImpl extends BaseGenericServiceImpl<ProductTypeAttr> implements ProductTypeAttrService {

    /**
     * Construct service.
     * @param genericDao product type attribute dao to use.
     */
    public ProductTypeAttrServiceImpl(final GenericDAO<ProductTypeAttr, Long> genericDao) {
        super(genericDao);
    }

    /** {@inheritDoc} */
    @Override
    public List<ProdTypeAttributeViewGroup> getViewGroupsByProductTypeId(final long productTypeId) {
        final List<ProdTypeAttributeViewGroup> groups = (List) getGenericDao()
                .findQueryObjectByNamedQuery("PRODUCT.TYPE.VIEWGROUP.BY.PROD.TYPE.ID", productTypeId);
        // Need to sort this here as ordering has adverse effect on query - do not use "order by rank"
        groups.sort((g1, g2) -> g1.getRank() - g2.getRank());
        return groups;
    }

    /** {@inheritDoc} */
    @Override
    public List<ProductTypeAttr> getByProductTypeId(final long productTypeId) {
        final List<ProductTypeAttr> attrs = getGenericDao().findByNamedQuery("PRODUCT.TYPE.ATTR.BY.PROD.TYPE.ID", productTypeId);
        // Need to sort this here as ordering has adverse effect on query - do not use "order by rank"
        attrs.sort((a1, a2) -> a1.getRank() - a2.getRank());
        return attrs;
    }

    /** {@inheritDoc} */
    @Override
    public List<ProductTypeAttr> getNavigatableByProductTypeId(final long productTypeId) {
        final List<ProductTypeAttr> attrs = getGenericDao().findByNamedQuery("PRODUCT.TYPE.NAV.ATTR.BY.PROD.TYPE.ID", productTypeId, Boolean.TRUE, Boolean.TRUE);
        // Need to sort this here as ordering has adverse effect on query - do not use "order by rank"
        attrs.sort((a1, a2) -> a1.getRank() - a2.getRank());
        return attrs;
    }

}
