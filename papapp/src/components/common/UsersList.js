import React, { useState, useEffect } from 'react';
import { useClient } from '../ClientContext';

const UsersList = ({ adminButtons = false }) => {
    const [users, setUsers] = useState([]);
    const [userTypes, setUserTypes] = useState([]);
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

    const handleUserTypes = async () => {
        try {
            const data = await client.getUserTypes();
            console.log(data);
            setUserTypes(data);
        } catch (error) {
            console.log(error.message);
        }
    };

    useEffect(() => {
        getUsers();
        handleUserTypes();
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
                        {adminButtons && <th>Akcje</th>}
                    </tr>
                </thead>
                <tbody>
                    {users.map((user) => (
                        <tr key={user.user_id}>
                            <td>{user.user_id}</td>
                            <td>{user.name}</td>
                            <td>{user.surname}</td>
                            <td>{user.mail}</td>
                            <td>{userTypes[user.status]}</td>
                            {adminButtons && <td>
                                <button className='btn btn-sm btn-danger'>Usuń</button>
                                <button className='btn btn-sm btn-warning'>Edytuj</button>
                            </td>}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default UsersList