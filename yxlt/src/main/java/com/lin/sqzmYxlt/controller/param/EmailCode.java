package com.lin.sqzmYxlt.controller.param;


/**
 * @author lin
 */
public class EmailCode {
    private String id;
    private String email;
    private String code;

    @Override
    public String toString() {
        return "EmailCode{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
