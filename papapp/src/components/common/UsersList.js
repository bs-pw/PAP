import React, { useState, useEffect } from 'react';
import { useClient } from '../ClientContext';
import { Navigate } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

const UsersList = ({ adminButtons = false }) => {
    const [users, setUsers] = useState([]);
    const [userTypes, setUserTypes] = useState([]);
    const [error, setError] = useState(null);
    const client = useClient();
    const navigate = useNavigate();

    const getUsers = async () => {
        try {
            const data = await client.getUsers();
            console.log(data);
            setUsers(data);
        } catch (error) {
            setError('Błąd podczas ładowania danych: ' + error.message);
        }
    };

    const handleDeleteUser = async (e) => {
        e.preventDefault();
        if (e.target.value == client.userId) {
            setError('Nie możesz usunąć samego siebie');
            return;
        }
        try {
            if (await client.deleteUser(e.target.value)) {
                getUsers();
            }
        } catch (error) {
            setError('Błąd podczas usuwania użytkownika!');
        }
    }

    const handleEditUser = async (e) => {
        e.preventDefault();
        navigate(`/admin/users/edit/` + e.target.value);

    }


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
                            <td>{user.type}</td>
                            {adminButtons && <td>
                                <button className='btn btn-sm btn-danger' value={user.user_id} onClick={handleDeleteUser}>Usuń</button>
                                {' '}
                                <button className='btn btn-sm btn-warning' value={user.user_id} onClick={handleEditUser}>Edytuj</button>
                            </td>}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default UsersList