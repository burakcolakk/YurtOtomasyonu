package com.yurt.project.service.impl;

import com.yurt.project.dto.request.CreateStudentRequest;
import com.yurt.project.dto.request.UpdateStudentRequest;
import com.yurt.project.dto.response.StudentResponse;
import com.yurt.project.entity.Room;
import com.yurt.project.entity.Student;
import com.yurt.project.exception.BusinessException;
import com.yurt.project.mapper.StudentMapper;
import com.yurt.project.repository.RoomRepository;
import com.yurt.project.repository.StudentRepository;
import com.yurt.project.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public StudentResponse createStudent(CreateStudentRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new BusinessException("Seçilen oda bulunamadı!"));

        if (room.getCurrentOccupancy() >= room.getCapacity()) {
            throw new BusinessException("Seçilen oda dolu! Lütfen başka bir oda seçiniz.");
        }

        Student student = studentMapper.toEntity(request);
        student.setRoom(room);
        room.setCurrentOccupancy(room.getCurrentOccupancy() + 1);

        roomRepository.save(room);
        Student savedStudent = studentRepository.save(student);

        return studentMapper.toResponse(savedStudent);
    }

    @Override
    @Transactional
    public StudentResponse updateStudent(Long id, UpdateStudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Öğrenci bulunamadı id: " + id));

        // Temel Bilgileri Güncelle
        if (request.getFirstName() != null) student.setFirstName(request.getFirstName());
        if (request.getLastName() != null) student.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null) student.setPhoneNumber(request.getPhoneNumber());
        if (request.getEmail() != null) student.setEmail(request.getEmail());
        if (request.getParentName() != null) student.setParentName(request.getParentName());
        if (request.getParentPhone() != null) student.setParentPhone(request.getParentPhone());
        if (request.getUniversityDepartment() != null) student.setUniversityDepartment(request.getUniversityDepartment());

        // ODA DEĞİŞİKLİĞİ MANTIĞI
        if (request.getRoomId() != null) {
            Room oldRoom = student.getRoom();

            if (oldRoom == null || !oldRoom.getId().equals(request.getRoomId())) {
                Room newRoom = roomRepository.findById(request.getRoomId())
                        .orElseThrow(() -> new BusinessException("Yeni oda bulunamadı!"));

                if (newRoom.getCurrentOccupancy() >= newRoom.getCapacity()) {
                    throw new BusinessException("Seçilen yeni oda dolu!");
                }

                if (oldRoom != null) {
                    // ESKİ ODA DOLULUĞUNU AZALT (Kritik: 0'ın altına düşmesin)
                    oldRoom.setCurrentOccupancy(Math.max(0, oldRoom.getCurrentOccupancy() - 1));
                    roomRepository.save(oldRoom);
                }

                newRoom.setCurrentOccupancy(newRoom.getCurrentOccupancy() + 1);
                roomRepository.save(newRoom);
                student.setRoom(newRoom);
            }
        }

        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toResponse(updatedStudent);
    }

    @Override
    @Transactional // Bu notasyon işlemin atomik olmasını sağlar (Ya hep ya hiç)
    public void deleteStudent(Long id) {
        // 1. Önce öğrenciyi bul (Oda bilgisine ulaşmak için)
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Silinecek öğrenci bulunamadı id: " + id));

        // 2. Öğrencinin bir odası var mı kontrol et
        Room room = student.getRoom();
        if (room != null) {
            // 3. Odadaki mevcut kişi sayısını 1 azalt
            // Math.max ile sayının 0'ın altına düşmesini engelliyoruz
            int newOccupancy = Math.max(0, room.getCurrentOccupancy() - 1);
            room.setCurrentOccupancy(newOccupancy);

            // 4. Odayı güncel halini kaydet
            roomRepository.save(room);
        }

        // 5. En son öğrenciyi sil
        studentRepository.delete(student);
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Öğrenci bulunamadı id: " + id));

        return studentMapper.toResponse(student);
    }
}
