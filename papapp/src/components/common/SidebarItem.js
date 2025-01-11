import React from 'react'
import { Link } from 'react-router-dom'

const SidebarItem = ({ link, label, onClick = '' }) => {
    return (
        <li className="nav-item">
            <Link to={link} className="nav-link text-white" onClick={onClick} dangerouslySetInnerHTML={{ __html: label }}></Link>
        </li>
    )
}

export default SidebarItem