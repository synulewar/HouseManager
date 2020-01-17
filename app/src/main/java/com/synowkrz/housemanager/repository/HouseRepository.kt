package com.synowkrz.housemanager.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.synowkrz.housemanager.*
import com.synowkrz.housemanager.babyTask.model.BabyProfile
import com.synowkrz.housemanager.babyTask.model.Feeding
import com.synowkrz.housemanager.database.HouseManagerDatabase
import com.synowkrz.housemanager.homeTaskList.model.DoneTask
import com.synowkrz.housemanager.homeTaskList.model.HomeTask
import com.synowkrz.housemanager.model.TaskGridItem
import com.synowkrz.housemanager.shopList.model.*
import kotlinx.coroutines.*
import java.sql.SQLException
import java.time.LocalDate

class HouseRepository(private val app: Context) {

    private val database = HouseManagerDatabase.getInstance(app)
    val items by lazy { database.taskItemDao.getAllTasks() }
    val babies by lazy { database.babyProfileDao.getAllBabies()}
    val shopList by lazy {database.shopListDao.getAllShopList()}
    val persistentItems by lazy {database.persistentShopItemDao.getAllPersistenShopItem()}
    val shopAreas by lazy { database.shopAreaDao.getAllShopAreas() }
    val tasks by lazy { database.homeTaskDao.getAllHomeTasks() }
    val firebaseDatabase = FirebaseDatabase.getInstance().reference

    private val repositoryJob = Job()
    private val repositoryScope = CoroutineScope(repositoryJob + Dispatchers.Main)


    //ShopList
    suspend fun insertNewShopList(shopList: ShopList) {
        withContext(Dispatchers.IO) {
            Log.d(TAG, "Add new shopList ${shopList.name}")
            database.shopListDao.insert(shopList)
            firebaseDatabase.child(SHOP_LIST_KEY).child(shopList.name).setValue(shopList)
        }
    }

    suspend fun updateShopList(shopList: ShopList) {
        withContext(Dispatchers.IO) {
            Log.d(TAG, "Update shopList ${shopList.name}")
            database.shopListDao.update(shopList)
            firebaseDatabase.child(SHOP_LIST_KEY).child(shopList.name).setValue(shopList)
        }
    }

    suspend fun removeShopList(shopList: ShopList) {
        withContext(Dispatchers.IO) {
            database.shopListDao.deleteShopList(shopList.name)
            firebaseDatabase.child(SHOP_LIST_KEY).child(shopList.name).removeValue()
        }
    }

    fun getShopListByNameAsync(name: String) : ShopList? {
        return database.shopListDao.getShopListByNameAsync(name)
    }

    fun getAllShopListsAsycn() : List<ShopList> {
        return database.shopListDao.getAllShopListAsync()
    }

    fun getAllActivetemsCountFromList(listName : String) : Int {
        return database.shopItemDao.getAllActivetemsCountFromList(listName)
    }


    fun registerShopListListener() {
        firebaseDatabase.child(SHOP_LIST_KEY).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                Log.d(TAG, "onCancelled")
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildMoved")
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildChanged")
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildAdded")
                val shopList = dataSnapshot.getValue(ShopList::class.java)
                Log.d(TAG, "onChildAdded name ${shopList?.name} shop ${shopList?.shopName}")
                repositoryScope.launch {
                    shopList?.let {
                        resolveShopList(it)
                    }
                }

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved")
            }
        })
    }

    suspend fun resolveShopList(shopList: ShopList) {
        withContext(Dispatchers.IO) {
            if (getShopListByNameAsync(shopList.name) != null) {
                return@withContext
            }
            Log.d(TAG, "Insert new shopList ${shopList}")
            database.shopListDao.insert(shopList)

        }
    }


    //Persistent shop item
    suspend fun insertNewPersistentShopItem(shopItem: PersistentShopItem) {
        withContext(Dispatchers.IO) {
            database.persistentShopItemDao.insert(shopItem)
            firebaseDatabase.child(PERSISTENT_SHOP_KEY).child(shopItem.name).setValue(shopItem)
        }
    }

    fun getAllPersistentItemAsync() : List<PersistentShopItem> {
        return database.persistentShopItemDao.getAllPersistenShopItemAsync()
    }

    fun getAllPersistentItemByCategoryAsync(category : String) : List<PersistentShopItem> {
        return database.persistentShopItemDao.getPersistentShopItemByCategoryAsync(category)
    }

    suspend fun updatePersistentShopItem(persistentShopItem: PersistentShopItem) {
        withContext(Dispatchers.IO) {
            database.persistentShopItemDao.update(persistentShopItem)
            firebaseDatabase.child(PERSISTENT_SHOP_KEY).child(persistentShopItem.name).setValue(persistentShopItem)
        }
    }


    fun getAllPersistentShopItemsByCategory(category: String) : LiveData<List<PersistentShopItem>> {
        return database.persistentShopItemDao.getPersistentShopItemByCategory(category)
    }

    fun getPersistentShopItemByName(name: String) : PersistentShopItem? {
        return database.persistentShopItemDao.getPersistentShopItemByName(name)
    }

    fun getPersistentShopItemByNamePart(name: String) : List<PersistentShopItem>? {
        return database.persistentShopItemDao.getPersistentShopItemByNamePart("%${name}%")
    }

    fun getPersistentShopItemByNamePartAndCategory(name: String, category: Category) : List<PersistentShopItem>? {
        return database.persistentShopItemDao.getPersistentShopItemByNamePartAndCategory("%${name}%", category.toString())
    }

    fun getAllPersistentItems() : LiveData<List<PersistentShopItem>> {
        return database.persistentShopItemDao.getAllPersistenShopItem()
    }

    suspend fun insertNewHomeTask(homeTask: HomeTask) {
        withContext(Dispatchers.IO) {
            database.homeTaskDao.insert(homeTask)
            firebaseDatabase.child(HOME_TASK).child(homeTask.name).setValue(homeTask)
        }
    }

    suspend fun updateHomeTask(homeTask: HomeTask) {
        withContext(Dispatchers.IO) {
            database.homeTaskDao.update(homeTask)
            firebaseDatabase.child(HOME_TASK).child(homeTask.name).setValue(homeTask)
        }
    }


    suspend fun deleteHomeTask(homeTask: HomeTask) {
        withContext(Dispatchers.IO) {
            database.homeTaskDao.delete(homeTask)
            firebaseDatabase.child(HOME_TASK).child(homeTask.name).removeValue()
        }
    }


    fun getAllHomeTaskAsync() : List<HomeTask> {
            return database.homeTaskDao.getAllTaskAsync()
    }

    fun getHomeTaskByName(name : String) : HomeTask? {
        return database.homeTaskDao.getTaskByName(name)
    }

    suspend fun refreshTaskList() {
        withContext(Dispatchers.IO) {
            Log.d(TAG, "Refresh task list")
            val taskList = getAllHomeTaskAsync()
            for (item in taskList) {
                item.daysExceeded = HomeTask.calculateDaysExceeded(LocalDate.parse(item.dueDate))
                item.expired = HomeTask.isTaskExpired(LocalDate.parse(item.dueDate))
                updateHomeTask(item)
            }
        }
    }


    fun registerPersistentItemListener() {
        firebaseDatabase.child(PERSISTENT_SHOP_KEY).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                Log.d(TAG, "onCancelled")
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildMoved")
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildChanged")
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildAdded")
                val persistentShopItem = dataSnapshot.getValue(PersistentShopItem::class.java)
                Log.d(TAG, "onChildAdded name ${persistentShopItem?.name} item ${persistentShopItem?.category}")
                persistentShopItem?.let {
                    repositoryScope.launch {
                        resolvePersistentShopItem(it)
                    }
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved")
            }
        })
    }


    suspend fun resolvePersistentShopItem(persistentShopItem: PersistentShopItem) {
        withContext(Dispatchers.IO) {
            val item = getPersistentShopItemByName(persistentShopItem.name)
            if (item?.name == persistentShopItem.name) {
                if (persistentShopItem.usage > item.usage) {
                    database.persistentShopItemDao.update(persistentShopItem)
                    Log.d(TAG, "Update usage old ${item.name} ${item.usage} new ${persistentShopItem.name} ${persistentShopItem.usage}")
                }
                return@withContext
            }
            Log.d(TAG, "Add new persistent shop item ${persistentShopItem.name}")
            database.persistentShopItemDao.insert(persistentShopItem)
        }
    }


    //ShopAreas
    fun getAllShopAreas() : LiveData<List<ShopArea>> {
        return database.shopAreaDao.getAllShopAreas()
    }

    fun getShopAreaByName(name: String) : ShopArea? {
        return database.shopAreaDao.getShopAreByName(name)
    }


    suspend fun insertNewShopArea(shopArea: ShopArea) {
        withContext(Dispatchers.IO) {
            database.shopAreaDao.insert(shopArea)
            firebaseDatabase.child(SHOP_AREA_KEY).child(shopArea.name).setValue(shopArea)
        }
    }

    suspend fun updateShopArea(shopArea: ShopArea) {
        withContext(Dispatchers.IO) {
            database.shopAreaDao.update(shopArea)
            firebaseDatabase.child(SHOP_AREA_KEY).child(shopArea.name).setValue(shopArea)
        }
    }

    suspend fun deletetShopArea(shopArea: ShopArea) {
        withContext(Dispatchers.IO) {
            database.shopAreaDao.deleteArea(shopArea.name)
            firebaseDatabase.child(SHOP_AREA_KEY).child(shopArea.name).removeValue()
        }
    }

    fun getAllShopAreaAsync() : List<ShopArea> {
        return database.shopAreaDao.getAllShopAreasAsync()
    }

    fun registerSchopAreaListener() {
        firebaseDatabase.child(SHOP_AREA_KEY).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                Log.d(TAG, "onCancelled")
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildMoved")
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildChanged")
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildAdded")
                val shopArea = dataSnapshot.getValue(ShopArea::class.java)
                Log.d(TAG, "ShopArea ${shopArea?.name} ${shopArea?.areas}")
                shopArea?.let {
                    repositoryScope.launch {
                        resolveShopArea(it)
                    }
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved")
            }
        })
    }

    suspend fun resolveShopArea(shopArea: ShopArea) {
        withContext(Dispatchers.IO) {
            val item = getShopAreaByName(shopArea.name)
            if (item?.name == shopArea.name) {
                if (item.areas != shopArea.areas) {
                    updateShopArea(shopArea)
                }
                return@withContext
            }
            Log.d(TAG, "Add new shopArea ${shopArea.name}")
            database.shopAreaDao.insert(shopArea)
        }
    }


    //Shop item
    fun getAllActiveItems(listName: String) : LiveData<List<ShopItem>> {
        return database.shopItemDao.getAllActivetemsFromList(listName)
    }

    fun getAllInactiveItems(listName: String) : LiveData<List<ShopItem>> {
        return database.shopItemDao.getAllInactivetemsFromList(listName)
    }


    suspend fun insertShopItem(shopItem: ShopItem) {
        withContext(Dispatchers.IO) {
            database.shopItemDao.insert(shopItem)
            firebaseDatabase.child(SHOP_ITEM_KEY).child(shopItem.id.toString()).setValue(shopItem)
        }
    }


    suspend fun updateShopItem(shopItem: ShopItem) {
        withContext(Dispatchers.IO) {
            database.shopItemDao.update(shopItem)
            firebaseDatabase.child(SHOP_ITEM_KEY).child(shopItem.id.toString()).setValue(shopItem)
        }
    }

    suspend fun getAllShopItemsAsync() : List<ShopItem> {
        return database.shopItemDao.getAllItemsAsync()
    }

    fun getShopItemById(id : Long) : ShopItem? {
        return database.shopItemDao.getShopItemByID(id)
    }

    suspend fun deleteShopItem(shopItem: ShopItem) {
        withContext(Dispatchers.IO) {
            Log.d(TAG, "Remove outdated shop item ${shopItem}")
            database.shopItemDao.deleteShopItem(shopItem)
            firebaseDatabase.child(SHOP_ITEM_KEY).child(shopItem.id.toString()).removeValue()
        }
    }


    fun registerItemListListener() {
        firebaseDatabase.child(SHOP_ITEM_KEY).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                Log.d(TAG, "onCancelled")
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildMoved")
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildChanged shopItem")
                val shopItem = dataSnapshot.getValue(ShopItem::class.java)
                Log.d(TAG, "ShopItem ${shopItem?.name}")
                shopItem?.let {
                    repositoryScope.launch {
                        resolveShopItem(it)
                    }
                }
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildAdded")
                val shopItem = dataSnapshot.getValue(ShopItem::class.java)
                Log.d(TAG, "ShopItem ${shopItem?.name}")
                shopItem?.let {
                    repositoryScope.launch {
                        resolveShopItem(it)
                    }
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved")
            }
        })
    }

    fun registerHomeTaskListener() {
        firebaseDatabase.child(HOME_TASK).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d(TAG, "onCancelled")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildMoved")
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildChanged")
                val homeTask = dataSnapshot.getValue(HomeTask::class.java)
                Log.d(TAG, "HomeTask ${homeTask?.name}")
                homeTask?.let {
                    repositoryScope.launch {
                        resolveHomeTask(it)
                    }
                }
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildAdded")
                val homeTask = dataSnapshot.getValue(HomeTask::class.java)
                Log.d(TAG, "HomeTask ${homeTask?.name}")
                homeTask?.let {
                    repositoryScope.launch {
                        resolveHomeTask(it)
                    }
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
               //
            }

        })
    }


    fun registerDoneTaskListener() {
        firebaseDatabase.child(DONE_TASK).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d(TAG, "onCancelled")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildMoved")
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildChanged")
                val doneTask = dataSnapshot.getValue(DoneTask::class.java)
                Log.d(TAG, "HomeTask ${doneTask ?.name}")
                doneTask ?.let {
                    repositoryScope.launch {
                        resolveDoneTask(it)
                    }
                }
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d(TAG, "onChildAdded")
                val doneTask = dataSnapshot.getValue(DoneTask::class.java)
                Log.d(TAG, "HomeTask ${doneTask ?.name}")
                doneTask ?.let {
                    repositoryScope.launch {
                        resolveDoneTask(it)
                    }
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved")
                val homeTask = dataSnapshot.getValue(HomeTask::class.java)
                Log.d(TAG, "HomeTask ${homeTask?.name}")
                repositoryScope.launch {
                    homeTask?.let {
                        deleteHomeTask(it)
                    }
                }

            }

        })
    }

    suspend fun resolveDoneTask(doneTask: DoneTask) {
        withContext(Dispatchers.IO) {
            val item = database.doneTaskDao.getDoneTaskById(doneTask.id)
            if (item?.id == doneTask.id) {
                return@withContext
            }
            database.doneTaskDao.insert(doneTask)
        }
    }

    suspend fun resolveHomeTask(homeTask: HomeTask) {
        withContext(Dispatchers.IO) {
            val item = getHomeTaskByName(homeTask.name)
            if (item?.name == homeTask.name) {
                Log.d(TAG, "update homeTask $homeTask")
                database.homeTaskDao.update(homeTask)
            } else {
                database.homeTaskDao.insert(homeTask)
            }
        }
    }

    suspend fun resolveShopItem(shopItem: ShopItem) {
        withContext(Dispatchers.IO) {
            val item = getShopItemById(shopItem.id)
            if (item?.id == shopItem.id) {
                if (item.active != shopItem.active) {
                    Log.d(TAG, "Update Shop Item ${shopItem.name}")
                    updateShopItem(shopItem)
                }
                return@withContext
            }
            Log.d(TAG, "Add new shopItem ${shopItem.name}")
            database.shopItemDao.insert(shopItem)
        }
    }

    suspend fun insertBabyProfile(babyProfile: BabyProfile) {
        withContext(Dispatchers.IO) {
            try {
                database.babyProfileDao.insert(babyProfile)
            } catch (e: Exception) {
                TODO()
            }
        }
    }

    suspend fun insertTask(taskGridItem: TaskGridItem) {
        withContext(Dispatchers.IO) {
            try {
                database.taskItemDao.insert(taskGridItem)
            } catch (e: SQLException) {
                TODO("Handle duplicate insert")
                // insert fail
            }
        }
    }

    suspend fun removeTask(taskGridItem: TaskGridItem) {
        withContext(Dispatchers.IO) {
            database.taskItemDao.deleteTask(taskGridItem.name)
        }
    }

    suspend fun removeBabyProfile(babyProfile: BabyProfile) {
        withContext(Dispatchers.IO) {
            database.babyProfileDao.deleteBaby(babyProfile.name)
        }
    }

    suspend fun getAllBabiesData() : List<BabyProfile> {
        return withContext(Dispatchers.IO) {
             database.babyProfileDao.getAllBabiesData()
        }
    }

    suspend fun getBabyProfile(name: String) : BabyProfile? {
        return withContext(Dispatchers.IO) {
            database.babyProfileDao.getBabyByName(name)
        }
    }

    suspend fun insertFeeding(feeding: Feeding) {
        withContext(Dispatchers.IO) {
            database.feedingDao.insert(feeding)
        }
    }

    fun getAllFeedings() : LiveData<List<Feeding>> {
        return database.feedingDao.getAllFeedings()
    }

    fun getAllFeedingsByName(name: String) : LiveData<List<Feeding>> {
        return database.feedingDao.getAllFeedingsByName(name)
    }

    fun syncWithRemoteDatabase() {
        repositoryScope.launch {
            withContext(Dispatchers.IO) {
                var itemList = getAllShopItemsAsync()

                for (item in itemList) {
                    if (!item.active && (System.currentTimeMillis() - item.id) > STORAGE_TIMELIMIT) {
                        deleteShopItem(item)
                    }
                }
                // get new list and send to remote
                itemList = getAllShopItemsAsync()
                for (item in itemList) {
                    firebaseDatabase.child(SHOP_ITEM_KEY).child(item.id.toString()).setValue(item)
                }


                var homeTaskList = getAllHomeTaskAsync()
                for (item in homeTaskList) {
                    firebaseDatabase.child(HOME_TASK).child(item.name).setValue(item)
                }

                var doneTaskList = database.doneTaskDao.getAllDoneTaskAsync()
                for (item in doneTaskList) {
                    firebaseDatabase.child(DONE_TASK).child(item.id.toString()).setValue(item)
                }
            }
        }
    }

    fun getAllDoneTaskByName(name : String) : LiveData<List<DoneTask>> {
        return database.doneTaskDao.getAllDoneTaskByName(name)
    }

    suspend fun insertNewDoneTask(doneTask: DoneTask) {
        withContext(Dispatchers.IO) {
            database.doneTaskDao.insert(doneTask)
            firebaseDatabase.child(DONE_TASK).child(doneTask.id.toString()).setValue(doneTask)
        }
    }

    suspend fun deleteDoneTask(doneTask: DoneTask) {
        withContext(Dispatchers.IO) {
            database.doneTaskDao.delete(doneTask)
            firebaseDatabase.child(DONE_TASK).child(doneTask.id.toString()).removeValue()
        }
    }
}