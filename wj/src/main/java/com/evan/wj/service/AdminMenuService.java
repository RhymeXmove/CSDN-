package com.evan.wj.service;

import com.evan.wj.dao.AdminMenuDAO;
import com.evan.wj.pojo.AdminMenu;
import com.evan.wj.pojo.AdminRoleMenu;
import com.evan.wj.pojo.AdminUserRole;
import com.evan.wj.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminMenuService {
    @Autowired
    AdminMenuDAO adminMenuDAO;
    @Autowired
    UserService userService;
    @Autowired
    AdminUserRoleService adminUserRoleService;
    @Autowired
    AdminRoleMenuService adminRoleMenuService;

    public List<AdminMenu> getAllByParentId(int parentId) {
        return adminMenuDAO.findAllByParentId(parentId);
    }
    public List<AdminMenu> getMenusByCurrentUser() {

        // Get current user in DB.
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        User user = userService.findByUsername(username);

        // Get roles' ids of current user.
        List<Integer> rids = adminUserRoleService.listAllByUid(user.getId())
                .stream().map(AdminUserRole::getRid).collect(Collectors.toList());

        // Get menu items of these roles.
        List<Integer> menuIds = adminRoleMenuService.findAllByRid(rids)
                .stream().map(AdminRoleMenu::getMid).collect(Collectors.toList());
        List<AdminMenu> menus = adminMenuDAO.findAllById(menuIds).stream().distinct().collect(Collectors.toList());

        // Adjust the structure of the menu.
        handleMenus(menus);
        return menus;
    }

    public List<AdminMenu> getMenusByRoleId(int rid) {
        List<Integer> menuIds = adminRoleMenuService.findAllByRid(rid)
                .stream().map(AdminRoleMenu::getMid).collect(Collectors.toList());
        List<AdminMenu> menus = adminMenuDAO.findAllById(menuIds);

        handleMenus(menus);
        return menus;
    }

    // 处理树结构的代码
    public void handleMenus(List<AdminMenu> menus) {
        menus.forEach(m -> {
            List<AdminMenu> children = getAllByParentId(m.getId());
            m.setChildren(children);
        });

        menus.removeIf(m -> m.getParentId() != 0);
    }


    /**
     * Adjust the Structure of the menu.
     *
     * @param menus Menu items list without structure
     */
//    public void handleMenus(List<AdminMenu> menus) {
//        for (AdminMenu menu : menus) {
//            List<AdminMenu> children = getAllByParentId(menu.getId());
//            menu.setChildren(children);
//        }
//
//        Iterator<AdminMenu> iterator = menus.iterator();
//        while (iterator.hasNext()) {
//            AdminMenu menu = iterator.next();
//            if (menu.getParentId() != 0) {
//                iterator.remove();
//            }
//        }
//    }


}
