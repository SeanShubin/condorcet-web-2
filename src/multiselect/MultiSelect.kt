package multiselect

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

class MultiSelect : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        fieldSet {
            legend {
                +"Multiple Select"
            }
            div(classes = "multiselect") {
                div{
                    span{
                        +"3 Selected"
                    }
                    span {
                        +"Clear"
                    }
                    span {
                        +"Invert"
                    }
                    input{}
                    span{
                        +"Select All"
                    }
                }
                div(classes = "selected") {
                    ul{
                        li(classes="two-column-grid"){
                            selected("Alice")
                            selected("Carol")
                            selected("Eve")
                            selected("Trent")
                            selected("Peggy")
                        }
                    }
                }
                div(classes = "choices") {
                    ul{
                        li(classes="two-column-grid"){
                            notSelected("Bob")
                            notSelected("Dave")
                            notSelected("Mallory")
                            notSelected("Walter")
                            notSelected("Victor")
                        }
                    }
                }
            }

        }
    }

    fun RBuilder.selected(name:String){
        +name
        button{
            +"-"
        }
    }

    fun RBuilder.notSelected(name:String){
        +name
        button{
            +"+"
        }
    }
}

fun RBuilder.multiSelect() = child(MultiSelect::class) {}
