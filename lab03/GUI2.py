import random
import tkinter as tk
from tkinter import ttk

from посредник import Colleague, GameMediator, Mediator, Npc, Pet


class GameGUI:
    def __init__(self, root):
        self.root = root
        self.root.title("Mediator Pattern Game")
        self.root.geometry("800x600")
        self.canvas = tk.Canvas(self.root, width=800, height=600, bg="white")
        self.canvas.pack()

        # Создаем посредника и персонажей
        self.mediator = GameMediator()
        self.npc = Npc(self.mediator)
        self.pet = Pet(self.mediator)
        self.mediator.register(self.npc)
        self.mediator.register(self.pet)

        # Переменные для позиций персонажей
        self.npc_x, self.npc_y = 150, 300
        self.pet_x, self.pet_y = 650, 300

        # Создаем персонажей как кружки с подписями
        self.npc_circle = self.canvas.create_oval(self.npc_x-20, self.npc_y-20, self.npc_x+20, self.npc_y+20, fill='blue')
        self.npc_label = self.canvas.create_text(self.npc_x, self.npc_y+30, text="NPC", fill='blue')

        self.pet_circle = self.canvas.create_oval(self.pet_x-20, self.pet_y-20, self.pet_x+20, self.pet_y+20, fill='red')
        self.pet_label = self.canvas.create_text(self.pet_x, self.pet_y+30, text="Pet", fill='red')

        # Предметы (квадратики)
        self.npc_item_obj = None
        self.pet_item_obj = None

        # Создаем интерфейсные элементы
        self.create_widgets()

    def create_widgets(self):
        # Поле для сообщений
        self.text_box = tk.Text(self.root, width=60, height=10)
        self.text_box.place(x=10, y=520)

        # Кнопки
        btn_frame = ttk.Frame(self.root)
        btn_frame.place(x=10, y=10)

        ttk.Button(btn_frame, text="Pet ищет меч", command=self.pet_search).grid(row=0, column=0, padx=5)
        ttk.Button(btn_frame, text="NPC предлагает обмен", command=self.npc_trade).grid(row=0, column=1, padx=5)
        ttk.Button(btn_frame, text="Обменять предметы", command=self.exchange_items).grid(row=0, column=2, padx=5)

    def pet_search(self):
        # Pet ищет предмет
        self.send_item(self.pet, 'Меч')
        self.log("Pet отправил запрос на поиск 'Меч'")
        self.pet_item = 'Меч'
        self.update_items()

    def npc_trade(self):
        # NPC предлагает обмен
        self.send_item(self.npc, 'Зелье')
        self.log("NPC отправил 'Зелье'")
        self.npc_item = 'Зелье'
        self.update_items()

    def exchange_items(self):
        # Визуальный обмен
        self.pet_item, self.npc_item = self.npc_item, self.pet_item
        self.visual_exchange()
        self.log("Произошел обмен предметами")
        self.update_items()

    def send_item(self, character, item_name):
        # Визуальный эффект поиска или обмена
        if character == self.pet:
            self.animate_item_to_character(item_name, self.pet_x, self.pet_y)
        elif character == self.npc:
            self.animate_item_to_character(item_name, self.npc_x, self.npc_y)

    def animate_item_to_character(self, item_name, target_x, target_y):
        # Создаем квадратик (предмет)
        item = self.canvas.create_rectangle(0, 0, 20, 20, fill='green')
        start_x, start_y = (self.npc_x + self.pet_x) / 2, 0  # старт в центре
        self.canvas.coords(item, start_x, start_y, start_x+20, start_y+20)

        # подпись для предмета
        label = self.canvas.create_text(start_x+10, start_y+10, text=item_name, fill='black')

        def move():
            current_coords = self.canvas.coords(item)
            cx, cy = (current_coords[0]+current_coords[2])/2, (current_coords[1]+current_coords[3])/2
            dx = (target_x - cx) * 0.2
            dy = (target_y - cy) * 0.2
            if abs(target_x - cx) < 1 and abs(target_y - cy) < 1:
                self.canvas.coords(item, target_x-10, target_y-10, target_x+10, target_y+10)
                self.canvas.coords(label, target_x, target_y)
                return
            self.canvas.move(item, dx, dy)
            self.canvas.move(label, dx, dy)
            self.root.after(20, move)

        move()

    def visual_exchange(self):
        # Перемещение квадратиков между персонажами
        if self.npc_item_obj:
            self.animate_item_transfer(self.npc_item_obj, self.npc_x, self.npc_y, label_text=self.npc_item)
            self.npc_item_obj = None
        if self.pet_item_obj:
            self.animate_item_transfer(self.pet_item_obj, self.pet_x, self.pet_y, label_text=self.pet_item)
            self.pet_item_obj = None

    def animate_item_transfer(self, item_id, target_x, target_y, label_text):
        def move():
            current_coords = self.canvas.coords(item_id)
            cx, cy = (current_coords[0]+current_coords[2])/2, (current_coords[1]+current_coords[3])/2
            dx = (target_x - cx) * 0.2
            dy = (target_y - cy) * 0.2
            if abs(target_x - cx) < 1 and abs(target_y - cy) < 1:
                self.canvas.coords(item_id, target_x-10, target_y-10, target_x+10, target_y+10)
                self.canvas.coords(label, target_x, target_y)
                return
            self.canvas.move(item_id, dx, dy)
            self.canvas.move(label, dx, dy)
            self.root.after(20, move)

        # Создаем подпись
        label = self.canvas.create_text(
            (self.npc_x + self.pet_x) / 2,
            (self.npc_y + self.pet_y) / 2 - 30,  # чуть выше
            text=label_text,
            fill='black'
        )

        move()

    def update_items(self):
        # Удаляем старые квадратики и подписи
        if self.npc_item_obj:
            self.canvas.delete(self.npc_item_obj)
        if self.pet_item_obj:
            self.canvas.delete(self.pet_item_obj)

        # Создаем новые квадратики
        if hasattr(self, 'npc_item'):
            self.npc_item_obj = self.canvas.create_rectangle(self.npc_x - 10, self.npc_y - 50,
                                                               self.npc_x + 10, self.npc_y - 30,
                                                               fill='yellow')
            self.canvas.create_text(self.npc_x, self.npc_y - 40, text=self.npc_item, fill='black')
        if hasattr(self, 'pet_item'):
            self.pet_item_obj = self.canvas.create_rectangle(self.pet_x - 10, self.pet_y - 50,
                                                               self.pet_x + 10, self.pet_y - 30,
                                                               fill='yellow')
            self.canvas.create_text(self.pet_x, self.pet_y - 40, text=self.pet_item, fill='black')

    def log(self, message):
        self.root.children['!text'].insert(tk.END, message + "\n")
        self.root.children['!text'].see(tk.END)

if __name__ == "__main__":
    root = tk.Tk()
    app = GameGUI(root)
    root.mainloop()