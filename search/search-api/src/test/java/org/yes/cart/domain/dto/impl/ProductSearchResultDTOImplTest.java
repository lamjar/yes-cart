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

package org.yes.cart.domain.dto.impl;

import org.junit.Test;
import org.yes.cart.domain.dto.ProductSearchResultDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * User: Denis
 * Date: 6/9/14
 * Time: 1:19 PM
 */
public class ProductSearchResultDTOImplTest {
    @Test
    public void testCopy() throws Exception {

        final ProductSearchResultDTOImpl first = new ProductSearchResultDTOImpl();
        first.setId(1);
        first.setCode("First");
        first.setManufacturerCode("ManFirst");
        first.setDefaultSkuCode("FirstCode");
        first.setName("FirstName");
        first.setDisplayName("FirstDisplayName");
        first.setDescription("FirstDescription");
        first.setDisplayDescription("FirstDisplayDescription");
        first.setType("typeA");
        first.setDisplayType("displayTypeA");
        first.setService(true);
        first.setEnsemble(true);
        first.setShippable(true);
        first.setDigital(true);
        first.setDownloadable(true);
        first.setTag("tag");
        first.setBrand("brand");
        first.setAvailablefrom(LocalDateTime.now());
        first.setAvailableto(LocalDateTime.now());
        first.setCreatedTimestamp(Instant.now());
        first.setUpdatedTimestamp(Instant.now());
        first.setAvailability(1);
        first.setQtyOnWarehouse(new HashMap<Long, Map<String, BigDecimal>>() {{
            put(10L, new HashMap<>());
        }});
        first.setDefaultImage("FirstDefaultImage");
        first.setFeatured(true);

        final ProductSearchResultDTO copy = first.copy();

        assertEquals(first.getId(), copy.getId());
        assertEquals(first.getCode(), copy.getCode());
        assertEquals(first.getManufacturerCode(), copy.getManufacturerCode());
        assertEquals(first.isMultisku(), copy.isMultisku());
        assertEquals(first.getDefaultSkuCode(), copy.getDefaultSkuCode());
        assertEquals(first.getName(), copy.getName());
        assertEquals(first.getDisplayName(), copy.getDisplayName());
        assertEquals(first.getDescription(), copy.getDescription());
        assertEquals(first.getDisplayDescription(), copy.getDisplayDescription());
        assertEquals(first.getType(), copy.getType());
        assertEquals(first.getDisplayType(), copy.getDisplayType());
        assertTrue(copy.isService());
        assertTrue(copy.isEnsemble());
        assertTrue(copy.isShippable());
        assertTrue(copy.isDigital());
        assertTrue(copy.isDownloadable());
        assertEquals(first.getTag(), copy.getTag());
        assertEquals(first.getBrand(), copy.getBrand());
        assertEquals(first.getAvailablefrom(), copy.getAvailablefrom());
        assertEquals(first.getAvailableto(), copy.getAvailableto());
        assertEquals(first.getCreatedTimestamp(), copy.getCreatedTimestamp());
        assertEquals(first.getUpdatedTimestamp(), copy.getUpdatedTimestamp());
        assertEquals(first.getAvailability(), copy.getAvailability());
        assertEquals(first.getQtyOnWarehouse(10L), copy.getQtyOnWarehouse(10L));
        assertEquals(first.getDefaultImage(), copy.getDefaultImage());
        assertEquals(first.getFeatured(), copy.getFeatured());


        // Do not override equals and hash code for ProductSearchResultDTO because we can have
        // multiple copies in memory used by hash maps and hash sets
        assertFalse(first.equals(copy));
        assertFalse(first.hashCode() == copy.hashCode());

    }

    @Test
    public void testTypeMask() throws Exception {

        final ProductSearchResultDTOImpl dto = new ProductSearchResultDTOImpl();

        assertFalse(dto.isService());
        assertFalse(dto.isEnsemble());
        assertFalse(dto.isShippable());
        assertFalse(dto.isDigital());
        assertFalse(dto.isDownloadable());

        dto.setService(true);
        dto.setEnsemble(false);
        dto.setShippable(false);
        dto.setDigital(false);
        dto.setDownloadable(false);

        assertTrue(dto.isService());
        assertFalse(dto.isEnsemble());
        assertFalse(dto.isShippable());
        assertFalse(dto.isDigital());
        assertFalse(dto.isDownloadable());

        dto.setService(false);
        dto.setEnsemble(true);
        dto.setShippable(false);
        dto.setDigital(false);
        dto.setDownloadable(false);

        assertFalse(dto.isService());
        assertTrue(dto.isEnsemble());
        assertFalse(dto.isShippable());
        assertFalse(dto.isDigital());
        assertFalse(dto.isDownloadable());

        dto.setService(false);
        dto.setEnsemble(false);
        dto.setShippable(true);
        dto.setDigital(false);
        dto.setDownloadable(false);

        assertFalse(dto.isService());
        assertFalse(dto.isEnsemble());
        assertTrue(dto.isShippable());
        assertFalse(dto.isDigital());
        assertFalse(dto.isDownloadable());

        dto.setService(false);
        dto.setEnsemble(false);
        dto.setShippable(false);
        dto.setDigital(true);
        dto.setDownloadable(false);

        assertFalse(dto.isService());
        assertFalse(dto.isEnsemble());
        assertFalse(dto.isShippable());
        assertTrue(dto.isDigital());
        assertFalse(dto.isDownloadable());

        dto.setService(false);
        dto.setEnsemble(false);
        dto.setShippable(false);
        dto.setDigital(false);
        dto.setDownloadable(true);

        assertFalse(dto.isService());
        assertFalse(dto.isEnsemble());
        assertFalse(dto.isShippable());
        assertFalse(dto.isDigital());
        assertTrue(dto.isDownloadable());

        dto.setService(false);
        dto.setEnsemble(false);
        dto.setShippable(false);
        dto.setDigital(false);
        dto.setDownloadable(false);

        assertFalse(dto.isService());
        assertFalse(dto.isEnsemble());
        assertFalse(dto.isShippable());
        assertFalse(dto.isDigital());
        assertFalse(dto.isDownloadable());

        dto.setService(false);
        dto.setEnsemble(false);
        dto.setShippable(false);
        dto.setDigital(false);
        dto.setDownloadable(false);

        assertFalse(dto.isService());
        assertFalse(dto.isEnsemble());
        assertFalse(dto.isShippable());
        assertFalse(dto.isDigital());
        assertFalse(dto.isDownloadable());

        dto.setService(true);
        dto.setEnsemble(true);
        dto.setShippable(true);
        dto.setDigital(true);
        dto.setDownloadable(true);

        assertTrue(dto.isService());
        assertTrue(dto.isEnsemble());
        assertTrue(dto.isShippable());
        assertTrue(dto.isDigital());
        assertTrue(dto.isDownloadable());

        dto.setService(false);
        dto.setEnsemble(false);
        dto.setShippable(false);
        dto.setDigital(false);
        dto.setDownloadable(false);

        assertFalse(dto.isService());
        assertFalse(dto.isEnsemble());
        assertFalse(dto.isShippable());
        assertFalse(dto.isDigital());
        assertFalse(dto.isDownloadable());

    }
}
