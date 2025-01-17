import React from 'react'

const List = ({ listName, adminButtons = null, userButtons = false, columnNames, data, error, handleDelete, handleEdit, handleView, id }) => {
    if (adminButtons === true) adminButtons = [true, true]
    //console.log(adminButtons)
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
                                {adminButtons[0] && <><button className='btn btn-sm btn-danger' value={item[id]} onClick={handleDelete}>Usuń</button>{' '}</>}
                                {adminButtons[1] && <button className='btn btn-sm btn-warning' value={item[id]} onClick={handleEdit}>Edytuj</button>}
                            </td>}
                            {userButtons && <td>
                                <button className='btn btn-sm btn-primary' value={item[id]} onClick={handleView}>Podgląd</button>
                            </td>}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div >
    );
}

export default List