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

package io.hawkcd.services;

import io.hawkcd.core.Message;
import io.hawkcd.core.MessageDispatcher;
import io.hawkcd.core.security.*;
import io.hawkcd.db.DbRepositoryFactory;
import io.hawkcd.db.IDbRepository;
import io.hawkcd.model.ServiceResult;
import io.hawkcd.model.User;
import io.hawkcd.model.UserGroup;
import io.hawkcd.model.dto.UserGroupDto;
import io.hawkcd.model.enums.NotificationType;
import io.hawkcd.model.enums.PermissionScope;
import io.hawkcd.model.enums.PermissionType;
import io.hawkcd.services.interfaces.IUserGroupService;
import io.hawkcd.services.interfaces.IUserService;

import java.util.ArrayList;
import java.util.List;

public class UserGroupService extends CrudService<UserGroup> implements IUserGroupService {
    private static final Class CLASS_TYPE = UserGroup.class;

    private IUserService userService;

    public UserGroupService() {
        IDbRepository repository = DbRepositoryFactory.create(DATABASE_TYPE, CLASS_TYPE);
        super.setRepository(repository);
        super.setObjectType(CLASS_TYPE.getSimpleName());

        this.userService = new UserService();
    }

    public UserGroupService(IDbRepository repository, IUserService userService) {
        super.setRepository(repository);
        this.userService = userService;
        super.setObjectType(CLASS_TYPE.getSimpleName());
    }

    @Override
    @Authorization(scope = PermissionScope.SERVER, type = PermissionType.ADMIN)
    public ServiceResult getById(String id) {
        return super.getById(id);
    }

    @Override
    @Authorization(scope = PermissionScope.SERVER, type = PermissionType.NONE)
    public ServiceResult getAll() {
        return super.getAll();
    }

    @Override
    @Authorization(scope = PermissionScope.SERVER, type = PermissionType.ADMIN)
    public ServiceResult add(UserGroup userGroup) {
        return super.add(userGroup);
    }

    @Override
    @Authorization(scope = PermissionScope.SERVER, type = PermissionType.ADMIN)
    public ServiceResult addUserGroupDto(UserGroupDto userGroupDto) {
        UserGroup userGroup = new UserGroup();
        userGroup.setName(userGroupDto.getName());

        this.add(userGroup);

        UserGroupDto updatedUserGroupDto = this.getUserGroupDto(userGroup);
        ServiceResult result = new ServiceResult(updatedUserGroupDto, NotificationType.SUCCESS, "UserGroup created successfully.");

        return result;
    }

    @Override
//    @Authorization(scope = PermissionScope.SERVER, type = PermissionType.ADMIN)
    public ServiceResult update(UserGroup userGroup) {
        ServiceResult result = super.update(userGroup);
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        String methodName = ste[1].getMethodName();
        String className = this.getClass().getSimpleName();

        Message message = AuthorizationFactory.getAuthorizationManager().constructAuthorizedMessage(result,className,methodName);

        MessageDispatcher.dispatchIncomingMessage(message);

        return result;
    }

    @Override
    @Authorization(scope = PermissionScope.SERVER, type = PermissionType.ADMIN)
    public ServiceResult updateUserGroupDto(UserGroupDto userGroupDto) {
        UserGroup userGroup = (UserGroup) this.getById(userGroupDto.getId()).getEntity();
        userGroup.setName(userGroupDto.getName());
        userGroup.setUserIds(userGroupDto.getUserIds());
        userGroup.setPermissions(userGroupDto.getPermissions());

        this.update(userGroup);

        UserGroupDto updatedUserGroupDto = this.getUserGroupDto(userGroup);
        ServiceResult result = new ServiceResult(updatedUserGroupDto, NotificationType.SUCCESS, "UserGroup updated successfully.");

        for (String userId : userGroup.getUserIds()) {
//            SessionPool.getInstance().updateUserObjects(userId);
        }

        return result;
    }

    @Override
    @Authorization(scope = PermissionScope.SERVER, type = PermissionType.ADMIN)
    public ServiceResult delete(UserGroup userGroup) {
        UserGroup userGroupToDelete = (UserGroup) this.getById(userGroup.getId()).getEntity();
        if (userGroupToDelete == null) {
            return super.createServiceResult(null, NotificationType.ERROR, "does not exist.");
        }

        if (!userGroupToDelete.getUserIds().isEmpty()) {
            return super.createServiceResult(null, NotificationType.ERROR, "has Users assigned to it.");
        }

        return super.delete(userGroup);
    }

    @Override
    @Authorization(scope = PermissionScope.SERVER, type = PermissionType.ADMIN)
    public ServiceResult assignUserToGroup(User user, UserGroupDto userGroupDto) {
        UserGroup userGroup = (UserGroup) this.getById(userGroupDto.getId()).getEntity();

        boolean userHasGroupId = user.getUserGroupId().contains(userGroup.getId());
        boolean groupHasUserId = userGroup.getUserIds().contains(user.getId());

        ServiceResult userGroupResult;
        if (userHasGroupId && !groupHasUserId) {
            userGroup.getUserIds().add(user.getId());
            this.userService.update(user);
            this.update(userGroup);
            UserGroupDto userGroupDtoResult = this.getUserGroupDto(userGroup);

            ServiceResult userResult = new ServiceResult(user, NotificationType.SUCCESS, "PrincipalUser assigned successfully.");

            userGroupResult = new ServiceResult(userGroupDtoResult, NotificationType.SUCCESS, "UserGroup updated successfully.");
        } else {
            userGroupResult = new ServiceResult(null, NotificationType.ERROR, "PrincipalUser already assigned to PrincipalUser Group.");
        }

        return userGroupResult;
    }

    @Override
    @Authorization(scope = PermissionScope.SERVER, type = PermissionType.ADMIN)
    public ServiceResult unassignUserFromGroup(User user, UserGroupDto userGroupDto) {
        UserGroup userGroup = (UserGroup) this.getById(userGroupDto.getId()).getEntity();

        boolean userHasGroupId = user.getUserGroupId().contains(userGroup.getId());
        boolean groupHasUserId = userGroup.getUserIds().contains(user.getId());

        ServiceResult userGroupResult = null;
        if (!userHasGroupId && groupHasUserId) {
            userGroup.getUserIds().remove(user.getId());
            this.userService.update(user);
            this.update(userGroup);
            UserGroupDto userGroupDtoResult = this.getUserGroupDto(userGroup);

            ServiceResult userResult = new ServiceResult(user, NotificationType.SUCCESS, "PrincipalUser unassigned successfully.");

            userGroupResult = new ServiceResult(userGroupDtoResult, NotificationType.SUCCESS, "UserGroup updated successfully.");
        } else {
            userGroupResult = new ServiceResult(null, NotificationType.ERROR, "PrincipalUser already unassigned from PrincipalUser Group.");
        }

        return userGroupResult;
    }

    @Override
    @Authorization(scope = PermissionScope.SERVER, type = PermissionType.NONE)
    public ServiceResult getAllUserGroups() {
        List<UserGroup> userGroups = (List<UserGroup>) this.getAll().getEntity();
        List<UserGroupDto> userGroupDtos = new ArrayList<>();

        for (UserGroup userGroup : userGroups) {
            List<String> userIds = userGroup.getUserIds();
            UserGroupDto userGroupDto = new UserGroupDto();
            userGroupDto.setId(userGroup.getId());
            userGroupDto.setName(userGroup.getName());
            userGroupDto.setPermissions(userGroup.getPermissions());
            userGroupDto.setUserIds(userGroup.getUserIds());

            for (String userId : userIds) {
                User currentUser = (User) this.userService.getById(userId).getEntity();
                userGroupDto.getUsers().add(currentUser);
            }

            userGroupDtos.add(userGroupDto);
        }
        ServiceResult userGroupDtosServiceResult = new ServiceResult(userGroupDtos, NotificationType.SUCCESS, "PrincipalUser Groups retrieved successfully.");

        return userGroupDtosServiceResult;
    }

    private UserGroupDto getUserGroupDto(UserGroup userGroup) {
        UserGroupDto userGroupDto = new UserGroupDto();
        userGroupDto.setId(userGroup.getId());
        userGroupDto.setName(userGroup.getName());
        userGroupDto.setUserIds(userGroup.getUserIds());
        userGroupDto.setPermissions(userGroup.getPermissions());
        userGroupDto.setUsers(new ArrayList<>());
        List<String> userIds = userGroupDto.getUserIds();
        for (String userId : userIds) {
            User user = (User) this.userService.getById(userId).getEntity();
            userGroupDto.getUsers().add(user);
        }

        return userGroupDto;
    }
}
