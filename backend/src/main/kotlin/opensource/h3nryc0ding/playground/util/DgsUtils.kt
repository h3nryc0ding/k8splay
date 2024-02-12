package opensource.h3nryc0ding.playground.util

import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.reactive.internal.DgsReactiveRequestData

val DgsDataFetchingEnvironment.exchange
    get() = (this.getDgsContext().requestData as DgsReactiveRequestData).serverRequest!!.exchange()

val DgsDataFetchingEnvironment.request
    get() = this.exchange.request

val DgsDataFetchingEnvironment.response
    get() = this.exchange.response
