package db

interface Table<PkType, T : HasPrimaryKey<PkType>> {
    fun add(value: T)
    fun list(): List<T>
    fun remove(key: PkType)
    fun update(value: T)
    fun addOrUpdate(value: T)
}
