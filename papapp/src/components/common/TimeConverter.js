import React from 'react';

const convertMinutesToTime = (minutes) => {
    console.log(minutes)
    minutes = Number(minutes)
    const hours = Math.floor(minutes / 60);
    const remainingMinutes = minutes % 60;
    return `${hours.toString().padStart(2, '0')}:${remainingMinutes.toString().padStart(2, '0')}`;
};

function TimeConverter({ minutes }) {

    return (
        <>
            {convertMinutesToTime(minutes)}
        </>
    );
}

const convertDayCodeToDay = (dayCode) => {
    dayCode = Number(dayCode)
    const days = ["Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek", "Sobota", "Niedziela"];
    const repetitionLabels = ["Co tydzień", "Co 2 tygodnie(nieparzyste)", "Co 2 tygodnie(parzyste)"]
    const repetition = repetitionLabels[Math.floor(dayCode / 7)];
    // console.log(Math.floor(dayCode / 7))
    const day = days[dayCode % 7];
    return `${day} [${repetition}]`
}

function DayConverter({ dayCode }) {

    return (
        <>
            {convertDayCodeToDay(dayCode)}
        </>
    )
}
export { TimeConverter, DayConverter, convertMinutesToTime, convertDayCodeToDay };
