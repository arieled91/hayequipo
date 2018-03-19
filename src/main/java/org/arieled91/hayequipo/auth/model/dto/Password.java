package org.arieled91.hayequipo.auth.model.dto;


public class Password {

    private String oldPassword = null;

//    @ValidPassword
    private String newPassword = null;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}