import { Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import StudentList from './pages/StudentList';
import Login from './pages/Login';
import AddRoom from './pages/AddRoom';
import AddStudent from './pages/AddStudent'; // Dosya yolunun ve isminin doğruluğundan emin ol
import EditStudent from './pages/EditStudent';
import AddBuilding from './pages/AddBuilding';
import Dashboard from './pages/Dashboard';
import BuildingList from './pages/BuildingList';
import Profile from './pages/Profile';
import RoomDetail from './pages/RoomDetail';
import RoomList from './pages/RoomList';
// Diğer importlar... 

function App() {
  return (
    <>
      <Navbar /> {/* Artık güvenle çalışacak */}
      <main className="container mx-auto p-4">
        <Routes>
    <Route path="/login" element={<Login />} />
    <Route path="/students" element={<StudentList />} />
    <Route path="/add-student" element={<AddStudent />} />
    <Route path="/edit-student/:id" element={<EditStudent />} />
    <Route path="/add-building" element={<AddBuilding />} />
    <Route path="/dashboard" element={<Dashboard />} />
    <Route path="/add-room" element={<AddRoom />} />
    <Route path="/profile" element={<Profile />} />
    <Route path="/room/:id" element={<RoomDetail />} />
    <Route path="/buildings" element={<BuildingList />} />
    <Route path="/rooms" element={<RoomList />} />
    <Route path="/room-detail" element={<RoomDetail />} />  

    <Route path="/" element={<Navigate to="/login" />} />
</Routes>
      </main>
    </>
  );
}

export default App;