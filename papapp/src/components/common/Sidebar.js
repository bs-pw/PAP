import React from 'react'
import { useNavigate } from 'react-router-dom'

const Sidebar = () => {
    const handleLogout = async () => {
        try {
            const response = await fetch('http://localhost/api/auth/logout', {
                method: 'GET',
                credentials: 'include'
            });

            if (response.ok) {
                window.location.href = '/';
            } else {
                console.error('Błąd podczas wylogowania');
            }
        } catch (error) {
            console.error('Błąd połączenia z serwerem:', error);
        }
    };

    return (
        <div className="bg-dark text-white p-3" style={{ width: 250 }}>
            <h2 className="mb-4">Menu</h2>
            <ul className="nav flex-column">
                <li className="nav-item">
                    <a className="nav-link text-white" href="#">
                        Dummy
                    </a>
                </li>
                <li className="nav-item">
                    <a className="nav-link text-white" href="#">
                        Dummy
                    </a>
                </li>
                <li className="nav-item">
                    <a className="nav-link text-white" href="#">
                        Dummy
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