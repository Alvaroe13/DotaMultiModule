package com.alvaro.hero_domain

sealed class HeroAttribute(
    val uiValue: String,
    val abbreviation: String
) {

    object Agility : HeroAttribute(uiValue = "Agility", abbreviation = "agi")
    object Strength : HeroAttribute(uiValue = "Strength", abbreviation = "str")
    object Intelligence : HeroAttribute(uiValue = "Intelligence", abbreviation = "int")
    object Unknown : HeroAttribute(uiValue = "Unknown", abbreviation = "unknown")

}


fun getHeroAttributeFromUiValue(uiValue: String): HeroAttribute {
    return when (uiValue) {
        HeroAttribute.Agility.uiValue -> {
            HeroAttribute.Agility
        }
        HeroAttribute.Strength.uiValue -> {
            HeroAttribute.Strength
        }
        HeroAttribute.Intelligence.uiValue -> {
            HeroAttribute.Intelligence
        }
        else -> {
            HeroAttribute.Unknown
        }
    }
}

fun getHeroAttributeFromAbbreviation(abbreviation: String): HeroAttribute {
    return when (abbreviation) {
        HeroAttribute.Agility.abbreviation -> {
            HeroAttribute.Agility
        }
        HeroAttribute.Strength.abbreviation -> {
            HeroAttribute.Strength
        }
        HeroAttribute.Intelligence.abbreviation -> {
            HeroAttribute.Intelligence
        }
        else -> {
            HeroAttribute.Unknown
        }
    }
}

fun Hero.minAttackDmg(): Int {
    return when (primaryAttribute) {
        HeroAttribute.Strength -> {
            baseAttackMin + baseStr
        }
        HeroAttribute.Intelligence -> {
            baseAttackMin + baseInt
        }
        HeroAttribute.Agility -> {
            baseAttackMin + baseAgi
        }
        else -> {
            0
        }
    }
}

fun Hero.maxAttackDmg(): Int {
    return when (primaryAttribute) {
        HeroAttribute.Strength -> {
            baseAttackMax + baseStr
        }
        HeroAttribute.Intelligence -> {
            baseAttackMax + baseInt
        }
        HeroAttribute.Agility -> {
            baseAttackMax + baseAgi
        }
        else -> {
            0
        }
    }
}