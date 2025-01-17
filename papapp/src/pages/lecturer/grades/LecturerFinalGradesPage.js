import React, { useState, useEffect } from 'react';
import { useClient } from '../../../components/ClientContext';
import { useParams } from 'react-router-dom';
import FormInput from '../../../components/common/FormInput';

const LecturerFinalGradesPage = () => {
    const [listData, setListData] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const { semesterId, courseId } = useParams('');

    const getDataToList = async () => {
        try {
            const data = await client.getCourseFinalGrades(semesterId, courseId);
            let formData = {}
            data.map((item) => (formData[item.user_id] = { name: `${item.name} ${item.surname}`, grade: item.grade, user_id: item.user_id }))
            setListData(formData);
        } catch (error) {
            setError(error.message);
        }
    };

    const onChange = (e) => {
        //console.log(e);
        let keys = e.target.name;
        listData[keys].grade = e.target.value;
    }

    const onSubmit = async (e) => {
        e.preventDefault();

        try {
            await client.updateFinalGrades(semesterId, courseId, listData)
            setError("Zaktualizowano!")
        }
        catch (error) {
            await setError(error.message);
        }

        getDataToList();
    }

    useEffect(() => {
        getDataToList();
        // setListData({ 1: { name: "Kolokwium", grade: 5, description: "test" } })
    }, [])

    return (
        <div>
            <h1>Oceny końcowe</h1>

            {error && <p>{error}</p>}
            <form onSubmit={onSubmit}>
                <table className='table'>
                    <thead>
                        <tr>
                            <th>Student</th>
                            <th>Ocena</th>
                        </tr>
                    </thead>
                    <tbody>
                        {Object.entries(listData).map(([key, item]) => (
                            <tr>
                                <td>{item.name} {item.max && `[max: ${item.max} pkt.]`}</td>

                                <td><FormInput key={item.user_id} name={item.user_id} type="number" defaultValue={item.grade} onChange={onChange} step="0.5" min={2} max={5} /></td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                <button type="submit" className="btn btn-primary" /*dangerouslySetInnerHTML={{ __html: buttonName }}*/>Zapisz</button>
            </form>
        </div >
    )
}

export default LecturerFinalGradesPage