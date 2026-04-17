# Базовый класс компонента
class Component:
    def __init__(self, mediator):
        self.mediator = mediator


# Компоненты
class RaceUI(Component):
    def __init__(self, mediator):
        super().__init__(mediator)
        self.race = None

    def select_race(self, race):
        self.race = race
        print(f"Выбрана раса: {race}")
        self.mediator.notify(self, 'race_selected')


class ClassUI(Component):
    def __init__(self, mediator):
        super().__init__(mediator)
        self.available_classes = []
        self.selected_class = None

    def update_available_classes(self, classes):
        self.available_classes = classes
        print(f"Доступные классы обновлены: {classes}")

    def select_class(self, cls):
        if cls in self.available_classes:
            self.selected_class = cls
            print(f"Выбран класс: {cls}")
            self.mediator.notify(self, 'class_selected')
        else:
            print(f"Класс {cls} недоступен для выбранной расы.")


class SkillsUI(Component):
    def __init__(self, mediator):
        super().__init__(mediator)
        self.skills = []

    def update_skills(self, skills):
        self.skills = skills
        print(f"Обновлены навыки: {skills}")

    def select_skill(self, skill):
        if skill in self.skills:
            print(f"Выбран навык: {skill}")
        else:
            print(f"Навык {skill} недоступен.")


# Посредник
class CharacterCreationMediator:
    def __init__(self):
        self.race_ui = None
        self.class_ui = None
        self.skills_ui = None

    def register_components(self, race_ui, class_ui, skills_ui):
        self.race_ui = race_ui
        self.class_ui = class_ui
        self.skills_ui = skills_ui

    def notify(self, sender, event):
        if sender == self.race_ui and event == 'race_selected':
            # Обновляем доступные классы и навыки в зависимости от расы
            race = self.race_ui.race
            if race == 'Эльф':
                self.class_ui.update_available_classes(['Лучник', 'Маг'])
                self.skills_ui.update_skills(['Стрельба', 'Магия'])
            elif race == 'Дварф':
                self.class_ui.update_available_classes(['Воин', 'Кузнец'])
                self.skills_ui.update_skills(['Кузнечное дело', 'Броня'])
            else:
                self.class_ui.update_available_classes(['Пехотинец'])
                self.skills_ui.update_skills(['Выносливость'])

        elif sender == self.class_ui and event == 'class_selected':
            # Можно добавить дополнительные действия при выборе класса
            pass


# Пример использования
def main():
    mediator = CharacterCreationMediator()

    race_ui = RaceUI(mediator)
    class_ui = ClassUI(mediator)
    skills_ui = SkillsUI(mediator)

    mediator.register_components(race_ui, class_ui, skills_ui)

    # Симуляция выбора
    race_ui.select_race('Эльф')
    class_ui.select_class('Маг')
    skills_ui.select_skill('Магия')


if __name__ == "__main__":
    main()