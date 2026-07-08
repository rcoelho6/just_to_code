package com.example.test.just_to_code.code.repositories;

import com.example.test.just_to_code.code.repositories.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
