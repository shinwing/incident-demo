import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import IncidentList from './components/incident/IncidentList';

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Navigate to="/incidents" />} /> {/* 默认重定向到 /incidents */}
                    <Route path="/incidents" element={<IncidentList />} /> {/* 事件列表组件 */}
                    {/* 如果你有其他路由，可以在这里添加 */}
                </Routes>
            </div>
        </Router>
    );
}

export default App;
