package com.yurt.project.repository;
import com.yurt.project.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
public interface VisitorRepository extends JpaRepository<Visitor, Long> {}