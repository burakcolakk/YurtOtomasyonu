import React from 'react';

export default function Profile() {
    const user = JSON.parse(localStorage.getItem('user')) || { username: 'Admin', role: 'Yönetici' };

    return (
        <div className="p-8 max-w-2xl mx-auto">
            <div className="bg-white rounded-3xl shadow-xl overflow-hidden">
                <div className="bg-indigo-700 h-32 flex items-center justify-center">
                    <div className="w-24 h-24 bg-white rounded-full border-4 border-white text-4xl flex items-center justify-center">
                        👤
                    </div>
                </div>
                <div className="p-8 text-center">
                    <h2 className="text-2xl font-bold text-gray-800">{user.username}</h2>
                    <p className="text-indigo-600 font-medium mb-6">{user.role}</p>
                    
                    <div className="grid grid-cols-1 gap-4 text-left">
                        <div className="p-4 bg-gray-50 rounded-xl">
                            <span className="text-gray-500 text-sm">Sistem Yetkisi</span>
                            <p className="font-semibold text-gray-800 font-mono">FULL_ACCESS_ADMIN</p>
                        </div>
                        <div className="p-4 bg-gray-50 rounded-xl">
                            <span className="text-gray-500 text-sm">Son Giriş Tarihi</span>
                            <p className="font-semibold text-gray-800">{new Date().toLocaleDateString()}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}