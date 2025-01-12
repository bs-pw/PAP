import React, { useState, useEffect } from 'react';
import { useClient } from '../../../components/ClientContext';
import { Link, useNavigate } from 'react-router-dom';
import List from '../../../components/common/List';

const SemesterPage = () => {
    const [semesters, setSemesters] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const navigate = useNavigate();

    const getSemesters = async () => {
        try {
            const data = await client.getSemesters();
            setSemesters(data);
        } catch (error) {
            setError('Błąd podczas ładowania danych: ' + error.message);
        }
    };

    const handleDeleteSemester = async (e) => {
        e.preventDefault();
        try {
            if (await client.deleteSemester(e.target.value)) {
                getSemesters();
            }
        } catch (error) {
            setError('Błąd podczas usuwania semestru!');
        }
    }

    const handleEditSemester = async (e) => {
        e.preventDefault();
        navigate(`/admin/semesters/edit/` + e.target.value);
    }

    useEffect(() => {
        getSemesters();
    }, []);

    console.log(semesters);

    return (
        <>
            <Link to="/admin/semesters/create" className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i class="bi bi-plus-lg"></i> Nowy</Link >
            <List listName='Lista semestrów' columnNames={['Kod', 'Data początku', 'Data końca']} data={semesters} error={error} adminButtons={true} handleDelete={handleDeleteSemester} handleEdit={handleEditSemester} id="semester_code" />
        </>
    )
}

export default SemesterPage