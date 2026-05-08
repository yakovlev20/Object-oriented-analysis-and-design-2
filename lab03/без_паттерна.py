import tkinter as tk
from tkinter import ttk

class Npc:
    def __init__(self):
        self.item = None

    def trade(self, item):
        self.item = item
        return self.item

class Pet:
    def __init__(self):
        self.item = None

    def search_item(self, item_name):
        self.item = item_name
        return self.item

    def trade(self, item):
        self.item = item
        return self.item

class GameGUI:
    def __init__(self, root):
        self.root = root
        self.root.title("Без паттерна: Взаимодействие персонажей")
        
        # Создаем персонажей
        self.npc = Npc()
        self.pet = Pet()
        
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
        ttk.Button(btn_frame, text="Обмен предметов", command=self.exchange_items).grid(row=0, column=2, padx=5)

        # Изначально предметы у персонажей
        self.npc_item = None
        self.pet_item = None

    def pet_search(self):
        item_found = self.pet.search_item('Меч')
        self.log_message(f"Pet нашел предмет: {item_found}")
        self.pet_item = item_found
        self.update_labels()

    def npc_trade(self):
        if self.pet_item:
            traded_item = self.npc.trade('Зелье')
            self.log_message(f"NPC получил предмет: {traded_item}")
            self.npc_item = traded_item
            self.update_labels()
        else:
            self.log_message("Pet еще не нашел предмет для обмена.")

    def exchange_items(self):
        # Обмен предметами напрямую
        self.pet_item, self.npc_item = self.npc_item, self.pet_item
        self.log_message("Произошел обмен предметами между персонажами")
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