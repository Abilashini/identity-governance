/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.mgt.admin.ui.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.event.EventMgtConfigBuilder;
import org.wso2.carbon.identity.event.EventMgtConstants;
import org.wso2.carbon.identity.event.EventMgtException;
import org.wso2.carbon.identity.event.event.Event;
import org.wso2.carbon.identity.event.internal.EventMgtServiceDataHolder;
import org.wso2.carbon.identity.event.services.EventMgtService;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.common.AbstractUserOperationEventListener;

import java.util.HashMap;


/**
 * This is an implementation of UserOperationEventListener. This defines
 * additional operations
 * for some of the core user management operations
 */
public class IdentityMgtEventListener extends AbstractUserOperationEventListener {

    private static final Log log = LogFactory.getLog(IdentityMgtEventListener.class);
    EventMgtService eventMgtService = EventMgtServiceDataHolder.getInstance().getEventMgtService();

    /**
     * This method checks if the user account exist or is locked. If the account is
     * locked, the authentication process will be terminated after this method
     * returning false.
     */
    @Override
    public boolean doPreAuthenticate(String userName, Object credential,
                                     UserStoreManager userStoreManager) throws UserStoreException {

        if (log.isDebugEnabled()) {
            log.debug("Pre authenticator is called in IdentityMgtEventListener");
        }
        try {
            String eventName = EventMgtConstants.Event.PRE_AUTHENTICATION;

            HashMap<String, Object> properties = new HashMap<>();
            properties.put(EventMgtConstants.EventProperty.USER_NAME, userName);
            properties.put(EventMgtConstants.EventProperty.USER_STORE_MANAGER, userStoreManager);
            properties.put(EventMgtConstants.EventProperty.IDENTITY_MGT_CONFIG, EventMgtConfigBuilder
                    .getInstance());
            properties.put(EventMgtConstants.EventProperty.TENANT_ID, PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getTenantId());

            Event identityMgtEvent = new Event(eventName, properties);

            eventMgtService.handleEvent(identityMgtEvent);
        } catch (EventMgtException e) {
            throw new UserStoreException("Error when authenticating user", e);
        }

        return true;
    }

    @Override
    public boolean doPostAuthenticate(String userName, boolean authenticated, UserStoreManager userStoreManager)
            throws UserStoreException {

        if (log.isDebugEnabled()) {
            log.debug("Pre authenticator is called in IdentityMgtEventListener");
        }
        try {
            String eventName = EventMgtConstants.Event.POST_AUTHENTICATION;

            HashMap<String, Object> properties = new HashMap<>();
//            properties.put(EventMgtConstants.EventProperty.MODULE, module);
            properties.put(EventMgtConstants.EventProperty.USER_NAME, userName);
            properties.put(EventMgtConstants.EventProperty.USER_STORE_MANAGER, userStoreManager);
            properties.put(EventMgtConstants.EventProperty.IDENTITY_MGT_CONFIG, EventMgtConfigBuilder
                    .getInstance());
            properties.put(EventMgtConstants.EventProperty.TENANT_ID, PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getTenantId());
            properties.put(EventMgtConstants.EventProperty.OPERATION_STATUS, authenticated);
            Event identityMgtEvent = new Event(eventName, properties);

            eventMgtService.handleEvent(identityMgtEvent);
        } catch (EventMgtException e) {
            throw new UserStoreException("Error when authenticating user", e);
        }

        return true;
    }

    /**
     * This method retrieves the configurations for the tenant ID of the user
     */
//    protected IdentityMgtConfig getConfiguration(int tenantId) {
//        IdentityMgtConfig identityMgtConfig = null;
//        try {
//            identityMgtConfig = new IdentityMgtConfig();
//            Properties properties = identityMgtConfig.getConfiguration(tenantId);
//            identityMgtConfig.setConfiguration(properties);
//        } catch (IdentityMgtException ex) {
//            log.error("Error when retrieving configurations of tenant: " + tenantId, ex);
//        }
//        return identityMgtConfig;
//        return null;
//    }
}
