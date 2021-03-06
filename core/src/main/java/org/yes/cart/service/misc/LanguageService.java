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

package org.yes.cart.service.misc;

import java.util.List;
import java.util.Map;

/**
 * User: Igor Azarny iazarny@yahoo.com
 * Date: 6/25/11
 * Time: 10:19 PM
 */
public interface  LanguageService {

    /**
     * Get most appropriate  full language name.
     *
     * @param language to char language code.
     *
     * @return language name
     */
    String resolveLanguageName(String language);

    /**
     * Get supported languages.
     *
     * @return  supported languages.
     */
    Map<String, String> getLanguageName();

    /**
     * Get supported languages list.
     *
     * @return  supported languages list.
     */
    List<String> getSupportedLanguages();

    /**
     * Get shop specific supported languages list. By convention the 0th
     * element is the default language
     *
     * @param shopCode languages supported by shop instance
     *
     * @return  supported languages list.
     */
    List<String> getSupportedLanguages(String shopCode);

}
