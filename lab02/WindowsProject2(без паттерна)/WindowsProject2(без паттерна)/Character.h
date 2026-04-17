#pragma once
#include <string>
#include <memory>
#include <vector>

// Перечисление типов улучшений
enum class UpgradeType {
    NONE,
    SHIELD,
    MAGIC_AURA,
    STRENGTH,
    SPEED
};

// Структура для хранения характеристик
struct CharacterStats {
    std::wstring description;
    int health;
    int damage;
    int armor;
    double speed;

    CharacterStats(const std::wstring& desc = L"", int hp = 0, int dmg = 0, int arm = 0, double spd = 0.0)
        : description(desc), health(hp), damage(dmg), armor(arm), speed(spd) {
    }
};

// Класс персонажа
class Character {
private:
    CharacterStats baseStats;
    std::vector<UpgradeType> upgrades;
    std::wstring characterName;

public:
    Character(const std::wstring& name, const CharacterStats& stats)
        : characterName(name), baseStats(stats) {
    }

    // Добавление улучшения
    void AddUpgrade(UpgradeType upgrade) {
        upgrades.push_back(upgrade);
    }

    // Получение текущего описания
    std::wstring GetDescription() const {
        std::wstring desc = characterName;

        for (auto upgrade : upgrades) {
            switch (upgrade) {
            case UpgradeType::SHIELD:
                desc += L" + Щит";
                break;
            case UpgradeType::MAGIC_AURA:
                desc += L" + Магическая аура";
                break;
            case UpgradeType::STRENGTH:
                desc += L" + Усиление силы";
                break;
            case UpgradeType::SPEED:
                desc += L" + Ускорение";
                break;
            default:
                break;
            }
        }

        return desc;
    }

    // Получение характеристик с учетом улучшений
    int GetHealth() const {
        int total = baseStats.health;

        for (auto upgrade : upgrades) {
            if (upgrade == UpgradeType::STRENGTH) {
                total += 100;
            }
        }

        return total;
    }

    int GetDamage() const {
        int total = baseStats.damage;

        for (auto upgrade : upgrades) {
            switch (upgrade) {
            case UpgradeType::MAGIC_AURA:
                total += 25;
                break;
            case UpgradeType::STRENGTH:
                total += 35;
                break;
            default:
                break;
            }
        }

        return total;
    }

    int GetArmor() const {
        int total = baseStats.armor;

        for (auto upgrade : upgrades) {
            switch (upgrade) {
            case UpgradeType::SHIELD:
                total += 20;
                break;
            case UpgradeType::SPEED:
                total -= 5;
                break;
            default:
                break;
            }
        }

        return total;
    }

    double GetSpeed() const {
        double total = baseStats.speed;

        for (auto upgrade : upgrades) {
            switch (upgrade) {
            case UpgradeType::MAGIC_AURA:
                total += 0.3;
                break;
            case UpgradeType::STRENGTH:
                total -= 0.2;
                break;
            case UpgradeType::SPEED:
                total += 0.5;
                break;
            default:
                break;
            }
        }

        return total;
    }

    // Получение количества улучшений
    int GetUpgradeCount() const {
        return static_cast<int>(upgrades.size());
    }

    // Сброс улучшений
    void ResetUpgrades() {
        upgrades.clear();
    }

    // Проверка наличия улучшения
    bool HasUpgrade(UpgradeType upgrade) const {
        for (auto u : upgrades) {
            if (u == upgrade) {
                return true;
            }
        }
        return false;
    }
};

// Фабричные функции для создания персонажей
inline std::shared_ptr<Character> CreatePet() {
    return std::make_shared<Character>(L"Питомец", CharacterStats(L"Питомец", 50, 10, 5, 1.5));
}

inline std::shared_ptr<Character> CreateNpc() {
    return std::make_shared<Character>(L"NPC", CharacterStats(L"NPC (неигровой персонаж)", 100, 15, 10, 1.0));
}

inline std::shared_ptr<Character> CreateBoss() {
    return std::make_shared<Character>(L"Босс", CharacterStats(L"Босс", 500, 50, 30, 0.7));
}