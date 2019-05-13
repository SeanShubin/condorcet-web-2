package model

interface Authorization {
    val canCreateElections: Boolean
    val canViewAllElections: Boolean
}

object Guest : Authorization {
    override val canCreateElections = false
    override val canViewAllElections = true
}

object Standard : Authorization {
    override val canCreateElections = true
    override val canViewAllElections = true
}

object Admin : Authorization {
    override val canCreateElections = true
    override val canViewAllElections = true
}

object Owner : Authorization {
    override val canCreateElections = true
    override val canViewAllElections = true
}
