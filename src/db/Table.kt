package db

interface Table<PkType, T : HasPrimaryKey<PkType>> {
    fun search(key:PkType):T?
    fun listAll():List<T>
    fun add(value: T)
    fun update(value: T)
    fun remove(key: PkType)

    fun listWhere(p:(T)->Boolean): List<T> = listAll().filter(p)
    fun existsWhere(p:(T)->Boolean):Boolean = listWhere(p).isNotEmpty()
    fun find(key:PkType):T = search(key)!!
    fun keyExists(key:PkType):Boolean = search(key) != null


//    fun add(values: List<T>)
//    fun countAll(): Int
//    fun countWhere(p:(T)->Boolean): Int
//    fun removeAllByKeys(keys:List<PkType>)
//    fun removeAllByValues(values:List<T>)
//    fun addOrUpdate(value: T)
}
