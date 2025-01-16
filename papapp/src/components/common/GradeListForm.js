import React from 'react'
import FormInput from './FormInput'

const GradeListForm = ({ listName, error, columnNames, data, onChange, onSubmit, buttonName }) => {

    return (
        <div>
            <h1>{listName}</h1>

            {error && <p>{error}</p>}
            <form onSubmit={onSubmit}>
                <table className='table'>
                    <thead>
                        <tr>
                            {
                                columnNames.map((column, index) => {
                                    return <th key={index}>{column}</th>
                                })
                            }
                        </tr>
                    </thead>
                    <tbody>
                        {data.map((item) => (
                            <tr>
                                <td>{item.user_id} {item.user_name} {item.user_surname}</td>

                                <td><FormInput key={item.category_id} name={`data[${item.category_id}][grade]`} type="number" value={item.value} onChange={onChange} step={item.step} /></td>

                                <td><FormInput key={item.key} name={`data[${item.key}][${item.fieldId}]`} type={item.type} value={item.value} onChange={onChange} step={item.step} /></td>


                            </tr>
                        ))}
                    </tbody>
                </table>
                <button type="submit" className="btn btn-primary" dangerouslySetInnerHTML={{ __html: buttonName }}></button>
            </form>
        </div >
    )
}

export default GradeListForm