package com.wisdom.blog.repository;

import com.wisdom.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    Page<User> findByNameLike(String name, Pageable pageable);

    User findByUsername(String username);

    List<User> findByUsernameIn(Collection<String> usernames);

}
