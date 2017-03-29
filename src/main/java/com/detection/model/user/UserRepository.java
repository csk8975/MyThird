package com.detection.model.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @fileName UserRepository.java
 * @author csk
 * @createTime 2017年3月1日 下午3:57:07
 * @version 1.0
 * @function
 */

public interface UserRepository extends JpaRepository<User, Long> {
    public List<User> findByToken(String token);
    public User findByUserName(String userName);
}
