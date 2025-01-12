import React from 'react'

const List = ({ listName, adminButtons, columnNames, data, error, handleDelete, handleEdit }) => {
    data.map((item) => (
        console.log(item)
    ))
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
                                <button className='btn btn-sm btn-danger' value={item.user_id} onClick={handleDelete}>Usuń</button>
                                {' '}
                                <button className='btn btn-sm btn-warning' value={item.user_id} onClick={handleEdit}>Edytuj</button>
                            </td>}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div >
    );
}

export default List