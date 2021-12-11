package com.ntouzidis.crm2022.module.user.service;

import com.ntouzidis.crm2022.module.user.entity.Tenant;
import com.ntouzidis.crm2022.module.user.entity.User;

public interface RootService {

    /**
     * Delete a user of role Admin, providing it's ID or the entity
     *
     * @param admin entity of Admin user to delete
     * @param id of the Admin user to delete, will be used if admin param is null
     * @return the deleted Admin user
     */
    User deleteAdminUser(User admin, Long id);

    Tenant deleteTenant(Long id);
}
