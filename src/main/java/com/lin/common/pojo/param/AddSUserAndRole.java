package com.lin.common.pojo.param;

import java.util.List;

/**
 * @author lin
 */
public class AddSUserAndRole {
    private String sId;
    private List<String> listRoleId;

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
