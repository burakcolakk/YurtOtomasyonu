import React, { useEffect, useState } from 'react';
import { getStudents, deleteStudent } from '../services/api';

const StudentList = () => {
    const [students, setStudents] = useState([]);
    const [error, setError] = useState('');

    // Verileri API'den Çek
    useEffect(() => {
        loadStudents();
    }, []);

    const loadStudents = async () => {
        try {
            const data = await getStudents();
            setStudents(data);
        } catch (err) {
            setError('Veriler yüklenirken hata oluştu!');
            console.error(err);
        }
    };

    // Silme İşlemi
    const handleDelete = async (id) => {
        // Kullanıcıya sor (Güvenlik önlemi)
        if (window.confirm("Bu öğrenciyi silmek istediğinize emin misiniz?")) {
            try {
                await deleteStudent(id);
                // Silinen öğrenciyi listeden çıkarmak için state'i güncelle (Sayfa yenilemeden)
                setStudents(students.filter(student => student.id !== id));
            } catch (err) {
                alert("Silme işlemi başarısız oldu.");
            }
        }
    };

    return (
        <div className="list-container">
            <h2>📋 Kayıtlı Öğrenciler</h2>
            
            {error && <p className="error-message">{error}</p>}

            {students.length === 0 ? (
                <p>Henüz kayıtlı öğrenci yok.</p>
            ) : (
                <table className="student-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Ad</th>
                            <th>Soyad</th>
                            <th>TC No</th>
                            <th>Oda No</th>
                            <th>İşlemler</th>
                        </tr>
                    </thead>
                    <tbody>
                        {students.map((student) => (
                            <tr key={student.id}>
                                <td>{student.id}</td>
                                <td>{student.firstName}</td>
                                <td>{student.lastName}</td>
                                <td>{student.tcNo}</td>
                                <td>
                                    {/* Backend'den gelen oda bilgisi varsa göster, yoksa - koy */}
                                    {student.room ? student.room.roomNumber : '-'}
                                </td>
                                <td>
                                    <button className="btn-edit">Düzenle</button>
                                    <button 
                                        className="btn-delete" 
                                        onClick={() => handleDelete(student.id)}
                                    >
                                        Sil
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default StudentList;