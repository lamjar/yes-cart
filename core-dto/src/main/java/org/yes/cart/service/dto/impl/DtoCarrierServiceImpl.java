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

package org.yes.cart.service.dto.impl;

import com.inspiresoftware.lib.dto.geda.adapter.repository.AdaptersRepository;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import org.yes.cart.dao.GenericDAO;
import org.yes.cart.domain.dto.CarrierDTO;
import org.yes.cart.domain.dto.ShopDTO;
import org.yes.cart.domain.dto.factory.DtoFactory;
import org.yes.cart.domain.dto.impl.CarrierDTOImpl;
import org.yes.cart.domain.dto.impl.ShopDTOImpl;
import org.yes.cart.domain.entity.Carrier;
import org.yes.cart.domain.entity.CarrierShop;
import org.yes.cart.domain.entity.Shop;
import org.yes.cart.exception.UnableToCreateInstanceException;
import org.yes.cart.exception.UnmappedInterfaceException;
import org.yes.cart.service.domain.CarrierService;
import org.yes.cart.service.domain.GenericService;
import org.yes.cart.service.dto.DtoCarrierService;

import java.util.*;

/**
 * User: Igor Azarny iazarny@yahoo.com
 * Date: 09-May-2011
 * Time: 14:12:54
 */
public class DtoCarrierServiceImpl
        extends AbstractDtoServiceImpl<CarrierDTO, CarrierDTOImpl, Carrier>
        implements DtoCarrierService {


    private final GenericDAO<Shop, Long> shopDao;

    private final Assembler shopAssembler;

    /**
     * Construct service.
     *
     * @param dtoFactory               dto factory
     * @param carrierGenericService    generic service to use
     * @param adaptersRepository       converter factory.
     */
    public DtoCarrierServiceImpl(final GenericService<Carrier> carrierGenericService,
                                 final GenericDAO<Shop, Long> shopDao,
                                 final DtoFactory dtoFactory,
                                 final AdaptersRepository adaptersRepository) {
        super(dtoFactory, carrierGenericService, adaptersRepository);

        this.shopDao = shopDao;

        shopAssembler = DTOAssembler.newAssembler(ShopDTOImpl.class, Shop.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<CarrierDTO, Map<ShopDTO, Boolean>> getAllWithShops() throws UnmappedInterfaceException, UnableToCreateInstanceException {
        final List<Carrier> all = getService().findAll();
        final Map<CarrierDTO, Map<ShopDTO, Boolean>> dtos = new LinkedHashMap<>(all.size() * 2);
        for (final Carrier carrier : all) {
            final CarrierDTO dto = getNew();
            assembler.assembleDto(dto, carrier, getAdaptersRepository(), entityFactory);
            createPostProcess(dto, carrier);
            final Map<ShopDTO, Boolean> assignments = getShopAssignmentsForCarrier(carrier);
            dtos.put(dto, assignments);
        }
        return dtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<CarrierDTO, Boolean> findAllByShopId(final long shopId) throws UnmappedInterfaceException, UnableToCreateInstanceException {
        final List<Carrier> all = ((CarrierService) getService()).findCarriersByShopId(shopId, true);
        final List<CarrierDTO> dtos = new ArrayList<>(all.size());
        fillDTOs(all, dtos);
        final Map<Long, Boolean> disabledMap = new HashMap<>(dtos.size() * 2);
        for (final Carrier carrier : all) {
            for (final CarrierShop shop : carrier.getShops()) {
                if (shop.getShop().getShopId() == shopId) {
                    disabledMap.put(carrier.getCarrierId(), shop.isDisabled());
                    break;
                }
            }
        }
        final Map<CarrierDTO, Boolean> dtoPairs = new LinkedHashMap<>(all.size());
        for (final CarrierDTO dto : dtos) {
            dtoPairs.put(dto, disabledMap.get(dto.getCarrierId()));
        }
        return dtoPairs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<ShopDTO, Boolean> getAssignedCarrierShops(final long carrierId) throws UnmappedInterfaceException, UnableToCreateInstanceException {
        final Carrier carrier = getService().findById(carrierId);
        return getShopAssignmentsForCarrier(carrier);
    }

    private Map<ShopDTO, Boolean> getShopAssignmentsForCarrier(final Carrier carrier) throws UnmappedInterfaceException, UnableToCreateInstanceException {
        if (carrier == null) {
            return Collections.emptyMap();
        }
        final Collection<CarrierShop> assigned = carrier.getShops();
        final Map<Long, Boolean> enabledMap = new HashMap<>(assigned.size() * 2);
        for (final CarrierShop shop : assigned) {
            enabledMap.put(shop.getShop().getShopId(), shop.isDisabled());
        }
        final List<ShopDTO> shopDTOs = new ArrayList<>(assigned.size());
        fillCarrierShopsDTOs(shopDTOs, assigned);
        final Map<ShopDTO, Boolean> dtoPairs = new LinkedHashMap<>(shopDTOs.size());
        for (final ShopDTO dto : shopDTOs) {
            dtoPairs.put(dto, enabledMap.get(dto.getShopId()));
        }
        return dtoPairs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ShopDTO> getAvailableCarrierShops(final long carrierId) throws UnmappedInterfaceException, UnableToCreateInstanceException {
        final Carrier carrier = getService().findById(carrierId);
        if (carrier == null) {
            return Collections.emptyList();
        }
        final List<Shop> all = shopDao.findAll();
        final Iterator<Shop> allIt = all.iterator();
        while (allIt.hasNext()) {
            final Shop current = allIt.next();
            for (final CarrierShop shop : carrier.getShops()) {
                if (shop.getShop().getShopId() == current.getShopId()) {
                    allIt.remove();
                }
            }
        }

        final List<ShopDTO> shopDTOs = new ArrayList<>(all.size());
        fillShopsDTOs(shopDTOs, all);

        return shopDTOs;
    }


    private void fillCarrierShopsDTOs(final List<ShopDTO> result, final Collection<CarrierShop> shops)
            throws UnmappedInterfaceException, UnableToCreateInstanceException {
        for (CarrierShop shop : shops) {
            final ShopDTO shopDTO = dtoFactory.getByIface(ShopDTO.class);
            shopAssembler.assembleDto(shopDTO, shop.getShop(), getAdaptersRepository(), dtoFactory);
            result.add(shopDTO);
        }
    }

    private void fillShopsDTOs(final List<ShopDTO> result, final Collection<Shop> shops)
            throws UnmappedInterfaceException, UnableToCreateInstanceException {
        for (Shop shop : shops) {
            final ShopDTO shopDTO = dtoFactory.getByIface(ShopDTO.class);
            shopAssembler.assembleDto(shopDTO, shop, getAdaptersRepository(), dtoFactory);
            result.add(shopDTO);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void assignToShop(final long carrierId, final long shopId, final boolean soft) {
        final Carrier carrier = getService().findById(carrierId);
        final Collection<CarrierShop> assigned = carrier.getShops();
        for (final CarrierShop shop : assigned) {
            if (shop.getShop().getShopId() == shopId) {
                if (shop.isDisabled() && !soft) {
                    shop.setDisabled(false);
                    getService().update(carrier);
                }
                return;
            }
        }
        final Shop shop = shopDao.findById(shopId);
        if (shop != null) {
            final CarrierShop carrierShop = shopDao.getEntityFactory().getByIface(CarrierShop.class);
            carrierShop.setCarrier(carrier);
            carrierShop.setShop(shop);
            carrierShop.setDisabled(soft);
            assigned.add(carrierShop);
        }
        getService().update(carrier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unassignFromShop(final long carrierId, final long shopId, final boolean soft) {
        final Carrier carrier = getService().findById(carrierId);
        final Iterator<CarrierShop> assigned = carrier.getShops().iterator();
        while (assigned.hasNext()) {
            final CarrierShop shop = assigned.next();
            if (shop.getShop().getShopId() == shopId) {
                if (soft) {
                    shop.setDisabled(true);
                } else {
                    assigned.remove();
                }
                getService().update(carrier);
                break;
            }
        }
    }

    /**
     * Get the dto interface.
     *
     * @return dto interface.
     */
    @Override
    public Class<CarrierDTO> getDtoIFace() {
        return CarrierDTO.class;
    }

    /**
     * Get the dto implementation class.
     *
     * @return dto implementation class.
     */
    @Override
    public Class<CarrierDTOImpl> getDtoImpl() {
        return CarrierDTOImpl.class;
    }

    /**
     * Get the entity interface.
     *
     * @return entity interface.
     */
    @Override
    public Class<Carrier> getEntityIFace() {
        return Carrier.class;
    }
}
