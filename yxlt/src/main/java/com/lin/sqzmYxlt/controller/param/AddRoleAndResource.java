package com.lin.sqzmYxlt.controller.param;

import java.util.List;

/**
 * @author lin
 */
public class AddRoleAndResource {
    private String roleId;
    private List<String> listResourceId;

    @Override
    public String toString() {
        return "AddRoleAndResource{" +
                "roleId='" + roleId + '\'' +
                ", listResourceId=" + listResourceId +
                '}';
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getListResourceId() {
        return listResourceId;
    }

    public void setListResourceId(List<String> listResourceId) {
        this.listResourceId = listResourceId;
    }
}
