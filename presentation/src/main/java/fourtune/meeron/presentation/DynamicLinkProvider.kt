package fourtune.meeron.presentation

import android.net.Uri
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import forutune.meeron.domain.Const
import javax.inject.Inject

class DynamicLinkProvider @Inject constructor() {
    operator fun invoke(workspaceId: Long, onGenerateLink: (String) -> Unit) {
        Firebase.dynamicLinks.createDynamicLink()
            .setLink(Uri.parse("${Const.DynamicLinkDomainUrl}?id=${workspaceId}"))
            .setDomainUriPrefix(Const.DynamicLinkDomainUrl)
            .setIosParameters(DynamicLink.IosParameters.Builder(Const.IosBundleId).build())
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .buildShortDynamicLink()
            .addOnSuccessListener { link ->
                onGenerateLink(link.shortLink.toString())
            }
    }
}