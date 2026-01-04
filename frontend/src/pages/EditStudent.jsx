import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';

export default function EditStudent() {
    const { id } = useParams();
    const navigate = useNavigate();
    
    const [rooms, setRooms] = useState([]); // Mevcut tüm odalar için
    const [student, setStudent] = useState({
        firstName: '',
        lastName: '',
        tcKimlikNo: '',
        email: '',
        phoneNumber: '',
        roomId: '' // roomNumber yerine roomId kullanıyoruz
    });

    useEffect(() => {
        const fetchData = async () => {
            try {
                // 1. Öğrenci bilgilerini getir
                const studentRes = await api.get(`/students/${id}`);
                // Backend'den gelen veriyi state'e uygun hale getir (Örn: room objesinden id'yi al)
                const studentData = studentRes.data;
                setStudent({
                    ...studentData,
                    roomId: studentData.room?.id || '' // Mevcut oda ID'sini ata
                });

                // 2. Tüm odaları getir (Seçim kutusu için)
                const roomsRes = await api.get('/rooms');
                setRooms(roomsRes.data);
            } catch (err) {
                console.error("Veriler yüklenemedi", err);
                alert("Bilgiler alınırken bir hata oluştu.");
                navigate('/students');
            }
        };
        fetchData();
    }, [id, navigate]);

    const handleChange = (e) => {
        setStudent({ ...student, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Backend'e roomId bilgisini de içeren güncel objeyi gönder
            await api.put(`/students/${id}`, student);
            alert("Öğrenci ve oda bilgileri başarıyla güncellendi!");
            navigate('/students');
        } catch (err) {
            console.error("Güncelleme hatası", err);
            alert("Güncelleme başarısız. Odanın dolu olmadığından emin olun.");
        }
    };

    return (
        <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
            <div className="max-w-md w-full bg-white rounded-xl shadow-lg p-8">
                <h2 className="text-2xl font-bold text-gray-800 mb-6 text-center">Öğrenci Düzenle</h2>
                
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Ad</label>
                        <input type="text" name="firstName" value={student.firstName} onChange={handleChange}
                            className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" required />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Soyad</label>
                        <input type="text" name="lastName" value={student.lastName} onChange={handleChange}
                            className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" required />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">TC Kimlik No</label>
                        <input type="text" name="tcKimlikNo" value={student.tcKimlikNo} onChange={handleChange}
                            className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" required />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">E-posta</label>
                        <input type="email" name="email" value={student.email} onChange={handleChange}
                            className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" />
                    </div>

                    {/* ODA SEÇİM KUTUSU (SELECT BOX) */}
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Atanan Oda</label>
                        <select 
                            name="roomId" 
                            value={student.roomId} 
                            onChange={handleChange}
                            className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none bg-white"
                            required
                        >
                            <option value="">Oda Seçiniz...</option>
                            {rooms.map((room) => (
                                <option key={room.id} value={room.id}>
                                    {room.building?.name} - Oda {room.roomNumber} ({room.currentOccupancy}/{room.capacity})
                                </option>
                            ))}
                        </select>
                        <p className="text-xs text-gray-500 mt-1">* Oda değişikliği kapasiteyi otomatik günceller.</p>
                    </div>

                    <div className="flex gap-4 pt-4">
                        <button type="button" onClick={() => navigate('/students')}
                            className="flex-1 px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition">
                            İptal
                        </button>
                        <button type="submit"
                            className="flex-1 px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 shadow-md transition">
                            Kaydet
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}