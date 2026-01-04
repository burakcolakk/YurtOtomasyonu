import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom' // Bunu ekle
import App from './App'
import './index.css'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter> {/* Uygulamayı buraya al */}
      <App />
    </BrowserRouter>
  </React.StrictMode>,
)