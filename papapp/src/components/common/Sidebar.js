import React from 'react'
import { useClient } from '../ClientContext';
import { useNavigate } from 'react-router-dom'
import SidebarItem from './SidebarItem';
import GoUpLink from './GoUpLink';

const Sidebar = () => {
    const client = useClient();
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            const data = await client.logout();
            navigate('/');
        } catch (error) {
            console.error('Błąd połączenia z serwerem:', error);
        }
    };

    const usersItems = [
        { link: '/', label: 'Strona główna' },
        // { link: '/lecturer', label: 'Wykładowcy' },
        { link: '/settings', label: 'Ustawienia' },
    ];

    const adminItems = [
        { link: '/admin/users', label: 'Użytkownicy' },
        { link: '/admin/courses', label: 'Kursy' },
        { link: '/admin/semesters', label: 'Semestry' },
        // { link: '/admin/coursesinsemester/24Z', label: 'Kursy w semestrach' },
    ];

    const lecturerItem = [
        { link: '/lecturer', label: 'Oceny' },
        { link: '/admin/semesters', label: '<i class="bi bi-gear"></i> Koordynator' },
    ];

    const studentItems = [
        { link: '/student/semester', label: 'Oceny' },
    ]

    return (
        <div className="bg-dark text-white p-3" style={{ width: 250 }}>
            <h2 className="mb-4">Menu</h2>
            <GoUpLink />
            <ul className="nav flex-column">
                {usersItems.map((item, index) => (
                    <SidebarItem key={index} link={item.link} label={item.label} />
                ))}

                {client.userTypeId == 0 && (
                    <>
                        <SidebarItem link="#" label='<i class="bi bi-gear"></i> Panel Administracyjny' />
                        <ul className="nav flex-column mx-3">
                            {adminItems.map((item, index) => (
                                <SidebarItem key={index} link={item.link} label={item.label} />
                            ))}
                        </ul>
                    </>
                )}

                {(client.userTypeId == 1 || client.userTypeId == 2) && (
                    <>
                        <SidebarItem link="#" label='<i class="bi bi-briefcase-fill"></i> Panel Wykładowcy' />
                        <ul className="nav flex-column mx-3">
                            {lecturerItem.map((item, index) => (
                                <SidebarItem key={index} link={item.link} label={item.label} />
                            ))}
                        </ul>
                    </>
                )}

                {(client.userTypeId == 2 || client.userTypeId == 3) && (
                    <>
                        <SidebarItem link="#" label='<i class="bi bi-briefcase-fill"></i> Panel Studenta' />
                        <ul className="nav flex-column mx-3">
                            {studentItems.map((item, index) => (
                                <SidebarItem key={index} link={item.link} label={item.label} />
                            ))}
                        </ul>
                    </>
                )}

                <SidebarItem link="#" label='<i class="bi bi-power"></i> Wyloguj' onClick={handleLogout} />
            </ul>
        </div>

    )
}

export default Sidebar