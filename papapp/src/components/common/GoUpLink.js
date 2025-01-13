import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const GoUpLink = () => {
    const location = useLocation(); // Pobieramy aktualną lokalizację
    const pathParts = location.pathname.split('/').filter(Boolean); // Dzielimy ścieżkę na części

    const parentPath = `/${pathParts.slice(0, -1).join('/')}`; // Usuwamy ostatni segment i tworzymy nową ścieżkę

    return (
        <Link to={parentPath} className='nav-link align-right'>↑</Link>
    );
};

export default GoUpLink;
