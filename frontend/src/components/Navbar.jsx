import { useNavigate, Link } from 'react-router-dom';
import api from '../api/axiosConfig';

export default function Navbar() {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            // Eğer backend tarafında logout endpointi hazırsa aktif edebilirsin
            // await api.post('/auth/logout'); 
            
            localStorage.removeItem('user'); 
            navigate('/login'); 
            alert('Başarıyla çıkış yapıldı.');
        } catch (error) {
            console.error('Çıkış yapma hatası:', error);
            alert('Çıkış yaparken bir hata oluştu.');
        }
    };

    return (
        <nav className="bg-gradient-to-r from-indigo-700 to-indigo-900 text-white shadow-lg sticky top-0 z-50">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex items-center justify-between h-16">
                    
                    {/* Logo/Başlık - Dashboard'a yönlendirir */}
                    <div className="flex-shrink-0">
                        <Link to="/dashboard" className="text-2xl font-bold tracking-wide hover:text-indigo-200 transition">
                            Yurt Yönetim Sistemi
                        </Link>
                    </div>

                    {/* Navigasyon Linkleri - Link bileşeni kullanımı sayfayı yenilemez */}
                    <div className="hidden md:block">
                        <div className="ml-10 flex items-baseline space-x-2">
                            <Link to="/dashboard" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-indigo-600 transition">
                                Dashboard
                            </Link>
                            <Link to="/students" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-indigo-600 transition">
                                Öğrenciler
                            </Link>
                            <Link to="/buildings" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-indigo-600 transition">
                                Binalar
                            </Link>
                            <Link to="/rooms" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-indigo-600 transition">
                                Odalar
                            </Link>
                        </div>
                    </div>

                    {/* Sağ Taraf: Çıkış Butonu */}
                    <div className="hidden md:block">
                        <button
                            onClick={handleLogout}
                            className="px-4 py-2 border border-transparent text-sm font-medium rounded-lg text-white bg-red-600 hover:bg-red-700 shadow-md transition transform hover:scale-105"
                        >
                            Güvenli Çıkış
                        </button>
                    </div>

                    {/* Mobil Menü (İsteğe bağlı) */}
                    <div className="-mr-2 flex md:hidden">
                        <button type="button" className="inline-flex items-center justify-center p-2 rounded-md text-indigo-200 hover:text-white hover:bg-indigo-600 outline-none">
                            <svg className="h-6 w-6" stroke="currentColor" fill="none" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16" />
                            </svg>
                        </button>
                    </div>
                </div>
            </div>
        </nav>
    );
}