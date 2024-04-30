package com.hieuubuntu.identityservice.permissions;

import com.hieuubuntu.identityservice.entity.User;
import com.hieuubuntu.identityservice.repositories.PermissionRepositoryImpl;
import com.hieuubuntu.identityservice.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class CanPermission implements ConstraintValidator<CanPer, Object> {
    private String name;

    private final UserRepository userRepository;
    private final PermissionRepositoryImpl permissionRepositoryImpl;

    public CanPermission(UserRepository userRepository,
                         PermissionRepositoryImpl permissionRepositoryImpl) {
        this.userRepository = userRepository;
        this.permissionRepositoryImpl = permissionRepositoryImpl;
    }

    @Override
    public void initialize(CanPer constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        name = constraintAnnotation.name();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (name == null) { // Không truyền vào tên quyền, return true:
            return true;
        }

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName(); // TODO: giải mã:

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }

        return checkPermission(user.getId());
    }

    private Boolean checkPermission(Integer userId) {
        return permissionRepositoryImpl.userCanPermission(userId, name);
    }

}
