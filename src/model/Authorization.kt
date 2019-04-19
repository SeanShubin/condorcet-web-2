package model

interface Authorization {
    fun canCreateElections(): Boolean
}

object Guest : Authorization {
    override fun canCreateElections() = false
}

object Standard : Authorization {
    override fun canCreateElections(): Boolean = true
}

object Admin : Authorization {
    override fun canCreateElections(): Boolean = true
}

object Owner : Authorization {
    override fun canCreateElections(): Boolean = true
}
