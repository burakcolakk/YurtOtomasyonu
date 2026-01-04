import { useEffect, useState } from 'react';
import api from '../api/axiosConfig';
import { useNavigate } from 'react-router-dom';

export default function StudentList() {
    const [students, setStudents] = useState([]);
    const navigate = useNavigate();
    const [searchQuery, setSearchQuery] = useState("");

    // Arama mantığı: Ad, Soyad veya TC'ye göre filtrele
    const filteredStudents = students.filter(s => 
        s.firstName.toLowerCase().includes(searchQuery.toLowerCase()) ||
        s.lastName.toLowerCase().includes(searchQuery.toLowerCase()) ||
        s.tcKimlikNo.includes(searchQuery)
    );

    useEffect(() => {
        fetchStudents();
    }, []);

    const fetchStudents = async () => {
        try {
            const res = await api.get('/students');
            setStudents(res.data);
        } catch (err) {
            console.error("Veri çekilemedi", err);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm("Bu öğrenciyi silmek istediğinize emin misiniz?")) {
            try {
                await api.delete(`/students/${id}`);
                setStudents(students.filter(s => s.id !== id));
            } catch (err) {
                alert("Silme işlemi başarısız.");
            }
        }
    };

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-gray-800">Öğrenci Yönetimi</h1>
                <button 
                    onClick={() => navigate('/add-student')}
                    className="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition"
                >
                    + Yeni Öğrenci
                </button>
            </div>

            {/* ARAMA ÇUBUĞU EKLEDİK */}
            <div className="mb-4">
                <input 
                    type="text"
                    placeholder="Öğrenci ara (Ad, Soyad veya TC)..."
                    className="w-full md:w-1/3 px-4 py-2 border rounded-lg shadow-sm focus:ring-2 focus:ring-indigo-500 outline-none"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                />
            </div>

            <div className="bg-white rounded-xl shadow overflow-hidden">
                <table className="min-w-full">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Ad Soyad</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">TC No</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Oda</th>
                            <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase">İşlemler</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                        {/* BURAYI GÜNCELLEDİK: students yerine filteredStudents kullanıyoruz */}
                        {filteredStudents.map((student) => (
                            <tr key={student.id} className="hover:bg-gray-50">
                                <td className="px-6 py-4">{student.firstName} {student.lastName}</td>
                                <td className="px-6 py-4">{student.tcKimlikNo}</td>
                                <td className="px-6 py-4">{student.roomNumber || 'Atanmadı'}</td>
                                <td className="px-6 py-4 text-center">
                                    <button 
                                        onClick={() => navigate(`/edit-student/${student.id}`)}
                                        className="text-blue-600 hover:text-blue-900 mr-4 font-medium"
                                    >
                                        Düzenle
                                    </button>
                                    <button 
                                        onClick={() => handleDelete(student.id)}
                                        className="text-red-600 hover:text-red-900 font-medium"
                                    >
                                        Sil
                                    </button>
                                </td>
                            </tr>
                        ))}

                        {/* Arama sonucu bulunamazsa gösterilecek mesaj */}
                        {filteredStudents.length === 0 && (
                            <tr>
                                <td colSpan="4" className="px-6 py-4 text-center text-gray-500">
                                    Aradığınız kriterlere uygun öğrenci bulunamadı.
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}