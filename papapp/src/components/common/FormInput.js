import React from 'react'

const FormInput = ({ name, type, label = false, value, onChange, step, required, defaultValue, min, max }) => {
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
                defaultValue={defaultValue}
                min={min}
                max={max}
            />
        </div>
    )
}

export default FormInput