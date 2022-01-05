package com.alvaro.hero_datasource_test.cache

import com.alvaro.hero_datasource.cache.HeroCache
import com.alvaro.hero_domain.Hero
import com.alvaro.hero_domain.HeroRole

class HeroCacheFake(
    private val database: HeroDatabaseFake
) : HeroCache {

    override suspend fun getHero(id: Int): Hero? {
        return database.heros.find { it.id == id }
    }

    override suspend fun removeHero(id: Int) {
        database.heros.removeIf { it.id == id }
    }

    override suspend fun selectAll(): List<Hero> {
        return database.heros
    }

    override suspend fun insert(hero: Hero) {

        if (database.heros.isEmpty()) {  // -> BLOCK = first insertion
            database.heros.add(hero)
            return
        }

        for (_hero in database.heros) {   // -> BLOCK = replace existing item
            if (hero.id == _hero.id) {
                database.heros.remove(_hero)
                database.heros.add(hero)
                return
            }
        }

        database.heros.add(hero)  // -> BLOCK = Not first insertion and item to be inserted does NOT exist already
    }

    override suspend fun insert(heros: List<Hero>) {

        if (database.heros.isEmpty()) {  // -> BLOCK = first insertion
            database.heros.addAll(heros)
            return
        }

        heros.forEach { _hero ->
            if (database.heros.contains(_hero)) {
                database.heros.remove(_hero)
            }
            database.heros.add(_hero)
        }
    }

    override suspend fun searchByName(localizedName: String): List<Hero> {
        return database.heros.find { _hero ->
            _hero.localizedName == localizedName
        }?.let { heroFound ->
            listOf(heroFound)
        } ?: listOf()
    }

    override suspend fun searchByAttr(primaryAttr: String): List<Hero> {
        return database.heros.filter { _hero ->
            _hero.primaryAttribute.uiValue == primaryAttr
        }
    }

    override suspend fun searchByAttackType(attackType: String): List<Hero> {
        return database.heros.filter { _hero ->
            _hero.attackType.uiValue == attackType
        }
    }

    override suspend fun searchByRole(
        carry: Boolean,
        escape: Boolean,
        nuker: Boolean,
        initiator: Boolean,
        durable: Boolean,
        disabler: Boolean,
        jungler: Boolean,
        support: Boolean,
        pusher: Boolean
    ): List<Hero> {
        val heros: MutableList<Hero> = mutableListOf()
        if(carry){
            heros.addAll(database.heros.filter { it.roles.contains(HeroRole.Carry) })
        }
        if(escape){
            heros.addAll(database.heros.filter { it.roles.contains(HeroRole.Escape) })
        }
        if(nuker){
            heros.addAll(database.heros.filter { it.roles.contains(HeroRole.Nuker) })
        }
        if(initiator){
            heros.addAll(database.heros.filter { it.roles.contains(HeroRole.Initiator) })
        }
        if(durable){
            heros.addAll(database.heros.filter { it.roles.contains(HeroRole.Durable) })
        }
        if(disabler){
            heros.addAll(database.heros.filter { it.roles.contains(HeroRole.Disabler) })
        }
        if(jungler){
            heros.addAll(database.heros.filter { it.roles.contains(HeroRole.Jungler) })
        }
        if(support){
            heros.addAll(database.heros.filter { it.roles.contains(HeroRole.Support) })
        }
        if(pusher){
            heros.addAll(database.heros.filter { it.roles.contains(HeroRole.Pusher) })
        }
        return heros.distinctBy { it.id }
    }
}