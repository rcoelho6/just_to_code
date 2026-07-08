package com.example.test.just_to_code.code.infrastructures.repositories;

import com.example.test.just_to_code.code.infrastructures.models.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long> {

}
