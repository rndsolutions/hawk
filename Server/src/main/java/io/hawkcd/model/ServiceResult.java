/*
 * Copyright (C) 2016 R&D Solutions Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.hawkcd.model;

import io.hawkcd.model.enums.NotificationType;

public class ServiceResult {
    private Object entity;
    private NotificationType notificationType;
    private String message;

    public ServiceResult() {

    }

    public ServiceResult(Object entity, NotificationType notificationType, String message) {
        this.entity = entity;
        this.notificationType = notificationType;
        this.message = message;
    }

    public Object getEntity() {
        return this.entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public NotificationType getNotificationType() {
        return this.notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
