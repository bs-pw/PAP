import React from 'react'

const FormInput = ({ name, type, label = false, value, onChange, step, required }) => {
    return (
        <div className="mb-3">
            {label && <label htmlFor={name} className="form-label">{label}</label>}
            <input
                type={type}
                className="form-control"
                id={name}
                name={name}
                value={value}
                onChange={onChange}
                required={required}
                step={step}
            />
        </div>
    )
}

export default FormInput