package picker.picker_backend.Auth.dto;

import picker.picker_backend.Auth.entity.User;

public class EncryptedUser extends User {
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    private String decrypted(){
        return "";
    }
    private String encrypted(){
        return "";
    }


}
