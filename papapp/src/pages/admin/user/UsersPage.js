import React, { useState, useEffect } from 'react';
import { useClient } from '../../../components/ClientContext';
import { useNavigate } from 'react-router-dom';
import List from '../../../components/common/List';
import { Link } from 'react-router-dom'

const UsersPage = () => {
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

    const columnNames = ['ID', 'Imię', 'Nazwisko', 'Typ użytkownika', 'Email'];

    return (
        <>
            <Link to="/admin/users/register" className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i class="bi bi-plus-lg"></i> Nowy</Link >
            <List listName='Lista użytkowników' adminButtons={true} columnNames={columnNames} data={users} error={error} handleDelete={handleDeleteUser} handleEdit={handleEditUser} />
        </>
    )
}

export default UsersPage