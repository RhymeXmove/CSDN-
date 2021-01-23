package com.evan.wj.dao;

import com.evan.wj.pojo.AdminMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AdminMenuDAO extends JpaRepository<AdminMenu, Integer> {
     AdminMenu findById(int id);
     List<AdminMenu> findAllByParentId(int parentId);
}

