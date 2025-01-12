import React from 'react'
import FormInput from './FormInput'
import FormSelect from './FormSelect'

const Form = ({ formName, onSubmit, inputData = null, selectData = null, buttonName, message }) => {
    return (
        <div className="container mt-5">
            <h2>{formName}</h2>
            <form onSubmit={onSubmit}>
                {inputData && inputData.map((input) => (
                    <FormInput key={input.name} {...input} />
                ))}
                {selectData && selectData.map((select) => (
                    <FormSelect key={select.name} {...select} />
                ))}

                {message && <p>{message}</p>}
                <button type="submit" className="btn btn-primary" dangerouslySetInnerHTML={{ __html: buttonName }}></button>
            </form>
        </div>
    )
}

export default Form