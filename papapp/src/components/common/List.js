import React from 'react'

const List = ({ listName, adminButtons, userButtons = false, columnNames, data, error, handleDelete, handleEdit, handleViev, id }) => {
    return (
        <div>
            <h1>{listName}</h1>

            {error && <p>{error}</p>}

            <table className='table'>
                <thead>
                    <tr>
                        {columnNames.map((columnName) => (
                            <th key={columnName}>{columnName}</th>
                        ))}
                        {adminButtons && <th>Akcje</th>}
                        {userButtons && <th></th>}
                    </tr>
                </thead>
                <tbody>
                    {data.map((item) => (
                        <tr>
                            {
                                Object.keys(item).map((key) => (
                                    <td key={key}>{item[key]}</td>
                                ))
                            }
                            {adminButtons && <td>
                                <button className='btn btn-sm btn-danger' value={item[id]} onClick={handleDelete}>Usuń</button>
                                {' '}
                                <button className='btn btn-sm btn-warning' value={item[id]} onClick={handleEdit}>Edytuj</button>
                            </td>}
                            {userButtons && <td>
                                <button className='btn btn-sm btn-primary' value={item[id]} onClick={handleViev}>Podgląd</button>
                            </td>}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div >
    );
}

export default List