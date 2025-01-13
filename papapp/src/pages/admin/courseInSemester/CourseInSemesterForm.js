import React, { useEffect, useState } from 'react'
import { useClient } from '../../../components/ClientContext'
import Form from '../../../components/common/Form';
import { useNavigate, useParams } from 'react-router-dom';

const CourseInSemesterForm = () => {
    const [courses, setCourses] = useState([]);
    const [selectedCours, setSelectedCours] = useState();
    const [message, setMessage] = useState("");
    const client = useClient();
    const { semesterId } = useParams();
    const navigate = useNavigate();

    const getCourses = async (e) => {
        const data = await client.getCourses();
        setCourses(data);
        console.log(data)
    }

    const handleChange = (e) => {
        setSelectedCours(e.target.value);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const data = await client.addCourseInSemester(semesterId, selectedCours);
            navigate(`/admin/semesters/${semesterId}/courses`);
        } catch (error) {
            await setMessage(error.message || "Błąd podczas dodawania kursu");
        }
    }

    useEffect(() => {
        getCourses();
    }, [])

    const selectData = [{
        name: "course_code", label: "Kod kursu", options: courses.map(course => ({ value: course.course_code, label: course.title })), defaultValue: selectedCours, onChange: handleChange
    }]

    return (
        <Form selectData={selectData} buttonName="Dodaj" formName={`Dodaj przedmiot do semestru ${semesterId}`} onSubmit={handleSubmit} message={message} />
    )
}

export default CourseInSemesterForm