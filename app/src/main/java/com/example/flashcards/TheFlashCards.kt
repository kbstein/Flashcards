package com.example.flashcards


object TheFlashCards {
    fun createCards(): Array<Card> {
        return arrayOf(Card("H", "Hydrogen"),
            Card("Na", "Sodium"), Card("K" , "Potassium"),
            Card("Mg" , "Magnesium"), Card("Ca" , "Calcium"),
            Card("Ra" , "Radium"), Card("Ti" , "Titanium"),
            Card("Nb" , "Niobium"), Card("Ta" , "Tantalum"),
            Card("Cr" , "Chromium"), Card("W" , "Tungsten"),
            Card("U" , "Uranium"), Card("Fe" , "Iron"),
            Card("Cu" , "Copper"), Card("Au" , "Gold"),
            Card("Zn" , "Zinc"), Card("Cd" , "Cadmium"),
            Card("Hg" , "Mercury"), Card("" , ""),
            Card("B" , "Boron"), Card("Al" , "Aluminum"),
            Card("C" , "Carbon"), Card("Sn" , "Tin"),
            Card("N" , "Nitrogen"), Card("F" , "Fluorine"))
    }
}
