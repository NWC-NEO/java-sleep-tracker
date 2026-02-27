package ru.yandex.practicum.sleeptracker;

public enum Chronotype {
    OWL("Сова"),
    LARK("Жаворонок"),
    PIGEON("Голубь"),
    NO_DATA("Данные отсутствуют");

    private final String name;

    Chronotype(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
