package db

interface Table<PkType, T : TableRow<PkType>> {
    fun listAll():List<T>
    fun add(value: T)
    fun update(value: T)
    fun remove(key: PkType)

    fun search(key: PkType): T? {
        val matching = listWhere { it.primaryKey == key }
        return when (matching.size) {
            0 -> null
            1 -> matching[0]
            else -> throw RuntimeException("Expected 0 or 1 matching $key, got ${matching.size}")
        }
    }
    fun listWhere(p:(T)->Boolean): List<T> = listAll().filter(p)
    fun existsWhere(p:(T)->Boolean):Boolean = listWhere(p).isNotEmpty()
    fun removeValue(value: T) {
        remove(value.primaryKey)
    }

    fun removeWhere(p: (T) -> Boolean) {
        listWhere(p).forEach(::removeValue)
    }
    fun find(key:PkType):T = search(key)!!
    fun keyExists(key:PkType):Boolean = search(key) != null
    fun addAll(list: List<T>) {
        list.forEach(::add)
    }

    fun countWhere(p: (T) -> Boolean): Int = listWhere(p).size
    fun countAll(): Int = listAll().size
    fun addOrUpdate(value: T) {
        if (search(value.primaryKey) == null) {
            add(value)
        } else {
            update(value)
        }
    }
}
