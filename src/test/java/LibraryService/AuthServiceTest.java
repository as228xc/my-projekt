package LibraryService;

import LibraryRepository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AdminRepository adminRepository;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        adminRepository = mock(AdminRepository.class);
        authService = new AuthService(adminRepository);

    }

    @Test
    void loginAdmin_returnsFalse_whenUsernameIsBlank(){
        boolean result = authService.loginAdmin("", "secret");
        assertFalse(result);
        verify(adminRepository, never()).validateLogin(anyString(), anyString());
    }

    @Test
    void loginAdmin_returnsTrue_whenRepoApprovesLogIn(){

        when(adminRepository.validateLogin("admin", "secret")).thenReturn(true);

        boolean result = authService.loginAdmin("admin", "secret");
        assertTrue(result);
        verify(adminRepository).validateLogin("admin","secret");
    }


    @Test
    void loginAdmin_returnsFalse_whenRepoNotRejectsLogIn(){
        when(adminRepository.validateLogin("admin", "wrong")).thenReturn(false);
        boolean result = authService.loginAdmin("admin", "wrong");
        assertFalse(result);

        verify(adminRepository).validateLogin("admin","wrong");


    }

    @Test
    void loginAdmin_returnsFalse_whenPasswordIsBlank(){
        boolean result = authService.loginAdmin("admin", " ");
        assertFalse(result);

        verify(adminRepository, never()).validateLogin(anyString(), anyString());

    }





}