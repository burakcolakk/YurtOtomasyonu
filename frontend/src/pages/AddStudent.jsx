import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';

export default function AddStudent() {
    const navigate = useNavigate();
    const [buildings, setBuildings] = useState([]);
    const [rooms, setRooms] = useState([]); // Seçilen binadaki odalar
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        tcKimlikNo: '',
        email: '',
        phoneNumber: '',
        buildingId: '',
        roomId: ''
    });

    // Sayfa açıldığında binaları çek
    useEffect(() => {
        api.get('/buildings').then(res => setBuildings(res.data));
    }, []);

    // Bina değiştiğinde o binanın odalarını çek
    useEffect(() => {
        if (formData.buildingId) {
            api.get(`/rooms/building/${formData.buildingId}`).then(res => {
                // Sadece kapasitesi uygun olan (boş) odaları filtreleyebilirsin
                setRooms(res.data);
            });
        } else {
            setRooms([]);
        }
    }, [formData.buildingId]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post('/students', formData);
            alert("Öğrenci başarıyla kaydedildi ve odaya yerleştirildi!");
            navigate('/students');
        } catch (err) {
            alert("Kayıt sırasında hata: " + (err.response?.data?.message || "Bilgileri kontrol edin."));
        }
    };

    return (
        <div className="max-w-3xl mx-auto mt-10 p-8 bg-white rounded-2xl shadow-xl border border-gray-100">
            <h2 className="text-3xl font-bold text-gray-800 mb-6">🎓 Yeni Öğrenci Kaydı</h2>
            
            <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {/* Kişisel Bilgiler */}
                <input type="text" placeholder="Ad" required className="input-style"
                       onChange={(e) => setFormData({...formData, firstName: e.target.value})} />
                
                <input type="text" placeholder="Soyad" required className="input-style"
                       onChange={(e) => setFormData({...formData, lastName: e.target.value})} />
                
                <input type="text" placeholder="TC Kimlik No" required className="input-style"
                       onChange={(e) => setFormData({...formData, tcKimlikNo: e.target.value})} />
                
                <input type="email" placeholder="E-posta" className="input-style"
                       onChange={(e) => setFormData({...formData, email: e.target.value})} />

                {/* Yerleştirme Bilgileri */}
                <div className="md:col-span-2 grid grid-cols-1 md:grid-cols-2 gap-4 bg-indigo-50 p-4 rounded-xl">
                    <select required className="input-style" value={formData.buildingId}
                            onChange={(e) => setFormData({...formData, buildingId: e.target.value, roomId: ''})}>
                        <option value="">Bina Seçiniz</option>
                        {buildings.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
                    </select>

                    <select required className="input-style" disabled={!formData.buildingId}
                            onChange={(e) => setFormData({...formData, roomId: e.target.value})}>
                        <option value="">Oda Seçiniz</option>
                        {rooms.map(r => (
                            <option key={r.id} value={r.id} disabled={r.currentOccupancy >= r.capacity}>
                                Oda {r.roomNumber} ({r.currentOccupancy}/{r.capacity} Dolu)
                            </option>
                        ))}
                    </select>
                </div>

                <button type="submit" className="md:col-span-2 bg-indigo-600 text-white py-3 rounded-xl font-bold hover:bg-indigo-700 transition shadow-lg">
                    Kaydı Tamamla
                </button>
            </form>
        </div>
    );
}