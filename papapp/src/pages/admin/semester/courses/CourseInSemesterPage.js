import React, { useState, useEffect } from 'react';
import { useClient } from '../../../../components/ClientContext';
import { Link, useNavigate, useParams } from 'react-router-dom';
import List from '../../../../components/common/List';

const CourseInSemesterPage = () => {
    const [courseInSemester, setCourseInSemester] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const navigate = useNavigate();
    const { semesterId } = useParams('');

    const getCoursesInSemester = async () => {
        try {
            let data;
            if (client.userTypeId == 0)
                data = await client.getCoursesInSemesterBySemester(semesterId);
            else
                data = await client.getCoursesInSemesterByCoordinator(semesterId, client.userId);
            const result = data.map(({ course_code }) => ({ course_code }));
            setCourseInSemester(result);
        } catch (error) {
            await setError('Błąd podczas ładowania danych: ' + error.message);
        }
    };

    const handleDeleteSemester = async (e) => {
        e.preventDefault();
        try {
            if (await client.deleteCourseInSemester(semesterId, e.target.value)) {
                await getCoursesInSemester();
            }
        } catch (error) {
            setError('Błąd podczas usuwania semestru!');
        }
    }

    const handleVievGroups = (e) => {
        e.preventDefault();
        navigate(`${e.target.value}`);
    }

    useEffect(() => {
        getCoursesInSemester();
    }, []);

    //console.log(courseInSemester);

    return (
        <>
            {client.userTypeId == 0 && <Link to={`/admin/semesters/${semesterId}/courses/add`} className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i className="bi bi-plus-lg"></i> Nowy</Link >}
            <List listName={`Przedmioty w semestrze ${semesterId}`} columnNames={['Kod przedmotu']} data={courseInSemester} error={error} adminButtons={client.userTypeId == 0 && [true, false]} userButtons={true} handleView={handleVievGroups} handleDelete={handleDeleteSemester} id="course_code" />
        </>
    )
}

export default CourseInSemesterPage