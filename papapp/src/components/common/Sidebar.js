import React from 'react'
import { useClient } from '../ClientContext';
import { useNavigate } from 'react-router-dom'

const Sidebar = () => {
    const client = useClient();
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            const data = await client.logout();
            navigate('/');
        } catch (error) {
            console.error('Błąd połączenia z serwerem:', error);
        }
    };

    return (
        <div className="bg-dark text-white p-3" style={{ width: 250 }}>
            <h2 className="mb-4">Menu</h2>
            <ul className="nav flex-column">
                <li className="nav-item">
                    <a className="nav-link text-white" href="/">
                        Home
                    </a>
                </li>
                <li className="nav-item">
                    <a className="nav-link text-white" href="/lecturer">
                        Wykładowcy
                    </a>
                </li>
                <li className="nav-item">
                    <a className="nav-link text-white" href="/admin/register">
                        Zarejestruj
                    </a>
                </li>
                <li className="nav-item">
                    <a className="nav-link text-white" href="#">
                        Dummy
                    </a>
                </li>
                <li>
                    <a className="nav-link text-white" href="#" onClick={handleLogout}>
                        <i className="bi bi-power"></i> Wyloguj
                    </a>
                </li>
            </ul>
        </div>

    )
}

export default Sidebar