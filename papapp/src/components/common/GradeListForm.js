import React from 'react'
import FormInput from './FormInput'

const GradeListForm = ({ listName, error, data, onChange, onSubmit }) => {

    return (
        <div>
            <h1>{listName}</h1>

            {error && <p>{error}</p>}
            <form onSubmit={onSubmit}>
                <table className='table'>
                    <thead>
                        <tr>
                            <td></td>
                            <td></td>
                        </tr>
                    </thead>
                    <tbody>
                        {data.map((item) => (
                            <tr>
                                <td></td>
                                <td><FormInput onChange={onChange} /></td>
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