// Dosya: src/pages/Login.jsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';

export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setError(''); // Önceki hatayı temizle
        try {
            // Backend'deki /auth/login endpointine istek atıyoruz
            const response = await api.post('/auth/login', { username, password });
            
            // Dönen token'ı tarayıcı hafızasına kaydediyoruz
            localStorage.setItem('token', response.data.token); 
            
            // Başarılıysa öğrenci listesine yönlendir (Henüz yapmadık ama oraya gidecek)
            navigate('/students'); 
        } catch (err) {
            // Hata mesajını göster
            setError('Giriş başarısız! Kullanıcı adı veya şifre hatalı.');
            console.error(err);
        }
    };

    return (
        <div className="flex items-center justify-center h-screen bg-gray-100">
            <div className="p-8 bg-white rounded shadow-md w-96">
                <h2 className="mb-4 text-2xl font-bold text-center text-gray-800">Yurt Yönetim Paneli</h2>
                
                {/* Selenium için ID: error-message */}
                {error && <div id="error-message" className="mb-4 text-red-600 text-sm font-semibold text-center">{error}</div>}
                
                <form onSubmit={handleLogin}>
                    <div className="mb-4">
                        <label className="block mb-2 text-sm font-bold text-gray-700">Kullanıcı Adı</label>
                        <input
                            id="username" /* Selenium ID */
                            type="text"
                            className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            placeholder="admin"
                            required
                        />
                    </div>
                    <div className="mb-6">
                        <label className="block mb-2 text-sm font-bold text-gray-700">Şifre</label>
                        <input
                            id="password" /* Selenium ID */
                            type="password"
                            className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="******"
                            required
                        />
                    </div>
                    <button
                        id="login-btn" /* Selenium ID */
                        type="submit"
                        className="w-full p-2 text-white bg-blue-600 rounded hover:bg-blue-700 transition duration-200"
                    >
                        Giriş Yap
                    </button>
                </form>
            </div>
        </div>
    );
}