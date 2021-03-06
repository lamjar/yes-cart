/*
 * Copyright 2009 - 2016 Denys Pavlov, Igor Azarnyi
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

package org.yes.cart.service.vo.impl;

import org.yes.cart.domain.entity.Attribute;
import org.yes.cart.domain.misc.MutablePair;
import org.yes.cart.domain.vo.VoDashboardWidget;
import org.yes.cart.domain.vo.VoManager;
import org.yes.cart.domain.vo.VoManagerRole;
import org.yes.cart.domain.vo.VoManagerShop;
import org.yes.cart.payment.persistence.entity.PaymentGatewayCallback;
import org.yes.cart.payment.service.PaymentModuleGenericService;
import org.yes.cart.service.domain.AttributeService;
import org.yes.cart.service.domain.ShopService;
import org.yes.cart.service.vo.VoDashboardWidgetPlugin;
import org.yes.cart.service.vo.VoDashboardWidgetService;

import java.util.*;

/**
 * User: denispavlov
 * Date: 13/02/2017
 * Time: 07:19
 */
public class VoDashboardWidgetPluginUnprocessedPgCallbacks extends AbstractVoDashboardWidgetPluginImpl implements VoDashboardWidgetPlugin {

    private List<String> roles = Collections.emptyList();

    private PaymentModuleGenericService<PaymentGatewayCallback> paymentModuleGenericService;
    private final ShopService shopService;

    public VoDashboardWidgetPluginUnprocessedPgCallbacks(final PaymentModuleGenericService<PaymentGatewayCallback> paymentModuleGenericService,
                                                         final ShopService shopService,
                                                         final AttributeService attributeService,
                                                         final String widgetName) {
        super(attributeService, widgetName);
        this.paymentModuleGenericService = paymentModuleGenericService;
        this.shopService = shopService;
    }

    @Override
    public boolean applicable(final VoManager manager) {
        if (manager.getManagerShops().size() > 0) {
            for (final VoManagerRole role : manager.getManagerRoles()) {
                if (roles.contains(role.getCode())) {
                    return manager.getManagerShops().size() > 0;
                }
            }
        }
        return false;
    }

    @Override
    protected void processWidgetData(final VoManager manager, final VoDashboardWidget widget, final Attribute config) {

        final Set<String> shops = new HashSet<>();
        for (final VoManagerShop shop : manager.getManagerShops()) {
            shops.add(this.shopService.getById(shop.getShopId()).getCode());
            final Set<Long> subs = this.shopService.getAllShopsAndSubs().get(shop.getShopId());
            if (subs != null) {
                shops.add(this.shopService.getById(shop.getShopId()).getCode());
            }
        }

        final Map<String, Integer> counts = new HashMap<>();
        final List<PaymentGatewayCallback> callbacks = this.paymentModuleGenericService.findByCriteria(
                " where e.processed = ?1 and e.shopCode in (?2)",
                Boolean.FALSE,
                shops
        );
        for (final PaymentGatewayCallback callback : callbacks) {

            counts.merge(callback.getLabel(), 1, (a, b) -> a + b);

        }

        final List<MutablePair<String, Integer>> data = new ArrayList<>();
        if (counts.isEmpty()) {
            data.add(new MutablePair<>("-", 0));
        } else {
            for (final Map.Entry<String, Integer> entry : counts.entrySet()) {
                data.add(new MutablePair<>(entry.getKey(), entry.getValue()));
            }
        }

        widget.setData(new HashMap<String, Object>() {{
            put("hasUnprocessed", !counts.isEmpty());
            put("unprocessed", data);
        }});

    }

    /**
     * Spring IoC.
     *
     * @param roles roles for accessing this widget
     */
    public void setRoles(final List<String> roles) {
        this.roles = roles;
    }

    /**
     * Spring IoC.
     *
     * @param dashboardWidgetService dashboard service
     */
    public void setDashboardWidgetService(VoDashboardWidgetService dashboardWidgetService) {
        dashboardWidgetService.registerWidgetPlugin(this);
    }

    public void setProductService(final PaymentModuleGenericService<PaymentGatewayCallback> paymentModuleGenericService) {
        this.paymentModuleGenericService = paymentModuleGenericService;
    }

}
