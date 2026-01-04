package com.yurt.project.repository;

import com.yurt.project.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // TC No ile öğrenci bul
    Optional<Student> findByTcKimlikNo(String tcKimlikNo);
    List<Student> findByRoomId(Long roomId);

    // TC No zaten kayıtlı mı?
    boolean existsByTcKimlikNo(String tcKimlikNo);
}