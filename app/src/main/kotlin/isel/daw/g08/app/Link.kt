package isel.daw.g08.app

import com.fasterxml.jackson.annotation.JsonCreator

data class Link @JsonCreator constructor(val href : String,
                                         val title : String)