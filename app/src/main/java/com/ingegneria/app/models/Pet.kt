package com.ingegneria.app.models

class Pet(
    // Stats
    var name: String,
    var level: Int = 0,
    var mult: Double = .0,
    var xp: Int = 0,
    var hp: Int = 0,
    // ID of the customization on the pet
    var hat: Int? = null
) {

    fun maxXp(): Int {
        return level * 15
    }

    fun maxHp(): Int {
        return level * 10
    }

    fun takeDamage(dmg: Int) {
        hp -= dmg
        if (hp < 0)
            hp = 0
    }

    fun heal(amount: Int) {
        hp += amount
        if (hp > maxHp())
            hp = maxHp()
    }

    fun gainXp(amount: Int) {
        xp += (amount*mult).toInt()
        // If xp > max
        while (xp >= maxXp()) {
            xp -= maxXp()
            level++;
        }
    }

    fun resetMult() {
        mult = .0
    }

    fun increaseMult(amount: Double) {
        mult += amount
    }

    fun decreaseMult(amount: Double) {
        mult -= amount
    }

    fun updateDb() {
        // Firebase stuff?
    }
}