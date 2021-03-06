/*
 *   Copyright (C) 2016 R&D Solutions Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *
 */

package io.hawkcd.core;

import io.hawkcd.core.subscriber.Envelope;
import io.hawkcd.model.ServiceResult;
import io.hawkcd.model.User;
import io.hawkcd.model.enums.NotificationType;
import io.hawkcd.model.enums.PermissionType;

import java.util.HashMap;
import java.util.Map;

/**
 * The Message class represents a wrapper message object that's sent by Publishers to Subscribers.
 * In addition to the message that has to be sent to users it also carries various matadata.
 */
public class Message {
    private String serviceCalled;
    private String methodCalled;
    private String packageName;

    //True if the message should be delivered only to the caller
    private boolean isTargetOwner;
    private boolean isUserUpdate;

    //Holds the result being returned by the service call
    private Envelope envelopе;
    private NotificationType resultNotificationType;
    private String resultMessage;

    //The user context in which the message is created
    private User owner;
    private Map<String, PermissionType> permissionTypeByUser;

    public Message(Envelope envelopе) {
        this.envelopе = envelopе;
        this.permissionTypeByUser = new HashMap<>();
    }

    public Message(String serviceCalled, String methodCalled, ServiceResult serviceResult, User usr) {
        this.serviceCalled = serviceCalled;
        this.methodCalled = methodCalled;
        this.owner = usr;
        if (serviceResult != null) {
            this.envelopе = new Envelope(serviceResult.getEntity());
            this.resultNotificationType = serviceResult.getNotificationType();
            this.resultMessage = serviceResult.getMessage();
        }

        this.permissionTypeByUser = new HashMap<>();
    }

    public Message(String serviceCalled,
            String packageName,
            String methodCalled,
            Object resultObject,
            NotificationType resultNotificationType,
            String resultMessage,
            User user) {
        this.serviceCalled = serviceCalled;
        this.packageName = packageName;
        this.methodCalled = methodCalled;
        this.envelopе = new Envelope(resultObject);
        this.resultNotificationType = resultNotificationType;
        this.resultMessage = resultMessage;
        this.owner = user;
        this.permissionTypeByUser = new HashMap<>();
    }

    public Message(String serviceCalled,
            String packageName,
            String methodCalled,
            Object resultObject,
            NotificationType resultNotificationType,
            String resultMessage,
            User user,
            boolean isTargetOwner) {
        this(serviceCalled, packageName, methodCalled, resultObject, resultNotificationType, resultMessage, user);
        this.setTargetOwner(isTargetOwner);
    }

    @Override
    public String toString() {
        return "Message{" +
                "serviceCalled='" + serviceCalled + '\'' +
                ", methodCalled='" + methodCalled + '\'' +
                ", resultNotificationType=" + resultNotificationType +
                ", resultMessage='" + resultMessage + '\'' +
                ", owner=" + owner +
                '}';
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getServiceCalled() {
        return serviceCalled;
    }

    public String getMethodCalled() {
        return methodCalled;
    }

    public boolean isTargetOwner() {
        return isTargetOwner;
    }

    public void setTargetOwner(boolean targetOwner) {
        isTargetOwner = targetOwner;
    }

    public boolean isUserUpdate() {
        return isUserUpdate;
    }

    public void setUserUpdate(boolean userUpdate) {
        isUserUpdate = userUpdate;
    }

    public Object getEnvelope() {
        return this.envelopе.getObject();
    }

    public void setEnvelope(Object object) {
        this.envelopе.setObject(object);
    }

    public NotificationType getResultNotificationType() {
        return resultNotificationType;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setOwner(User usr) {
        this.owner = usr;
    }

    public User getOwner() {
        return this.owner;
    }

    public Map<String, PermissionType> getPermissionTypeByUser() {
        return permissionTypeByUser;
    }

    public void setPermissionTypeByUser(Map<String, PermissionType> permissionTypeByUser) {
        this.permissionTypeByUser = permissionTypeByUser;
    }
}
