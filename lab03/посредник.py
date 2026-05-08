from abc import ABC, abstractmethod


# Абстрактный класс Посредника
class Mediator(ABC):
    @abstractmethod
    def notify(self, sender, event, data=None):
        pass

# Абстрактный класс Коллеги (персонажей)
class Colleague(ABC):
    def __init__(self, mediator: Mediator):
        self.mediator = mediator

    def send(self, event, data=None):
        self.mediator.notify(self, event, data)

    @abstractmethod
    def handle_event(self, event, data):
        pass

# Реализация персонажа Npc
class Npc(Colleague):
    def handle_event(self, event, data):
        if event == 'find_item':
            print(f"{self.__class__.__name__} получил информацию о найденном предмете: {data}")
        elif event == 'trade_request':
            print(f"{self.__class__.__name__} получил запрос на обмен: {data}")

# Реализация персонажа Pet
class Pet(Colleague):
    def handle_event(self, event, data):
        if event == 'search_item':
            print(f"{self.__class__.__name__} ищет предмет: {data}")
        elif event == 'trade_offer':
            print(f"{self.__class__.__name__} предлагает обмен: {data}")

# Конкретная реализация посредника
class GameMediator(Mediator):
    def __init__(self):
        self.characters = []

    def register(self, character: Colleague):
        self.characters.append(character)

    def notify(self, sender, event, data=None):
        for character in self.characters:
            if character != sender:
                character.handle_event(event, data)

# Пример использования
def main():
    mediator = GameMediator()

    npc = Npc(mediator)
    pet = Pet(mediator)

    mediator.register(npc)
    mediator.register(pet)

    # Персонажи взаимодействуют через посредника
    pet.send('search_item', 'Меч')
    npc.send('trade_request', 'Обменять меч на зелье')

if __name__ == "__main__":
    main()