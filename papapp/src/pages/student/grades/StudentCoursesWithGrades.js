import React, { useState, useEffect } from 'react';
import { useClient } from '../../../components/ClientContext';
import { Link, useNavigate, useParams } from 'react-router-dom';
import List from '../../../components/common/List';

const StudentCoursesWithGrades = () => {
    const [courseInSemester, setCourseInSemester] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const navigate = useNavigate();
    const { semesterId } = useParams('');

    const getCoursesInSemester = async () => {
        try {
            const data = await client.getStudentsFinalGrades(semesterId, client.userId);
            const result = data.map(({ course_code }) => ({ course_code }));
            setCourseInSemester(result);
        } catch (error) {
            await setError('Błąd podczas ładowania danych: ' + error.message);
        }
    };

    const handleView = (e) => {
        e.preventDefault();
        navigate(`${e.target.value}`);
    }

    useEffect(() => {
        getCoursesInSemester();
    }, []);

    //console.log(courseInSemester);

    return (
        <List listName={`Przedmioty w semestrze ${semesterId}`} columnNames={['Kod przedmotu']} data={courseInSemester} error={error} userButtons={true} handleView={handleView} id="course_code" />
    )
}

export default StudentCoursesWithGrades