import React, { useState, useEffect } from 'react';
import { useClient } from '../../../components/ClientContext';
import { useParams } from 'react-router-dom';
import FormInput from '../../../components/common/FormInput';

const LecturerGradesListPage = ({ type = "student" }) => {
    const [listData, setListData] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const { semesterId, courseId, searchId } = useParams('');
    const [colName, setColName] = useState('Student');

    const getDataToList = async () => {
        try {
            let data;
            if (type === "student") {
                data = await client.getGradeCategoriesInCourse(semesterId, courseId);
                let formData = {}
                data.map(({ category_id, description, max_grade }) => (formData[category_id] = { name: description, category_id, user_id: searchId, max: max_grade }))
                data = await client.getAllGradesOfCourseForUser(semesterId, courseId, searchId);
                data.map(({ category_id, category_description, grade, description }) => (formData[category_id] = { ...formData[category_id], name: category_description, grade, description }))
                setListData(formData);
            } else {
                const catData = await client.getGradeCategory(semesterId, courseId, searchId);
                data = await client.getCourseStudents(semesterId, courseId);
                let formData = {};
                data.map(({ user_id, name, surname }) => (formData[user_id] = { category_id: searchId, user_id, name: `${name} ${surname}`, max: catData.max_grade }))
                data = await client.getGradesByCategory(semesterId, courseId, searchId);
                data.map(({ user_id, category_description, grade, description }) => (formData[user_id] = { ...formData[user_id], grade, description }))
                setListData(formData);
            }
        } catch (error) {
            setError(error.message);
        }
    };

    const onChange = (e) => {
        console.log(e);
        let keys = e.target.name.split(',');
        listData[keys[0]][keys[1]] = e.target.value;
    }

    const onSubmit = async (e) => {
        e.preventDefault();

        try {
            await client.insertGrade(semesterId, courseId, listData)
            setError("Zaktualizowano!")
        }
        catch (error) {
            await setError(error.message);
        }

        getDataToList();
    }

    useEffect(() => {
        if (type === "student") {
            setColName('Kategoria')
        }
        getDataToList();
        // setListData({ 1: { name: "Kolokwium", grade: 5, description: "test" } })
    }, [])

    return (
        <div>
            <h1>Oceny</h1>

            {error && <p>{error}</p>}
            <form onSubmit={onSubmit}>
                <table className='table'>
                    <thead>
                        <tr>
                            <th>{colName}</th>
                            <th>Ocena</th>
                            <th>Opis</th>
                        </tr>
                    </thead>
                    <tbody>
                        {Object.entries(listData).map(([key, item]) => (
                            <tr>
                                <td>{item.name} {item.max && `[max: ${item.max} pkt.]`}</td>

                                <td><FormInput key={key} name={[key, "grade"]} type="number" defaultValue={item.grade} onChange={onChange} step="0.01" min={0} max={item.max} /></td>

                                <td><FormInput key={key} objectData={key} name={[key, "description"]} type="text" defaultValue={item.description} onChange={onChange} /></td>

                            </tr>
                        ))}
                    </tbody>
                </table>
                <button type="submit" className="btn btn-primary" /*dangerouslySetInnerHTML={{ __html: buttonName }}*/>Zapisz</button>
            </form>
        </div >
    )
}

export default LecturerGradesListPage