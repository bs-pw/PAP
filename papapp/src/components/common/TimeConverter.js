import React from 'react';

function TimeConverter({ minutes }) {
    const convertMinutesToTime = (minutes) => {
        console.log(minutes)
        minutes = Number(minutes)
        const hours = Math.floor(minutes / 60);
        const remainingMinutes = minutes % 60;
        return `${hours.toString().padStart(2, '0')}:${remainingMinutes.toString().padStart(2, '0')}`;
    };

    return (
        <>
            {convertMinutesToTime(minutes)}
        </>
    );
}

function DayConverter({ dayCode }) {
    const convertDayCodeToDay = (dayCode) => {
        const days = ["Poniedziałek", "Wtorek"]
    }

    export default TimeConverter;
