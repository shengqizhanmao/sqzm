package com.lin.sqzmHtgl.controller.param;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author lin
 */
public class AddSUserAndRole {
    private String sId;
    private List<String> listRoleId;

    @NotNull
    @Override
    public String toString() {
        return "AddSUserAndRole{" +
                "sId='" + sId + '\'' +
                ", listRoleId=" + listRoleId +
                '}';
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public List<String> getListRoleId() {
        return listRoleId;
    }

    public void setListRoleId(List<String> listRoleId) {
        this.listRoleId = listRoleId;
    }
}
