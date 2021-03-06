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

package org.jahia.modules.userregistration.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jahia.bin.ActionResult;
import org.jahia.bin.Action;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.mail.MailService;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.jahia.services.usermanager.JahiaUser;
import org.jahia.services.usermanager.JahiaUserManagerService;
import org.jahia.settings.SettingsBean;
import org.json.JSONObject;

/**
 * Action handler for creating new user and sending an e-mail notification.
 *
 * @author : rincevent
 * @since : JAHIA 6.1
 *        Created : 29 juin 2010
 */
public class NewUser extends Action {

    private JahiaUserManagerService userManagerService;
    private MailService mailService;
    private String templatePath;

    public void setUserManagerService(JahiaUserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, Resource resource,
                                  JCRSessionWrapper session, Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {

        String username = getParameter(parameters, "username");
        String password = getParameter(parameters, "password");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ActionResult.BAD_REQUEST;
        }

        Properties properties = new Properties();
        properties.put("j:email",parameters.get("desired_email").get(0));
        properties.put("j:firstName",parameters.get("desired_firstname").get(0));
        properties.put("j:lastName",parameters.get("desired_lastname").get(0));
        for (Map.Entry<String, List<String>> param : parameters.entrySet()) {
            if (param.getKey().startsWith("j:")) {
                String value = getParameter(parameters, param.getKey());
                if (value != null) {
                    properties.put(param.getKey(), value);
                }
            }
        }

        final JahiaUser user = userManagerService.createUser(username, password, properties);

        // Prepare mail to be sent :
        boolean toAdministratorMail = Boolean.valueOf(getParameter(parameters, "toAdministrator", "false"));
        String to = toAdministratorMail ? SettingsBean.getInstance().getMail_administrator():getParameter(parameters, "to");
        String from = parameters.get("from")==null?SettingsBean.getInstance().getMail_from():getParameter(parameters, "from");
        String cc = parameters.get("cc")==null?null:getParameter(parameters, "cc");
        String bcc = parameters.get("bcc")==null?null:getParameter(parameters, "bcc");
        
        Map<String,Object> bindings = new HashMap<String,Object>();
        bindings.put("newUser",user);
        if (mailService.isEnabled()) {
            mailService.sendMessageWithTemplate(templatePath,bindings,to,from,cc,bcc,resource.getLocale(),"Jahia User Registration");
        }
        return new ActionResult(HttpServletResponse.SC_ACCEPTED,parameters.get("userredirectpage").get(0), new JSONObject());
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }
}
