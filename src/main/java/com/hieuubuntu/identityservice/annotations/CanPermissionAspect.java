package com.hieuubuntu.identityservice.annotations;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.hieuubuntu.identityservice.entity.User;
import com.hieuubuntu.identityservice.exception.error_code.ErrorCode;
import com.hieuubuntu.identityservice.exception.type.CanPermissionException;
import com.hieuubuntu.identityservice.repositories.PermissionRepositoryImpl;
import com.hieuubuntu.identityservice.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class CanPermissionAspect {

    private final UserRepository userRepository;
    private final PermissionRepositoryImpl permissionRepositoryImpl;

    public CanPermissionAspect(UserRepository userRepository, PermissionRepositoryImpl permissionRepositoryImpl) {
        this.userRepository = userRepository;
        this.permissionRepositoryImpl = permissionRepositoryImpl;
    }

    @Before("@annotation(canPer)")
    public void checkPermission(CanPer canPer) {

        String permissionName = canPer.name();
        if (permissionName != null) {
            String message = mapAttribute(ErrorCode.NOT_PERMISSION.getMessage(), permissionName);
            var context = SecurityContextHolder.getContext();
            String username = context.getAuthentication().getName();
            User userInfo = userRepository.findByUsername(username);

            if (userInfo == null
                    || userInfo.getId() == null
                    || Boolean.FALSE.equals(checkPermission(userInfo.getId(), permissionName))) {
                throw new CanPermissionException(message);
            }
        }
    }

    private String mapAttribute(String message, String attribute) {
        return message.replace("{name}", attribute);
    }

    private Boolean checkPermission(Integer userId, String name) {
        return permissionRepositoryImpl.userCanPermission(userId, name);
    }
}
