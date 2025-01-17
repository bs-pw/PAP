import React, { useState, useEffect } from 'react';
import { useClient } from '../../components/ClientContext';
import { useParams } from 'react-router-dom';
import List from '../../components/common/List';

const StudentFinalGradesInSem = () => {
    const [finalGrades, setFinalGrades] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const { semesterId } = useParams();

    const getStudentFinalGradesInSem = async () => {
        try {
            const data = await client.getStudentsFinalGrades(semesterId, client.userId);
            const filtered = data.map(({ course_code, grade }) => ({ course_code, grade }))
            setFinalGrades(filtered);
        } catch (error) {
            setError('Błąd podczas ładowania danych: ' + error.message);
        }
    }

    useEffect(() => {
        getStudentFinalGradesInSem();
    }, []);

    return (
        <List listName={`Oceny końcowe w ${semesterId}`} columnNames={['Przedmiot', 'Ocena']} data={finalGrades} error={error} />
    )
}

export default StudentFinalGradesInSem