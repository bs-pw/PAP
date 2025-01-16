import React, { useState, useEffect } from 'react';
import { useClient } from '../../../components/ClientContext';
import { Link, useNavigate, useParams } from 'react-router-dom';
import List from '../../../components/common/List';
// import GradeListForm from '../../../components/common/GradeListForm';
import FormInput from '../../../components/common/FormInput';

const LecturerGradesListPage = ({ type = "student" }) => {
    const [listData, setListData] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const navigate = useNavigate();
    const { semesterId, courseId, searchId } = useParams('');
    const [listName, setListName] = useState(`Lista studentów ${courseId} ${semesterId}`);
    const [columnNames, setColumnNames] = useState(["ID", "Imię", "Nazwisko"]);
    const [idList, setIdList] = useState("user_id");

    const getDataToList = async () => {
        try {
            let data;
            if (type === "student") {
                data = await client.getGradeCategoriesInCourse(semesterId, courseId);
                let formData = {}
                data.map(({ category_id, description }) => (formData[category_id] = { name: description }))
                data = await client.getAllGradesOfCourseForUser(semesterId, courseId, searchId);
                // data = data.map(({ user_id, name, surname }) => ({ user_id, name, surname }))
                data.map(({ category_id, category_description, grade, description }) => (formData[category_id] = { name: category_description, grade, description }))
                setListData(formData);
            } else {
                data = await client.getGradesByCategory(semesterId, courseId, searchId);
                // data = data.map(({ category_id, description, max_grade }) => ({ category_id, description, max_grade }))
            }
            //setListData(data);
            console.log(data)
        } catch (error) {
            setError(error.message);
        }
    };

    const onChange = (e) => {
        console.log(e);
        // console.log(e.target.name[0]);
        // return;
        let keys = e.target.name.split(',');
        listData[keys[0]][keys[1]] = e.target.value;
        console.log(listData)

    }

    const onSubmit = async (e) => {
        e.preventDefault();

        try {
            await client.insertGrade(semesterId, courseId, listData)
        }
        catch (error) {
            setError(error.message);
        }


    }

    useEffect(() => {
        if (type !== "student") {
            setListName(`Kategorie ocen ${courseId} ${semesterId}`);
            setColumnNames(["ID", "Nazwa", "Maks punktów"]);
            setIdList("category_id")
        }
        getDataToList();
        // setListData({ 1: { name: "Kolokwium", grade: 5, description: "test" } })
        console.log(listData)
    }, [])


    // const item = { key: '2', fieldId: 'grade', value: '5', type: 'text' }

    // const data = listData.map(({ grade }) => ({ grade }))
    // const columnNames = ["Imię i Nazwisko", "Ocena", "Opis"]

    return (
        // <List listName={listName} columnNames={columnNames} data={listData} error={error} userButtons={true} handleViev={handleView} id={idList} />
        <div>
            <h1>Oceny</h1>

            {error && <p>{error}</p>}
            <form onSubmit={onSubmit}>
                <table className='table'>
                    <thead>
                        <tr>
                            <th>Kategoria</th>
                            <th>Ocena</th>
                            <th>Opis</th>
                        </tr>
                    </thead>
                    <tbody>
                        {/* Object.entries(map).map(([k,v]) => `${k}_${v}`) */}
                        {Object.entries(listData).map(([key, item]) => (
                            <tr>
                                <td>{item.name}</td>

                                <td><FormInput key={key} name={[key, "grade"]} type="number" defaultValue={item.grade} onChange={onChange} step="0.01" /></td>

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