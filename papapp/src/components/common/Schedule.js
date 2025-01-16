import React from "react";
import "./Schedule.css"; // Dodajemy stylizację

// Funkcja pomocnicza do konwersji minut na godziny
const formatTime = (minutes) => {
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return `${hours.toString().padStart(2, "0")}:${mins.toString().padStart(2, "0")}`;
};

const Schedule = ({ data }) => {
    // Tabela dni tygodnia
    const daysOfWeek = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];

    return (
        <div className="schedule-container">
            {daysOfWeek.map((day, dayIndex) => (
                <div key={day} className="day-column">
                    <div className="day-header">{day}</div>
                    <div className="day-content">
                        {data
                            .filter((item) => item.day === dayIndex) // Filtrujemy zajęcia na dany dzień
                            .map((item) => {
                                const startTime = formatTime(item.hour);
                                const endTime = formatTime(item.hour + item.length);

                                return (
                                    <div
                                        key={item.class_id_for_group}
                                        className="class-block"
                                        style={{ height: `${item.length}px` }} // Wysokość bloku na podstawie długości zajęć
                                    >
                                        <div className="class-info">
                                            <strong>{item.course_code}</strong> gr{item.group_number} ({item.type})
                                        </div>
                                        <div className="class-time">
                                            {startTime} - {endTime}
                                        </div>
                                        <div className="class-room">Room: {item.where}</div>
                                    </div>
                                );
                            })}
                    </div>
                </div>
            ))}
        </div>
    );
};

export default Schedule;
