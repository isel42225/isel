package isel.daw.g08.app

open class HalResponse(val _links : Map<String, Link>)

open class EmbeddedHalResponse(links : Map<String, Link>,
                               val _embedded : Map<String, HalResponse>) : HalResponse(links)

