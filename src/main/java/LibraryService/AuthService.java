package LibraryService;

import LibraryRepository.AdminRepository;

public class AuthService {

    private final AdminRepository adminRepository;

    public AuthService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public boolean loginAdmin(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return false;
        }

        return adminRepository.validateLogin(username, password);
    }
}