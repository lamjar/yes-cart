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

/**
 *
 * Responsible to provide plural forms for different languages.
 *
 * Igor Azarny iazarny@yahoo.com
 * Date: 21-Sep-2011
 * Time: 12:42:47
 */
public interface PluralFormService {


   /**
     * Get the resource key, that may depends from quantity of items in shopping cart.
     * The declension of numerals (plural forms) may vary from languages and be complex, see
     * http://translate.sourceforge.net/wiki/l10n/pluralforms or http://www.traktat.com/language/book/chisl/skc.php
     * for details, so better to have something simple at this moment rather than some external libs.
     *
     * @param num         items count.
     * @param lang        two chars iso language code
     * @param form        plural forms array , that hold resource for given language, example EN - item, items, RU - продукт, продуктов, продукта
     *
     * @return plural form
     */
    String getPluralForm(String lang,  int num, String[] form);


}
