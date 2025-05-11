package picker.picker_backend.Auth.service;

public interface  AuthService {
    public void saveAccessToken(String username, String token);
    public void saveRefreshToken(String username, String token);
    public String getToken(String key);
    public void deleteToken(String key);
}
