import React from 'react'

const SidebarItem = ({ link, label, onClick = '' }) => {
    return (
        <li className="nav-item">
            <a className="nav-link text-white" href={link} onClick={onClick} dangerouslySetInnerHTML={{ __html: label }}>
            </a>
        </li>
    )
}

export default SidebarItem