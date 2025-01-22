import React, { useState, useEffect } from 'react';
import { useClient } from '../../../../../components/ClientContext';
import { Link, useNavigate, useParams } from 'react-router-dom';
import List from '../../../../../components/common/List';
import { TimeConverter, DayConverter } from '../../../../../components/common/TimeConverter';

const GradeCategoriesPage = () => {
    const [gradeCategories, setGradeCategories] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const navigate = useNavigate();
    const { semesterId, courseId } = useParams('');

    const getGradeCategories = async () => {
        try {
            const response = await client.getGradeCategoriesInCourse(semesterId, courseId);
            const data = response.map(({ category_id, description, max_grade }) => ({ category_id, description, max_grade }))
            setGradeCategories(data);
        } catch (error) {
            setError(error.message);
        }
    }

    const handleDelete = async (e) => {
        e.preventDefault();
        try {
            await client.deleteGradeCategory(semesterId, courseId, e.target.value);
            getGradeCategories();
        } catch (error) {
            setError(error.message);
        }
    }

    const handleEdit = (e) => {
        e.preventDefault();
        navigate(`${e.target.value}/edit`);
    }

    useEffect(() => {
        getGradeCategories();
    }, [])

    return (
        <>
            {!client.isLocked && <Link to={`add`} className='nav-link text-primary' style={{ fontSize: "1.2em" }}><i className="bi bi-plus-lg"></i> Nowy</Link >}
            <List listName={`Punktacja przedmiotu ${courseId} w semestrze ${semesterId}`} columnNames={['Id', 'Opis', 'Max punktów']} data={gradeCategories} error={error} adminButtons={!client.isLocked} handleDelete={handleDelete} handleEdit={handleEdit} id="category_id" />
        </>
    )
}

export default GradeCategoriesPage