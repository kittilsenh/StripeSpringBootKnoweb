// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import PaymentForm from './PaymentForm';
import SuccessPage from './SuccessPage';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<PaymentForm />} />
                <Route path="/success" element={<SuccessPage />} />
            </Routes>
        </Router>
    );
}

export default App;
