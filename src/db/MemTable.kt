package db

class MemTable<PkType, T : HasPrimaryKey<PkType>>(val name: String) : Table<PkType, T> {
    val rows = mutableListOf<T>()
    val pkIndex = mutableMapOf<PkType, T>()

    override fun add(value: T) {
        if (pkIndex.containsKey(value.primaryKey))
            throw RuntimeException("Primary key $value already exists for table $name")
        rows.add(value)
        pkIndex[value.primaryKey] = value
    }

    override fun list(): List<T> = rows
    override fun remove(key: PkType) {
        rows.removeAll { it.primaryKey == key }
        pkIndex.remove(key)
    }

    override fun update(value: T) {
        val index = rows.indexOfFirst { it.primaryKey == value.primaryKey }
        if (index < 0)
            throw RuntimeException("Primary key $value does not exist for table $name")
        rows[index] = value
        pkIndex[value.primaryKey] = value
    }

    override fun addOrUpdate(value: T) {
        if (pkIndex.containsKey(value.primaryKey)) {
            update(value)
        } else {
            add(value)
        }
    }
}