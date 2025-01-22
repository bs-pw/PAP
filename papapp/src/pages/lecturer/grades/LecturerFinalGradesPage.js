import React, { useState, useEffect } from 'react';
import { useClient } from '../../../components/ClientContext';
import { useNavigate, useParams } from 'react-router-dom';
import FormInput from '../../../components/common/FormInput';

const LecturerFinalGradesPage = () => {
    const [listData, setListData] = useState([]);
    const [error, setError] = useState('');
    const client = useClient();
    const { semesterId, courseId } = useParams('');
    const navigate = useNavigate();
    const [refreshKey, setRefreshKey] = useState(0);

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

    const generateProtocol = async (e) => {
        e.preventDefault();
        try {
            await client.closeSemesterAndGetProtocol(semesterId, courseId).then(res => res.blob())
                .then(async (blob) => {
                    var file = window.URL.createObjectURL(blob);
                    window.open(file, '_blank');
                    await client.isClosed(semesterId, courseId);
                    setRefreshKey(prevKey => prevKey + 1);
                });
        } catch (error) {
            await setError(error.message);
        }
    }

    useEffect(() => {
        getDataToList();
    }, [refreshKey])

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

                                <td><FormInput key={item.user_id} name={item.user_id} type="number" defaultValue={item.grade} onChange={onChange} step="0.5" min={2} max={5} disabled={client.isLocked} /></td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                <div className='d-flex justify-content-between'>
                    {!client.isLocked && <button type="submit" className="btn btn-primary">Zapisz</button>}
                    <button type="submit" className="btn btn-danger" onClick={generateProtocol}>{client.isLocked ? 'W' : 'Zamknij semestr i w'}ygeneruj raport</button>
                </div>
            </form>
        </div >
    )
}

export default LecturerFinalGradesPage