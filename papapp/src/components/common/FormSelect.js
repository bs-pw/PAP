import React from 'react'

const FormSelect = ({ name, label, defaultValue, options, onChange }) => {
    console.log(options)
    return (
        <div className="mb-3">
            <label htmlFor="text" className="form-label">{label}</label>
            <select className="form-select" name={name} value={defaultValue} onChange={onChange}>
                <option value="-1">Wybierz...</option>
                {options.map((item) => (
                    <option key={item.value} value={item.value}>
                        {item.label}
                    </option>
                ))}
            </select>
        </div>
    )
}

export default FormSelect