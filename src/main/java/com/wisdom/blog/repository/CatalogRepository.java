package com.wisdom.blog.repository;

import com.wisdom.blog.domain.Catalog;
import com.wisdom.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogRepository extends JpaRepository<Catalog,Long> {

    List<Catalog> findByUser(User user);

    List<Catalog> findByUserAndName(User user,String name);

}
