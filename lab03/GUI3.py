import tkinter as tk
from tkinter import ttk
from посредник import Mediator, Colleague, Npc, Pet, GameMediator

class GameGUI:
    def __init__(self, root):
        self.root = root
        self.root.title("Mediator Pattern Game")
        
        # Создаем посредника и персонажей
        self.mediator = GameMediator()
        self.npc = Npc(self.mediator)
        self.pet = Pet(self.mediator)
        self.mediator.register(self.npc)
        self.mediator.register(self.pet)
        
        # Создаем интерфейсные элементы
        self.create_widgets()

    def create_widgets(self):
        # Поле для отображения сообщений
        self.text_box = tk.Text(self.root, width=60, height=10)
        self.text_box.pack(padx=10, pady=5)

        # Поле для отображения обмена предметами
        self.exchange_frame = ttk.Frame(self.root)
        self.exchange_frame.pack(padx=10, pady=10, fill='x')

        # Метки для отображения предметов у персонажей
        self.npc_label = ttk.Label(self.exchange_frame, text="NPC предмет: ничего")
        self.npc_label.grid(row=0, column=0, padx=10)

        self.pet_label = ttk.Label(self.exchange_frame, text="Pet предмет: ничего")
        self.pet_label.grid(row=0, column=1, padx=10)

        # Кнопки для взаимодействий
        btn_frame = ttk.Frame(self.root)
        btn_frame.pack(padx=10, pady=10)

        ttk.Button(btn_frame, text="Поиск меча (Pet)", command=self.pet_search).grid(row=0, column=0, padx=5)
        ttk.Button(btn_frame, text="Обмен меча на зелье (NPC)", command=self.npc_trade).grid(row=0, column=1, padx=5)

        # Кнопки для обмена предметами
        ttk.Button(btn_frame, text="Обмен предметов", command=self.exchange_items).grid(row=0, column=2, padx=5)

        # Изначально предметы у персонажей
        self.npc_item = None
        self.pet_item = None

    def pet_search(self):
        self.pet.send('search_item', 'Меч')
        self.log_message("Pet отправил запрос на поиск предмета: Меч")
        # Обновляем предмет у питомца
        self.pet_item = 'Меч'
        self.update_labels()

    def npc_trade(self):
        self.npc.send('trade_request', 'Обменять меч на зелье')
        self.log_message("Npc отправил запрос на обмен: Обменять меч на зелье")
        # Обновляем предмет у NPC
        self.npc_item = 'Зелье'
        self.update_labels()

    def exchange_items(self):
        # Обмен предметами между персонажами
        self.pet_item, self.npc_item = self.npc_item, self.pet_item
        self.log_message("Произошел обмен предметами")
        self.update_labels()

    def update_labels(self):
        npc_text = f"NPC предмет: {self.npc_item if self.npc_item else 'ничего'}"
        pet_text = f"Pet предмет: {self.pet_item if self.pet_item else 'ничего'}"
        self.npc_label.config(text=npc_text)
        self.pet_label.config(text=pet_text)

    def log_message(self, message):
        self.text_box.insert(tk.END, message + "\n")
        self.text_box.see(tk.END)

if __name__ == "__main__":
    root = tk.Tk()
    app = GameGUI(root)
    root.mainloop()