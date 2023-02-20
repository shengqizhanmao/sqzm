package com.lin.sqzmYxlt.controller.param;

public class UpdatePassword {
    private String id;
    private String password;
    private String newPassword;

    @Override
    public String toString() {
        return "UpdatePassword{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
