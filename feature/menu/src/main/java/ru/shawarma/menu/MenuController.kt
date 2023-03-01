package ru.shawarma.menu

import ru.shawarma.menu.entities.MenuElement

interface MenuController {

    fun reloadMenu()

    fun addToCart(menuItem: MenuElement.MenuItem)

    fun removeFromCart(menuItem: MenuElement.MenuItem)
}