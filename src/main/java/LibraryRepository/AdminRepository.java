package LibraryRepository;

public interface AdminRepository {
    boolean validateLogin(String username, String password);
}
