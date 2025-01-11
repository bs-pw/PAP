import React from 'react'

const FormSelect = ({ name, label, user_type_id, userTypes, onChange }) => {
    return (
        <div className="mb-3">
            <label htmlFor="text" className="form-label">{label}</label>
            <select className="form-select" name={name} value={user_type_id} onChange={onChange}>
                {userTypes.map((userType) => (
                    <option key={userType.user_type_id} value={userType.user_type_id}>
                        {userType.type}
                    </option>
                ))}
            </select>
        </div>
    )
}

export default FormSelect