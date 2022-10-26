package ru.practicum.ewm.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStorage extends JpaRepository<User, Long> {

    @Query("select u from User u")
    List<User> findAllUsers(Pageable pageable);

    @Query("select u from User u where u.id in :ids")
    List<User> findUserByList(List<Long> ids);
}
