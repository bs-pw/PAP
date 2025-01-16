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
                        {data.map((element) => (
                            <tr>
                                <td>{element.label}</td>
                                {
                                    element.forms.map((item) => (
                                        <td><FormInput key={item.key} name={`data[${item.key}][${item.fieldId}]`} type={item.type} value={item.value} onChange={onChange} step={item.step} /></td>
                                    ))
                                }

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