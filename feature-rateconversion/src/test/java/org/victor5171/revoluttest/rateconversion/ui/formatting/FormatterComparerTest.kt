package org.victor5171.revoluttest.rateconversion.ui.formatting

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch
import org.junit.Assert
import org.junit.Test

class FormatterComparerTest {
    @Test
    fun test1() {
        val previousStringBuilder = StringBuilder("100")
        val new = "100.00"

        val diffMatchPatch = DiffMatchPatch()
        val differences = diffMatchPatch.diffMain(previousStringBuilder.toString(), new)

        previousStringBuilder.proccessDifferences(differences)

        Assert.assertEquals(new, previousStringBuilder.toString())
    }

    @Test
    fun test2() {
        val previousStringBuilder = StringBuilder("1,00.00")
        val new = "100.00"

        val diffMatchPatch = DiffMatchPatch()
        val differences = diffMatchPatch.diffMain(previousStringBuilder.toString(), new)

        previousStringBuilder.proccessDifferences(differences)

        Assert.assertEquals(new, previousStringBuilder.toString())
    }

    @Test
    fun test3() {
        val previousStringBuilder = StringBuilder("00100")
        val new = "100.00"

        val diffMatchPatch = DiffMatchPatch()
        val differences = diffMatchPatch.diffMain(previousStringBuilder.toString(), new)

        previousStringBuilder.proccessDifferences(differences)

        Assert.assertEquals(new, previousStringBuilder.toString())
    }

    @Test
    fun test4() {
        val previousStringBuilder = StringBuilder("1000.00")
        val new = "1,000.00"

        val diffMatchPatch = DiffMatchPatch()
        val differences = diffMatchPatch.diffMain(previousStringBuilder.toString(), new)

        previousStringBuilder.proccessDifferences(differences)

        Assert.assertEquals(new, previousStringBuilder.toString())
    }

    private fun StringBuilder.proccessDifferences(differences: List<DiffMatchPatch.Diff>) {
        println(this)

        var index = 0

        differences.forEach {
            when(it.operation) {
                DiffMatchPatch.Operation.DELETE -> {
                    replace(index, index + it.text.length, "")
                }
                DiffMatchPatch.Operation.INSERT -> {
                    insert(index, it.text)
                    index += it.text.length
                }
                DiffMatchPatch.Operation.EQUAL -> index += it.text.length
            }

            println(this)
        }
    }
}