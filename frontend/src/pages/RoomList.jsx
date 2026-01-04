import { useEffect, useState } from 'react';
import api from '../api/axiosConfig';
import { useNavigate } from 'react-router-dom';

export default function RoomList() {
    const [rooms, setRooms] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        api.get('/rooms').then(res => setRooms(res.data));
    }, []);

    // Arama filtresi (Oda numarasına göre)
    const filteredRooms = rooms.filter(room => 
        room.roomNumber.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="p-8 bg-gray-50 min-h-screen">
            <div className="flex justify-between items-center mb-8">
                <div>
                    <h1 className="text-3xl font-bold text-gray-800">Oda Yönetimi</h1>
                    <p className="text-gray-500">Tüm binalardaki odaların doluluk durumu</p>
                </div>
                <button 
                    onClick={() => navigate('/add-room')}
                    className="bg-indigo-600 text-white px-5 py-2.5 rounded-xl hover:bg-indigo-700 transition shadow-md font-semibold"
                >
                    + Yeni Oda Tanımla
                </button>
            </div>

            {/* Arama Çubuğu */}
            <div className="mb-6">
                <input 
                    type="text" 
                    placeholder="Oda numarası ile ara... (Örn: 101)"
                    className="w-full max-w-md px-4 py-2 rounded-xl border border-gray-200 focus:ring-2 focus:ring-indigo-500 outline-none shadow-sm"
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-6 gap-6">
                {filteredRooms.map(room => {
                    const isFull = room.currentOccupancy >= room.capacity;
                    const occupancyRate = (room.currentOccupancy / room.capacity) * 100;

                    return (
                        /* GÜNCELLEME: onClick ve cursor-pointer eklendi */
                        <div 
                            key={room.id} 
                            onClick={() => navigate(`/room/${room.id}`)} 
                            className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden hover:shadow-lg transition cursor-pointer transform hover:-translate-y-1"
                        >
                            <div className={`h-2 w-full ${isFull ? 'bg-red-500' : 'bg-green-500'}`} />
                            <div className="p-5">
                                <div className="flex justify-between items-start mb-2">
                                    <span className="text-2xl font-bold text-gray-800">#{room.roomNumber}</span>
                                    <span className={`text-[10px] uppercase px-2 py-1 rounded-full font-bold ${isFull ? 'bg-red-100 text-red-600' : 'bg-green-100 text-green-600'}`}>
                                        {isFull ? 'Dolu' : 'Müsait'}
                                    </span>
                                </div>
                                <div className="text-sm text-gray-500 mb-4">
                                    Kat: {room.floorNumber} | {room.building?.name || 'Bina Belirtilmemiş'}
                                </div>
                                
                                {/* Doluluk Barı */}
                                <div className="w-full bg-gray-100 h-1.5 rounded-full mb-2">
                                    <div 
                                        className={`h-full rounded-full ${isFull ? 'bg-red-500' : 'bg-indigo-500'}`} 
                                        style={{ width: `${occupancyRate}%` }}
                                    />
                                </div>
                                <div className="text-xs font-semibold text-gray-600">
                                    Kapasite: {room.currentOccupancy} / {room.capacity}
                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}