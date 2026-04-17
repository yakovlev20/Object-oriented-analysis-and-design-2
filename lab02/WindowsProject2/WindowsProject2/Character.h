#pragma once
#include <string>
#include <memory>

enum class UpgradeType {
    SHIELD,
    MAGIC_AURA,
    STRENGTH,
    SPEED
};

// Базовый интерфейс персонажа
class AbstractCharacter {
public:
    virtual ~AbstractCharacter() = default;
    virtual std::wstring GetDescription() const = 0;
    virtual int GetHealth() const = 0;
    virtual int GetDamage() const = 0;
    virtual int GetArmor() const = 0;
    virtual double GetSpeed() const = 0;

    virtual std::shared_ptr<AbstractCharacter> GetBaseCharacter() const { return nullptr; }
    virtual int GetDecoratorCount() const = 0;

    virtual bool HasUpgrade(UpgradeType type) const = 0;
};

// Конкретные персонажи
class Pet : public AbstractCharacter {
public:
    std::wstring GetDescription() const override {
        return L"Питомец";
    }

    int GetHealth() const override { return 50; }
    int GetDamage() const override { return 10; }
    int GetArmor() const override { return 5; }
    double GetSpeed() const override { return 1.5; }
    int GetDecoratorCount() const override { return 0; }

    bool HasUpgrade(UpgradeType type) const override {
        return false; // У питомца нет улучшений
    }
};

class Npc : public AbstractCharacter {
public:
    std::wstring GetDescription() const override {
        return L"NPC (неигровой персонаж)";
    }

    int GetHealth() const override { return 100; }
    int GetDamage() const override { return 15; }
    int GetArmor() const override { return 10; }
    double GetSpeed() const override { return 1.0; }
    int GetDecoratorCount() const override { return 0; }

    bool HasUpgrade(UpgradeType type) const override {
        return false;
    }
};

class Boss : public AbstractCharacter {
public:
    std::wstring GetDescription() const override {
        return L"Босс";
    }

    int GetHealth() const override { return 500; }
    int GetDamage() const override { return 50; }
    int GetArmor() const override { return 30; }
    double GetSpeed() const override { return 0.7; }
    int GetDecoratorCount() const override { return 0; }

    bool HasUpgrade(UpgradeType type) const override {
        return false;
    }
};

// Базовый декоратор
class CharacterDecorator : public AbstractCharacter {
protected:
    std::shared_ptr<AbstractCharacter> character;

public:
    CharacterDecorator(std::shared_ptr<AbstractCharacter> character)
        : character(character) {
    }

    std::wstring GetDescription() const override {
        return character->GetDescription();
    }

    int GetHealth() const override {
        return character->GetHealth();
    }

    int GetDamage() const override {
        return character->GetDamage();
    }

    int GetArmor() const override {
        return character->GetArmor();
    }

    double GetSpeed() const override {
        return character->GetSpeed();
    }

    std::shared_ptr<AbstractCharacter> GetBaseCharacter() const override {
        return character;
    }

    int GetDecoratorCount() const override {
        return character->GetDecoratorCount() + 1;
    }

    // Реализуем HasUpgrade, делегируя обернутому объекту
    bool HasUpgrade(UpgradeType type) const override {
        return character->HasUpgrade(type);
    }
};

// Конкретные декораторы
class ShieldDecorator : public CharacterDecorator {
public:
    ShieldDecorator(std::shared_ptr<AbstractCharacter> character)
        : CharacterDecorator(character) {
    }

    std::wstring GetDescription() const override {
        return character->GetDescription() + L" + Щит";
    }

    int GetArmor() const override {
        return character->GetArmor() + 20;
    }

    bool HasUpgrade(UpgradeType type) const override {
        if (type == UpgradeType::SHIELD) {
            return true; // Этот декоратор добавляет щит
        }
        return CharacterDecorator::HasUpgrade(type);
    }
};

class MagicAuraDecorator : public CharacterDecorator {
public:
    MagicAuraDecorator(std::shared_ptr<AbstractCharacter> character)
        : CharacterDecorator(character) {
    }

    std::wstring GetDescription() const override {
        return character->GetDescription() + L" + Магическая аура";
    }

    int GetDamage() const override {
        return character->GetDamage() + 25;
    }

    double GetSpeed() const override {
        return character->GetSpeed() + 0.3;
    }

    bool HasUpgrade(UpgradeType type) const override {
        if (type == UpgradeType::MAGIC_AURA) {
            return true;
        }
        return CharacterDecorator::HasUpgrade(type);
    }
};

class StrengthDecorator : public CharacterDecorator {
public:
    StrengthDecorator(std::shared_ptr<AbstractCharacter> character)
        : CharacterDecorator(character) {
    }

    std::wstring GetDescription() const override {
        return character->GetDescription() + L" + Усиление силы";
    }

    int GetHealth() const override {
        return character->GetHealth() + 100;
    }

    int GetDamage() const override {
        return character->GetDamage() + 35;
    }

    double GetSpeed() const override {
        return character->GetSpeed() - 0.2; // Сила замедляет
    }

    bool HasUpgrade(UpgradeType type) const override {
        if (type == UpgradeType::STRENGTH) {
            return true;
        }
        return CharacterDecorator::HasUpgrade(type);
    }
};

class SpeedDecorator : public CharacterDecorator {
public:
    SpeedDecorator(std::shared_ptr<AbstractCharacter> character)
        : CharacterDecorator(character) {
    }

    std::wstring GetDescription() const override {
        return character->GetDescription() + L" + Ускорение";
    }

    double GetSpeed() const override {
        return character->GetSpeed() + 0.5;
    }

    int GetArmor() const override {
        return character->GetArmor() - 5; // Скорость снижает защиту
    }

    bool HasUpgrade(UpgradeType type) const override {
        if (type == UpgradeType::SPEED) {
            return true;
        }
        return CharacterDecorator::HasUpgrade(type);
    }
};