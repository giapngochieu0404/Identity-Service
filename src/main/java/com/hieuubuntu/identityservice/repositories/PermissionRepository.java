package com.hieuubuntu.identityservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hieuubuntu.identityservice.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    public Boolean userCanPermission(Integer userId, String permission);
}
