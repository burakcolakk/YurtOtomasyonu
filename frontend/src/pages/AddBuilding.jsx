import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';

export default function AddBuilding() {
    const [building, setBuilding] = useState({ name: '', address: '', totalFloors: '' });
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post('/buildings', building);
            alert("Bina başarıyla eklendi!");
            navigate('/buildings');
        } catch (err) {
            console.error("Hata:", err);
        }
    };

    return (
        <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-xl shadow-lg">
            <h2 className="text-2xl font-bold mb-6 text-gray-800">Yeni Bina Ekle</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-sm font-medium text-gray-700">Bina Adı</label>
                    <input type="text" required className="w-full border p-2 rounded mt-1" 
                           onChange={(e) => setBuilding({...building, name: e.target.value})} />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700">Adres</label>
                    <textarea className="w-full border p-2 rounded mt-1" 
                              onChange={(e) => setBuilding({...building, address: e.target.value})} />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700">Toplam Kat</label>
                    <input type="number" required className="w-full border p-2 rounded mt-1" 
                           onChange={(e) => setBuilding({...building, totalFloors: e.target.value})} />
                </div>
                <button type="submit" className="w-full bg-indigo-600 text-white py-2 rounded-lg hover:bg-indigo-700 transition">
                    Kaydet
                </button>
            </form>
        </div>
    );
}