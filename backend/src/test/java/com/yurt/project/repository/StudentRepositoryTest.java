package com.yurt.project.repository;

import com.yurt.project.entity.Student;
import com.yurt.project.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // Uygulamanın tamamını (Context) ayağa kaldırır
@Transactional  // Test bitince veritabanına yapılan işlemleri geri alır (Rollback)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindStudent_ShouldWork() {
        // 1. HAZIRLIK (Arrange)
        // Öğrenci User'a bağlı olduğu için önce basit bir User oluşturalım (Opsiyonel ama temiz olur)
        User user = new User();
        user.setUsername("testogrenci");
        user.setPassword("1234");
        user.setEmail("test@ogrenci.com");
        userRepository.save(user);

        Student student = new Student();
        student.setTcKimlikNo("99999999999");
        student.setFirstName("Mehmet");
        student.setLastName("Yılmaz");
        student.setPhoneNumber("5551112233");
        student.setUser(user);

        // 2. EYLEM (Act) - Veritabanına Gerçek Kayıt
        studentRepository.save(student);

        // Veritabanından geri çağırma (Custom metodumuzu test ediyoruz)
        Optional<Student> foundStudent = studentRepository.findByTcKimlikNo("99999999999");
        boolean exists = studentRepository.existsByTcKimlikNo("99999999999");

        // 3. KONTROL (Assert)
        assertTrue(foundStudent.isPresent()); // Kayıt bulunmalı
        assertEquals("Mehmet", foundStudent.get().getFirstName()); // İsim doğru gelmeli
        assertTrue(exists); // Var mı kontrolü true dönmeli
    }

    @Test
    void findByTcKimlikNo_WhenNotExists_ShouldReturnEmpty() {
        // Olmayan bir TC aratıyoruz
        Optional<Student> result = studentRepository.findByTcKimlikNo("00000000000");

        assertFalse(result.isPresent()); // Boş dönmeli
    }
}