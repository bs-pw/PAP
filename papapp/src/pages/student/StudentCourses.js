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
      const data = await client.getStudentCourses(client.userId);
      const result = data.map(({ course_code, semester, group_number }) => ({
        course_code,
        semester,
        group_number
      }));
      setCourses(result);
    } catch (error) {
      setError('Błąd podczas ładowania danych: ' + error.message);
    }
  }

  const handleViev = (e) => {
    e.preventDefault();
    navigate('/student/courses/' + e.target.dataset.courseId);
  }

  useEffect(() => {
    getCourses();
  }, []);

  return (
    <List listName='Lista kursów' columnNames={['Nazwa', 'Semestr', 'Nr grupy']} data={courses} error={error} userButtons={true} handleViev={handleViev} id="course_code" />
  )
}

export default StudentCourses