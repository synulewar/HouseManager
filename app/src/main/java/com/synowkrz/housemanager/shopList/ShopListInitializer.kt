package com.synowkrz.housemanager.shopList

import android.app.Application
import android.util.Log
import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.repository.HouseRepository
import com.synowkrz.housemanager.shopList.model.Category
import com.synowkrz.housemanager.shopList.model.Measurements
import com.synowkrz.housemanager.shopList.model.PersistentShopItem

suspend fun initializeShopitems(repository: HouseRepository, app: Application) {


    val breadFromDb = repository.getPersistentShopItemByName(app.getString(R.string.bread))
    if (breadFromDb != null) {
        Log.d("KRZYSIO", "Shop items already initialized")
        return
    }

    //Bread
    Log.d("KRZYSIO", "Initialize persisten shop list")
    val bread = PersistentShopItem(app.getString(R.string.bread), Category.BREAD, 1.0, Measurements.QUANTITY, 0)
    val bun = PersistentShopItem(app.getString(R.string.bun), Category.BREAD, 1.0, Measurements.QUANTITY, 0)
    val muffin = PersistentShopItem(app.getString(R.string.muffin), Category.BREAD, 1.0, Measurements.QUANTITY, 0)
    val baguette = PersistentShopItem(app.getString(R.string.baguette), Category.BREAD, 1.0, Measurements.QUANTITY, 0)

    //FruitAndVegetables
    val tomato = PersistentShopItem(app.getString(R.string.tomato), Category.FRUIT_VEGETABLES, 1.0, Measurements.WEIGHT, 0)
    val potato = PersistentShopItem(app.getString(R.string.potato), Category.FRUIT_VEGETABLES, 1.0, Measurements.WEIGHT, 0)
    val cucumber = PersistentShopItem(app.getString(R.string.cucumber), Category.FRUIT_VEGETABLES, 1.0, Measurements.WEIGHT, 0)
    val onion = PersistentShopItem(app.getString(R.string.onion), Category.FRUIT_VEGETABLES, 1.0, Measurements.WEIGHT, 0)
    val garlic = PersistentShopItem(app.getString(R.string.garlic), Category.FRUIT_VEGETABLES, 1.0, Measurements.WEIGHT, 0)
    val pepper = PersistentShopItem(app.getString(R.string.pepper), Category.FRUIT_VEGETABLES, 1.0, Measurements.WEIGHT, 0)
    val apple = PersistentShopItem(app.getString(R.string.apple), Category.FRUIT_VEGETABLES, 1.0, Measurements.WEIGHT, 0)
    val banana = PersistentShopItem(app.getString(R.string.banana), Category.FRUIT_VEGETABLES, 1.0, Measurements.WEIGHT, 0)
    val strawberries = PersistentShopItem(app.getString(R.string.strawberries), Category.FRUIT_VEGETABLES, 1.0, Measurements.WEIGHT, 0)

    //MEAT
    val ham = PersistentShopItem(app.getString(R.string.ham), Category.MEAT, 1.0, Measurements.WEIGHT, 0)
    val chickenFillets = PersistentShopItem(app.getString(R.string.chicken_fillets), Category.MEAT, 1.0, Measurements.WEIGHT, 0)
    val mincedMeat = PersistentShopItem(app.getString(R.string.minced_meat), Category.MEAT, 1.0, Measurements.WEIGHT, 0)
    val porkNeck = PersistentShopItem(app.getString(R.string.pork_neck), Category.MEAT, 1.0, Measurements.WEIGHT, 0)
    val sausage = PersistentShopItem(app.getString(R.string.sausage), Category.MEAT, 1.0, Measurements.WEIGHT, 0)
    val frankfurter = PersistentShopItem(app.getString(R.string.frankfurter), Category.MEAT, 1.0, Measurements.WEIGHT, 0)

    //DAIRY
    val egg = PersistentShopItem(app.getString(R.string.egg), Category.DAIRY, 1.0, Measurements.QUANTITY, 0)
    val milk = PersistentShopItem(app.getString(R.string.milk), Category.DAIRY, 1.0, Measurements.VOLUME, 0)
    val cheese = PersistentShopItem(app.getString(R.string.cheese), Category.DAIRY, 1.0, Measurements.WEIGHT, 0)
    val whiteCheese = PersistentShopItem(app.getString(R.string.white_cheese), Category.DAIRY, 1.0, Measurements.WEIGHT, 0)

    //ALCOHOL
    val beer = PersistentShopItem(app.getString(R.string.beer), Category.ALCOHOL, 1.0, Measurements.VOLUME, 0)
    val vodka = PersistentShopItem(app.getString(R.string.vodka), Category.ALCOHOL, 1.0, Measurements.VOLUME, 0)
    val whisky = PersistentShopItem(app.getString(R.string.whisky), Category.ALCOHOL, 1.0, Measurements.VOLUME, 0)
    val beerFree = PersistentShopItem(app.getString(R.string.beer_free), Category.ALCOHOL, 1.0, Measurements.VOLUME, 0)

    //SWEETS
    val cookies = PersistentShopItem(app.getString(R.string.cookies), Category.SWEETS, 1.0, Measurements.QUANTITY, 0)
    val sweets = PersistentShopItem(app.getString(R.string.sweets), Category.SWEETS, 1.0, Measurements.QUANTITY, 0)
    val chocolate = PersistentShopItem(app.getString(R.string.chocolate), Category.SWEETS, 1.0, Measurements.QUANTITY, 0)
    val jelly = PersistentShopItem(app.getString(R.string.jelly), Category.SWEETS, 1.0, Measurements.QUANTITY, 0)

    //SNACKS
    val chips = PersistentShopItem(app.getString(R.string.chips), Category.SNACKS, 1.0, Measurements.QUANTITY, 0)
    val breadSticks = PersistentShopItem(app.getString(R.string.bread_sticks), Category.SNACKS, 1.0, Measurements.QUANTITY, 0)
    val peanuts = PersistentShopItem(app.getString(R.string.peanuts), Category.SNACKS, 1.0, Measurements.QUANTITY, 0)
    val crackers = PersistentShopItem(app.getString(R.string.crackers), Category.SNACKS, 1.0, Measurements.QUANTITY, 0)

    //HYGIENE
    val tissue = PersistentShopItem(app.getString(R.string.tissue), Category.HYGIENE, 1.0, Measurements.QUANTITY, 0)
    val wipes = PersistentShopItem(app.getString(R.string.wipes), Category.HYGIENE, 1.0, Measurements.QUANTITY, 0)
    val diaper = PersistentShopItem(app.getString(R.string.diaper), Category.HYGIENE, 1.0, Measurements.QUANTITY, 0)
    val toilet_paper = PersistentShopItem(app.getString(R.string.toilet_paper), Category.HYGIENE, 1.0, Measurements.QUANTITY, 0)
    val soap = PersistentShopItem(app.getString(R.string.soap), Category.HYGIENE, 1.0, Measurements.QUANTITY, 0)
    val showerGel = PersistentShopItem(app.getString(R.string.shower_gel), Category.HYGIENE, 1.0, Measurements.QUANTITY, 0)
    val toothPaste = PersistentShopItem(app.getString(R.string.tooth_paste), Category.HYGIENE, 1.0, Measurements.QUANTITY, 0)

    //DRINKABLES
    val water = PersistentShopItem(app.getString(R.string.water), Category.DRINKABLES, 1.0, Measurements.QUANTITY, 0)

    var producList : List<PersistentShopItem> = listOf(bread, bun, muffin, baguette, tomato, potato, cucumber, onion, garlic, pepper, apple, banana, strawberries,
        ham, chickenFillets, mincedMeat, porkNeck, sausage, frankfurter, egg, milk, cheese, whiteCheese, beer, vodka, whisky, beerFree,
        cookies, sweets, chocolate, jelly, chips, breadSticks, peanuts, crackers, tissue, wipes, diaper, toilet_paper, soap, showerGel, toothPaste, water)

    for (item in producList) {
        repository.insertNewPersistentShopItem(item)
    }
}