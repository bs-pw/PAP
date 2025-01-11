import React from 'react'
import { useClient } from '../ClientContext';

const Header = () => {
    const client = useClient();
    return (
        <header className="bg-primary text-white text-center py-4 position-relative">
            <h1 className="mb-0">PAP</h1>
            <p className="position-absolute m-0" style={{ bottom: '10px', right: '10px' }}>
                Witaj, {client.name} {client.surname}
            </p>
        </header>

    )
}

export default Header