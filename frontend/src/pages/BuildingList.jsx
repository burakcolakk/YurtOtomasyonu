import { useEffect, useState } from 'react';
import api from '../api/axiosConfig';

export default function BuildingList() {
    const [buildings, setBuildings] = useState([]);

    useEffect(() => {
        api.get('/buildings').then(res => setBuildings(res.data));
    }, []);

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-6">Binalar ve Kapasite</h1>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                {buildings.map(b => (
                    <div key={b.id} className="bg-white p-6 rounded-xl shadow-md border-l-4 border-indigo-500">
                        <h2 className="text-xl font-bold text-indigo-900">{b.name}</h2>
                        <p className="text-gray-500 text-sm mt-1">{b.address}</p>
                        <div className="mt-4 flex justify-between items-center text-sm font-semibold">
                            <span>Kat Sayısı: {b.totalFloors}</span>
                            <span className="text-indigo-600">Aktif</span>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}