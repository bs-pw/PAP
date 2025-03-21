import React, { useState, useEffect } from 'react';
import { useClient } from '../../../components/ClientContext';
import { Link, useNavigate, useParams } from 'react-router-dom';
import List from '../../../components/common/List';

const LecturerGradePage = ({ type = "student" }) => {

    const [listData, setListData] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const navigate = useNavigate();
    const { semesterId, courseId } = useParams('');
    const [listName, setListName] = useState(`Lista studentów ${courseId} ${semesterId}`);
    const [columnNames, setColumnNames] = useState(["ID", "Imię", "Nazwisko"]);
    const [idList, setIdList] = useState("user_id");

    const getDataToList = async () => {
        try {
            let data;
            if (type === "student") {
                data = await client.getCourseStudents(semesterId, courseId);
                data = data.map(({ user_id, name, surname }) => ({ user_id, name, surname }))
            } else {
                data = await client.getGradeCategoriesInCourse(semesterId, courseId);
                data = data.map(({ category_id, description, max_grade }) => ({ category_id, description, max_grade }))
            }
            setListData(data);
        } catch (error) {
            setError(error.message);
        }
    };

    const handleView = (e) => {
        e.preventDefault();
        navigate(`${e.target.value}`);
    }


    useEffect(() => {
        if (type !== "student") {
            setListName(`Kategorie ocen ${courseId} ${semesterId}`);
            setColumnNames(["ID", "Nazwa", "Maks punktów"]);
            setIdList("category_id")
        }
        getDataToList();
    }, [])

    return (
        <List listName={listName} columnNames={columnNames} data={listData} error={error} userButtons={true} handleView={handleView} id={idList} />
    )
}

export default LecturerGradePage