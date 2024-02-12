package opensource.h3nryc0ding.playground.util

import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.reactive.internal.DgsReactiveRequestData

val DgsDataFetchingEnvironment.request
    get() = (this.getDgsContext().requestData as DgsReactiveRequestData).serverRequest!!

val DgsDataFetchingEnvironment.response
    get() = this.request.exchange().response
