import React from 'react'

const List = ({ listName, adminButtons, columnNames, data, error, handleDelete, handleEdit, id }) => {
    return (
        <div>
            <h1>{listName}</h1>

            {error && <p>{error}</p>}

            <table className='table'>
                <thead>
                    <tr>
                        {columnNames.map((columnName) => (
                            <th>{columnName}</th>
                        ))}
                        {adminButtons && <th>Akcje</th>}
                    </tr>
                </thead>
                <tbody>
                    {data.map((item) => (
                        <tr>
                            {
                                Object.keys(item).map((key) => (
                                    <td>{item[key]}</td>
                                ))
                            }
                            {adminButtons && <td>
                                <button className='btn btn-sm btn-danger' value={item[id]} onClick={handleDelete}>Usuń</button>
                                {' '}
                                <button className='btn btn-sm btn-warning' value={item[id]} onClick={handleEdit}>Edytuj</button>
                            </td>}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div >
    );
}

export default List