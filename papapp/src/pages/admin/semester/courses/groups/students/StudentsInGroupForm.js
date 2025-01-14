import React, { useEffect, useState } from 'react'
import { useClient } from '../../../../../../components/ClientContext'
import Form from '../../../../../../components/common/Form';
import { useNavigate, useParams } from 'react-router-dom';

const StudentsInGroupForm = () => {
    const [students, setStudents] = useState([]);
    const [selectedStudent, setSelectedStudent] = useState();
    const [message, setMessage] = useState("");
    const client = useClient();
    const { semesterId, courseId, groupId } = useParams();
    const navigate = useNavigate();

    const getStudents = async (e) => { }
    const handleChange = (e) => {
        setSelectedStudent(e.target.value);
    }
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await client.addStudentInGroup
        } catch (error) {
            setMessage(error.message);
        }
    }

    return (
        <div>StudentsInGroupForm</div>
    )
}

export default StudentsInGroupForm