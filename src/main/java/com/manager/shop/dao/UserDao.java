package com.manager.shop.dao;

import com.manager.shop.model.User;
import com.manager.shop.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserDao extends JpaRepository<User,Integer> {
    User findByEmailId(@Param("email")String email);

    List<UserWrapper>getAllUser();

    List<String> getAllAdmin();

    @Transactional // anotaciones para ejecutar el query de modificacion creaod en UserDao
    @Modifying
    Integer updateStatus(@Param("status")String status,@Param("id")Integer id);

}
