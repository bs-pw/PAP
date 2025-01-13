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
        // console.log("ti")
        try {
            if (await client.deleteSemester(e.target.value)) {
                await getSemesters();
            }
        } catch (error) {
            setError('Błąd podczas usuwania semestru!');
        }
    }

    const handleEditSemester = (e) => {
        e.preventDefault();
        navigate(`/admin/semesters/edit/` + e.target.value);
    }

    const handleVievCoursesInSemester = (e) => {
        e.preventDefault();
        navigate(`/admin/semesters/${e.target.value}/courses`);
    }

    useEffect(() => {
        getSemesters();
    }, []);

    console.log(semesters);

    return (
        <>
            <Link to="/admin/semesters/create" className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i class="bi bi-plus-lg"></i> Nowy</Link >
            <List listName='Lista semestrów' columnNames={['Kod', 'Data początku', 'Data końca']} data={semesters} error={error} adminButtons={true} userButtons={true} handleDelete={handleDeleteSemester} handleEdit={handleEditSemester} handleViev={handleVievCoursesInSemester} id="semester_code" />
        </>
    )
}

export default SemesterPage