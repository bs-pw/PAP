import React, { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { useClient } from '../../components/ClientContext';

const LecturerGradeMenuPage = () => {

    const { semesterId, courseId } = useParams();
    const [amICoo, setAmICoo] = useState(false);

    const client = useClient();

    const amICoordinator = async () => {
        try {
            const data = await client.checkIfIsCoordinatorOfCourse(semesterId, courseId, client.userId);
            setAmICoo(data);
        } catch (error) {
            //console.log('Error');
        }
    }

    useEffect(() => {
        amICoordinator();
    }, [])

    return (
        <>
            <Link to="grade-by-student" className="nav-link">Oceny wg studentów</Link>
            <Link to="grade-by-category" className="nav-link">Oceny wg kategorii</Link>
            {amICoo && <Link to="final-grade" className="nav-link">Oceny końcowe</Link>}
        </>
    )
}

export default LecturerGradeMenuPage