package com.hieuubuntu.identityservice.repositories;

import com.hieuubuntu.identityservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    public Boolean userCanPermission(Integer userId, String permission);
}
