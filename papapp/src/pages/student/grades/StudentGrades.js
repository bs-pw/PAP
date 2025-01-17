import React, { useState, useEffect } from 'react';
import { useClient } from '../../../components/ClientContext';
import { useParams } from 'react-router-dom';
import List from '../../../components/common/List';

const StudentGrades = () => {
    const [listData, setListData] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const { semesterId, courseId } = useParams('');

    const getDataToList = async () => {
        try {
            let data = await client.getGradeCategoriesInCourse(semesterId, courseId);
            //console.log(data)
            let formData = {}
            data.map(({ category_id, description, max_grade }) => (formData[category_id] = { name: description, max: max_grade }))
            data = await client.getAllGradesOfCourseForUser(semesterId, courseId, client.userId);
            data.map(({ category_id, category_description, grade, description }) => (formData[category_id] = { ...formData[category_id], grade, description }))
            setListData(formData);
        } catch (error) {
            setError(error.message);
        }
    };

    useEffect(() => {
        getDataToList();
        //console.log(listData)
    }, [])

    let showData = [];
    Object.entries(listData).map(([key, item]) => (
        showData.push([item.name, `[${item.max}pkt]`, item.grade, item.description])
    ))

    //console.log(showData)

    return (
        <List listName={`Oceny z ${courseId}`} columnNames={['Kategoria', 'Max', 'Punkty', 'Opis']} data={showData} error={error} />
    )
}

export default StudentGrades