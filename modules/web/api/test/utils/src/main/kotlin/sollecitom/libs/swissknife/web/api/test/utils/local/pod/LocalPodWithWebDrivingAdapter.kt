package sollecitom.libs.swissknife.web.api.test.utils.local.pod

import sollecitom.libs.swissknife.core.test.utils.local.pod.LocalPod
import sollecitom.libs.swissknife.web.service.domain.WithWebInterface

interface LocalPodWithWebDrivingAdapter : LocalPod, WithWebInterface {

    companion object
}