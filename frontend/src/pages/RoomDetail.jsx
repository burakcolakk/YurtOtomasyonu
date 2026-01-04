import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';

export default function RoomDetail() {
    const { id } = useParams(); // URL'deki oda ID'sini alır
    const [room, setRoom] = useState(null);
    const [students, setStudents] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchDetails = async () => {
            try {
                // Oda temel bilgilerini çek
                const roomRes = await api.get(`/rooms/${id}`);
                setRoom(roomRes.data);

                // Bu odaya atanmış öğrencileri çek
                // Not: Backend'de /students/room/{id} endpoint'inin olduğundan emin olun
                const studentsRes = await api.get(`/students/room/${id}`);
                setStudents(studentsRes.data);
            } catch (err) {
                console.error("Detaylar yüklenemedi", err);
            }
        };
        fetchDetails();
    }, [id]);

    if (!room) return <div className="p-8 text-center text-gray-500">Yükleniyor...</div>;

    return (
        <div className="p-8 max-w-4xl mx-auto min-h-screen">
            <button onClick={() => navigate(-1)} className="mb-6 text-indigo-600 font-medium hover:underline flex items-center">
                <span className="mr-2">←</span> Odalara Geri Dön
            </button>

            <div className="bg-white rounded-3xl shadow-xl overflow-hidden border border-gray-100">
                {/* Üst Bilgi Kartı */}
                <div className="bg-gradient-to-r from-indigo-700 to-purple-700 p-8 text-white">
                    <div className="flex justify-between items-center">
                        <div>
                            <h1 className="text-4xl font-bold">Oda #{room.roomNumber}</h1>
                            <p className="opacity-80 mt-2 text-lg">
                                {room.building?.name || 'Bina Tanımsız'} | Kat: {room.floorNumber}
                            </p>
                        </div>
                        <div className="text-right">
                            <span className="text-sm uppercase tracking-widest opacity-70">Doluluk</span>
                            <p className="text-3xl font-bold">{students.length} / {room.capacity}</p>
                        </div>
                    </div>
                </div>

                {/* Öğrenci Listesi Bölümü */}
                <div className="p-8">
                    <h2 className="text-2xl font-bold text-gray-800 mb-6 flex items-center">
                        <span className="mr-3 text-indigo-500">👥</span> 
                        Bu Odada Konaklayanlar
                    </h2>

                    {students.length > 0 ? (
                        <div className="grid grid-cols-1 gap-4">
                            {students.map(student => (
                                <div key={student.id} className="flex justify-between items-center p-5 bg-gray-50 rounded-2xl border border-gray-100 hover:bg-white hover:shadow-md transition duration-200">
                                    <div>
                                        <p className="font-bold text-gray-900 text-lg">
                                            {student.firstName} {student.lastName}
                                        </p>
                                        <div className="flex gap-4 mt-1">
                                            <p className="text-sm text-gray-500 font-mono">TC: {student.tcKimlikNo}</p>
                                            <p className="text-sm text-gray-500">📞 {student.phoneNumber || 'Telefon Yok'}</p>
                                        </div>
                                    </div>
                                    <button 
                                        onClick={() => navigate(`/edit-student/${student.id}`)}
                                        className="bg-white text-indigo-600 border border-indigo-100 px-5 py-2 rounded-xl text-sm font-bold hover:bg-indigo-600 hover:text-white transition shadow-sm"
                                    >
                                        Düzenle
                                    </button>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="text-center py-16 bg-gray-50 rounded-3xl border-2 border-dashed border-gray-200">
                            <p className="text-gray-400 text-lg italic">Bu oda şu an boş görünüyor.</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}