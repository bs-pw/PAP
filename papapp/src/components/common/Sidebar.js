import React from 'react'

const Sidebar = () => {
    return (
        <div className="bg-dark text-white p-3 vh-100" style={{ width: 250 }}>
            <h2 className="mb-4">Menu</h2>
            <ul className="nav flex-column">
                <li className="nav-item">
                    <a className="nav-link text-white" href="#">
                        Home
                    </a>
                </li>
                <li className="nav-item">
                    <a className="nav-link text-white" href="#">
                        About
                    </a>
                </li>
                <li className="nav-item">
                    <a className="nav-link text-white" href="#">
                        Services
                    </a>
                </li>
                <li className="nav-item">
                    <a className="nav-link text-white" href="#">
                        Contact
                    </a>
                </li>
                <li>
                    <a className="nav-link text-white" href="#">
                        <i class="bi bi-power"></i> Wyloguj
                    </a>
                </li>
            </ul>
        </div>

    )
}

export default Sidebar