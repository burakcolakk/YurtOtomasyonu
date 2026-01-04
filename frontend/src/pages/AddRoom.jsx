import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';

export default function AddRoom() {
    const navigate = useNavigate();
    const [buildings, setBuildings] = useState([]);
    const [formData, setFormData] = useState({
        roomNumber: '',
        capacity: '',
        floor: '',
        buildingId: ''
    });
    const [loading, setLoading] = useState(false);

    // Form açıldığında mevcut binaları çekiyoruz ki seçim yapabilelim
    useEffect(() => {
        const fetchBuildings = async () => {
            try {
                const response = await api.get('/buildings');
                setBuildings(response.data);
            } catch (error) {
                console.error("Binalar yüklenemedi:", error);
            }
        };
        fetchBuildings();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            // Backend'deki @PostMapping("/rooms") endpointine gönderiyoruz
            await api.post('/rooms', formData);
            alert('Oda başarıyla oluşturuldu!');
            navigate('/rooms'); // Oda listesine yönlendir
        } catch (error) {
            console.error("Oda ekleme hatası:", error);
            alert('Oda eklenirken bir hata oluştu.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-2xl mx-auto mt-10 p-8 bg-white shadow-xl rounded-2xl border border-gray-100">
            <h2 className="text-3xl font-bold text-gray-800 mb-6 flex items-center gap-2">
                <span className="text-indigo-600">🏠</span> Yeni Oda Kaydı
            </h2>
            
            <form onSubmit={handleSubmit} className="space-y-5">
                {/* Bina Seçimi */}
                <div>
                    <label className="block text-sm font-semibold text-gray-700 mb-1">Hangi Binaya Ait?</label>
                    <select
                        required
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none transition"
                        value={formData.buildingId}
                        onChange={(e) => setFormData({...formData, buildingId: e.target.value})}
                    >
                        <option value="">Bina Seçiniz...</option>
                        {buildings.map(b => (
                            <option key={b.id} value={b.id}>{b.name}</option>
                        ))}
                    </select>
                </div>

                <div className="grid grid-cols-2 gap-4">
                    {/* Oda Numarası */}
                    <div>
                        <label className="block text-sm font-semibold text-gray-700 mb-1">Oda Numarası</label>
                        <input
                            type="text"
                            placeholder="Örn: 101"
                            required
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none transition"
                            value={formData.roomNumber}
                            onChange={(e) => setFormData({...formData, roomNumber: e.target.value})}
                        />
                    </div>

                    {/* Kat */}
                    <div>
                        <label className="block text-sm font-semibold text-gray-700 mb-1">Kat</label>
                        <input
                            type="number"
                            placeholder="Örn: 1"
                            required
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none transition"
                            value={formData.floor}
                            onChange={(e) => setFormData({...formData, floor: e.target.value})}
                        />
                    </div>
                </div>

                {/* Kapasite */}
                <div>
                    <label className="block text-sm font-semibold text-gray-700 mb-1">Kapasite (Kişi Sayısı)</label>
                    <input
                        type="number"
                        placeholder="Örn: 4"
                        required
                        min="1"
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none transition"
                        value={formData.capacity}
                        onChange={(e) => setFormData({...formData, capacity: e.target.value})}
                    />
                </div>

                <button
                    type="submit"
                    disabled={loading}
                    className="w-full bg-indigo-600 text-white py-3 rounded-lg font-bold hover:bg-indigo-700 transition shadow-lg disabled:bg-gray-400"
                >
                    {loading ? 'Kaydediliyor...' : 'Odayı Kaydet'}
                </button>
            </form>
        </div>
    );
}