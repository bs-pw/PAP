import React from "react";
import { days, repetitionLabels, convertMinutesToTime } from "./TimeConverter.js";

const Schedule = ({ data }) => {
    return (
        <div className="container-fluid py-4">
            <div className="row">
                {days.map((day, dayIndex) => (
                    <div key={day} className="col">
                        <div className="card">
                            <div className="card-header bg-success text-white text-center fw-bold">
                                {day}
                            </div>
                            <div className="card-body" style={{ minHeight: "50vh" }}>
                                {data
                                    .filter((item) => item.day === dayIndex)
                                    .map((item) => {
                                        const startTime = convertMinutesToTime(item.hour);
                                        const endTime = convertMinutesToTime(item.hour + item.length);

                                        return (
                                            <div
                                                key={item.class_id_for_group}
                                                className="mb-3 p-2 text-white text-center"
                                                style={{
                                                    backgroundColor: "#2196F3",
                                                    borderRadius: "4px",
                                                }}
                                            >
                                                <div className="fw-bold">
                                                    {item.course_code} gr{item.group_number} ({item.type})
                                                </div>
                                                <div className="mt-1">
                                                    {startTime} - {endTime}
                                                </div>
                                                <div className="mt-1" style={{ fontSize: "11px" }}>
                                                    Room: {item.where}
                                                </div>
                                            </div>
                                        );
                                    })}
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Schedule;
