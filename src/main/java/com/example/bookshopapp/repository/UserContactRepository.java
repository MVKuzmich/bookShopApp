package com.example.bookshopapp.repository;

import com.example.bookshopapp.data.user.UserContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserContactRepository extends JpaRepository<UserContactEntity, Integer> {
}
