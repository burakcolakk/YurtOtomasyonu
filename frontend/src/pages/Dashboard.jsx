import { useEffect, useState } from 'react';
import api from '../api/axiosConfig';

export default function Dashboard() {
    const [stats, setStats] = useState({ studentCount: 0, buildingCount: 0, roomCount: 0 });

    useEffect(() => {
        // Backend'de bu özet endpointi yoksa her biri için ayrı get atabilirsin
        const fetchStats = async () => {
            const students = await api.get('/students');
            const buildings = await api.get('/buildings');
            const rooms = await api.get('/rooms');
            setStats({
                studentCount: students.data.length,
                buildingCount: buildings.data.length,
                roomCount: rooms.data.length
            });
        };
        fetchStats();
    }, []);

    return (
        <div className="p-6">
            <h1 className="text-3xl font-bold text-gray-800 mb-8">Yurt Genel Durumu</h1>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                <StatCard title="Toplam Öğrenci" count={stats.studentCount} color="bg-blue-500" />
                <StatCard title="Aktif Bina" count={stats.buildingCount} color="bg-purple-500" />
                <StatCard title="Toplam Oda" count={stats.roomCount} color="bg-orange-500" />
            </div>
        </div>
    );
}

function StatCard({ title, count, color }) {
    return (
        <div className={`${color} text-white p-6 rounded-2xl shadow-xl transform hover:scale-105 transition`}>
            <h3 className="text-lg font-semibold opacity-80">{title}</h3>
            <p className="text-4xl font-extrabold mt-2">{count}</p>
        </div>
    );
}