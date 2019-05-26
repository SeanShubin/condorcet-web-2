package db

interface HasPrimaryKey<T> {
    val primaryKey: T
}
