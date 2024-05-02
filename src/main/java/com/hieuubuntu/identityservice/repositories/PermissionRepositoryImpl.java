package com.hieuubuntu.identityservice.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class PermissionRepositoryImpl{
    @PersistenceContext
    private EntityManager entityManager;

    public Boolean userCanPermission(Integer modelId, String permissionName) {
        var params = new HashMap<String, Object>();
        var queryBuilder = new StringBuilder("select count(*) from permissions" +
                " inner join role_has_permissions" +
                " on (permissions.id = role_has_permissions.permission_id)" +
                " inner join model_has_roles" +
                " on (model_has_roles.role_id = role_has_permissions.role_id)" +
                " where permissions.name = :permission_name" +
                " and model_has_roles.model_id = :model_id" +
                " and model_has_roles.model_type = 'User'");

        params.put("permission_name", permissionName);
        params.put("model_id", modelId);

        var execute = entityManager.createNativeQuery(queryBuilder.toString(), Long.class);
        params.forEach(execute::setParameter);

        Long count = (Long) execute.getSingleResult();

        return count != null && count > 0;
    }
}

