package com.stockmarket.stockmarketapi.repository;

import org.springframework.data.repository.CrudRepository;
import com.stockmarket.stockmarketapi.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
