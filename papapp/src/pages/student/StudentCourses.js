import { useNavigate } from 'react-router-dom';
import { useClient } from '../../components/ClientContext';
import List from '../../components/common/List';
import React, { useState, useEffect } from 'react';

const StudentCourses = () => {
  const [courses, setCourses] = useState([]);
  const [error, setError] = useState('');
  const client = useClient();
  const navigate = useNavigate();

  const getCourses = async () => {
    try {
      const data = await client.getStudentCourses();
      setCourses(data);
    } catch (error) {
      setError('Błąd podczas ładowania danych: ' + error.message);
    }
  }

  useEffect(() => {
    getCourses();
  }, []);

  return (
    <List listName='Lista kursów' columnNames={['ID', 'Nazwa', 'ECTS', 'Semestr', 'Typ kursu']} data={courses} error={error} id="course_id" />
  )
}

export default StudentCourses