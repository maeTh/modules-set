/**
 * This file is part of Jahia, next-generation open source CMS:
 * Jahia's next-generation, open source CMS stems from a widely acknowledged vision
 * of enterprise application convergence - web, search, document, social and portal -
 * unified by the simplicity of web content management.
 *
 * For more information, please visit http://www.jahia.com.
 *
 * Copyright (C) 2002-2012 Jahia Solutions Group SA. All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * As a special exception to the terms and conditions of version 2.0 of
 * the GPL (or any later version), you may redistribute this Program in connection
 * with Free/Libre and Open Source Software ("FLOSS") applications as described
 * in Jahia's FLOSS exception. You should have received a copy of the text
 * describing the FLOSS exception, and it is also available here:
 * http://www.jahia.com/license
 *
 * Commercial and Supported Versions of the program (dual licensing):
 * alternatively, commercial and supported versions of the program may be used
 * in accordance with the terms and conditions contained in a separate
 * written agreement between you and Jahia Solutions Group SA.
 *
 * If you are unsure which license is appropriate for your use,
 * please contact the sales department at sales@jahia.com.
 */

package org.jahia.modules.formbuilder.initializers;

import org.jahia.services.content.nodetypes.ExtendedPropertyDefinition;
import org.jahia.services.content.nodetypes.ValueImpl;
import org.jahia.services.content.nodetypes.initializers.ChoiceListInitializer;
import org.jahia.services.content.nodetypes.initializers.ChoiceListInitializerService;
import org.jahia.services.content.nodetypes.initializers.ChoiceListValue;
import org.jahia.services.content.nodetypes.initializers.ModuleChoiceListInitializer;

import javax.jcr.PropertyType;
import java.util.*;

/**
 * @author rincevent
 * Created : 8 mars 2010
 */
public class ChoiceListTypeInitializers implements ModuleChoiceListInitializer {
    private String key;
    private Map<String, String> initializersMap;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setInitializersMap(Map<String, String> initializersMap) {
        this.initializersMap = initializersMap;
    }

    public Map<String, String> getInitializersMap() {
        return initializersMap;
    }

    public List<ChoiceListValue> getChoiceListValues(ExtendedPropertyDefinition epd, String param, List<ChoiceListValue> values, Locale locale,
                                                     Map<String, Object> context) {
        Map<String, ChoiceListInitializer> map = ChoiceListInitializerService.getInstance().getInitializers();
        List<ChoiceListValue> vs = new ArrayList<ChoiceListValue>();
        for (Map.Entry<String, String> initializer : initializersMap.entrySet()) {
            String[] subInitializers = initializer.getValue().split(";")[0].split(",");
            boolean valid = true;
            for (String subInitializer : subInitializers) {
                if (!map.containsKey(subInitializer)) {
                    valid = false;
                }
            }
            if (valid) {
                vs.add(new ChoiceListValue(initializer.getKey(), new HashMap<String, Object>(), new ValueImpl(
                        initializer.getValue(), PropertyType.STRING, false)));
            }
        }
        return vs;
    }
}
