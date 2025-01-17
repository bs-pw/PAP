import React, { useState, useEffect } from 'react';
import { useClient } from '../components/ClientContext';

const DashboardPage = () => {

    const [lecturers, setLecturers] = useState([]);
    const [error, setError] = useState(null);
    const client = useClient();

    const getLecturers = async () => {
        try {
            const data = await client.getLecturers();
            //console.log(data);
            setLecturers(data);
        } catch (error) {
            setError('Błąd podczas ładowania danych: ' + error.message);
        }
    };

    useEffect(() => {
        getLecturers();
    }, []);

    return (
        <div>
            <h1>Lista wykładowców</h1>

            {error && <p>{error}</p>}

            <table className='table'>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Imię</th>
                        <th>Nazwisko</th>
                        <th>Email</th>
                    </tr>
                </thead>
                <tbody>
                    {lecturers.map((lecturer) => (
                        <tr key={lecturer.user_id}>
                            <td>{lecturer.user_id}</td>
                            <td>{lecturer.name}</td>
                            <td>{lecturer.surname}</td>
                            <td>{lecturer.mail}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default DashboardPage;