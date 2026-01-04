import axios from 'axios';

const API_URL = 'http://localhost:8181/api';

// Token'ı localStorage'dan al
const getAuthHeader = () => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.token) {
        return { Authorization: `Bearer ${user.token}` };
    }
    return {};
};

export const login = async (username, password) => {
    const response = await axios.post(`${API_URL}/auth/login`, { username, password });
    if (response.data.token) {
        localStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
};

export const getStudents = async () => {
    const response = await axios.get(`${API_URL}/students`, { headers: getAuthHeader() });
    return response.data;
};

export const addStudent = async (studentData) => {
    const response = await axios.post(`${API_URL}/students`, studentData, { headers: getAuthHeader() });
    return response.data;
};

// --- YENİ EKLENEN METODLAR ---

// Öğrenci Silme
export const deleteStudent = async (id) => {
    await axios.delete(`${API_URL}/students/${id}`, { headers: getAuthHeader() });
};

// Öğrenci Güncelleme (Bir sonraki adımda kullanacağız)
export const updateStudent = async (id, studentData) => {
    const response = await axios.put(`${API_URL}/students/${id}`, studentData, { headers: getAuthHeader() });
    return response.data;
};

export const logout = () => {
    localStorage.removeItem('user');
};