import React, { useState, useEffect } from 'react';
import { useClient } from '../components/ClientContext';

const DashboardPage = () => {

    const [users, setUsers] = useState([]);
    const [error, setError] = useState(null);
    const client = useClient();

    const getUsers = async () => {
        try {
            const data = await client.getUsers();
            console.log(data);
            setUsers(data);
        } catch (error) {
            setError('Błąd podczas ładowania danych: ' + error.message);
        }
    };

    useEffect(() => {
        getUsers();
    }, []);


    return (
        <div>
            <h1>Lista użytkowników</h1>

            {error && <p>{error}</p>}

            <table className='table'>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Imię</th>
                        <th>Nazwisko</th>
                        <th>Email</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map((user) => (
                        <tr key={user.user_id}>
                            <td>{user.user_id}</td>
                            <td>{user.name}</td>
                            <td>{user.surname}</td>
                            <td>{user.mail}</td>
                            <td>{user.status}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default DashboardPage;