package org.victor5171.revoluttest.rateconversion.ui

import android.content.Context
import android.graphics.drawable.Drawable
import java.util.Locale
import org.victor5171.revoluttest.rateconversion.R

object CurrencyIconRetriever {

    private const val DRAWABLE_TYPE = "drawable"
    private val cacheOfIdentifiers = mutableMapOf<String, Int>()

    fun getIcon(context: Context, currencyIdentifier: String): Drawable {
        val resources = context.resources

        val identifier =
            cacheOfIdentifiers[currencyIdentifier] ?: findIdentifierInResourcesAndCacheIt(
                context, currencyIdentifier
            )

        return resources.getDrawable(identifier, context.theme)
    }

    private fun findIdentifierInResourcesAndCacheIt(
        context: Context,
        currencyIdentifier: String
    ): Int {
        val iconName = "ic_${currencyIdentifier.toLowerCase(Locale.ROOT)}"

        val identifier = context.resources.getIdentifier(
            iconName,
            DRAWABLE_TYPE,
            context.packageName
        ).takeIf { it != 0 } ?: R.drawable.ic_missing_flag

        cacheOfIdentifiers[currencyIdentifier] = identifier
        return identifier
    }
}
