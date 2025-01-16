import React, { useState, useEffect } from 'react'
import { useClient } from '../../../components/ClientContext'
import Form from '../../../components/common/Form';
import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

const CourseForm = () => {
    const [formData, setFormData] = useState({
        course_code: "",
        title: ""
    });

    const [message, setMessage] = useState("");
    const [formName, setFormName] = useState("Nowy kurs");
    const [buttonName, setButtonName] = useState("Utwórz");

    const client = useClient();
    const { id } = useParams();
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (id) {
            try {
                const response = await client.updateCourse(id, formData);
                // setMessage("Kurs został dodany");
                navigate("..");
            } catch (error) {
                setMessage(error.message || "Błąd");
            }
        } else {
            try {
                const response = await client.createCourse(formData);
                // setMessage("Kurs został dodany");
                navigate("/admin/courses");
            } catch (error) {
                setMessage(error.message || "Błąd");
            }
        }
    }

    const handleGetCourse = async (courseId) => {
        try {
            const response = await client.getCourse(courseId);
            setFormData(response);
        } catch (error) {
            navigate("/admin/course")
        }
    }

    useEffect(() => {
        if (id) {
            setFormName('Edytuj kurs');
            setButtonName('Zapisz');
            handleGetCourse(id);
        }
    }, [])

    const inputData = [
        { label: 'Kod', name: 'course_code', type: 'text', value: formData.course_code, onChange: handleChange, required: true },
        { label: 'Nazwa', name: 'title', type: 'text', value: formData.title, onChange: handleChange, required: true },
    ]

    return (
        <Form formName={formName} inputData={inputData} buttonName={buttonName} onSubmit={handleSubmit} message={message} />
    )

}

export default CourseForm