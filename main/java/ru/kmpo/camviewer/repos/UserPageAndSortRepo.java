package ru.kmpo.camviewer.repos;

        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.Pageable;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.PagingAndSortingRepository;
        import org.springframework.data.repository.query.Param;
        import org.springframework.stereotype.Repository;
        import ru.kmpo.camviewer.domain.User;

@Repository
public interface UserPageAndSortRepo extends PagingAndSortingRepository<User, Long> {

    @Query("SELECT DISTINCT u FROM User u join u.roles r where r.name = 'ROLE_CAMERA'")
    Page<User> findAllCameras(Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u join u.roles r where r.name <> 'ROLE_CAMERA'")
    Page<User> findAllUsers(Pageable pageable);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE %:searchText% OR LOWER(u.firstName) LIKE %:searchText% OR LOWER(u.email) LIKE %:searchText%")
    Page<User> findAllUsers(Pageable pageable, @Param("searchText") String searchText);
}
